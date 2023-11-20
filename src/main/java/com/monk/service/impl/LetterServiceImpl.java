package com.monk.service.impl;

import com.google.gson.Gson;
import com.monk.exception.IdNotFoundException;
import com.monk.exception.VoiceoverNotFoundException;
import com.monk.mapper.ApplicationMapper;
import com.monk.model.dto.LetterDTO;
import com.monk.model.dto.LetterMetadataDTO;
import com.monk.model.dto.FileRecordDTO;
import com.monk.model.entity.Letter;
import com.monk.model.entity.FileRecord;
import com.monk.model.request.NewVoiceoverRequest;
import com.monk.model.request.VoiceSettingRequest;
import com.monk.repository.LetterRepository;
import com.monk.service.LetterServiceI;
import com.monk.service.FileServiceI;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.monk.config.Constant.JSON;

@Service
@AllArgsConstructor
public class LetterServiceImpl implements LetterServiceI {

    private final LetterRepository essayRepository;

    private final OpenAiService openAiService;

    private final Gson gson;

    private OkHttpClient client;

    private final FileServiceI fileService;

    private final ApplicationMapper applicationMapper;

    @Value("${eleven.labs.api.key}")
    private String ELEVEN_LABS_API_KEY;

    @Override
    public LetterDTO getEssay(long id) {
        Letter letter = essayRepository
                        .findById(id)
                        .orElseThrow(IdNotFoundException::new);

        return applicationMapper.essayToDTO(letter);
    }

    @Override
    public LetterDTO getCurrentEssay() {

        List<Letter> sortedLetterList = essayRepository.findAll().stream().sorted((x1, x2) ->{
            boolean before = x1.getCreatedAt().before(x2.getCreatedAt());
            return before ? -1 : 1;
        }).toList();

        if (sortedLetterList.isEmpty()) {
            return null;
        }

        return applicationMapper.essayToDTO(
                sortedLetterList.get(sortedLetterList.size() - 1)
        );
    }

    @Override
    public List<LetterMetadataDTO> getEssayMetadataList() {
        return essayRepository
                .findAllProjections()
                .stream()
                .map(essay ->  LetterMetadataDTO
                                    .builder()
                                    .id(essay.getId())
                                    .build()
                )
                .sorted((x1, x2) -> (x1.getId() < x2.getId()) ? -1 : 1)
                .toList();
    }

    @Override
    public Resource getVoiceover(long id) {
        Letter letter = essayRepository.findById(id).orElseThrow(IdNotFoundException::new);

        if (letter.getVoiceover() == null) {
            throw new VoiceoverNotFoundException();
        }
        return fileService.selectFile(letter.getVoiceover().getId());
    }

    @Override
    @Transient
    public LetterDTO createEssay() {

        String prompt = "write a love letter. Assume that you are a lover whom has lost his loved one and hopes " +
                "that one day god will enable you to find her once again. Make it 300 words.  Remove the loved " +
                "ones name part. place Monk Bot in your name part. Make it more abstract and write like a novelist.";

        Flowable<ChatCompletionChunk> flowableResponse = makeChatCompletionRequest(prompt);

        Single<List<ChatCompletionChunk>> singleResponse = flowableResponse.collect(ArrayList::new, List::add);

        List<ChatCompletionChunk> tokens = singleResponse.blockingGet();

        String essayContent = tokens.stream().map(item -> {
                    String content = item.getChoices().get(0).getMessage().getContent();
                    if(content != null && !tokens.equals("null")) {
                        return content;
                    }
                    return "";
                })
                .reduce("", (x1, x2) -> x1 + x2);

        Letter letter = Letter.builder()
                .title("")
                .content(essayContent)
                .prompt(prompt)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

        essayRepository.save(letter);

        return applicationMapper.essayToDTO(letter);
    }

    @Override
    public FileRecordDTO createVoiceOver(long id) throws IOException {

        Letter letter = essayRepository.findById(id).orElseThrow(IdNotFoundException::new);

        NewVoiceoverRequest newVoiceoverRequest = NewVoiceoverRequest.builder()
                .text(letter.getTitle() + "\n" + letter.getContent())
                .model_id("eleven_monolingual_v1")
                .voice_settings(
                        VoiceSettingRequest.builder()
                                .stability(0.5)
                                .similarity_boost(0.5)
                                .build()
                )
                .build();

        RequestBody body = RequestBody.create(JSON, gson.toJson(newVoiceoverRequest));

        Request request = new Request.Builder()
                .url("https://api.elevenlabs.io/v1/text-to-speech" + "/pNInz6obpgDQGcFmaJgB" + "/stream")
                .post(body)
                .addHeader("accept", "audio/mpeg")
                .addHeader("xi-api-key", ELEVEN_LABS_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        assert response.body() != null;

        FileRecord fileRecord = fileService.create(response.body().bytes(), "mp3");
        letter.setVoiceover(fileRecord);
        essayRepository.save(letter);
        response.close();

        return applicationMapper.fileRecordToDTO(fileRecord);
    }

    @Override
    public LetterDTO getEssayByIndex(int index) {

        List<Letter> letterList = essayRepository
                .findAll()
                .stream()
                .sorted((x1, x2) -> x1.getCreatedAt().before(x2.getCreatedAt()) ? -1 : 1)
                .toList();

        if (letterList.size() <= index || letterList.isEmpty()) {
            throw new IllegalArgumentException("Index can't be out of bounds");
        }

        return applicationMapper.essayToDTO(letterList.get(index));
    }

    public Flowable<ChatCompletionChunk> makeChatCompletionRequest(String prompt) {
        ChatMessage chatMessage = new ChatMessage(
                "user",
                prompt
        );

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-4")
                .messages(
                        List.of(
                                chatMessage
                        )
                )
                .build();

        return openAiService.streamChatCompletion(chatCompletionRequest);
    }
}

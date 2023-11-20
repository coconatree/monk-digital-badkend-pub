package com.monk.service;

import com.monk.model.dto.LetterDTO;
import com.monk.model.dto.LetterMetadataDTO;
import com.monk.model.dto.FileRecordDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface LetterServiceI {
    LetterDTO getEssay(long id);
    LetterDTO getCurrentEssay();
    List<LetterMetadataDTO> getEssayMetadataList();
    Resource getVoiceover(long id);
    LetterDTO createEssay();
    FileRecordDTO createVoiceOver(long id) throws IOException;
    LetterDTO getEssayByIndex(int index);
}

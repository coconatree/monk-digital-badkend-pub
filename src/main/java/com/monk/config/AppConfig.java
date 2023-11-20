package com.monk.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monk.model.entity.MonkUser;
import com.monk.model.pojo.EMonkRole;
import com.monk.repository.MonkUserRepository;
import com.monk.service.FileServiceI;
import com.theokanning.openai.service.OpenAiService;
import com.twilio.Twilio;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Configuration
@AllArgsConstructor
public class AppConfig {

    @Value("${open.ai.api.key}")
    private final String OPEN_AI_API_KEY;


    @Value("${twilio.account.sid}")
    private final String TWILIO_ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private final String TWILIO_AUTH_TOKEN;

    private MonkUserRepository monkUserRepository;

    @Bean
    CommandLineRunner initTwilioService(FileServiceI storageService) {
        return (args) -> Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }

    @Bean
    public Gson createGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }

    @Bean
    public CommandLineRunner registerTestUser() {
        return (args) -> {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            Date currDate = new Date(System.currentTimeMillis());

            MonkUser testUser = MonkUser.builder()
                    .monkRole(EMonkRole.USER)
                    .email("emre.caniklioglu.00@gmail.com")
                    .createdAt(currDate)
                    .password(encoder.encode("1234567890"))
                    .build();

            MonkUser testAdmin = MonkUser.builder()
                    .monkRole(EMonkRole.ADMIN)
                    .email("admin@admin.com")
                    .createdAt(currDate)
                    .password(encoder.encode("1234567890"))
                    .build();

            monkUserRepository.saveAll(List.of(testUser, testAdmin));
        };
    }

    @Bean
    public OpenAiService createOpenAiService() {
        return new OpenAiService(OPEN_AI_API_KEY);
    }

    @Bean
    public OkHttpClient createClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    CommandLineRunner initFileService(FileServiceI storageService) {
        return (args) -> storageService.init();
    }
}

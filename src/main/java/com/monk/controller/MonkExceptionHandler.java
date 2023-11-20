package com.monk.controller;

import com.monk.exception.PathVariableRequiredExcepiton;
import com.monk.exception.StorageException;
import com.monk.exception.StorageFileNotFoundException;
import com.monk.exception.VoiceoverNotFoundException;
import com.monk.model.pojo.MonkApiResponse;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
@AllArgsConstructor
public class MonkExceptionHandler {

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    ResponseEntity<MonkApiResponse> handleInternalServerException() {
        return ResponseEntity.status(500).body(
                MonkApiResponse.builder()
                        .message("Something went wrong on our side. We are working on it. Please try again later.")
                        .build()
        );
    }

    @ExceptionHandler({ PathVariableRequiredExcepiton.class })
    ResponseEntity<MonkApiResponse> handlePathVariableRequiredException(
            PathVariableRequiredExcepiton exception
    )
    {
        return ResponseEntity.badRequest().body(
                MonkApiResponse.builder()
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({ StorageFileNotFoundException.class })
    ResponseEntity<MonkApiResponse> handleStorageFileNotFoundException() {
        return ResponseEntity.badRequest().body(
                MonkApiResponse.builder()
                        .message("File not found.")
                        .build()
        );
    }

    @ExceptionHandler({ StorageException.class })
    ResponseEntity<MonkApiResponse> handleStorageException() {
        return ResponseEntity.badRequest().body(
                MonkApiResponse.builder()
                        .message("Failed to store the file.")
                        .build()
        );
    }

    @ExceptionHandler({ VoiceoverNotFoundException.class })
    ResponseEntity<MonkApiResponse> handleVoiceoverNotFoundException(
            VoiceoverNotFoundException exception
    )
    {
        return ResponseEntity.badRequest().body(
                MonkApiResponse.builder()
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({MailException.class, MessagingException.class})
    ResponseEntity<MonkApiResponse> handleMessageAndMailException(
            RuntimeException exception
    ) {
        return ResponseEntity.internalServerError().body(
            MonkApiResponse.builder()
                    .message("Failed to send the message.")
                    .build()
        );
    }
}

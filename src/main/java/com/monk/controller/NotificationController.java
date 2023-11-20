package com.monk.controller;

import com.monk.model.pojo.MonkApiResponse;
import com.monk.model.request.NewHttpProjectNotificationRequest;
import com.monk.service.NotificationServiceI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/notification")
@CrossOrigin(origins = {"http://localhost:5173", "https://www.monk-digital.com"})
public class NotificationController {

    private final NotificationServiceI notificationService;

    @PostMapping(
            path = "/monk-http",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkApiResponse> notifyMonkHttpProjectUse(
            HttpServletRequest request,
            @RequestBody NewHttpProjectNotificationRequest newHttpProjectNotificationRequest
    )
    {
        notificationService.createNotificationRecord(
                request,
                newHttpProjectNotificationRequest
        );
        return ResponseEntity.ok(
                MonkApiResponse.builder()
                        .message("Success")
                        .build()
        );
    }
}

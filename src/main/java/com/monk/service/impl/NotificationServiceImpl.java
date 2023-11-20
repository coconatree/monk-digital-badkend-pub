package com.monk.service.impl;

import com.monk.model.request.NewHttpProjectNotificationRequest;
import com.monk.repository.HttpProjectNotificationRepository;
import com.monk.service.NotificationServiceI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationServiceI {

    private final EmailServiceImpl emailService;
    private final HttpProjectNotificationRepository httpProjectNotificationRepository;
    @Override
    public void createNotificationRecord(
            HttpServletRequest request,
            NewHttpProjectNotificationRequest newHttpProjectNotificationRequest
    )
    {

    }
}

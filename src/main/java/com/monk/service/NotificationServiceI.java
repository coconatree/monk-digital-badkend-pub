package com.monk.service;

import com.monk.model.request.NewHttpProjectNotificationRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface NotificationServiceI {
    void createNotificationRecord(
            HttpServletRequest request,
            NewHttpProjectNotificationRequest newHttpProjectNotificationRequest
    );
}

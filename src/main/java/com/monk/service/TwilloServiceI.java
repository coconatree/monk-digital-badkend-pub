package com.monk.service;

import com.monk.model.request.NewPhoneMessageRequest;

public interface TwilloServiceI {
    void sendSMS(NewPhoneMessageRequest newPhoneMessageRequest);
    void sendWhatsappMessage(NewPhoneMessageRequest newPhoneMessageRequest);
}

package com.monk.service.impl;

import com.monk.model.request.NewPhoneMessageRequest;
import com.monk.service.TwilloServiceI;
import com.twilio.rest.api.v2010.account.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TwilloServiceImpl implements TwilloServiceI {

    @Override
    public void sendSMS(NewPhoneMessageRequest newPhoneMessageRequest) {
        Message smsMessage = Message.creator(
                        new com.twilio.type.PhoneNumber(newPhoneMessageRequest.getCountryCode() + newPhoneMessageRequest.getNumber()),
                        new com.twilio.type.PhoneNumber("+17813286413"),
                        newPhoneMessageRequest.getMessage()
                )
                .create();
    }

    @Override
    public void sendWhatsappMessage(NewPhoneMessageRequest newPhoneMessageRequest) {
        System.out.println("UNIMPLEMENTED METHOD - REQUIRES PAID VERSION");
    }
}

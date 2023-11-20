package com.monk.exception;

public class VoiceoverNotFoundException extends MonkException {
    public VoiceoverNotFoundException() {
        super("Voiceover doesn't exists");
    }
}

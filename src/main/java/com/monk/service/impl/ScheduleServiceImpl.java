package com.monk.service.impl;

import com.monk.model.dto.LetterDTO;
import com.monk.service.LetterServiceI;
import com.monk.service.ScheduleServiceI;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleServiceI {

    private final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
    private final Logger logger = Logger.getLogger(ScheduleServiceImpl.class.toString());

    private LetterServiceI essayService;

    @Override
    //@Scheduled(fixedRate = DAY_IN_MILLISECONDS)
    public void createEssayAndVoiceOver() {
        logger.info("Creating a new letter with voiceover");
        LetterDTO letterDTO = essayService.createEssay();
        try {
            essayService.createVoiceOver(letterDTO.getId());
            logger.info("Created the letter with voiceover.");
        }
        catch (IOException e) {
            logger.warning("Failed to save the voiceover to the file system.\n" + e.getMessage());
        }
    }
}

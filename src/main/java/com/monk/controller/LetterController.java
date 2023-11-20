package com.monk.controller;

import com.monk.exception.PathVariableRequiredExcepiton;
import com.monk.model.dto.LetterDTO;
import com.monk.model.dto.LetterMetadataDTO;
import com.monk.service.LetterServiceI;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/letter")
@CrossOrigin(origins = {"http://localhost:5173", "https://www.monk-digital.com"})
public class LetterController {

    private final LetterServiceI letterService;

    @GetMapping(
            path = "/select/{id}"
    )
    public ResponseEntity<LetterDTO> selectEssay(
            @PathVariable(name = "id") Optional<Long> id
    )
    {
        if (id.isEmpty()) {
            throw new PathVariableRequiredExcepiton();
        }

        return ResponseEntity.ok(
                letterService.getEssay(id.get())
        );
    }

    @GetMapping(
            path = "/list/metadata",
            consumes = {},
            produces = {}
    )
    public ResponseEntity<List<LetterMetadataDTO>> selectAllEssayMetadata()
    {
        return ResponseEntity.ok(
                letterService.getEssayMetadataList()
        );
    }

    @GetMapping(
            path = "/voiceover/select/{id}",
            consumes = {},
            produces = {}
    )
    public Resource selectVoiceover(
            @PathVariable(name = "id") Optional<Long> id
    )
    {
        if (id.isEmpty()) {
            throw new PathVariableRequiredExcepiton();
        }

        return letterService.getVoiceover(id.get());
    }
}

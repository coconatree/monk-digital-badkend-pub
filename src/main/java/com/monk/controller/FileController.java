package com.monk.controller;

import com.monk.exception.PathVariableRequiredExcepiton;
import com.monk.mapper.ApplicationMapper;
import com.monk.model.pojo.MonkApiResponse;
import com.monk.model.dto.FileRecordDTO;
import com.monk.model.entity.FileRecord;
import com.monk.service.FileServiceI;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = {"http://localhost:5173", "https://www.monk-digital.com"})
public class FileController {

    private final FileServiceI fileService;
    private final ApplicationMapper applicationMapper;

    @GetMapping(
            path     = "/select/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Resource> selectFile(
            @PathVariable(name = "id") Optional<Long> id
    )
    {
        if (id.isEmpty()) {
            throw new PathVariableRequiredExcepiton();
        }

        return ResponseEntity.ok(
            fileService.selectFile(id.get())
        );
    }

    @GetMapping(
            path     = "/record/select/{id}",
            consumes = {},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<FileRecordDTO> selectFileRecord(
            @PathVariable(name = "id") Optional<Long> id
    )
    {
        if (id.isEmpty()) {
            throw new PathVariableRequiredExcepiton();
        }

        return ResponseEntity.ok(
                fileService.selectFileRecord(id.get())
        );
    }

    @GetMapping(
            path     = "record/list/",
            consumes = {},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Collection<FileRecordDTO>> selectFileRecordList()
    {
        return ResponseEntity.ok(fileService.selectFileRecordList());
    }

    @PostMapping(
            path     = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkApiResponse> uploadFile(
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException
    {
        FileRecord fileRecord = fileService.create(file, "mp3");

        return ResponseEntity.ok(
                MonkApiResponse.builder()
                        .status(200)
                        .message("Successfully uploaded and created the file record.")
                        .data(applicationMapper.fileRecordToDTO(fileRecord))
                        .build()
        );
    }
}

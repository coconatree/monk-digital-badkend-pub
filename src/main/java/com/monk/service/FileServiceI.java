package com.monk.service;

import com.monk.model.dto.FileRecordDTO;
import com.monk.model.entity.FileRecord;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface FileServiceI {
    void init();
    Resource selectFile(long id);
    FileRecordDTO selectFileRecord(long id);
    Collection<FileRecordDTO> selectFileRecordList();
    FileRecord create(MultipartFile file, String extensionType);
    FileRecord create(byte[] data, String extensionType);
    FileRecordDTO update(long id, MultipartFile file);
    FileRecordDTO delete(long id);
}

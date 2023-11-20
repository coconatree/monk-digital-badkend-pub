package com.monk.service.impl;

import com.monk.config.Constant;
import com.monk.exception.IdNotFoundException;
import com.monk.exception.StorageException;
import com.monk.exception.StorageFileNotFoundException;
import com.monk.mapper.ApplicationMapper;
import com.monk.model.dto.FileRecordDTO;
import com.monk.model.entity.FileRecord;
import com.monk.repository.FileRecordRepository;
import com.monk.service.FileServiceI;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Collection;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileServiceI {

    private final FileRecordRepository fileRecordRepository;
    private final ApplicationMapper applicationMapper;

    private final Path rootLocation = Paths.get(Constant.FILE_UPLOAD_DIR);

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Failed to initialize the storage service");
        }
    }

    @Override
    public Resource selectFile(long id) {

        FileRecord fileRecord = fileRecordRepository
                .findById(id)
                .orElseThrow(IdNotFoundException::new);

        return loadAsResource(fileRecord);
    }

    @Override
    public FileRecordDTO selectFileRecord(long id) {
        return applicationMapper.fileRecordToDTO(
                fileRecordRepository
                        .findById(id)
                        .orElseThrow(IdNotFoundException::new)
        );
    }

    @Override
    public Collection<FileRecordDTO> selectFileRecordList() {
        return fileRecordRepository
                .findAll()
                .stream()
                .map(applicationMapper::fileRecordToDTO)
                .toList();
    }

    @Override
    @Transient
    public FileRecord create(MultipartFile file, String extensionType) {
        try {
            return create(file.getBytes(), extensionType);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read the multipart file");
        }
    }

    @Override
    public FileRecord create(byte[] data, String extensionType) {
        try {
            if (data == null || data.length == 0) {
                throw new StorageException("Failed to store empty file.");
            }

            long count = fileRecordRepository.count();

            String filename = generateFilename(count);

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(filename))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = new ByteArrayInputStream(data)) {
                Files.copy(
                        inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            FileRecord fileRecord = FileRecord.builder()
                    .filename(filename)
                    .extensionType(extensionType)
                    .createdAt(new Date(System.currentTimeMillis()))
                    .updatedAt(new Date(System.currentTimeMillis()))
                    .build();

            fileRecordRepository.save(fileRecord);
            return fileRecord;
        }
        catch (IOException e) {
            throw new StorageException("Failed to store the file");
        }
    }

    @Override
    @Transient
    public FileRecordDTO update(long id, MultipartFile file) {

        FileRecord fileRecord = fileRecordRepository
                .findById(id)
                .orElseThrow(IdNotFoundException::new);



        return null;
    }

    @Override
    public FileRecordDTO delete(long id) {

        FileRecord fileRecord = fileRecordRepository
                .findById(id)
                .orElseThrow(IdNotFoundException::new);

        deleteFile(fileRecord);
        fileRecordRepository.deleteById(id);

        return applicationMapper.fileRecordToDTO(fileRecord);
    }

    private Resource loadAsResource(FileRecord fileRecord) {
        try {
            Path file = load(fileRecord.getFilename());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(fileRecord.getId());
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(fileRecord.getId());
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(Paths.get(filename));
    }

    private FileRecord saveFile(FileRecord fileRecord) {
        return null;
    }

    private String generateFilename(long count) {
        return "id-" + (count + 1) + "-" + System.currentTimeMillis() + ".mp3";
    }

    private void deleteFile(FileRecord fileRecord) {

    }
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}

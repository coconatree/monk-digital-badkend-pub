package com.monk.mapper;

import com.monk.model.dto.ContactDTO;
import com.monk.model.dto.LetterDTO;
import com.monk.model.dto.FileRecordDTO;
import com.monk.model.dto.MonkUserDTO;
import com.monk.model.entity.Contact;
import com.monk.model.entity.Letter;
import com.monk.model.entity.FileRecord;
import com.monk.model.entity.MonkUser;
import com.monk.model.request.NewContactRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    FileRecordDTO fileRecordToDTO(FileRecord fileRecord);
    LetterDTO essayToDTO(Letter letter);
    ContactDTO contactToDTO(Contact contact);
    Contact contactFromNewRequest(NewContactRequest newContactRequest);
    MonkUserDTO monkUserToDTO(MonkUser monkUser);
}

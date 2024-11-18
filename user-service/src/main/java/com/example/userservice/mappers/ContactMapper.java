package com.example.userservice.mappers;

import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.response.ContactResponse;
import com.example.userservice.entities.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {
//    @Mapping(target = "contactReplyId", source = "contactReply.id")
    ContactResponse toContactResponse(Contact contact);
    Contact toContact(ContactRequest request);
}

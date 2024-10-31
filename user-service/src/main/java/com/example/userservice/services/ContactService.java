package com.example.userservice.services;

import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.request.ContactUpdateRequest;
import com.example.userservice.dtos.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    Page<ContactResponse> getAllContacts(Pageable pageable);
    ContactResponse getContactById(Long id);
    List<ContactResponse> getContactByContactReply(Long contactReplyId);
    Page<ContactResponse> getContactsByContactReplyIsNull(Pageable pageable);
    Page<ContactResponse> getContactsByIsRead(boolean isRead, Pageable pageable);
    Page<ContactResponse> getContactsByIsImportant(boolean isImportant, Pageable pageable);
    Page<ContactResponse> getContactsByIsSpam(boolean isSpam, Pageable pageable);
    Page<ContactResponse> getContactsBySearch(String search, Pageable pageable);
    ContactResponse addContact(ContactRequest contactRequest);
    void deleteContact(Long id);
    Page<ContactResponse> searchContactBySpecification(Pageable pageable, String sort, String[] contact);
    void updateStatusContact(Long id, ContactUpdateRequest contactUpdateRequest);
}

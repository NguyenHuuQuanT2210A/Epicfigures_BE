package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.ContactResponse;
import com.example.userservice.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
public class ContactController {
    private final ContactService contactService;

    @GetMapping("/getAll")
    ApiResponse<Page<ContactResponse>> getAllContacts(@RequestParam(defaultValue = "1", name = "page") int page,
                                                @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get all Contacts")
                .data(contactService.getAllContacts(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/searchBySpecification")
    public ApiResponse<?> searchBySpecification(@RequestParam(defaultValue = "1", name = "page") int page,
                                                       @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) String[] contact) {
        return ApiResponse.builder()
                .message("Search Contact By Specification")
                .data(contactService.searchContactBySpecification(PageRequest.of(page -1, limit), sort, contact))
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<ContactResponse> getContactById(@PathVariable Long id) {
        return ApiResponse.<ContactResponse>builder()
                .message("Get Contact by Id")
                .data(contactService.getContactById(id))
                .build();
    }

    @GetMapping("/contactReplyId/{contactReplyId}")
    ApiResponse<List<ContactResponse>> getContactByContactReplyId(@PathVariable Long contactReplyId) {
        return ApiResponse.<List<ContactResponse>>builder()
                .message("Get Contact by Contact Reply Id")
                .data(contactService.getContactByContactReply(contactReplyId))
                .build();
    }

    @GetMapping("/getContactsReplyIsNull")
    ApiResponse<Page<ContactResponse>> getContactsReplyIsNull(@RequestParam(defaultValue = "1", name = "page") int page,
                                                      @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get Contacts Reply Is Null")
                .data(contactService.getContactsByContactReplyIsNull(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/getContactsByIsRead")
    ApiResponse<Page<ContactResponse>> getContactsByIsRead(@RequestParam(defaultValue = "1", name = "page") int page,
                                                      @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                           @RequestParam boolean isRead) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get Contacts By Is Read")
                .data(contactService.getContactsByIsRead(isRead, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/getContactsByIsImportant")
    ApiResponse<Page<ContactResponse>> getContactsByIsImportant(@RequestParam(defaultValue = "1", name = "page") int page,
                                                      @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                                @RequestParam boolean isImportant) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get Contacts By Is Important")
                .data(contactService.getContactsByIsImportant(isImportant, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/getContactsByIsSpam")
    ApiResponse<Page<ContactResponse>> getContactsByIsSpam(@RequestParam(defaultValue = "1", name = "page") int page,
                                                      @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                           @RequestParam boolean isSpam) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get Contacts By Is Spam")
                .data(contactService.getContactsByIsSpam(isSpam, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<Page<ContactResponse>> getContactsBySearch(@RequestParam(defaultValue = "1", name = "page") int page,
                                                    @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                    @RequestParam String search) {
        return ApiResponse.<Page<ContactResponse>>builder()
                .message("Get Contact by Search")
                .data(contactService.getContactsBySearch(search, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @PostMapping()
    ApiResponse<?> saveContact(@RequestBody ContactRequest request) {
        return ApiResponse.<ContactResponse>builder()
                .message("Create a new Contact")
                .data(contactService.addContact(request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ApiResponse.<Void>builder()
                .message("Delete Contact Successfully")
                .build();
    }
}

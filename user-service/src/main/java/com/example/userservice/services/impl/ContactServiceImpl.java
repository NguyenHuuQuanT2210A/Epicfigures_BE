package com.example.userservice.services.impl;

import com.example.userservice.configs.KafkaProducer;
import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.request.ContactUpdateRequest;
import com.example.userservice.dtos.response.ContactResponse;
import com.example.userservice.entities.Contact;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.mappers.ContactMapper;
import com.example.userservice.repositories.ContactRepository;
import com.example.userservice.repositories.specification.SpecSearchCriteria;
import com.example.userservice.repositories.specification.SpecificationBuilder;
import com.example.userservice.repositories.specification.contactSpec.ContactSpecification;
import com.example.userservice.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.userservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;
import static com.example.userservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.userservice.util.AppConst.SORT_BY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final KafkaProducer kafkaProducer;

    @Override
    public Page<ContactResponse> getAllContacts(Pageable pageable) {
        Page<Contact> inventories = contactRepository.findAll(pageable);
        return inventories.map(contactMapper::toContactResponse);
    }

    @Override
    public ContactResponse getContactById(Long id) {
        return contactMapper.toContactResponse(findContactById(id));
    }

    @Override
    public List<ContactResponse> getContactByContactReply(Long contactReplyId) {
        return contactRepository.findByContactReply(findContactById(contactReplyId)).stream().map(contactMapper::toContactResponse).toList();
    }

    @Override
    public Page<ContactResponse> getContactsByContactReplyIsNull(Pageable pageable) {
        return contactRepository.findByContactReplyIsNull(pageable).map(contactMapper::toContactResponse);
    }

    @Override
    public Page<ContactResponse> getContactsByIsRead(boolean isRead, Pageable pageable) {
        return contactRepository.findByIsRead(isRead, pageable).map(contactMapper::toContactResponse);
    }

    @Override
    public Page<ContactResponse> getContactsByIsImportant(boolean isImportant, Pageable pageable) {
        return contactRepository.findByIsImportant(isImportant, pageable).map(contactMapper::toContactResponse);
    }

    @Override
    public Page<ContactResponse> getContactsByIsSpam(boolean isSpam, Pageable pageable) {
        return contactRepository.findByIsSpam(isSpam, pageable).map(contactMapper::toContactResponse);
    }

    @Override
    public Page<ContactResponse> getContactsBySearch(String search, Pageable pageable) {
        return contactRepository.findAllBySearch(search, pageable).map(contactMapper::toContactResponse);
    }

    @Override
    public ContactResponse addContact(ContactRequest contactRequest) {
        if (contactRepository.existsByEmailAndIsSpam(contactRequest.getEmail(), true)){
            throw new NotFoundException("Email is spam");
        }
        Contact contact = contactMapper.toContact(contactRequest);
        if (contactRequest.getContactReplyId() != null){
            var contactReply = findContactById(contactRequest.getContactReplyId());
            contact.setContactReply(contactReply);
            kafkaProducer.sendReplyContact(ContactRequest.builder()
                    .username(contactReply.getUsername())
                    .phoneNumber(contactReply.getPhoneNumber())
                    .email(contactReply.getEmail())
                    .note(contact.getNote())
                    .contactReplyId(contactReply.getId())
                    .build());
        }
        return contactMapper.toContactResponse(contactRepository.save(contact));
    }

    @Override
    public void updateStatusContact(Long id, ContactUpdateRequest contactUpdateRequest) {
        Contact contact = findContactById(id);
        if (contactUpdateRequest.getIsRead() != null && !contactUpdateRequest.getIsRead().isEmpty()){
            contact.setRead(Boolean.parseBoolean(contactUpdateRequest.getIsRead()));
        }
        if (contactUpdateRequest.getIsImportant() != null && !contactUpdateRequest.getIsImportant().isEmpty()){
            contact.setImportant(Boolean.parseBoolean(contactUpdateRequest.getIsImportant()));
        }
        if (contactUpdateRequest.getIsSpam() != null && !contactUpdateRequest.getIsSpam().isEmpty()){
            contact.setSpam(Boolean.parseBoolean(contactUpdateRequest.getIsSpam()));
        }
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id) {
        findContactById(id);
        contactRepository.deleteById(id);
    }

    private Contact findContactById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Contact not found"));
    }

    @Override
    public Page<ContactResponse> searchContactBySpecification(Pageable pageable, String sort, String[] contact) {
        log.info("getContactsBySpecifications");
        Pageable pageableSorted = sortData(sort, pageable);

        SpecificationBuilder builder = new SpecificationBuilder();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (contact != null) {
            parseCriteriaBuilder(builder, contact, pattern, false, null);
        }

        Page<Contact> contacts = contactRepository.findAll(build(builder.params), pageableSorted);
        return contacts.map(contactMapper::toContactResponse);
    }

    private Pageable sortData(String sort, Pageable pageable) {
        Pageable pageableSorted = pageable;
        if (StringUtils.hasText(sort)){
            Pattern patternSort = Pattern.compile(SORT_BY);
            Matcher matcher = patternSort.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);

                pageableSorted = matcher.group(3).equalsIgnoreCase("desc")
                        ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).descending())
                        : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).ascending());
            }
        }
        return pageableSorted;
    }

    private void parseCriteriaBuilder(SpecificationBuilder builder, String[] entities, Pattern pattern, boolean isJoinQuery, String joinEntity) {
        for (String e : entities) {
            Matcher matcher = pattern.matcher(e);
            if (matcher.find()) {
                if (e.startsWith(OR_PREDICATE_FLAG)) {
                    builder.with(OR_PREDICATE_FLAG, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }else {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }
            }
        }
    }

    private Specification<Contact> build(List<SpecSearchCriteria> params){

        Specification<Contact> specificationContactReplyNull = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("contactReply"));

        if (params.isEmpty()){
            return Specification.where(specificationContactReplyNull);
        }
        Specification<Contact> specification = new ContactSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            specification = params.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new ContactSpecification(params.get(i)))
                    : Specification.where(specification).and(new ContactSpecification(params.get(i)));
        }

        specification = Specification.where(specification).and(specificationContactReplyNull);
        return specification;
    }
}

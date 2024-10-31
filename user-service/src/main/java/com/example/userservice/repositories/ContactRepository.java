package com.example.userservice.repositories;

import com.example.userservice.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    @Query("SELECT c FROM Contact c WHERE c.username LIKE %:search% OR c.email LIKE %:search% OR c.phoneNumber LIKE %:search%")
    Page<Contact> findAllBySearch(@Param("search") String search, Pageable pageable);
    List<Contact> findByContactReply(Contact contact);
    Page<Contact> findByContactReplyIsNull(Pageable pageable);
    Page<Contact> findByIsRead(boolean isRead, Pageable pageable);
    Page<Contact> findByIsImportant(boolean isImportant, Pageable pageable);
    Page<Contact> findByIsSpam(boolean isSpam, Pageable pageable);
    boolean existsByEmailAndIsSpam(String email, boolean isSpam);
}

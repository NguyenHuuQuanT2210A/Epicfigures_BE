package com.example.inventoryservice.repository;

import com.example.inventoryservice.entities.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, Integer> {
    Page<InventoryStatus> findByDeletedAtIsNull(Pageable pageable);
    Page<InventoryStatus> findByDeletedAtIsNotNull(Pageable pageable);

    @Query("SELECT i FROM InventoryStatus i WHERE i.name LIKE %?1% AND i.deletedAt IS NULL")
    Page<InventoryStatus> findByNameLikeAndDeletedAtIsNull(String name, Pageable pageable);
}

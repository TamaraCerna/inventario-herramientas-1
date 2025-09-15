package com.example.demo.repository;

import com.example.demo.entity.ToolEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Integer> {
    List<ToolEntity> findAll();
    Optional<ToolEntity> findById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tools WHERE id_tool = :toolId", nativeQuery = true)
    void forceDeleteById(@Param("toolId") int toolId);
}

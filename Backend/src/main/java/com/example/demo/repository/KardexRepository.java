package com.example.demo.repository;

import com.example.demo.entity.KardexEntity;
import com.example.demo.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Integer> {
    @Query("SELECT k FROM KardexEntity k " +
            "WHERE k.kardexDate BETWEEN :init AND :end " +
            "ORDER BY k.kardexDate ASC")
    List<KardexEntity> findByDateRange(@Param("init") LocalDate init,
                                       @Param("end") LocalDate end);

    List<KardexEntity> findByKardexTool(ToolEntity tool);


}

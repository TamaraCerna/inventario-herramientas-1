package com.example.demo.repository;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.enums.StateTool;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockToolsRepository extends JpaRepository<StockToolsEntity, Integer> {

    @Query("SELECT l.loanTool, COUNT(l) as total " +
            "FROM LoanEntity l " +
            "GROUP BY l.loanTool " +
            "ORDER BY total DESC")
    List<Object[]> getToolRanking();

    Optional<StockToolsEntity> findById(int integer);
    StockToolsEntity findBystockTool(StateTool stockTool);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM stock_herramientas WHERE tool_id = :toolId", nativeQuery = true)
    int deleteByToolIdNative(@Param("toolId") int toolId);


}



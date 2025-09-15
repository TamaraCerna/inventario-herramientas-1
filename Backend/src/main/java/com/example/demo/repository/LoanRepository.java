package com.example.demo.repository;

import com.example.demo.entity.LoanDTO;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.enums.StateLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, Integer > {
    List<LoanEntity> findByLoanState(StateLoan state);
    List<LoanEntity> findByLoanClient_ClientIdAndLoanState(int clientId, StateLoan loanState);
    @Query("SELECT new com.example.demo.entity.LoanDTO(" +
            "l.loanId, c.clientName, t.nameTool, l.loanState, " +
            "l.loanDateInit, l.loanDateFinish) " +
            "FROM LoanEntity l " +
            "JOIN l.loanClient c " +
            "JOIN l.loanTool t " +
            "WHERE c.clientId = :clientId AND l.loanState = :state")
    List<LoanDTO> findActiveLoansByClient(@Param("clientId") int clientId,
                                          @Param("state") StateLoan state);

}

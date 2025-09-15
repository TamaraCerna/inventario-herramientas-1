package com.example.demo.entity;

import com.example.demo.entity.enums.StateLoan;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "loans")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;

    @Enumerated(EnumType.STRING)
    private StateLoan loanState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "loan_client_id")
    private ClientEntity loanClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_tool_id")
    private ToolEntity loanTool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_tariff_id")
    private StockToolsEntity loanTariff;

    private LocalDate loanDateInit;
    private LocalDate loanDateFinish;

    private int loanPenalty;
}

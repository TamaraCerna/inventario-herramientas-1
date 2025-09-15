package com.example.demo.entity;


import com.example.demo.entity.enums.StateLoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
        private int loanId;
        private String clientName;
        private String toolName;
        private StateLoan loanState;
        private LocalDate loanDateInit;
        private LocalDate loanDateFinish;
        private String estadoDetalle;  // Vigente / Atrasado
    }


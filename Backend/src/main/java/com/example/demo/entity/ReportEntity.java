package com.example.demo.entity;

import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data

public class ReportEntity {
    public List<LoanEntity> reportLoan;
    public List<ClientEntity> reportClient;
    public List<ToolEntity> reportTool;
}

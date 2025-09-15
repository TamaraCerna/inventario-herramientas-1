package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.ReportDTO;
import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // RF6.1
    @GetMapping("/loans")
    public ResponseEntity<List<ReportDTO>> getActiveLoans(
            @RequestParam(defaultValue = "Activo") StateLoan state) {
        List<ReportDTO> loans = reportService.listStateLoan(state);
        return ResponseEntity.ok(loans);
    }


    // RF6.2
    @GetMapping("/overdue-customers")
    public ResponseEntity<List<ClientEntity>> getOverdueCustomers() {
        List<ClientEntity> clients = reportService.listOverdueCustomers();
        return ResponseEntity.ok(clients);
    }

    // RF6.3
    @GetMapping("/ranking-tools")
    public ResponseEntity<List<Object[]>> getRankingTools() {
        List<Object[]> ranking = reportService.rankingTools();
        return ResponseEntity.ok(ranking);
    }

}


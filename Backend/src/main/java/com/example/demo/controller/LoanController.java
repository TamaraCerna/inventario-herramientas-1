package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/loans")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {
    @Autowired
    private LoanService loanService;

    // RF2.1
    @GetMapping("/clients")
    public List<ClientEntity> getClientsLoan() {
        return loanService.getClients();
    }

    @GetMapping("/tools")
    public List<ToolEntity> getToolsLoan() {
        return loanService.getTools();
    }

    @PostMapping("/create")
    public ResponseEntity<LoanEntity> createLoan(
            @RequestParam int clientId,
            @RequestParam int toolId,
            @RequestParam String initDate,
            @RequestParam String finishDate) {

        LoanEntity loan = loanService.qualifiedClient(
                clientId,
                toolId,
                LocalDate.parse(initDate),
                LocalDate.parse(finishDate)
        );

        return ResponseEntity.ok(loan);
    }

    //RF2.3
    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoansByClient(@RequestParam int clientId) {
        return ResponseEntity.ok(loanService.getActiveLoansByClient(clientId));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<String> returnTool(
            @PathVariable int loanId,
            @RequestParam int toolId,
            @RequestParam int userId) {
        LoanEntity loan = loanService.findLoanById(loanId);
        ToolEntity tool = loanService.findToolById(toolId);
        UserEntity user = loanService.findUserById(userId);
        loanService.returnTool(tool, loan, user);
        return ResponseEntity.ok("Devolución registrada con éxito.");
    }


    //RF2.4
    @PutMapping("/{id}/fine")
    public ResponseEntity<LoanEntity> calculateFine(@PathVariable int id) {
        LoanEntity updated = loanService.calculateFineById(id);
        return ResponseEntity.ok(updated);
    }

    //RF2.5
    @PostMapping("/block-client")
    public ResponseEntity<String> blockClient(@RequestBody ClientEntity client) {
        boolean canBorrow = loanService.blockClient(client);

        if (canBorrow) {
            return ResponseEntity.ok("El cliente puede solicitar un préstamo.");
        } else {
            return ResponseEntity.badRequest().body("El cliente está bloqueado y no puede solicitar un préstamo.");
        }
    }
}

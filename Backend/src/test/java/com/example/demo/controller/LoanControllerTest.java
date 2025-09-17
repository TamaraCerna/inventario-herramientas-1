package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    // RF2.1
    @Test
    void getClientsLoan_success() throws Exception {
        ClientEntity c1 = new ClientEntity();
        ClientEntity c2 = new ClientEntity();

        Mockito.when(loanService.getClients()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/loans/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Obtener herramientas
    @Test
    void getToolsLoan_success() throws Exception {
        ToolEntity t1 = new ToolEntity();
        ToolEntity t2 = new ToolEntity();

        Mockito.when(loanService.getTools()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/loans/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // RF2.1 - Crear préstamo
    @Test
    void createLoan_success() throws Exception {
        LoanEntity loan = new LoanEntity();
        loan.setLoanId(1);

        Mockito.when(loanService.qualifiedClient(
                        anyInt(),
                        anyInt(),
                        any(LocalDate.class),
                        any(LocalDate.class)))
                .thenReturn(loan);

        mockMvc.perform(post("/loans/create")
                        .param("clientId", "1")
                        .param("toolId", "10")
                        .param("initDate", "2025-01-01")
                        .param("finishDate", "2025-01-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1));
    }

    // RF2.3 - Préstamos activos por cliente
    @Test
    void getActiveLoansByClient_success() throws Exception {
        LoanDTO dto = new LoanDTO();
        dto.setClientName("Tamara");
        dto.setToolName("Martillo");

        Mockito.when(loanService.getActiveLoansByClient(1))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/loans/active").param("clientId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clientName").value("Tamara"))
                .andExpect(jsonPath("$[0].toolName").value("Martillo"));
    }

    // RF2.2/2.3 - Devolver herramienta
    @Test
    void returnTool_success() throws Exception {
        LoanEntity loan = new LoanEntity();
        ToolEntity tool = new ToolEntity();
        UserEntity user = new UserEntity();

        Mockito.when(loanService.findLoanById(1)).thenReturn(loan);
        Mockito.when(loanService.findToolById(10)).thenReturn(tool);
        Mockito.when(loanService.findUserById(100)).thenReturn(user);

        mockMvc.perform(put("/loans/1/return")
                        .param("toolId", "10")
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Devolución registrada con éxito."));

        Mockito.verify(loanService).returnTool(tool, loan, user);
    }

    // RF2.4 - Calcular multa
    @Test
    void calculateFine_success() throws Exception {
        LoanEntity updated = new LoanEntity();
        updated.setLoanId(5);

        Mockito.when(loanService.calculateFineById(5)).thenReturn(updated);

        mockMvc.perform(put("/loans/5/fine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(5));
    }

    // RF2.5 - Bloquear cliente
    @Test
    void blockClient_success() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setClientName("Tamara");

        // Caso: puede pedir préstamo
        Mockito.when(loanService.blockClient(any(ClientEntity.class))).thenReturn(true);

        mockMvc.perform(post("/loans/block-client")
                        .contentType("application/json")
                        .content("{\"clientName\":\"Tamara\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("El cliente puede solicitar un préstamo."));
    }

    @Test
    void blockClient_blocked() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setClientName("Tamara");

        // Caso: está bloqueado
        Mockito.when(loanService.blockClient(any(ClientEntity.class))).thenReturn(false);

        mockMvc.perform(post("/loans/block-client")
                        .contentType("application/json")
                        .content("{\"clientName\":\"Tamara\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El cliente está bloqueado y no puede solicitar un préstamo."));
    }
}


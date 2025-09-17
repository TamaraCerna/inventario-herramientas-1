package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.ReportDTO;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    // RF6.1
    @Test
    void getActiveLoans_defaultState_success() throws Exception {
        ReportDTO r1 = new ReportDTO();
        ReportDTO r2 = new ReportDTO();

        Mockito.when(reportService.listStateLoan(any(StateLoan.class)))
                .thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/reports/loans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    // RF6.1
    @Test
    void getActiveLoans_customState_success() throws Exception {
        ReportDTO r1 = new ReportDTO();

        Mockito.when(reportService.listStateLoan(StateLoan.Activo))
                .thenReturn(List.of(r1));

        mockMvc.perform(get("/reports/loans")
                        .param("state", "Activo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        Mockito.verify(reportService).listStateLoan(StateLoan.Activo);
    }

    // RF6.2
    @Test
    void getOverdueCustomers_success() throws Exception {
        ClientEntity c1 = new ClientEntity();
        ClientEntity c2 = new ClientEntity();

        Mockito.when(reportService.listOverdueCustomers())
                .thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/reports/overdue-customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(reportService).listOverdueCustomers();
    }

    // RF6.3
    @Test
    void getRankingTools_success() throws Exception {
        Object[] row1 = new Object[]{"Martillo", 5L};
        Object[] row2 = new Object[]{"Taladro", 3L};

        Mockito.when(reportService.rankingTools())
                .thenReturn(List.of(row1, row2));

        mockMvc.perform(get("/reports/ranking-tools"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(reportService).rankingTools();
    }
}


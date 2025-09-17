package com.example.demo.controller;

import com.example.demo.entity.KardexEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.service.KardexService;
import com.example.demo.service.ToolService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KardexController.class)
@AutoConfigureMockMvc(addFilters = false)
class KardexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KardexService kardexService;

    @MockBean
    private ToolService toolService;

    @Test
    void getToolHistory_success() throws Exception {
        // Arrange
        int toolId = 1;

        ToolEntity tool = new ToolEntity();
        // si tu ToolEntity tiene setId, puedes hacer:
        // tool.setIdTool(toolId);  // o el nombre que tenga tu campo

        KardexEntity k1 = new KardexEntity();
        KardexEntity k2 = new KardexEntity();

        Mockito.when(toolService.findById(toolId)).thenReturn(tool);
        Mockito.when(kardexService.historyKardex(tool))
                .thenReturn(List.of(k1, k2));

        // Act + Assert
        mockMvc.perform(get("/kardex/history/{toolId}", toolId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                // comprobamos que devuelve un arreglo de 2 elementos
                .andExpect(jsonPath("$.length()").value(2));

        // Verificamos llamadas a servicios
        Mockito.verify(toolService).findById(toolId);
        Mockito.verify(kardexService).historyKardex(tool);
    }

    @Test
    void searchKardex_success() throws Exception {
        // Arrange
        LocalDate init = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        KardexEntity k1 = new KardexEntity();

        Mockito.when(kardexService.seachKardex(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(k1));

        // Act + Assert
        mockMvc.perform(get("/kardex/search")
                        .param("init", init.toString()) // "2025-01-01"
                        .param("end", end.toString()))   // "2025-01-31"
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(1));

        Mockito.verify(kardexService)
                .seachKardex(eq(init), eq(end));
    }
}


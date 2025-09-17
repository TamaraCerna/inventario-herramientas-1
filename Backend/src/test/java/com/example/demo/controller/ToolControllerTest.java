package com.example.demo.controller;

import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.enums.StateTool;
import com.example.demo.service.LoanService;
import com.example.demo.service.ToolService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToolController.class)
@AutoConfigureMockMvc(addFilters = false)
class ToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolService toolService;

    @MockBean
    private LoanService loanService;

    @MockBean
    private UserService userService;

    // RF1.1
    @Test
    void getToolsLoan_success() throws Exception {
        ToolEntity t1 = new ToolEntity();
        ToolEntity t2 = new ToolEntity();

        Mockito.when(loanService.getTools())
                .thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(loanService).getTools();
    }

    // RF1.1
    @Test
    void registerTool_success() throws Exception {
        ToolEntity tool = new ToolEntity();
        tool.setIdTool(99);

        Mockito.when(toolService.registerNewTool(
                anyString(),
                any(StateTool.class),
                anyInt(),
                anyInt()
        )).thenReturn(tool);

        mockMvc.perform(post("/tools/register")
                        .param("name_tool", "Martillo Makita HDFTHA21")
                        .param("category_tool", StateTool.Martillo.name())
                        .param("replacement_value_tool", "30000")
                        .param("userId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTool").value(99));

        Mockito.verify(toolService).registerNewTool(
                eq("Martillo Makita HDFTHA21"),   // 👈 mismo texto que en .param("name_tool", ...)
                eq(StateTool.Martillo),
                eq(30000),
                eq(5)
        );

    }

    // RF1.2
    @Test
    void deleteTool_success() throws Exception {
        mockMvc.perform(delete("/tools/10"))
                .andExpect(status().isOk());

        Mockito.verify(toolService).deleteToolById(10);
    }
}


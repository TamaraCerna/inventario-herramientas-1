package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    void registerClient_success() throws Exception {
        // Arrange
        ClientEntity client = new ClientEntity();
        client.setClientName("Tamara");
        client.setClientRut("12.345.678-9");
        client.setClientPhone("999999999");
        client.setClientEmail("tamara@test.com");

        Mockito.when(clientService.registerNewClient(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(client);

        // Act + Assert
        mockMvc.perform(post("/clients/register")
                        .param("name", "Tamara")
                        .param("rut", "12.345.678-9")
                        .param("phone", "999999999")
                        .param("email", "tamara@test.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Tamara"))
                .andExpect(jsonPath("$.clientRut").value("12.345.678-9"));
    }

    @Test
    void rectifyClientStatus_success() throws Exception {
        ClientEntity fixed = new ClientEntity();
        fixed.setClientId(1L);
        fixed.setClientName("Cliente Actualizado");

        Mockito.when(clientService.rectifClientStatus(Mockito.any(ClientEntity.class)))
                .thenReturn(fixed);

        mockMvc.perform(put("/clients/1/rectify-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.clientName").value("Cliente Actualizado"));
    }

}


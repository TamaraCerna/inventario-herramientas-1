package com.example.demo.controller;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Roler;
import com.example.demo.service.LoanService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // <<< Desactiva seguridad
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LoanService loanService;


    // RF7.1
    @Test
    void registerUser_success() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        Mockito.when(userService.registerUser(anyString(), anyString(), anyString()))
                .thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .param("name", "Tamara")
                        .param("email", "tamara@test.com")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }


    // RF7.2
    @Test
    void assignRole_success() throws Exception {
        Mockito.doNothing().when(userService).rolUser(any(UserEntity.class), any(Roler.class));

        mockMvc.perform(put("/users/5/role")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Rol asignado correctamente."));
    }


    // RF7.3
    @Test
    void checkUserRole_success() throws Exception {
        Mockito.when(userService.checkUser(any(UserEntity.class))).thenReturn(true);

        mockMvc.perform(get("/users/5/check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    // RF7.4
    @Test
    void login_success() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(7);
        user.setUserName("Tamara");
        user.setUserEmail("tami@test.com");

        Mockito.when(userService.findByNameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/login")
                        .param("name", "Tamara")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Tamara"))
                .andExpect(jsonPath("$.email").value("tami@test.com"));
    }


    @Test
    void login_wrongCredentials() throws Exception {
        Mockito.when(userService.findByNameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/users/login")
                        .param("name", "wrong")
                        .param("password", "bad"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario o contraseña incorrectos."));
    }


    // RF4.1
    @Test
    void getStockTools_success() throws Exception {
        Mockito.when(userService.getAllStockTools())
                .thenReturn(List.of(new StockToolsEntity(), new StockToolsEntity()));

        mockMvc.perform(get("/users/modify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    // RF4.2
    @Test
    void setTariffDay_success() throws Exception {
        Mockito.when(userService.tariffAdminDay(any(), any(), anyInt()))
                .thenReturn(true);

        mockMvc.perform(put("/users/tariff/day")
                        .param("userId", "1")
                        .param("tariffId", "10")
                        .param("day", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Tarifa diaria actualizada correctamente."));
    }


    // RF4.3
    @Test
    void getAllTools_success() throws Exception {
        Mockito.when(loanService.getTools())
                .thenReturn(List.of(new ToolEntity(), new ToolEntity(), new ToolEntity()));

        mockMvc.perform(get("/users/modify/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }


    @Test
    void updateToolReplacementValue_success() throws Exception {
        Mockito.when(userService.updateToolAdmin(any(), any(), anyInt()))
                .thenReturn(true);

        mockMvc.perform(put("/users/tool/replacement")
                        .param("toolId", "1")
                        .param("userId", "2")
                        .param("value", "5000"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Valor de reposición actualizado correctamente."));
    }
}

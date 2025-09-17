package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.KardexEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Move;
import com.example.demo.repository.KardexRepository;
import com.example.demo.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;

    @Mock
    private ToolRepository toolRepository; // no lo usamos directo, pero existe en el service

    @InjectMocks
    private KardexService kardexService;

    @Test
    void moveKardex() {
        // Arrange
        Move move = Move.Ingreso; // tu enum correcto
        LocalDate date = LocalDate.of(2025, 1, 10);

        ToolEntity tool = new ToolEntity();
        UserEntity user = new UserEntity();
        ClientEntity client = new ClientEntity();

        int registeredMoney = 5000;

        // Act
        kardexService.moveKardex(move, date, tool, user, client, registeredMoney);

        // Assert
        ArgumentCaptor<KardexEntity> captor = ArgumentCaptor.forClass(KardexEntity.class);
        verify(kardexRepository).save(captor.capture());

        KardexEntity saved = captor.getValue();

        assertEquals(move, saved.getKardexName());
        assertEquals(date, saved.getKardexDate());
        assertEquals(tool, saved.getKardexTool());
        assertEquals(user, saved.getKardexUser());
        assertEquals(client, saved.getKardexClient());
        assertEquals(registeredMoney, saved.getKardexRegisteredMoney());
    }

    @Test
    void historyKardex() {
        // Arrange
        ToolEntity tool = new ToolEntity();

        KardexEntity mov1 = new KardexEntity();
        KardexEntity mov2 = new KardexEntity();
        List<KardexEntity> movimientos = List.of(mov1, mov2);

        when(kardexRepository.findByKardexTool(tool)).thenReturn(movimientos);

        // Act
        List<KardexEntity> result = kardexService.historyKardex(tool);

        // Assert
        assertEquals(2, result.size());
        assertSame(movimientos, result); // es la misma lista
        verify(kardexRepository, times(1)).findByKardexTool(tool);
    }

    @Test
    void seachKardex_deberiaConsultarMovimientosPorRangoDeFechas() {
        // Arrange
        LocalDate init = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        KardexEntity mov = new KardexEntity();
        List<KardexEntity> movimientos = List.of(mov);

        when(kardexRepository.findByDateRange(init, end)).thenReturn(movimientos);

        // Act
        List<KardexEntity> result = kardexService.seachKardex(init, end);

        // Assert
        assertEquals(1, result.size());
        assertSame(movimientos, result);
        verify(kardexRepository, times(1)).findByDateRange(init, end);
    }
}


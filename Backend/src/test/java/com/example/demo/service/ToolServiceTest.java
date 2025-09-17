package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.Move;
import com.example.demo.entity.enums.State;
import com.example.demo.entity.enums.StateTool;
import com.example.demo.repository.StockToolsRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KardexService kardexService;

    @Mock
    private UserService userService;

    @Mock
    private StockToolsRepository stockToolsRepository;

    @InjectMocks
    private ToolService toolService;

    //  RF1.1
    @Test
    void registerNewTool_registrarHerramientaYActualizarStockYKardex() {
        // Arrange
        int userId = 10;

        UserEntity user = new UserEntity();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockTool(StateTool.Martillo);
        stock.setStockQuantity(5);

        when(stockToolsRepository.findBystockTool(StateTool.Martillo))
                .thenReturn(stock);

        ToolEntity savedTool = new ToolEntity();
        savedTool.setIdTool(100);

        when(toolRepository.save(any(ToolEntity.class))).thenReturn(savedTool);

        // Act
        ToolEntity tool = toolService.registerNewTool(
                "Martillo eléctrico Makita HM1203C",
                StateTool.Martillo,
                8000,
                userId
        );

        // Assert: herramienta creada
        assertNotNull(tool);

        // Assert: stock actualizado
        assertEquals(6, stock.getStockQuantity());
        verify(stockToolsRepository).save(stock);

        // Assert: kardex registrado
        verify(kardexService, times(1)).moveKardex(
                eq(Move.Ingreso),
                any(LocalDate.class),
                eq(tool),
                eq(user),
                isNull(),
                eq(8000)
        );

        // Assert: herramienta guardada
        verify(toolRepository, times(1)).save(any(ToolEntity.class));
    }

    @Test
    void registerNewTool_excepcionDatosInvalidos() {
        assertThrows(RuntimeException.class,
                () -> toolService.registerNewTool(null, StateTool.Martillo, 5000, 1));

        assertThrows(RuntimeException.class,
                () -> toolService.registerNewTool("Martillo", null, 5000, 1));

        assertThrows(RuntimeException.class,
                () -> toolService.registerNewTool("Martillo", StateTool.Martillo, -1, 1));
    }

    @Test
    void registerNewTool_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> toolService.registerNewTool(
                        "Martillo", StateTool.Martillo, 5000, 99
                ));
    }

    //  RF1.2
    @Test
    void deleteToolById_eliminarHerramientaYActualizarStock() {
        ToolEntity tool = new ToolEntity();
        tool.setIdTool(1);

        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(3);

        tool.setStockTools(stock);

        when(toolRepository.findById(1)).thenReturn(Optional.of(tool));

        // Act
        toolService.deleteToolById(1);

        // Assert
        assertEquals(2, stock.getStockQuantity());
        verify(stockToolsRepository).save(stock);
        verify(toolRepository).delete(tool);
    }

    @Test
    void deleteToolById_deberiaLanzarExcepcionSiHerramientaNoExiste() {
        when(toolRepository.findById(20)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> toolService.deleteToolById(20));
    }

    //  findById
    @Test
    void findById_retornarHerramienta() {
        ToolEntity tool = new ToolEntity();
        tool.setIdTool(1);

        when(toolRepository.findById(1)).thenReturn(Optional.of(tool));

        ToolEntity result = toolService.findById(1);

        assertEquals(tool, result);
    }

    @Test
    void findById_cuandoNoExiste_debeLanzarExcepcion() {
        when(toolRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> toolService.findById(1));
    }

    //  getIdUser
    @Test
    void getIdUser_retornarUsuario() {
        UserEntity user = new UserEntity();
        user.setUserId(5);

        when(userRepository.findById(5)).thenReturn(Optional.of(user));

        UserEntity result = toolService.getIdUser(5);
        assertEquals(user, result);
    }

    @Test
    void getIdUser_lanzarExcepcion() {
        when(userRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> toolService.getIdUser(3));
    }
}


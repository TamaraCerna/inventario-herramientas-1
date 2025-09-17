package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.Move;
import com.example.demo.entity.enums.State;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ToolRepository toolRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private KardexService kardexService;
    @Mock
    private StockToolsRepository stockToolsRepository;

    @InjectMocks
    private LoanService loanService;

    // RF2.4
    @Test
    void clientFine_prestamoActivoYAtrasado() {
        // Arrange
        LoanEntity loan = new LoanEntity();
        loan.setLoanState(StateLoan.Activo);
        loan.setLoanDateFinish(LocalDate.now().minusDays(3)); // 3 días de atraso

        StockToolsEntity stock = new StockToolsEntity();
        stock.setFineDaily(1000); // 1000 por día

        ToolEntity tool = new ToolEntity();
        tool.setStockTools(stock);

        loan.setLoanTool(tool);

        // Act
        loanService.clientFine(loan);

        // Assert
        assertEquals(3000, loan.getLoanPenalty());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void clientFine_noActivoNoAtrasado() {
        // Prestamo no activo
        LoanEntity loanNoActivo = new LoanEntity();
        loanNoActivo.setLoanState(StateLoan.Finalizado);
        loanNoActivo.setLoanDateFinish(LocalDate.now().minusDays(5));

        // Prestamo activo sin atraso
        LoanEntity loanSinAtraso = new LoanEntity();
        loanSinAtraso.setLoanState(StateLoan.Activo);
        loanSinAtraso.setLoanDateFinish(LocalDate.now().plusDays(1));

        loanService.clientFine(loanNoActivo);
        loanService.clientFine(loanSinAtraso);

        verify(loanRepository, never()).save(any());
    }

    // RF2.5
    @Test
    void blockClient_clientServiceRestringido() {
        // Arrange
        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);

        ClientEntity restringido = new ClientEntity();
        restringido.setClientState(StateClient.Restringido);

        when(clientService.rectifClientStatus(client)).thenReturn(restringido);

        // Act
        boolean result = loanService.blockClient(client);

        // Assert
        assertFalse(result);
        verify(clientService, times(1)).rectifClientStatus(client);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void blockClient_prestamoConMulta() {
        // Arrange
        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);

        LoanEntity loan = new LoanEntity();
        loan.setLoanState(StateLoan.Activo);
        loan.setLoanDateFinish(LocalDate.now().minusDays(2)); // 2 días de atraso

        StockToolsEntity stock = new StockToolsEntity();
        stock.setFineDaily(500);
        ToolEntity tool = new ToolEntity();
        tool.setStockTools(stock);
        loan.setLoanTool(tool);

        client.setClientLoanActive(List.of(loan));
        when(clientService.rectifClientStatus(client)).thenReturn(client);

        // Act
        boolean result = loanService.blockClient(client);

        // Assert
        assertFalse(result);
        assertEquals(StateClient.Restringido, client.getClientState());
        assertTrue(loan.getLoanPenalty() > 0);
        verify(loanRepository, times(2)).save(loan);
    }

    // calculateFineById
    @Test
    void calculateFineById_existePrestamo() {
        LoanEntity loan = new LoanEntity();
        loan.setLoanState(StateLoan.Activo);
        loan.setLoanDateFinish(LocalDate.now().minusDays(1));

        StockToolsEntity stock = new StockToolsEntity();
        stock.setFineDaily(1000);
        ToolEntity tool = new ToolEntity();
        tool.setStockTools(stock);
        loan.setLoanTool(tool);

        when(loanRepository.findById(1)).thenReturn(Optional.of(loan));

        LoanEntity result = loanService.calculateFineById(1);

        assertEquals(loan, result);
        assertTrue(result.getLoanPenalty() > 0);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void calculateFineById_cuandoNoExistePrestamo_debeLanzarExcepcion() {
        when(loanRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> loanService.calculateFineById(99));
    }

    @Test
    void inicialStateTool_herramientaConStockPositivo() {
        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(3);

        ToolEntity tool = new ToolEntity();
        tool.setInitialStateTool(State.Disponible);
        tool.setStockTools(stock);

        assertTrue(loanService.inicialStateTool(tool));
    }

    @Test
    void inicialStateTool_herramientaSinStock() {
        StockToolsEntity stockCero = new StockToolsEntity();
        stockCero.setStockQuantity(0);

        ToolEntity toolNoDisponible = new ToolEntity();
        toolNoDisponible.setInitialStateTool(State.Prestada);
        toolNoDisponible.setStockTools(stockCero);

        assertFalse(loanService.inicialStateTool(toolNoDisponible));
    }

    // updateStock
    @Test
    void updateStock_debeSumarCantidadYGuardar() {
        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(5);

        when(stockToolsRepository.save(stock)).thenReturn(stock);

        StockToolsEntity result = loanService.updateStock(stock, 2);

        assertEquals(7, result.getStockQuantity());
        verify(stockToolsRepository, times(1)).save(stock);
    }

    // returnTool
    @Test
    void returnTool_estadoStockPrestamoYKardex() {
        // Arrange
        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(1);

        ToolEntity tool = new ToolEntity();
        tool.setInitialStateTool(State.Prestada);
        tool.setStockTools(stock);

        LoanEntity loan = new LoanEntity();
        loan.setLoanState(StateLoan.Activo);
        loan.setLoanPenalty(500);
        loan.setLoanDateFinish(LocalDate.now());

        UserEntity user = new UserEntity();

        when(stockToolsRepository.save(stock)).thenReturn(stock);

        // Act
        loanService.returnTool(tool, loan, user);

        // Assert estado herramienta
        assertEquals(State.Disponible, tool.getInitialStateTool());
        assertEquals(2, stock.getStockQuantity()); // +1

        // Guardado en repositorios
        verify(toolRepository, times(1)).save(tool);
        verify(loanRepository, times(1)).save(loan);

        // Kardex
        verify(kardexService, times(1)).moveKardex(
                eq(Move.Devolución),
                any(LocalDate.class),
                eq(tool),
                eq(user),
                isNull(), // cliente es null en returnTool
                eq(loan.getLoanPenalty())
        );
    }

    // qualifiedClient
    @Test
    void qualifiedClient_clienteAptoYHerramientaDisponible() {
        int clientId = 1;
        int toolId = 2;
        LocalDate init = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);
        client.setClientLoanActive(List.of());

        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(3);

        ToolEntity tool = new ToolEntity();
        tool.setInitialStateTool(State.Disponible);
        tool.setStockTools(stock);
        tool.setReplacementValueTool(10000);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(toolRepository.findById(toolId)).thenReturn(Optional.of(tool));
        when(clientService.rectifClientStatus(client)).thenReturn(client);
        when(stockToolsRepository.save(stock)).thenReturn(stock);
        when(loanRepository.save(any(LoanEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LoanEntity result = loanService.qualifiedClient(clientId, toolId, init, fin);

        // Assert préstamo creado correctamente
        assertNotNull(result);
        assertEquals(client, result.getLoanClient());
        assertEquals(tool, result.getLoanTool());
        assertEquals(StateLoan.Activo, result.getLoanState());
        assertEquals(init, result.getLoanDateInit());
        assertEquals(fin, result.getLoanDateFinish());
        assertEquals(stock, result.getLoanTariff());

        // Herramienta actualizada
        assertEquals(State.Prestada, tool.getInitialStateTool());
        assertEquals(2, stock.getStockQuantity()); // 3 - 1

        // Interacciones
        verify(loanRepository, times(1)).save(any(LoanEntity.class));
        verify(toolRepository, times(1)).save(tool);
        verify(kardexService, times(1)).moveKardex(
                eq(Move.Préstamo),
                any(LocalDate.class),
                eq(tool),
                isNull(),   // user null
                eq(client),
                eq(tool.getReplacementValueTool())
        );
    }

    @Test
    void qualifiedClient_HerramientaNoDisponible() {
        int clientId = 1;
        int toolId = 2;
        LocalDate init = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);
        client.setClientLoanActive(List.of());

        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(0);

        ToolEntity tool = new ToolEntity();
        tool.setInitialStateTool(State.Disponible);
        tool.setStockTools(stock);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(toolRepository.findById(toolId)).thenReturn(Optional.of(tool));

        assertThrows(IllegalStateException.class,
                () -> loanService.qualifiedClient(clientId, toolId, init, fin));
    }

    @Test
    void qualifiedClient_ClienteNoApto() {
        int clientId = 1;
        int toolId = 2;
        LocalDate init = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Restringido);
        client.setClientLoanActive(List.of());

        StockToolsEntity stock = new StockToolsEntity();
        stock.setStockQuantity(5);

        ToolEntity tool = new ToolEntity();
        tool.setInitialStateTool(State.Disponible);
        tool.setStockTools(stock);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(toolRepository.findById(toolId)).thenReturn(Optional.of(tool));

        // blockClient devuelve false
        when(clientService.rectifClientStatus(client)).thenReturn(client);

        assertThrows(IllegalArgumentException.class,
                () -> loanService.qualifiedClient(clientId, toolId, init, fin));
    }
}

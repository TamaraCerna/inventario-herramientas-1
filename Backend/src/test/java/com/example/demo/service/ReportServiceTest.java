package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.ReportDTO;
import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.StockToolsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StockToolsRepository stockRepository;

    @InjectMocks
    private ReportService reportService;

    // RF6.1
    @Test
    void listStateLoan_deberiaMapearPrestamosAVigentesYAtrasados() {
        // Arrange
        LocalDate today = LocalDate.now();

        // Préstamo atrasado
        LoanEntity loanAtrasado = new LoanEntity();
        loanAtrasado.setLoanId(1);
        loanAtrasado.setLoanState(StateLoan.Activo);
        loanAtrasado.setLoanDateInit(today.minusDays(10));
        loanAtrasado.setLoanDateFinish(today.minusDays(1));

        ClientEntity client1 = new ClientEntity();
        client1.setClientName("Cliente A");
        loanAtrasado.setLoanClient(client1);

        ToolEntity tool1 = new ToolEntity();
        tool1.setNameTool("Taladro");
        loanAtrasado.setLoanTool(tool1);

        // Préstamo vigente
        LoanEntity loanVigente = new LoanEntity();
        loanVigente.setLoanId(2);
        loanVigente.setLoanState(StateLoan.Activo);
        loanVigente.setLoanDateInit(today.minusDays(2));
        loanVigente.setLoanDateFinish(today.plusDays(3));

        ClientEntity client2 = new ClientEntity();
        client2.setClientName("Cliente B");
        loanVigente.setLoanClient(client2);

        ToolEntity tool2 = new ToolEntity();
        tool2.setNameTool("Martillo");
        loanVigente.setLoanTool(tool2);

        List<LoanEntity> loans = List.of(loanAtrasado, loanVigente);

        when(loanRepository.findByLoanState(StateLoan.Activo)).thenReturn(loans);

        // Act
        List<ReportDTO> result = reportService.listStateLoan(StateLoan.Activo);

        // Assert
        assertEquals(2, result.size());

        ReportDTO r1 = result.get(0);
        assertEquals(1, r1.getLoanId());
        assertEquals("Cliente A", r1.getClientName());
        assertEquals("Taladro", r1.getToolName());
        assertEquals(StateLoan.Activo, r1.getLoanState());
        assertEquals("Atrasado", r1.getEstadoDetalle());

        ReportDTO r2 = result.get(1);
        assertEquals(2, r2.getLoanId());
        assertEquals("Cliente B", r2.getClientName());
        assertEquals("Martillo", r2.getToolName());
        assertEquals(StateLoan.Activo, r2.getLoanState());
        assertEquals("Vigente", r2.getEstadoDetalle());

        verify(loanRepository, times(1)).findByLoanState(StateLoan.Activo);
    }

    // RF6.2
    @Test
    void listOverdueCustomers_deberiaRetornarClientesRestringidos() {
        // Arrange
        ClientEntity c1 = new ClientEntity();
        c1.setClientName("Cliente Restringido 1");
        c1.setClientState(StateClient.Restringido);

        ClientEntity c2 = new ClientEntity();
        c2.setClientName("Cliente Restringido 2");
        c2.setClientState(StateClient.Restringido);

        List<ClientEntity> clientes = List.of(c1, c2);

        when(clientRepository.findByClientState(StateClient.Restringido))
                .thenReturn(clientes);

        // Act
        List<ClientEntity> result = reportService.listOverdueCustomers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Cliente Restringido 1", result.get(0).getClientName());
        assertEquals("Cliente Restringido 2", result.get(1).getClientName());

        verify(clientRepository, times(1))
                .findByClientState(StateClient.Restringido);
    }

    // RF6.3
    @Test
    void rankingTools_deberiaRetornarRankingDesdeRepositorio() {
        // Arrange
        Object[] row1 = new Object[]{"Taladro", 10L};
        Object[] row2 = new Object[]{"Martillo", 5L};
        List<Object[]> ranking = List.of(row1, row2);

        when(stockRepository.getToolRanking()).thenReturn(ranking);

        // Act
        List<Object[]> result = reportService.rankingTools();

        // Assert
        assertEquals(2, result.size());
        assertSame(ranking, result);
        verify(stockRepository, times(1)).getToolRanking();
    }
}


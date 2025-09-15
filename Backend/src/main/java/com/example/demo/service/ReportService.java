package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.ReportDTO;
import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.StockToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StockToolsRepository stockRepository;

    //RF6.1 Listar préstamos activos y su estado (vigentes, atrasados).
    public List<ReportDTO> listStateLoan(StateLoan state) {
        List<LoanEntity> loans = loanRepository.findByLoanState(state);
        LocalDate today = LocalDate.now();

        return loans.stream().map(loan -> {
            String detalle = loan.getLoanDateFinish().isBefore(today) ? "Atrasado" : "Vigente";

            return new ReportDTO(
                    loan.getLoanId(),
                    loan.getLoanClient().getClientName(),
                    loan.getLoanTool().getNameTool(),
                    loan.getLoanState(),
                    loan.getLoanDateInit(),
                    loan.getLoanDateFinish(),
                    detalle
            );
        }).toList();
    }

    //RF6.2 Listar clientes con atrasos.
    public List<ClientEntity> listOverdueCustomers() {
        List<ClientEntity> client = clientRepository.findByClientState(StateClient.Restringido);
        return client;
    }

    // RF6.3 Reporte de las herramientas más prestadas (Ranking)
    public List<Object[]> rankingTools() {
        return stockRepository.getToolRanking();
    }


}

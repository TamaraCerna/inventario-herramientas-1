package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.Move;
import com.example.demo.entity.enums.State;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.entity.enums.StateLoan;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private ClientService clientService;
    @Autowired
    private KardexService kardexService;
    @Autowired
    private StockToolsRepository stockToolsRepository;

    //RF2.5 Bloquear nuevos préstamos a clientes con atrasos no regularizados.
    public boolean blockClient(ClientEntity client){
        client = clientService.rectifClientStatus(client);
        if(client.getClientState() == StateClient.Restringido){
            return false;
        }
        for(LoanEntity loan : client.getClientLoanActive()){
            clientFine(loan);
            if(loan.getLoanPenalty()>0){
                client.setClientState(StateClient.Restringido);
                loanRepository.save(loan);
                return false;
            }
        }
        return true;
    }

    // RF2.4 - Calcular automáticamente multas por atraso, para 1 préstamo.
    public void clientFine(LoanEntity loan) {
        if (loan.getLoanState() == StateLoan.Activo) {
            long daysLate = DAYS.between(loan.getLoanDateFinish(), LocalDate.now());
            if (daysLate > 0) {
                int fine = loan.getLoanTool().getStockTools().getFineDaily() * (int) daysLate;
                loan.setLoanPenalty(fine);
                // Opcional: cambiar estado a Finalizado si también devuelves aquí
                // loan.setLoanState(StateLoan.Finalizado);
                loanRepository.save(loan);
            }
        }
    }

    @Transactional
    public LoanEntity calculateFineById(int loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado: " + loanId));

        clientFine(loan);
        return loan;
    }


    //RF2.3 Registrar devolución de herramienta, actualizando estado, stock y el kardex.
    public List<LoanDTO> getActiveLoansByClient(int clientId) {
        return loanRepository.findActiveLoansByClient(clientId, StateLoan.Activo);

    }

    public LoanEntity findLoanById(int id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado: " + id));
    }
    public ToolEntity findToolById(int id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada: " + id));
    }
    public UserEntity findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
    }

    public StockToolsEntity updateStock(StockToolsEntity stock, int num){
        int acc = stock.getStockQuantity() + num;
        stock.setStockQuantity(acc);
        return stockToolsRepository.save(stock);
    }
    @Transactional
    public void returnTool(ToolEntity toolEntity, LoanEntity loan, UserEntity user) {
        clientFine(loan);
        toolEntity.setInitialStateTool(State.Disponible);
        updateStock(toolEntity.getStockTools(), 1);
        toolRepository.save(toolEntity);

        loan.setLoanState(StateLoan.Finalizado);
        loanRepository.save(loan);
        kardexService.moveKardex(
                Move.Devolución,
                LocalDate.now(),
                toolEntity,
                user,
                null,
                loan.getLoanPenalty()
        );
    }

    //RF2.2 Validar disponibilidad antes de autorizar el préstamo.
    public boolean inicialStateTool(ToolEntity tool) {

        return tool.getInitialStateTool() == State.Disponible
                && tool.getStockTools().getStockQuantity() > 0;
    }


    //RF2.1 Registrar un préstamo asociando cliente y herramienta, con fecha de entrega y
    //fecha pactada de devolución. Se actualiza el kardex.

    public List<ClientEntity> getClients() {
        return clientRepository.findAll();
    }
    public List<ToolEntity> getTools() {
        return toolRepository.findAll();
    }

    @Transactional
    public LoanEntity qualifiedClient(int clientId, int toolId,
                                      LocalDate init, LocalDate fin) {

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + clientId));

        ToolEntity tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada: " + toolId));

        StockToolsEntity stockTools = tool.getStockTools();

        if (!inicialStateTool(tool)) {
            throw new IllegalStateException("Herramienta no disponible para préstamo.");
        }
        if (!blockClient(client)) {
            throw new IllegalArgumentException("Cliente no apto para préstamo.");
        }
        LoanEntity loan = new LoanEntity();
        loan.setLoanClient(client);
        loan.setLoanTool(tool);
        loan.setLoanState(StateLoan.Activo);
        loan.setLoanDateInit(init);
        loan.setLoanDateFinish(fin);
        loan.setLoanTariff(stockTools);

        kardexService.moveKardex(
                Move.Préstamo,
                LocalDate.now(),
                tool,
                null,
                client,
                tool.getReplacementValueTool()
        );

        tool.setInitialStateTool(State.Prestada);
        updateStock(tool.getStockTools(), -1);
        toolRepository.save(tool);

        return loanRepository.save(loan);
    }


}

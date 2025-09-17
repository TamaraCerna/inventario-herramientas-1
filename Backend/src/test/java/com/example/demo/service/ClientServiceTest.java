package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void registerNewClient() {
        // Arrange
        String name = "Juan Pérez";
        String rut = "11.111.111-1";
        String phone = "912345678";
        String email = "juan@example.com";

        ClientEntity savedClient = new ClientEntity();
        savedClient.setClientName(name);
        savedClient.setClientRut(rut);
        savedClient.setClientPhone(phone);
        savedClient.setClientEmail(email);
        savedClient.setClientState(StateClient.Activo);

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(savedClient);

        // Act
        ClientEntity result = clientService.registerNewClient(name, rut, phone, email);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getClientName());
        assertEquals(rut, result.getClientRut());
        assertEquals(phone, result.getClientPhone());
        assertEquals(email, result.getClientEmail());
        assertEquals(StateClient.Activo, result.getClientState());

        // verifica llamado al repositorio
        verify(clientRepository, times(1)).save(any(ClientEntity.class));
    }
    @Test
    void rectifClientStatus_PrestamoConMulta() {
        // Arrange
        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);

        LoanEntity loanConMulta = new LoanEntity();
        loanConMulta.setLoanPenalty(1000); // multa distinta de 0

        client.setClientLoanActive(List.of(loanConMulta));

        when(clientRepository.save(client)).thenReturn(client);

        // Act
        ClientEntity result = clientService.rectifClientStatus(client);

        // Assert
        assertEquals(StateClient.Restringido, result.getClientState());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void rectifClientStatus_noTienePrestamosConMulta() {
        // Arrange
        ClientEntity client = new ClientEntity();
        client.setClientState(StateClient.Activo);

        LoanEntity loanSinMulta = new LoanEntity();
        loanSinMulta.setLoanPenalty(0); // sin multa

        client.setClientLoanActive(List.of(loanSinMulta));

        // Act
        ClientEntity result = clientService.rectifClientStatus(client);

        // Assert
        assertEquals(StateClient.Activo, result.getClientState());
        // nunca se debería llamar a save
        verify(clientRepository, never()).save(any(ClientEntity.class));
    }
}



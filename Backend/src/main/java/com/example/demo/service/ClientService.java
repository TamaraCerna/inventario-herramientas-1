package com.example.demo.service;


import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.LoanEntity;
import com.example.demo.entity.enums.StateClient;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    //RF3.1 Registrar información de clientes (nombre, contacto, rut, estado).
    public ClientEntity registerNewClient(String name, String rut, String phone, String email){
        ClientEntity client = new ClientEntity();
        client.setClientName(name);
        client.setClientRut(rut);
        client.setClientPhone(phone);
        client.setClientEmail(email);
        client.setClientState(StateClient.Activo);
        return clientRepository.save(client);
    }

    //RF3.2 Cambiar estado de cliente a “restringido” en caso de atrasos.
    public ClientEntity rectifClientStatus(ClientEntity client){
        for(LoanEntity loan : client.getClientLoanActive()) {
            if (loan.getLoanPenalty() != 0) {
                client.setClientState(StateClient.Restringido);
                return clientRepository.save(client);
            }
        }
        return client;
    }
}

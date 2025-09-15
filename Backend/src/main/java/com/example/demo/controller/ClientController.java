package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // RF3.1
    @PostMapping("/register")
    public ResponseEntity<ClientEntity> registerClient(
            @RequestParam String name,
            @RequestParam String rut,
            @RequestParam String phone,
            @RequestParam String email) {

        ClientEntity client = clientService.registerNewClient(name, rut, phone, email);
        return ResponseEntity.ok(client);
    }

    // RF3.2
    @PutMapping("/{id}/rectify-status")
    public ResponseEntity<ClientEntity> rectifyClientStatus(@PathVariable Long id) {
        ClientEntity client = new ClientEntity();
        client.setClientId(id);

        ClientEntity updated = clientService.rectifClientStatus(client);
        return ResponseEntity.ok(updated);
    }
}


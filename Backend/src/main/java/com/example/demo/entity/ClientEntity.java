package com.example.demo.entity;

import com.example.demo.entity.enums.StateClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, length = 100)
    private String clientName;

    @Column(nullable = false, unique = true, length = 20)
    private String clientRut;

    @Column(length = 20)
    private String clientPhone;

    @Column(nullable = false, unique = true, length = 150)
    private String clientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StateClient clientState;

    @OneToMany(mappedBy = "loanClient")
    @JsonIgnore
    private List<LoanEntity> clientLoanActive;
}


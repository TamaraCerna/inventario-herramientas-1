package com.example.demo.entity;

import com.example.demo.entity.enums.Move;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "kardexs")
public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kardexId;

    @Enumerated(EnumType.STRING)
    private Move kardexName;

    private LocalDate kardexDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = true)
    private ToolEntity kardexTool;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity kardexUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity kardexClient;

    private int kardexRegisteredMoney;

}

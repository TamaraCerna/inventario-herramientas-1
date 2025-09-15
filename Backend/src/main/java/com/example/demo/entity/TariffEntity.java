package com.example.demo.entity;

import com.example.demo.entity.enums.StateLoan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tariff")
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tariffId;

    @Enumerated(EnumType.STRING)
    private StateLoan tariffStateLoan;

    private int penaltyDay;


}

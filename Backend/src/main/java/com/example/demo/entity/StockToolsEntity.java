package com.example.demo.entity;

import com.example.demo.entity.enums.StateTool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "stock_herramientas")
public class StockToolsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private StateTool stockTool;
    private int stockQuantity;
    private int tariffDaily;
    private int fineDaily;
}

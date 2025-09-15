package com.example.demo.entity;

import com.example.demo.entity.enums.State;
import com.example.demo.entity.enums.StateTool;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tools")
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int idTool;
    @Column(name = "name_tool", nullable = false, length = 100)
    public String nameTool;
    @Enumerated(EnumType.STRING)
    public StateTool categoryTool;
    @Enumerated(EnumType.STRING)
    public State initialStateTool;
    public int replacementValueTool;
    @OneToOne
    @JoinColumn(name = "stock_id")
    public StockToolsEntity stockTools;
}

package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.KardexEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Move;
import com.example.demo.repository.KardexRepository;
import com.example.demo.repository.ToolRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class KardexService {
    @Autowired
    private KardexRepository kardexRepository;
    @Autowired
    private ToolRepository toolRepository;

    //RF5.1 Registrar automáticamente en el kardex cada movimiento (registro nuevas
    //herramientas, préstamo, devolución, baja, reparación).
    public void moveKardex(Move move, LocalDate date, ToolEntity tool,
                               UserEntity user, ClientEntity client, int registered_money){
        KardexEntity kardexEntity = new KardexEntity();
        kardexEntity.setKardexName(move);
        kardexEntity.setKardexDate(date);
        kardexEntity.setKardexTool(tool);
        kardexEntity.setKardexUser(user);
        kardexEntity.setKardexClient(client);
        kardexEntity.setKardexRegisteredMoney(registered_money);
        kardexRepository.save(kardexEntity);
    }


    //RF5.2 Consultar historial de movimientos por cada herramienta.
    public List<KardexEntity> historyKardex(ToolEntity tool) {
        return kardexRepository.findByKardexTool(tool);
    }

    //RF5.3 Generar listado de movimientos por rango de fechas.
    public List<KardexEntity> seachKardex(LocalDate init, LocalDate end) {
        return kardexRepository.findByDateRange(init, end);
    }

}

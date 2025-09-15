package com.example.demo.service;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Move;
import com.example.demo.entity.enums.State;
import com.example.demo.entity.enums.StateTool;
import com.example.demo.entity.ToolEntity;
import com.example.demo.repository.StockToolsRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KardexService kardexService;
    @Autowired
    private UserService userService;
    @Autowired
    private StockToolsRepository stockToolsRepository;

    //RF1.1, crear nueva herramienta
    public ToolEntity registerNewTool(String name_tool, StateTool category_tool,
                                            int replacement_value_tool, int userId){

        if (name_tool == null || category_tool == null || replacement_value_tool <= 0) {
            throw new RuntimeException("Datos inválidos para registrar la herramienta");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        StockToolsEntity stock = stockToolsRepository.findBystockTool(category_tool);

        ToolEntity tool = new ToolEntity();
        tool.setNameTool(name_tool);
        tool.setCategoryTool(category_tool);
        tool.setInitialStateTool(State.Disponible);
        tool.setReplacementValueTool(replacement_value_tool);
        toolRepository.save(tool);

        int value = stock.getStockQuantity();
        stock.setStockQuantity(value + 1);
        stockToolsRepository.save(stock);
        kardexService.moveKardex(Move.Ingreso, LocalDate.now(), tool, user, null, replacement_value_tool);
        return tool;
    }

    //RF1.2 Dar de baja herramientas dañadas o en desuso (solo Administrador).
    @Transactional
    public void deleteToolById(int id) {
        ToolEntity tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found with id " + id));

        StockToolsEntity stock = tool.getStockTools();
        if (stock != null && stock.getStockQuantity() > 0) {
            stock.setStockQuantity(stock.getStockQuantity() - 1);
            stockToolsRepository.save(stock);
        }

        toolRepository.delete(tool);
    }

    //RF5.2
    public ToolEntity findById(int id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada: " + id));
    }

    public UserEntity getIdUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("usuario no encontrado: " + id));
    }

    //devolución de herramienta

}

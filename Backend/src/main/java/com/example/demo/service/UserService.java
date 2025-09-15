package com.example.demo.service;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Roler;
import com.example.demo.repository.StockToolsRepository;
import com.example.demo.repository.TariffRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private StockToolsRepository stockToolsRepository;
    @Autowired
    private ToolRepository toolRepository;

    // RF7.2 Asignar roles de usuario (Administrador, Empleado).
    public void rolUser(UserEntity user, Roler rol){
        user.setUserType(rol);
        userRepository.save(user);
    }

    // RF7.1 Registrar usuarios del sistema con credenciales de acceso
    public UserEntity registerUser(String name, String email, String password) {
        UserEntity user = new UserEntity();
        user.setUserName(name);
        user.setUserEmail(email);
        user.setUserPassword(password);
        return userRepository.save(user);
    }


    // RF7.3 Validar permisos según rol.
    public boolean checkUser(UserEntity user){
        if(user.getUserType().equals(Roler.ADMIN)){
            return true;
        }
        System.out.println("Credenciales invalidas");
        return false;
    }

    // RF7.4 Autenticación con login y control de sesión.
    public Optional<UserEntity> findByNameAndPassword(String name, String password) {
        return userRepository.findByUserNameAndUserPassword(name, password);
    }

    public boolean loginUser(String name, String psw){
        UserEntity user = userRepository.findByUserName(name);
        if (user != null && user.getUserPassword().equals(psw)) {
            System.out.println("Ingreso exitoso");
            return true;
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
            return false;
        }
    }

    //RF4.1 Configurar tarifa diaria de arriendo (solo Administrador).
    public List<StockToolsEntity> getAllStockTools() {
        return stockToolsRepository.findAll();
    }

    // Devuelve un UserEntity o null si no existe
    public UserEntity getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // Devuelve un StockTool o null si no existe
    public StockToolsEntity getStockToolById(int id) {
        return stockToolsRepository.findById(id).orElse(null);
    }

    // Solo Admin puede actualizar tarifas
    public boolean tariffAdminDay(UserEntity user, StockToolsEntity stockTool, int day) {
        if (user != null && user.getUserType() == Roler.ADMIN) {
            stockTool.setTariffDaily(day);
            stockToolsRepository.save(stockTool);
            return true;
        }
        return false;
    }

    //RF4.2 Configurar tarifa diaria de multa por atraso (solo Administrador)

    public boolean tariffAdminFine(UserEntity user, StockToolsEntity stockTool, int fine) {
        if (user.getUserType() == Roler.ADMIN) {
            stockTool.setFineDaily(fine);
            stockToolsRepository.save(stockTool);
            return true;
        }
        return false;
    }




    //RF4.3 Registrar valor de reposición de cada herramienta (solo Administrador).
    public ToolEntity getToolById(int toolId) {
        return toolRepository.findById(toolId).orElse(null);
    }

    public boolean updateToolAdmin(ToolEntity tool, UserEntity user, int value) {
        if (user == null) {
            return false;
        }
        if (user.getUserType() != null && user.getUserType() == Roler.ADMIN) {
            tool.setReplacementValueTool(value);
            toolRepository.save(tool);
            return true;
        }

        return false;
    }



}

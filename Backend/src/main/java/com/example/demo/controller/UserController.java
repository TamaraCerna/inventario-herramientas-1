package com.example.demo.controller;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Roler;
import com.example.demo.service.LoanService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    LoanService loanService;

    // RF7.1 - Registrar usuario (acceso libre)
    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {

        UserEntity user = userService.registerUser(name, email, password);
        return ResponseEntity.ok(user);
    }

    // RF7.2 - Asignar rol (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<String> assignRole(
            @PathVariable int id,
            @RequestParam Roler role) {

        UserEntity user = new UserEntity();
        user.setUserId(id);
        userService.rolUser(user, role);

        return ResponseEntity.ok("✅ Rol asignado correctamente.");
    }

    // RF7.3 - Verificar rol de usuario (ADMIN o USER)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}/check")
    public ResponseEntity<Boolean> checkUserRole(@PathVariable int id) {
        UserEntity user = new UserEntity();
        user.setUserId(id);
        boolean result = userService.checkUser(user);
        return ResponseEntity.ok(result);
    }

    // RF7.4 - Login (ya no se usa si estás con Keycloak, pero lo dejo visible)
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String name,
            @RequestParam String password) {

        return userService.findByNameAndPassword(name, password)
                .<ResponseEntity<?>>map(user ->
                        ResponseEntity.ok(new LoginResponse(user.getUserId(), user.getUserName(), user.getUserEmail())))
                .orElse(ResponseEntity.badRequest().body("Usuario o contraseña incorrectos."));
    }

    record LoginResponse(int id, String name, String email) {}

    // RF4.1 - Obtener stock de herramientas (solo ADMIN o USER autenticado)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/modify")
    public List<StockToolsEntity> getStockTools() {
        return userService.getAllStockTools();
    }

    // RF4.2 - Modificar tarifas (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tariff/day")
    public ResponseEntity<String> setTariffDay(
            @RequestParam int userId,
            @RequestParam int tariffId,
            @RequestParam int day) {

        boolean updated = userService.tariffAdminDay(
                userService.getUserById(userId),
                userService.getStockToolById(tariffId),
                day
        );

        return updated
                ? ResponseEntity.ok("✅ Tarifa diaria actualizada correctamente.")
                : ResponseEntity.status(403).body("❌ Solo un Admin puede modificar tarifas.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tariff/fine")
    public ResponseEntity<String> setTariffFine(
            @RequestParam int userId,
            @RequestParam int tariffId,
            @RequestParam int fine) {

        boolean updated = userService.tariffAdminFine(
                userService.getUserById(userId),
                userService.getStockToolById(tariffId),
                fine
        );

        return updated
                ? ResponseEntity.ok("✅ Multa diaria actualizada correctamente.")
                : ResponseEntity.status(403).body("❌ Solo un Admin puede modificar multas.");
    }

    // RF4.3 - Modificar valor de reposición de herramienta (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modify/tools")
    public List<ToolEntity> getAllTools() {
        return loanService.getTools();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tool/replacement")
    public ResponseEntity<String> updateToolReplacementValue(
            @RequestParam int toolId,
            @RequestParam int userId,
            @RequestParam int value) {

        boolean updated = userService.updateToolAdmin(
                userService.getToolById(toolId),
                userService.getUserById(userId),
                value
        );

        return updated
                ? ResponseEntity.ok("✅ Valor de reposición actualizado correctamente.")
                : ResponseEntity.status(403).body("❌ Solo un Admin puede modificar el valor de reposición.");
    }
}

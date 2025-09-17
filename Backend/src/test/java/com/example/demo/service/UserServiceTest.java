package com.example.demo.service;

import com.example.demo.entity.StockToolsEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Roler;
import com.example.demo.repository.StockToolsRepository;
import com.example.demo.repository.TariffRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TariffRepository tariffRepository;

    @Mock
    private StockToolsRepository stockToolsRepository;

    @Mock
    private ToolRepository toolRepository;

    @InjectMocks
    private UserService userService;


    // RF7.2
    @Test
    void rolUser_asignarRolYGuardarUsuario() {
        UserEntity user = new UserEntity();

        userService.rolUser(user, Roler.ADMIN);

        assertEquals(Roler.ADMIN, user.getUserType());
        verify(userRepository, times(1)).save(user);
    }

    // RF7.1
    @Test
    void registerUser_crearYGuardarUsuario() {
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = userService.registerUser(
                "Tamara",
                "tamara@example.com",
                "secreta"
        );

        assertNotNull(result);
        assertEquals("Tamara", result.getUserName());
        assertEquals("tamara@example.com", result.getUserEmail());
        assertEquals("secreta", result.getUserPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // RF7.3
    @Test
    void checkUser_pruebaAdmin() {
        UserEntity admin = new UserEntity();
        admin.setUserType(Roler.ADMIN);

        assertTrue(userService.checkUser(admin));
    }

    @Test
    void checkUser_pruebaUser() {
        UserEntity empleado = new UserEntity();
        empleado.setUserType(Roler.USER);

        assertFalse(userService.checkUser(empleado));
    }

    // RF7.4
    @Test
    void findByNameAndPassword() {
        UserEntity user = new UserEntity();
        user.setUserName("Tamara");
        user.setUserPassword("1234");

        when(userRepository.findByUserNameAndUserPassword("Tamara", "1234"))
                .thenReturn(Optional.of(user));

        Optional<UserEntity> result =
                userService.findByNameAndPassword("Tamara", "1234");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1))
                .findByUserNameAndUserPassword("Tamara", "1234");
    }

    @Test
    void loginUser() {
        UserEntity user = new UserEntity();
        user.setUserName("Tamara");
        user.setUserPassword("1234");

        when(userRepository.findByUserName("Tamara")).thenReturn(user);

        boolean result = userService.loginUser("Tamara", "1234");

        assertTrue(result);
        verify(userRepository, times(1)).findByUserName("Tamara");
    }

    @Test
    void loginUser_usuarioNoExisteOContrasenaIncorrecta() {
        //usuario no existe
        when(userRepository.findByUserName("NoExiste")).thenReturn(null);

        assertFalse(userService.loginUser("NoExiste", "1234"));

        //contraseña incorrecta
        UserEntity user = new UserEntity();
        user.setUserName("Tamara");
        user.setUserPassword("correcta");

        when(userRepository.findByUserName("Tamara")).thenReturn(user);

        assertFalse(userService.loginUser("Tamara", "mala"));
    }

    // RF4.1
    @Test
    void getAllStockTools() {
        StockToolsEntity s1 = new StockToolsEntity();
        StockToolsEntity s2 = new StockToolsEntity();

        when(stockToolsRepository.findAll()).thenReturn(List.of(s1, s2));

        List<StockToolsEntity> result = userService.getAllStockTools();

        assertEquals(2, result.size());
        verify(stockToolsRepository, times(1)).findAll();
    }

    // getUserById / getStockToolById
    @Test
    void getUserById() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserById(1);

        assertEquals(user, result);
    }

    @Test
    void getUserById_idInexistente() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserEntity result = userService.getUserById(1);

        assertNull(result);
    }

    @Test
    void getStockToolById() {
        StockToolsEntity stock = new StockToolsEntity();
        stock.setId(10);

        when(stockToolsRepository.findById(10)).thenReturn(Optional.of(stock));

        StockToolsEntity result = userService.getStockToolById(10);

        assertEquals(stock, result);
    }

    @Test
    void getStockToolById_idInexistente() {
        when(stockToolsRepository.findById(10)).thenReturn(Optional.empty());

        StockToolsEntity result = userService.getStockToolById(10);

        assertNull(result);
    }

    // RF4.1 / RF4.2
    @Test
    void tariffAdminDay_() {
        UserEntity admin = new UserEntity();
        admin.setUserType(Roler.ADMIN);

        StockToolsEntity stock = new StockToolsEntity();
        stock.setTariffDaily(1000);

        boolean result = userService.tariffAdminDay(admin, stock, 2000);

        assertTrue(result);
        assertEquals(2000, stock.getTariffDaily());
        verify(stockToolsRepository, times(1)).save(stock);
    }

    @Test
    void tariffAdminDay_userONul() {
        //user null
        StockToolsEntity stock = new StockToolsEntity();

        boolean result1 = userService.tariffAdminDay(null, stock, 2000);
        assertFalse(result1);

        // no admin
        UserEntity empleado = new UserEntity();
        empleado.setUserType(Roler.USER);
        boolean result2 = userService.tariffAdminDay(empleado, stock, 2000);

        assertFalse(result2);
        verify(stockToolsRepository, never()).save(stock);
    }

    @Test
    void tariffAdminFine_Admin() {
        UserEntity admin = new UserEntity();
        admin.setUserType(Roler.ADMIN);

        StockToolsEntity stock = new StockToolsEntity();
        stock.setFineDaily(500);

        boolean result = userService.tariffAdminFine(admin, stock, 1000);

        assertTrue(result);
        assertEquals(1000, stock.getFineDaily());
        verify(stockToolsRepository, times(1)).save(stock);
    }

    @Test
    void tariffAdminFine_user() {
        UserEntity empleado = new UserEntity();
        empleado.setUserType(Roler.USER);

        StockToolsEntity stock = new StockToolsEntity();

        boolean result = userService.tariffAdminFine(empleado, stock, 1000);

        assertFalse(result);
        verify(stockToolsRepository, never()).save(stock);
    }

    // RF4.3
    @Test
    void updateToolAdmin_Admin() {
        UserEntity admin = new UserEntity();
        admin.setUserType(Roler.ADMIN);

        ToolEntity tool = new ToolEntity();
        tool.setReplacementValueTool(5000);

        boolean result = userService.updateToolAdmin(tool, admin, 9000);

        assertTrue(result);
        assertEquals(9000, tool.getReplacementValueTool());
        verify(toolRepository, times(1)).save(tool);
    }

    @Test
    void updateToolAdmin_UserNull() {
        ToolEntity tool = new ToolEntity();
        boolean result = userService.updateToolAdmin(tool, null, 8000);

        assertFalse(result);
        verify(toolRepository, never()).save(any());
    }

    @Test
    void updateToolAdmin_cuandoNoEsAdmin_debeRetornarFalseSinGuardar() {
        UserEntity empleado = new UserEntity();
        empleado.setUserType(Roler.USER);

        ToolEntity tool = new ToolEntity();

        boolean result = userService.updateToolAdmin(tool, empleado, 8000);

        assertFalse(result);
        verify(toolRepository, never()).save(any());
    }

    @Test
    void getToolById() {
        ToolEntity tool = new ToolEntity();
        tool.setIdTool(7);

        when(toolRepository.findById(7)).thenReturn(Optional.of(tool));

        ToolEntity result = userService.getToolById(7);

        assertEquals(tool, result);
    }

    @Test
    void getToolById_idNull() {
        when(toolRepository.findById(7)).thenReturn(Optional.empty());

        ToolEntity result = userService.getToolById(7);

        assertNull(result);
    }
}

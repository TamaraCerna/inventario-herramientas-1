-- usuarios
INSERT INTO users (user_name, user_email, user_password, user_type) VALUES
                                                                        ('CarlosMendez', 'carlos.mendez@toolrent.cl', 'C@rl0s', 'ADMIN'),
                                                                        ('Ana Rodríguez', 'ana.rodriguez@toolrent.cl', 'An@R0d', 'ADMIN'),
                                                                        ('Pedro Vargas', 'pedro.vargas@toolrent.cl', 'P3dr0V', 'ADMIN'),
                                                                        ('Laura Silva', 'laura.silva@toolrent.cl', 'L4ur@S', 'ADMIN'),
                                                                        ('Miguel Torres', 'miguel.torres@toolrent.cl', 'M1gu3l', 'ADMIN'),
                                                                        ('Juan Pérez', 'juan.perez@toolrent.cl', 'Ju@nP3', 'USER'),
                                                                        ('María González', 'maria.gonzalez@toolrent.cl', 'M4r1@G', 'USER'),
                                                                        ('Roberto Sánchez', 'roberto.sanchez@toolrent.cl', 'R0b3rt', 'USER'),
                                                                        ('Isabel Díaz', 'isabel.diaz@toolrent.cl', 'Is@b3l', 'USER'),
                                                                        ('Francisco López', 'francisco.lopez@toolrent.cl', 'Fr4nc1', 'USER'),
                                                                        ('Carmen Ruiz', 'carmen.ruiz@toolrent.cl', 'C4rm3n', 'USER'),
                                                                        ('Diego Herrera', 'diego.herrera@toolrent.cl', 'D13g0H', 'USER'),
                                                                        ('Elena Castro', 'elena.castro@toolrent.cl', '3l3n@C', 'USER'),
                                                                        ('Jorge Mendoza', 'jorge.mendoza@toolrent.cl', 'J0rg3M', 'USER'),
                                                                        ('Sofía Rojas', 'sofia.rojas@toolrent.cl', 'S0f1@R', 'USER'),
                                                                        ('Andrés Navarro', 'andres.navarro@toolrent.cl', '4ndr3s', 'USER'),
                                                                        ('Patricia Morales', 'patricia.morales@toolrent.cl', 'P4tr1c', 'USER'),
                                                                        ('Ricardo Vega', 'ricardo.vega@toolrent.cl', 'R1c4rd', 'USER'),
                                                                        ('Natalia Ortiz', 'natalia.ortiz@toolrent.cl', 'N4t4l1', 'USER'),
                                                                        ('Daniel Flores', 'daniel.flores@toolrent.cl', 'D4n13l', 'USER');
-- clientes
INSERT INTO clients (client_name, client_rut, client_phone, client_email, client_state) VALUES
('Juan Pérez', '11.111.111-1', 987654321, 'juan.perez@example.com', 'Activo'),
('María González', '12.222.222-2', 912345678, 'maria.gonzalez@example.com', 'Activo'),
('Pedro Ramírez', '13.333.333-3', 956789012, 'pedro.ramirez@example.com', 'Restringido'),
('Carolina Soto', '14.444.444-4', 923456789, 'carolina.soto@example.com', 'Activo'),
('Luis Fernández', '15.555.555-5', 934567890, 'luis.fernandez@example.com', 'Activo'),
('Ana Torres', '16.666.666-6', 976543210, 'ana.torres@example.com', 'Restringido'),
('Javier Morales', '17.777.777-7', 945612378, 'javier.morales@example.com', 'Activo'),
('Fernanda Rojas', '18.888.888-8', 934567812, 'fernanda.rojas@example.com', 'Activo'),
('Rodrigo Castro', '19.999.999-9', 965432187, 'rodrigo.castro@example.com', 'Activo'),
('Valentina Díaz', '10.101.010-0', 987612345, 'valentina.diaz@example.com', 'Restringido'),
('Camila Herrera', '21.111.111-1', 923478901, 'camila.herrera@example.com', 'Activo'),
('Diego Fuentes', '22.222.222-2', 934589012, 'diego.fuentes@example.com', 'Activo'),
('Claudia Méndez', '23.333.333-3', 965478123, 'claudia.mendez@example.com', 'Restringido'),
('Andrés Vargas', '24.444.444-4', 987654120, 'andres.vargas@example.com', 'Activo'),
('Sofía Navarro', '25.555.555-5', 912398765, 'sofia.navarro@example.com', 'Activo');

-- Insertar stock primero
INSERT INTO stock_herramientas (stock_tool, stock_quantity, tariff_daily, fine_daily) VALUES
('Martillo', 25, 1200, 500),
('Destornillador', 40, 800, 300),
('LlaveInglesa', 15, 1500, 700),
('Alicate', 30, 900, 350),
('Serrucho', 12, 2000, 800),
('Cincel', 18, 700, 250);

-- Insertar tools referenciando stock_id
INSERT INTO tools (name_tool, category_tool, initial_state_tool, replacement_value_tool, stock_id) VALUES
('Martillo de Carpintero', 'Martillo', 'Disponible', 12000, 1),
('Destornillador Plano', 'Destornillador', 'Disponible', 3000, 2),
('Llave Inglesa Ajustable', 'LlaveInglesa', 'Prestada', 8000, 3),
('Alicate Universal', 'Alicate', 'Disponible', 5000, 4),
('Serrucho Manual', 'Serrucho', 'Disponible', 7000, 5),
('Cincel de Madera', 'Cincel', 'Reparacion', 4000, 6);

INSERT INTO loans
(loan_state, loan_client_id, loan_tool_id, loan_tariff_id, loan_date_init, loan_date_finish, loan_penalty)
VALUES
('Activo', 1, 1, 1, '2025-09-20', '2025-09-25', 0),
('Activo', 2, 2, 1, '2025-09-22', '2025-09-27', 0),
('Finalizado', 3, 3, 2, '2025-08-05', '2025-08-10', 2000),
('Finalizado', 4, 4, 2, '2025-09-01', '2025-09-10', 3500),
('Activo', 5, 5, 3, '2025-09-28', '2025-10-03', 0),
('Finalizado', 6, 6, 3, '2025-08-15', '2025-08-20', 700),
('Activo', 7, 1, 1, '2025-09-29', '2025-10-04', 0),
('Finalizado', 8, 2, 1, '2025-09-10', '2025-09-15', 1500),
('Finalizado', 9, 3, 2, '2025-09-01', '2025-09-05', 0),
('Activo', 10, 4, 2, '2025-09-30', '2025-10-05', 0);









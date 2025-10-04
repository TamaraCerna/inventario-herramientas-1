import React, { useState } from "react";
import { registerUser, assignRole } from "../service/userService";

export const RegisterEmployee: React.FC = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [userId, setUserId] = useState<number | null>(null);
  const [role, setRole] = useState("User");

  const handleRegister = async () => {
    try {
      const user = await registerUser(name, email, password);
      alert("Empleado registrado correctamente.");
      setUserId(user.userId); 
      setName("");
      setEmail("");
      setPassword("");
    } catch (error) {
      console.error("Error registrando empleado:", error);
      alert("Hubo un error al registrar.");
    }
  };

  const handleAssignRole = async () => {
    if (!userId) {
      alert("Primero debes registrar un empleado.");
      return;
    }
    try {
      await assignRole(userId, role);
      alert("Rol asignado correctamente.");
      setRole("User");
    } catch (error) {
      console.error("Error asignando rol:", error);
      alert("Hubo un error al asignar el rol.");
    }
  };

  return (
    <div style={{ maxWidth: "500px", margin: "30px auto", fontFamily: "Arial" }}>
      {/* Formulario Registrar Empleado */}
      <h2 style={{ marginBottom: "20px" }}>Registrar Empleado</h2>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleRegister();
        }}
      >
        <div style={{ marginBottom: "15px" }}>
          <label>Nombre</label>
          <input
            type="text"
            value={name}
            placeholder="Ej: Juan Pérez"
            onChange={(e) => setName(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Email</label>
          <input
            type="email"
            value={email}
            placeholder="Ej: usuario@correo.com"
            onChange={(e) => setEmail(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Contraseña</label>
          <input
            type="password"
            value={password}
            placeholder="••••••••"
            onChange={(e) => setPassword(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <button type="submit" className="btn btn-success w-100">
          Registrar
        </button>
      </form>

      <hr style={{ margin: "30px 0" }} />

      {/* Formulario Asignar Rol */}
      <h3>Asignar Rol</h3>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleAssignRole();
        }}
      >
        <div style={{ marginBottom: "15px" }}>
          <label>ID Usuario</label>
          <input
            type="number"
            value={userId ?? ""}
            className="form-control"
            disabled 
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Rol</label>
          <select
            value={role}
            onChange={(e) => setRole(e.target.value)}
            className="form-control"
          >
            <option value="USER">Usuario</option>
            <option value="ADMIN">Administrador</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary w-100">
          Asignar Rol
        </button>
      </form>
    </div>
  );
};

export default RegisterEmployee;


import React, { useState } from "react";
import { registerClient } from "../service/registerClientService";

const RegisterClientForm: React.FC = () => {
  const [name, setName] = useState("");
  const [rut, setRut] = useState("");
  const [phone, setPhone] = useState<number>(0);
  const [email, setEmail] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const client = await registerClient(name, rut, phone, email);
      console.log("Cliente registrado:", client);
      alert("✅ Cliente registrado correctamente");
      setName("");
      setRut("");
      setPhone(0);
      setEmail("");
    } catch (err) {
      console.error(err);
      alert("❌ Error al registrar cliente");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="container mt-4" style={{ maxWidth: "400px" }}>
      <h3 className="mb-3">Registrar Cliente</h3>

      {/* Nombre */}
      <div className="mb-3">
        <label className="form-label">Nombre completo</label>
        <input
          type="text"
          className="form-control"
          placeholder="Ej: Juan Pérez"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>

      {/* RUT */}
      <div className="mb-3">
        <label className="form-label">RUT</label>
        <input
          type="text"
          className="form-control"
          placeholder="Ej: 12.345.678-9"
          value={rut}
          onChange={(e) => setRut(e.target.value)}
          required
        />
      </div>

      {/* Teléfono */}
      <div className="mb-3">
        <label className="form-label">Teléfono</label>
        <input
          type="number"
          className="form-control"
          placeholder="Ej: 987654321"
          value={phone}
          onChange={(e) => setPhone(Number(e.target.value))}
          required
        />
      </div>

      {/* Correo electrónico */}
      <div className="mb-3">
        <label className="form-label">Correo electrónico</label>
        <input
          type="email"
          className="form-control"
          placeholder="Ej: usuario@correo.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </div>

      <button type="submit" className="btn btn-success w-100">
        Registrar
      </button>
    </form>
  );
};

export default RegisterClientForm;


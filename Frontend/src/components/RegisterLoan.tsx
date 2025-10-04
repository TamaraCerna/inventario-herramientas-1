import React, { useState, useEffect } from "react";
import Swal from "sweetalert2";
import { createLoan } from "../service/registerLoanService";

interface Client {
  clientId: number;
  clientName: string;
}

interface Tool {
  idTool: number;
  nameTool: string;
}

const RegisterLoanForm: React.FC = () => {
  const [clientId, setClientId] = useState<number>(0);
  const [toolId, setToolId] = useState<number>(0);
  const [initDate, setInitDate] = useState<string>("");
  const [finishDate, setFinishDate] = useState<string>("");

  const [clients, setClients] = useState<Client[]>([]);
  const [tools, setTools] = useState<Tool[]>([]);
  const today = new Date().toISOString().split("T")[0];

  // Clientes
  useEffect(() => {
    fetch('${import.meta.env.VITE_API_URL}/loans/clients')
      .then((res) => res.json())
      .then((data) => setClients(data))
      .catch((err) => console.error("Error cargando clientes:", err));
  }, []);
  
  // Herramientas
  useEffect(() => {
    fetch('${import.meta.env.VITE_API_URL}/loans/tools')
      .then((res) => {
        if (!res.ok) throw new Error("Error cargando herramientas");
        return res.json();
      })
      .then((data) => setTools(data))
      .catch((err) => console.error(err));
  }, []);

  // Submit
const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  try {
    const loan = await createLoan(clientId, toolId, initDate, finishDate);
    console.log("Préstamo creado:", loan);

    Swal.fire({
      icon: "success",
      title: "Éxito",
      text: "✅ Préstamo registrado correctamente",
    });

    // Reset form
    setClientId(0);
    setToolId(0);
    setInitDate("");
    setFinishDate("");
  } catch (err: any) {
    console.error(err);

    let errorMessage = "❌ Error al registrar préstamo";

    // Si el servicio ya lanza Error con el texto del backend (verificado en registerLoanService.ts)
    if (err.message) {
      errorMessage = err.message;
    }

    // Personalizar si quieres
    if (errorMessage.includes("Cliente")) {
      errorMessage = "❌ Cliente no apto para préstamo";
    } else if (errorMessage.includes("Herramienta")) {
      errorMessage = "❌ Herramienta no disponible";
    }

    Swal.fire({
      icon: "error",
      title: "Error",
      text: errorMessage,
    });
  }
};


  return (
    <div className="container mt-4">
      <h2>Registrar Préstamo</h2>
      <form onSubmit={handleSubmit}>
        {/* Cliente */}
        <div className="mb-3">
          <label className="form-label">Cliente</label>
          <select
            className="form-select"
            value={clientId}
            onChange={(e) => setClientId(Number(e.target.value))}
            required
          >
            <option value={0}>Seleccione un cliente</option>
            {clients.map((c) => (
              <option key={c.clientId} value={c.clientId}>
                {c.clientName}
              </option>
            ))}
          </select>
        </div>

        {/* Herramienta */}
        <div className="mb-3">
          <label className="form-label">Herramienta</label>
          <select
            className="form-select"
            value={toolId}
            onChange={(e) => setToolId(Number(e.target.value))}
            required
          >
            <option value={0}>Seleccione una herramienta</option>
            {tools.map((t) => (
              <option key={t.idTool} value={t.idTool}>
                {t.nameTool}
              </option>
            ))}
          </select>
        </div>

        {/* Fechas */}
        <div className="mb-3">
          <label className="form-label">Fecha inicio</label>
          <input
            type="date"
            className="form-control"
            value={initDate}
            onChange={(e) => setInitDate(e.target.value)}
            min={today}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Fecha término</label>
          <input
            type="date"
            className="form-control"
            value={finishDate}
            onChange={(e) => setFinishDate(e.target.value)}
            min={today}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Registrar préstamo
        </button>
      </form>
    </div>
  );
};

export default RegisterLoanForm;







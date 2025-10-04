import React, { useState, useEffect } from "react";
import api from "../service/api";

interface Client {
  clientId: number;
  clientName: string;
}

interface Loan {
  loanId: number;
  toolName: string;
  loanDateInit: string;
  loanDateFinish: string;
  loanPenalty?: number;
}

const ReturnLoanForm: React.FC = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [loans, setLoans] = useState<Loan[]>([]);
  const [clientId, setClientId] = useState<number>(0);
  const [loanId, setLoanId] = useState<number>(0);
  const [fine, setFine] = useState<number | null>(null);

  // 🔹 Cargar clientes
  useEffect(() => {
    api
      .get("/loans/clients")
      .then((res) => setClients(res.data))
      .catch((err) => console.error("Error cargando clientes:", err));
  }, []);

  // 🔹 Cargar préstamos activos según cliente
  useEffect(() => {
    if (clientId > 0) {
      api
        .get(`/loans/active`, { params: { clientId } })
        .then((res) => setLoans(res.data))
        .catch((err) => console.error("Error cargando préstamos:", err));
    }
  }, [clientId]);

  // 🔹 Registrar devolución
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const loan = loans.find((l) => l.loanId === loanId);
      if (!loan) {
        alert("❌ Selecciona un préstamo válido");
        return;
      }

      await api.put(`/loans/${loanId}/return`, null, {
        params: { toolId: loan.loanId, userId: 1 },
      });

      alert("✅ Devolución registrada correctamente");
      setClientId(0);
      setLoanId(0);
      setLoans([]);
      setFine(null);
    } catch (err) {
      console.error(err);
      alert("❌ Error al registrar devolución");
    }
  };

  // 🔹 Calcular multa
  const handleCalculateFine = async () => {
    try {
      if (loanId === 0) {
        alert("❌ Selecciona un préstamo para calcular la multa");
        return;
      }

      const { data } = await api.put(`/loans/${loanId}/fine`);
      setFine(data.loanPenalty);

      alert(`✅ Multa calculada: $${data.loanPenalty}`);
    } catch (err) {
      console.error(err);
      alert("❌ Error al calcular multa");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Registrar Devolución</h2>
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

        {/* Préstamo activo */}
        <div className="mb-3">
          <label className="form-label">Préstamo activo</label>
          <select
            className="form-select"
            value={loanId}
            onChange={(e) => setLoanId(Number(e.target.value))}
            disabled={clientId === 0}
            required
          >
            <option value={0}>Seleccione un préstamo</option>
            {loans.map((l) => (
              <option key={l.loanId} value={l.loanId}>
                {l.toolName} (Del {l.loanDateInit} al {l.loanDateFinish})
              </option>
            ))}
          </select>
        </div>

        {/* Botón calcular multa */}
        <div className="mb-3">
          <button
            type="button"
            className="btn btn-warning me-2"
            onClick={handleCalculateFine}
            disabled={loanId === 0}
          >
            Calcular Multa
          </button>

          {fine !== null && (
            <span className="badge bg-danger">Multa: ${fine}</span>
          )}
        </div>

        {/* Botón registrar devolución */}
        <button type="submit" className="btn btn-primary">
          Registrar devolución
        </button>
      </form>
    </div>
  );
};

export default ReturnLoanForm;


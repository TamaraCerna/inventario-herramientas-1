import React, { useState, useEffect } from "react";

interface Client {
  clientId: number;
  clientName: string;
}

interface Loan {
  loanId: number;
  toolName: string;
  loanDateInit: string;
  loanDateFinish: string;
  loanPenalty?: number; // 👈 opcional
}

const ReturnLoanForm: React.FC = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [loans, setLoans] = useState<Loan[]>([]);
  const [clientId, setClientId] = useState<number>(0);
  const [loanId, setLoanId] = useState<number>(0);

  // 🔹 Cargar clientes
  useEffect(() => {
    fetch('${import.meta.env.VITE_API_URL}/loans/clients')
      .then((res) => res.json())
      .then((data) => setClients(data))
      .catch((err) => console.error("Error cargando clientes:", err));
  }, []);

  // 🔹 Cargar préstamos activos al elegir cliente
  useEffect(() => {
    if (clientId > 0) {
      fetch(`${import.meta.env.VITE_API_URL}/loans/active?clientId=${clientId}`)
        .then((res) => res.json())
        .then((data) => setLoans(data))
        .catch((err) => console.error("Error cargando préstamos:", err));
    } else {
      setLoans([]);
      setLoanId(0);
    }
  }, [clientId]);

  // 🔹 Calcular multa
  const handleCalculateFine = async () => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/loans/${loanId}/fine`,
        { method: "PUT" }
      );

      if (!response.ok) throw new Error("Error calculando multa");

      const updatedLoan: Loan = await response.json();

      // Actualizar lista de préstamos con el préstamo actualizado
      setLoans((prevLoans) =>
        prevLoans.map((l) =>
          l.loanId === updatedLoan.loanId ? updatedLoan : l
        )
      );

      alert("✅ Multa calculada correctamente");
    } catch (err) {
      console.error(err);
      alert("❌ Error al calcular la multa");
    }
  };

  // 🔹 Registrar devolución
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const loan = loans.find((l) => l.loanId === loanId);
      if (!loan) {
        alert("❌ Selecciona un préstamo válido");
        return;
      }

      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/${loanId}/return?toolId=${loan.loanId}&userId=1`,
        { method: "PUT" }
      );

      if (!response.ok) throw new Error("Error en devolución");

      const message = await response.text();
      alert("✅ " + message);

      // Resetear formulario
      setClientId(0);
      setLoanId(0);
      setLoans([]);
    } catch (err) {
      console.error(err);
      alert("❌ Error al registrar devolución");
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

        {/* Multa */}
        {loanId > 0 && (
          <div className="mb-3">
            <label className="form-label">Multa</label>
            <div className="d-flex">
              <input
                type="text"
                className="form-control"
                value={loans.find((l) => l.loanId === loanId)?.loanPenalty ?? 0}
                readOnly
              />
              <button
                type="button"
                className="btn btn-warning ms-2"
                onClick={handleCalculateFine}
              >
                Calcular multa
              </button>
            </div>
          </div>
        )}

        {/* Botón de devolución */}
        <button type="submit" className="btn btn-primary">
          Registrar devolución
        </button>
      </form>
    </div>
  );
};

export default ReturnLoanForm;




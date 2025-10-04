import React, { useEffect, useState } from 'react';
import { getActiveLoansByClient, returnTool } from '../service/loanService';

const LoanList: React.FC = () => {
  const [loans, setLoans] = useState<any[]>([]);
  const clientId: number = 1; // ejemplo: cliente fijo

  useEffect(() => {
    loadLoans(clientId);
  }, [clientId]);

  const loadLoans = async (clientId: number) => {
    try {
      const data = await getActiveLoansByClient(clientId);
      setLoans(data);
    } catch (err) {
      console.error('Error cargando préstamos', err);
    }
  };

  const handleReturn = async (loanId: number, toolId: number, userId: number) => {
    try {
      const message = await returnTool(loanId, toolId, userId);
      alert(message);
      loadLoans(clientId); // refrescar lista tras devolución
    } catch (err) {
      console.error('Error registrando devolución', err);
    }
  };

  return (
    <div>
      <h2>Préstamos Activos</h2>
      <table border={1}>
        <thead>
          <tr>
            <th>ID Préstamo</th>
            <th>ID Herramienta</th>
            <th>Fecha Inicio</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {loans.map((loan) => (
            <tr key={loan.id}>
              <td>{loan.id}</td>
              <td>{loan.toolId}</td>
              <td>{loan.startDate}</td>
              <td>
                <button onClick={() => handleReturn(loan.id, loan.toolId, loan.clientId)}>
                  Devolver
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default LoanList;


import React, { useEffect, useState } from "react";
import { getRankingTools } from "../service/reportService";

interface Tool {
  idTool: number;
  nameTool: string;
  categoryTool: string;
}

export const RankingTools: React.FC = () => {
  const [ranking, setRanking] = useState<any[]>([]);

  useEffect(() => {
    getRankingTools()
      .then(setRanking)
      .catch((err) => console.error("Error cargando ranking:", err));
  }, []);

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      <h2>Ranking de Herramientas</h2>
      <table style={{ borderCollapse: "collapse", width: "90%", marginTop: "20px" }}>
        <thead>
          <tr style={{ backgroundColor: "#b71c1c", color: "white" }}>
            <th style={{ border: "1px solid #ddd", padding: "10px" }}>#</th>
            <th style={{ border: "1px solid #ddd", padding: "10px" }}>Herramienta</th>
            <th style={{ border: "1px solid #ddd", padding: "10px" }}>Categoría</th>
            <th style={{ border: "1px solid #ddd", padding: "10px" }}>Veces Prestada</th>
          </tr>
        </thead>
        <tbody>
          {ranking.length === 0 ? (
            <tr>
              <td colSpan={4} style={{ textAlign: "center", padding: "10px" }}>
                No hay datos de ranking
              </td>
            </tr>
          ) : (
            ranking.map((r, index) => {
              const tool: Tool = r[0];
              const count: number = r[1];
              return (
                <tr key={tool.idTool}>
                  <td style={{ border: "1px solid #ddd", padding: "10px", textAlign: "center" }}>
                    {index + 1}
                  </td>
                  <td style={{ border: "1px solid #ddd", padding: "10px" }}>
                    {tool.nameTool}
                  </td>
                  <td style={{ border: "1px solid #ddd", padding: "10px" }}>
                    {tool.categoryTool}
                  </td>
                  <td style={{ border: "1px solid #ddd", padding: "10px", textAlign: "center" }}>
                    {count}
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
};

export default RankingTools;


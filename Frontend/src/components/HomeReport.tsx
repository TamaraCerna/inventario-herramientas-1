import { useMemo, useState } from "react";

type ReportRow = {
  fecha: string;        // "YYYY-MM-DD"
  tipo: "Préstamo" | "Devolución" | "Mantenimiento";
  herramienta: string;
  cantidad: number;
};

// 🔹 Datos demo (cámbialos por datos reales desde tu API)
const DATA: ReportRow[] = [
  { fecha: "2025-09-01", tipo: "Préstamo", herramienta: "Martillo", cantidad: 2 },
  { fecha: "2025-09-03", tipo: "Devolución", herramienta: "Martillo", cantidad: 1 },
  { fecha: "2025-09-05", tipo: "Mantenimiento", herramienta: "Alicate", cantidad: 1 },
  { fecha: "2025-09-10", tipo: "Préstamo", herramienta: "Destornillador", cantidad: 3 },
];

export default function HomeReport() {
  const [open, setOpen] = useState(false);               // menú
  const [desde, setDesde] = useState("");                // filtro fecha
  const [hasta, setHasta] = useState("");                // filtro fecha
  const [herramienta, setHerramienta] = useState("");    // filtro herramienta

  // 🔎 Filtro en memoria (reemplaza por llamados a tu API si quieres filtrar server-side)
  const rows = useMemo(() => {
    return DATA.filter(r => {
      const okHerr = !herramienta || r.herramienta.toLowerCase().includes(herramienta.toLowerCase());
      const okDesde = !desde || r.fecha >= desde;
      const okHasta = !hasta || r.fecha <= hasta;
      return okHerr && okDesde && okHasta;
    });
  }, [desde, hasta, herramienta]);

  const exportCSV = () => {
    const headers = ["Fecha", "Movimiento", "Herramienta", "Cantidad"];
    const csv = [
      headers.join(","),
      ...rows.map(r => [r.fecha, r.tipo, r.herramienta, r.cantidad].join(",")),
    ].join("\n");

    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "reporte.csv";
    a.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      {/* Título */}
      <h1
        style={{
          fontSize: "2.5rem",
          display: "inline-block",
          padding: "5px 10px",
          marginBottom: "20px",
        }}
      >
        Reporte:
      </h1>

      {/* Botón filtro y menú (mismo patrón que Kardex) */}
      <div style={{ marginBottom: "20px", position: "relative", display: "inline-block" }}>
        <button
          onClick={() => setOpen(!open)}
          style={{
            padding: "10px 20px",
            borderRadius: "30px",
            backgroundColor: "#1800ad",
            color: "white",
            border: "none",
            cursor: "pointer",
            marginRight: "10px",
          }}
        >
          filtro
        </button>

        <button
          onClick={exportCSV}
          style={{
            padding: "10px 20px",
            borderRadius: "30px",
            backgroundColor: "#0a7d12",
            color: "white",
            border: "none",
            cursor: "pointer",
          }}
        >
          exportar CSV
        </button>

        {open && (
          <ul
            style={{
              listStyle: "none",
              margin: 0,
              padding: "10px",
              backgroundColor: "white",
              boxShadow: "0px 4px 6px rgba(0,0,0,0.2)",
              position: "absolute",
              top: "100%",
              left: 0,
              borderRadius: "8px",
              width: "220px",
              zIndex: 10,
            }}
          >
            <li style={{ padding: "8px", fontWeight: "bold" }}>Por fecha</li>
            <li style={{ padding: "8px" }}>
              <label style={{ display: "block", fontSize: "0.9rem" }}>Desde</label>
              <input type="date" value={desde} onChange={(e) => setDesde(e.target.value)} />
            </li>
            <li style={{ padding: "8px" }}>
              <label style={{ display: "block", fontSize: "0.9rem" }}>Hasta</label>
              <input type="date" value={hasta} onChange={(e) => setHasta(e.target.value)} />
            </li>

            <li style={{ padding: "8px", fontWeight: "bold", marginTop: "6px" }}>Por herramienta</li>
            <li style={{ padding: "8px" }}>
              <input
                type="text"
                placeholder="Ej: Martillo"
                value={herramienta}
                onChange={(e) => setHerramienta(e.target.value)}
                style={{ width: "100%" }}
              />
            </li>
          </ul>
        )}
      </div>

      {/* Tabla */}
      <table style={{ borderCollapse: "collapse", width: "90%" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid black", padding: "8px" }}>Fecha</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Movimiento</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Herramienta</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Cantidad</th>
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr>
              <td colSpan={4} style={{ textAlign: "center", padding: "8px" }}>
                No hay datos con los filtros seleccionados
              </td>
            </tr>
          ) : (
            rows.map((r, i) => (
              <tr key={i}>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r.fecha}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r.tipo}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r.herramienta}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r.cantidad}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

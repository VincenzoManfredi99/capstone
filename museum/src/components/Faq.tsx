import { useState } from "react";

interface FaqProps {
  domanda: string;
  risposta: string;
}

function Faq({ domanda, risposta }: FaqProps) {
  const [aperto, setAperto] = useState<boolean>(false);

  return (
    <div
      onClick={() => setAperto(!aperto)}
      className="border rounded-4 p-3 mb-3 shadow-sm bg-white"
      style={{ cursor: "pointer" }}
    >
      <div className="d-flex justify-content-between align-items-center fw-bold">
        <span>{domanda}</span>
        <span>{aperto ? "︿" : "⌄"}</span>
      </div>

      {aperto && (
        <div className="mt-3 pt-2 border-top text-muted">{risposta}</div>
      )}
    </div>
  );
}

export default Faq;

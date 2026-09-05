import { useState } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";

interface AssetModalProps {
  show: boolean;
  onHide: () => void;
  operaSelezionata: { id?: string; _id?: string; titolo?: string } | null;
  onSuccess?: () => void;
}

export function AssetModal({
  show,
  onHide,
  operaSelezionata,
  onSuccess,
}: AssetModalProps) {
  const [fileFoto, setFileFoto] = useState<FileList | null>(null);
  const [file3D, setFile3D] = useState<File | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [messaggio, setMessaggio] = useState<string | null>(null);

  const operaId = operaSelezionata?.id || operaSelezionata?._id;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token || !operaId) return;

    setLoading(true);
    setMessaggio(null);

    try {
      // 1. Se l'utente ha caricato delle foto 2D
      if (fileFoto && fileFoto.length > 0) {
        for (const file of Array.from(fileFoto)) {
          const formData = new FormData();
          formData.append("file", file);
          formData.append("tipoUrl", "FOTO_2D");
          formData.append("operaId", operaId);

          const res = await fetch(
            `http://localhost:3001/opere/${operaId}/assets`,
            {
              method: "POST",
              headers: { Authorization: `Bearer ` }, // Inserisci il token corretto
              body: formData,
            },
          );
          if (!res.ok) throw new Error("Errore caricamento foto");
        }
      }

      // 2. Se l'utente ha caricato il file 3D (.glb)
      if (file3D) {
        const formData = new FormData();
        formData.append("file", file3D);
        formData.append("tipoUrl", "MODELLO_3D");
        formData.append("operaId", operaId);

        const res = await fetch(
          `http://localhost:3001/opere/${operaId}/assets`,
          {
            method: "POST",
            headers: { Authorization: `Bearer ${token}` },
            body: formData,
          },
        );
        if (!res.ok) throw new Error("Errore caricamento modello 3D");
      }

      setMessaggio("Asset caricati con successo!");
      setTimeout(() => {
        setMessaggio(null);
        if (onSuccess) onSuccess();
        onHide();
      }, 1200);
    } catch (err) {
      console.error("Errore di rete o salvataggio:", err);
      setMessaggio("Errore durante il caricamento degli asset.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Gestisci Asset: {operaSelezionata?.titolo}</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {messaggio && (
            <Alert
              variant={messaggio.includes("successo") ? "success" : "danger"}
            >
              {messaggio}
            </Alert>
          )}

          <Form.Group className="mb-3">
            <Form.Label>Carica Foto Multiple (Galleria)</Form.Label>
            <Form.Control
              type="file"
              multiple
              accept="image/*"
              onChange={(e: any) => setFileFoto(e.target.files)}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Carica Modello 3D (.glb, .gltf, ecc.)</Form.Label>
            <Form.Control
              type="file"
              accept=".glb,.gltf,.obj"
              onChange={(e: any) => setFile3D(e.target.files[0] || null)}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Chiudi
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? "Caricamento in corso..." : "Salva Asset"}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

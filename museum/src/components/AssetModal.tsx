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

    const formData = new FormData();
    if (fileFoto) {
      Array.from(fileFoto).forEach((file) => {
        formData.append("file", file);
        formData.append("tipoUrl", "FOTO_2D"); // <-- Modificato da "FOTO" a "FOTO_2D"
        formData.append("operaId", operaId);
      });
    }
    if (file3D) {
      formData.append("file", file3D);
      formData.append("tipoUrl", "MODELLO_3D"); // <-- Questo era già corretto
      formData.append("operaId", operaId);
    }

    try {
      const res = await fetch(`http://localhost:3001/opere/${operaId}/assets`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: formData,
      });

      if (res.ok) {
        setMessaggio("Asset caricati con successo!");
        setTimeout(() => {
          setMessaggio(null);
          if (onSuccess) onSuccess();
          onHide();
        }, 1200);
      } else {
        setMessaggio("Errore durante il caricamento degli asset.");
      }
    } catch (err) {
      console.error("Errore di rete:", err);
      setMessaggio("Errore di connessione al server.");
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

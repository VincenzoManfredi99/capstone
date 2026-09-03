import { Modal, Button, Form } from "react-bootstrap";

interface UploadScenaModalProps {
  show: boolean;
  onHide: () => void;
  onSubmit: (e: React.FormEvent) => void;
  onFileChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export function UploadScenaModal({
  show,
  onHide,
  onSubmit,
  onFileChange,
}: UploadScenaModalProps) {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Carica Immagine Panoramica 360°</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={onSubmit} id="scenaForm">
          <Form.Group className="mb-3">
            <Form.Label>Seleziona file immagine (JPG/PNG)</Form.Label>
            <Form.Control
              type="file"
              accept="image/*"
              onChange={onFileChange}
              required
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Annulla
        </Button>
        <Button variant="primary" type="submit" form="scenaForm">
          Carica e Salva
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

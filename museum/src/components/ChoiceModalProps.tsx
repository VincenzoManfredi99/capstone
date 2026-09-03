import { Modal, Button } from "react-bootstrap";

interface ChoiceModalProps {
  show: boolean;
  onHide: () => void;
  tempCoords: { pitch: number; yaw: number } | null;
  onSelectMovimento: () => void;
  onSelectOpera: () => void;
}

export function ChoiceModal({
  show,
  onHide,
  tempCoords,
  onSelectMovimento,
  onSelectOpera,
}: ChoiceModalProps) {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Scegli il tipo di Hotspot</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center py-4">
        <p className="text-muted mb-4">
          Coordinate salvate: Pitch: {tempCoords?.pitch.toFixed(1)}, Yaw:{" "}
          {tempCoords?.yaw.toFixed(1)}
        </p>
        <div className="d-flex justify-content-center gap-3">
          <Button
            variant="outline-success"
            size="lg"
            onClick={onSelectMovimento}
          >
            🚀 Movimento (Cambio Scena)
          </Button>
          <Button variant="outline-warning" size="lg" onClick={onSelectOpera}>
            🖼️ Opera d'Arte
          </Button>
        </div>
      </Modal.Body>
    </Modal>
  );
}

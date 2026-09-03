import { Modal, Button, Form } from "react-bootstrap";

interface ScenaItem {
  id?: string;
  _id?: string;
}

interface MovimentoModalProps {
  show: boolean;
  onHide: () => void;
  sceneList: ScenaItem[];
  scenaCorrente: ScenaItem | null;
  targetScenaId: string;
  setTargetScenaId: (id: string) => void;
  onSave: () => void;
}

export function MovimentoModal({
  show,
  onHide,
  sceneList,
  scenaCorrente,
  targetScenaId,
  setTargetScenaId,
  onSave,
}: MovimentoModalProps) {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Configura Hotspot di Movimento</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group>
          <Form.Label>Seleziona la scena di destinazione:</Form.Label>
          <Form.Control
            as="select"
            value={targetScenaId}
            onChange={(e) => setTargetScenaId(e.target.value)}
          >
            <option value="">-- Scegli una scena --</option>
            {sceneList
              .filter(
                (s) =>
                  (s.id || s._id) !== (scenaCorrente?.id || scenaCorrente?._id),
              )
              .map((s, idx) => (
                <option key={s.id || s._id} value={s.id || s._id}>
                  Scena alternativa {idx + 1}
                </option>
              ))}
          </Form.Control>
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Annulla
        </Button>
        <Button variant="success" onClick={onSave}>
          Salva Hotspot
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

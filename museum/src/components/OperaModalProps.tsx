import { Modal, Button, Form } from "react-bootstrap";

interface OperaModalProps {
  show: boolean;
  onHide: () => void;
  titoloOpera: string;
  setTitoloOpera: (val: string) => void;
  descrizioneOpera: string;
  setDescrizioneOpera: (val: string) => void;
  urlAudioOpera: string;
  setUrlAudioOpera: (val: string) => void;
  fileAsset: File | null;
  setFileAsset: (file: File | null) => void;
  tipoAsset: string;
  setTipoAsset: (val: string) => void;
  onSave: () => void;
}

export function OperaModal({
  show,
  onHide,
  titoloOpera,
  setTitoloOpera,
  descrizioneOpera,
  setDescrizioneOpera,
  urlAudioOpera,
  setUrlAudioOpera,
  onSave,
}: OperaModalProps) {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Inserisci Dettagli Opera</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group className="mb-3">
          <Form.Label>Titolo Opera</Form.Label>
          <Form.Control
            type="text"
            value={titoloOpera}
            onChange={(e) => setTitoloOpera(e.target.value)}
            placeholder="Es. Ritratto di dama"
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Descrizione</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            value={descrizioneOpera}
            onChange={(e) => setDescrizioneOpera(e.target.value)}
            placeholder="Breve storia o dettagli..."
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>URL Audio Guida (Opzionale)</Form.Label>
          <Form.Control
            type="text"
            value={urlAudioOpera}
            onChange={(e) => setUrlAudioOpera(e.target.value)}
            placeholder="https://..."
          />
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Annulla
        </Button>
        <Button variant="warning" onClick={onSave}>
          Salva Tutto
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

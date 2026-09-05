import { Modal, Button, Form, Spinner } from "react-bootstrap";

interface OperaModalProps {
  show: boolean;
  onHide: () => void;
  titoloOpera: string;
  setTitoloOpera: (val: string) => void;
  descrizioneOpera: string;
  setDescrizioneOpera: (val: string) => void;
  // Sostituiamo l'URL testuale con la gestione del file e dello stato di caricamento
  audioFile: File | null;
  setAudioFile: (file: File | null) => void;
  loadingAudio?: boolean; // Per mostrare un feedback di caricamento
  onSave: () => void;
}

export function OperaModal({
  show,
  onHide,
  titoloOpera,
  setTitoloOpera,
  descrizioneOpera,
  setDescrizioneOpera,
  audioFile,
  setAudioFile,
  loadingAudio = false,
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
            disabled={loadingAudio}
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
            disabled={loadingAudio}
          />
        </Form.Group>

        {/* Campo File Audio al posto del campo di testo URL */}
        <Form.Group className="mb-3">
          <Form.Label>Audio Guida (Opzionale)</Form.Label>
          <Form.Control
            type="file"
            accept="audio/*"
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
              if (e.target.files && e.target.files[0]) {
                setAudioFile(e.target.files[0]);
              }
            }}
            disabled={loadingAudio}
          />
          {audioFile && (
            <Form.Text className="text-muted d-block mt-1">
              File selezionato: {audioFile.name}
            </Form.Text>
          )}
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide} disabled={loadingAudio}>
          Annulla
        </Button>
        <Button variant="warning" onClick={onSave} disabled={loadingAudio}>
          {loadingAudio ? (
            <>
              <Spinner animation="border" size="sm" className="me-2" />
              Caricamento audio...
            </>
          ) : (
            "Salva Tutto"
          )}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

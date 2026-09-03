import { useState, useEffect } from "react";
import { Modal, Button, Row, Col, Carousel } from "react-bootstrap";

interface OperaSelezionata {
  id?: string;
  titolo?: string;
  descrizione?: string;
  url_audio?: string;
}

interface ViewOperaModalProps {
  show: boolean;
  onHide: () => void;
  operaSelezionata: OperaSelezionata | null;
}

export function ViewOperaModal({
  show,
  onHide,
  operaSelezionata,
}: ViewOperaModalProps) {
  // Cambiamo lo stato da stringa singola a un array di stringhe
  const [immaginiList, setImmaginiList] = useState<string[]>([]);

  useEffect(() => {
    if (!show || !operaSelezionata?.id) {
      setImmaginiList([]);
      return;
    }

    // Chiama l'endpoint che restituisce la lista di asset dell'opera
    fetch(`http://localhost:3001/opere/${operaSelezionata.id}/assets`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        const assetList = Array.isArray(data) ? data : data.content || [];

        // Filtriamo TUTTE le foto 2D associate all'opera
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const foto2DList = assetList.filter(
          (a: any) => a.tipoUrl === "FOTO_2D",
        );

        // Mettiamo tutti gli URL in un array
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const urls = foto2DList.map((f: any) => f.urlFile).filter(Boolean);

        setImmaginiList(urls);
      })
      .catch((err) => {
        console.error("Errore nel recupero degli asset:", err);
        setImmaginiList([]);
      });
  }, [show, operaSelezionata]);

  return (
    <Modal show={show} onHide={onHide} size="lg" centered>
      <Modal.Header closeButton>
        <Modal.Title>{operaSelezionata?.titolo || "Opera d'arte"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row className="align-items-center">
          <Col md={6} className="text-center mb-3 mb-md-0">
            {immaginiList.length > 0 ? (
              <Carousel
                interval={null}
                className="shadow-sm rounded overflow-hidden"
              >
                {immaginiList.map((url, index) => (
                  <Carousel.Item key={index}>
                    <img
                      className="d-block w-100 img-fluid"
                      src={url}
                      alt={`Slide ${index + 1}`}
                      style={{
                        maxHeight: "350px",
                        objectFit: "contain",
                        backgroundColor: "#000",
                      }}
                    />
                  </Carousel.Item>
                ))}
              </Carousel>
            ) : (
              <div className="text-muted p-5 border rounded bg-light">
                Nessuna immagine disponibile
              </div>
            )}
          </Col>
          <Col md={6}>
            <h5 className="text-secondary mb-3">Descrizione</h5>
            <p style={{ lineHeight: "1.6" }}>
              operaSelezionata?.descrizione || "Nessuna descrizione
              disponibile."
            </p>

            {operaSelezionata?.url_audio && (
              <div className="mt-4">
                <h6 className="text-muted">Audio Guida</h6>
                <audio controls className="w-100">
                  <source src={operaSelezionata.url_audio} type="audio/mpeg" />
                  Il tuo browser non supporta l'elemento audio.
                </audio>
              </div>
            )}
          </Col>
        </Row>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Chiudi
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

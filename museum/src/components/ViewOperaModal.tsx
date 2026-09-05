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
  const [immaginiList, setImmaginiList] = useState<string[]>([]);
  const [modello3dUrl, setModello3dUrl] = useState<string | null>(null);

  useEffect(() => {
    if (!show || !operaSelezionata?.id) {
      setImmaginiList([]);
      setModello3dUrl(null);
      return;
    }

    fetch(`http://localhost:3001/opere/${operaSelezionata.id}/assets`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        const assetList = Array.isArray(data) ? data : data.content || [];

        const foto2DList = assetList.filter(
          (a: any) => a.tipoUrl === "FOTO_2D",
        );
        const urls = foto2DList.map((f: any) => f.urlFile).filter(Boolean);
        setImmaginiList(urls);

        const asset3D = assetList.find((a: any) => a.tipoUrl === "MODELLO_3D");
        setModello3dUrl(asset3D ? asset3D.urlFile : null);
      })
      .catch((err) => {
        console.error("Errore nel recupero degli asset:", err);
        setImmaginiList([]);
        setModello3dUrl(null);
      });
  }, [show, operaSelezionata]);

  return (
    <>
      <style>{`
        .custom-fullscreen-modal {
          max-width: 90vw !important;
          width: 90vw !important;
        }
      `}</style>

      <Modal
        show={show}
        onHide={onHide}
        dialogClassName="custom-fullscreen-modal"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>
            {operaSelezionata?.titolo || "Opera d'arte"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row className="align-items-center">
            <Col lg={8} md={7} className="text-center mb-3 mb-md-0">
              {modello3dUrl ? (
                <div
                  className="border rounded bg-dark overflow-hidden"
                  style={{ height: "500px" }}
                >
                  {/* @ts-ignore */}
                  <model-viewer
                    src={modello3dUrl}
                    alt="Modello 3D Opera"
                    auto-rotate
                    camera-controls
                    shadow-intensity="1"
                    environment-image="neutral"
                    exposure="1"
                    style={{ width: "100%", height: "100%" }}
                  ></model-viewer>
                </div>
              ) : immaginiList.length > 0 ? (
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
                          maxHeight: "600px",
                          objectFit: "contain",
                          backgroundColor: "#000",
                        }}
                      />
                    </Carousel.Item>
                  ))}
                </Carousel>
              ) : (
                <div className="text-muted p-5 border rounded bg-light">
                  Nessun contenuto multimediale disponibile
                </div>
              )}
            </Col>

            <Col lg={4} md5={5}>
              {operaSelezionata?.url_audio && (
                <div className="my-4">
                  <h6 className="text-muted">Audio Guida</h6>
                  <audio controls className="w-100">
                    <source
                      src={operaSelezionata.url_audio}
                      type="audio/mpeg"
                    />
                    Il tuo browser non supporta l'elemento audio.
                  </audio>
                </div>
              )}
              <h5 className="text-secondary mb-3">Descrizione</h5>
              <p style={{ lineHeight: "1.6", fontSize: "1rem" }}>
                {operaSelezionata?.descrizione ||
                  "Nessuna descrizione disponibile."}
              </p>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Chiudi
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

import { useState, type ChangeEvent } from "react";
import { Modal, Form, Button, Container } from "react-bootstrap";
import ListaMusei from "./ListaMusei";

interface MuseoProps {
  currentUser?: Record<string, unknown>;
  onMuseoAggiunto?: () => void;
}

function Museo({ onMuseoAggiunto }: MuseoProps) {
  const [showModal, setShowModal] = useState<boolean>(false);

  const [refreshKey, setRefreshKey] = useState<number>(0);

  const [formData, setFormData] = useState({
    denominazione: "",
    indirizzo: "",
    citta: "",
    provincia: "",
    cap: "",
    accessoMuseo: "",
  });

  const handleClose = () => {
    setShowModal(false);
    setFormData({
      denominazione: "",
      indirizzo: "",
      citta: "",
      provincia: "",
      cap: "",
      accessoMuseo: "",
    });
  };

  const handleShow = () => setShowModal(true);

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (!userId) {
      alert("Errore: Utente non identificato. Effettua nuovamente il login.");
      return;
    }

    const payload = {
      ...formData,
      cap: Number(formData.cap),
      provincia: formData.provincia.trim().toUpperCase(),
      utenteId: userId,
    };

    try {
      const response = await fetch("http://localhost:3001/musei", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        const museoId = data.id || data._id;
        if (museoId) {
          localStorage.setItem("museoId", museoId);
        }
        localStorage.setItem("museoDenominazione", formData.denominazione);

        alert("Museo inserito con successo!");
        handleClose();

        setRefreshKey((prev) => prev + 1);
        if (onMuseoAggiunto) onMuseoAggiunto();
      } else {
        const errorData = await response.json();
        alert(
          `Errore di validazione: ${errorData.message || JSON.stringify(errorData)}`,
        );
      }
    } catch (error) {
      console.error("Errore di rete:", error);
    }
  };

  return (
    <>
      <Container>
        <h2 className="mb-4">Musei</h2>
        <Button variant="primary" onClick={handleShow} className="mb-4">
          Aggiungi un nuovo Museo
        </Button>

        <Modal show={showModal} onHide={handleClose} centered>
          <Modal.Header closeButton>
            <Modal.Title>Aggiungi un nuovo Museo</Modal.Title>
          </Modal.Header>

          <Modal.Body>
            <Form onSubmit={handleSubmit} id="museoForm">
              <Form.Group className="mb-3" controlId="denominazione">
                <Form.Label>Denominazione</Form.Label>
                <Form.Control
                  type="text"
                  name="denominazione"
                  value={formData.denominazione}
                  onChange={handleChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="indirizzo">
                <Form.Label>Indirizzo</Form.Label>
                <Form.Control
                  type="text"
                  name="indirizzo"
                  value={formData.indirizzo}
                  onChange={handleChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="citta">
                <Form.Label>Città</Form.Label>
                <Form.Control
                  type="text"
                  name="citta"
                  value={formData.citta}
                  onChange={handleChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="provincia">
                <Form.Label>Provincia</Form.Label>
                <Form.Control
                  type="text"
                  name="provincia"
                  value={formData.provincia}
                  onChange={handleChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="cap">
                <Form.Label>Cap</Form.Label>
                <Form.Control
                  type="number"
                  name="cap"
                  value={formData.cap}
                  onChange={handleChange}
                  required
                />
              </Form.Group>
            </Form>
          </Modal.Body>

          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Annulla
            </Button>

            <Button variant="primary" type="submit" form="museoForm">
              Salva Museo
            </Button>
          </Modal.Footer>
        </Modal>

        <ListaMusei key={refreshKey} />
      </Container>
    </>
  );
}

export default Museo;

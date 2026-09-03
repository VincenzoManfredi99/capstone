import { useEffect, useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import {
  Table,
  Container,
  Button,
  Spinner,
  Alert,
  Modal,
  Form,
} from "react-bootstrap";
import { useNavigate } from "react-router";

interface Museo {
  id?: string;
  _id?: string;
  denominazione: string;
  indirizzo: string;
  citta: string;
  provincia: string;
  cap?: number | string;
  accessoMuseo?: string;
}

const ListaMusei = () => {
  const navigate = useNavigate();
  const [musei, setMusei] = useState<Museo[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [showEditModal, setShowEditModal] = useState<boolean>(false);
  const [selectedMuseo, setSelectedMuseo] = useState<Museo | null>(null);
  const [formData, setFormData] = useState({
    denominazione: "",
    indirizzo: "",
    citta: "",
    provincia: "",
    cap: "",
    accessoMuseo: "",
  });

  const token = localStorage.getItem("token");

  // Spostiamo la chiamata direttamente dentro useEffect per rispettare le regole di React
  useEffect(() => {
    const fetchMusei = async () => {
      try {
        setLoading(true);
        const response = await fetch("http://localhost:3001/musei", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error("Errore durante il recupero dei musei dal server.");
        }

        const data = await response.json();
        setMusei(Array.isArray(data) ? data : data.content || []);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("Errore sconosciuto");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchMusei();
  }, [token]);

  // Funzione separata per ricaricare i dati dopo la modifica
  const reloadMusei = async () => {
    try {
      const response = await fetch("http://localhost:3001/musei", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setMusei(Array.isArray(data) ? data : data.content || []);
      }
    } catch (err) {
      console.error("Errore nel ricaricare i musei", err);
    }
  };

  const handleOpenEdit = (museo: Museo) => {
    setSelectedMuseo(museo);
    setFormData({
      denominazione: museo.denominazione || "",
      indirizzo: museo.indirizzo || "",
      citta: museo.citta || "",
      provincia: museo.provincia || "",
      cap: museo.cap ? String(museo.cap) : "",
      accessoMuseo: museo.accessoMuseo || "",
    });
    setShowEditModal(true);
  };

  const handleCloseEdit = () => {
    setShowEditModal(false);
    setSelectedMuseo(null);
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleUpdateSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const museoId = selectedMuseo?._id || selectedMuseo?.id;
    const userId = localStorage.getItem("userId");

    if (!museoId) return;

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
      const response = await fetch(`http://localhost:3001/musei/${museoId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        alert("Museo modificato con successo!");
        handleCloseEdit();
        reloadMusei();
      } else {
        const errorData = await response.json();
        alert(
          `Errore durante la modifica: ${errorData.message || JSON.stringify(errorData)}`,
        );
      }
    } catch (err) {
      console.error("Errore di rete:", err);
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="text-muted mt-2">Caricamento musei in corso...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">Errore: {error}</Alert>
      </Container>
    );
  }

  return (
    <Container className="my-5">
      <Table hover responsive>
        <thead className="text-muted small border-top-0">
          <tr>
            <th className="fw-semibold py-3 bg-transparent">Nome</th>
            <th className="fw-semibold py-3 bg-transparent">Indirizzo</th>
            <th className="fw-semibold py-3 bg-transparent">Città</th>
            <th className="fw-semibold py-3 bg-transparent">Provincia</th>
            <th className="fw-semibold py-3 bg-transparent text-end"></th>
          </tr>
        </thead>
        <tbody>
          {musei.length === 0 ? (
            <tr>
              <td colSpan={5} className="text-center py-4 text-muted">
                Nessun museo presente nel database.
              </td>
            </tr>
          ) : (
            musei.map((museo) => (
              <tr key={museo._id || museo.id}>
                <td
                  className="py-3 fw-semibold text-dark"
                  style={{ cursor: "pointer" }} // Trasforma il cursore in una manina
                  onClick={() => {
                    const id = museo._id || museo.id;
                    if (id) {
                      // Passiamo sia l'URL con l'ID sia lo state con il nome in un sol colpo
                      navigate(`/sala?museoId=${id}`, {
                        state: {
                          nomeMuseo: museo.denominazione || museo.denominazione,
                        },
                      });
                    }
                  }}
                  title="Clicca per gestire le sale"
                >
                  {museo.denominazione}
                </td>
                <td className="py-3 text-muted">{museo.indirizzo}</td>
                <td className="py-3 text-muted">{museo.citta}</td>
                <td className="py-3 text-muted">{museo.provincia}</td>
                <td className="py-3 text-end">
                  <Button
                    variant="link"
                    className="text-decoration-none p-0 text-primary fw-medium"
                    onClick={() => handleOpenEdit(museo)}
                  >
                    Edit
                  </Button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </Table>

      <Modal show={showEditModal} onHide={handleCloseEdit} centered>
        <Modal.Header closeButton>
          <Modal.Title>Modifica Museo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleUpdateSubmit} id="editMuseoForm">
            <Form.Group className="mb-3" controlId="editDenominazione">
              <Form.Label>Denominazione</Form.Label>
              <Form.Control
                type="text"
                name="denominazione"
                value={formData.denominazione}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="editIndirizzo">
              <Form.Label>Indirizzo</Form.Label>
              <Form.Control
                type="text"
                name="indirizzo"
                value={formData.indirizzo}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="editCitta">
              <Form.Label>Città</Form.Label>
              <Form.Control
                type="text"
                name="citta"
                value={formData.citta}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="editProvincia">
              <Form.Label>Provincia</Form.Label>
              <Form.Control
                type="text"
                name="provincia"
                value={formData.provincia}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="editCap">
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
          <Button variant="secondary" onClick={handleCloseEdit}>
            Annulla
          </Button>
          <Button variant="primary" type="submit" form="editMuseoForm">
            Salva Modifiche
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default ListaMusei;

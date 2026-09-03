import { useState, useEffect } from "react";
import { Container, Button, Modal, Form, Table } from "react-bootstrap";
import { useLocation, useSearchParams, useNavigate } from "react-router";

function Sala() {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [sale, setSale] = useState<any[]>([]);
  const [salaSelezionata, setSalaSelezionata] = useState<any | null>(null);

  const location = useLocation();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const museoId = searchParams.get("museoId");

  const [formData, setFormData] = useState({
    nome: "",
    descrizione: "",
    ordine: "",
  });

  const fetchSale = () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    fetch("http://localhost:3001/sala", {
      headers: { Authorization: `Bearer ` + token },
    })
      .then((res) => res.json())
      .then((data) => {
        const arraySale = Array.isArray(data) ? data : data.content || [];

        const saleFiltrate = arraySale.filter((s: any) => {
          const idSalaMuseo = s.museoId || s.museo?.id || s.museo;
          return String(idSalaMuseo) === String(museoId);
        });

        setSale(saleFiltrate);
      })
      .catch((err) => console.error("Errore nel recupero delle sale:", err));
  };

  useEffect(() => {
    if (museoId) fetchSale();
  }, [museoId]);

  const handleOpenCreate = () => {
    setSalaSelezionata(null);
    setFormData({ nome: "", descrizione: "", ordine: "" });
    setShowModal(true);
  };

  const handleOpenEdit = (sala: any) => {
    setSalaSelezionata(sala);
    setFormData({
      nome: sala.nome || "",
      descrizione: sala.descrizione || "",
      ordine: sala.ordine !== undefined ? String(sala.ordine) : "",
    });
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
    setSalaSelezionata(null);
    setFormData({ nome: "", descrizione: "", ordine: "" });
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem("token");

    const payload = {
      ...formData,
      ordine: Number(formData.ordine),
      museoId: museoId,
    };

    const isEdit = salaSelezionata !== null;
    const salaId = salaSelezionata?.id || salaSelezionata?._id;
    const url = isEdit
      ? `http://localhost:3001/sala/${salaId}`
      : "http://localhost:3001/sala";
    const method = isEdit ? "PUT" : "POST";

    fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (res.ok) {
          handleClose();
          fetchSale();
        } else {
          alert(
            isEdit
              ? "Errore durante la modifica della sala"
              : "Errore durante il salvataggio della sala",
          );
        }
      })
      .catch((err) => console.error("Errore di rete:", err));
  };

  const nomeMuseo =
    (location.state as { nomeMuseo?: string })?.nomeMuseo || "Museo";

  return (
    <Container className="py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Sale museo: {nomeMuseo}</h2>
        <Button variant="primary" onClick={handleOpenCreate}>
          Aggiungi una nuova Sala
        </Button>
      </div>

      <Table hover responsive className="align-middle border-bottom">
        <thead className="text-muted small border-top-0">
          <tr>
            <th className="fw-semibold py-3 bg-transparent">Ordine</th>
            <th className="fw-semibold py-3 bg-transparent">Nome Sala</th>
            <th className="fw-semibold py-3 bg-transparent">Descrizione</th>
            <th className="fw-semibold py-3 bg-transparent text-end"></th>
          </tr>
        </thead>
        <tbody>
          {sale.length === 0 ? (
            <tr>
              <td colSpan={4} className="text-center py-4 text-muted">
                Nessuna sala presente per questo museo.
              </td>
            </tr>
          ) : (
            sale.map((s) => {
              const salaId = s.id || s._id;
              return (
                <tr key={salaId}>
                  <td className="py-3 text-muted">{s.ordine}</td>
                  <td
                    className="py-3 fw-semibold text-dark"
                    style={{ cursor: "pointer" }}
                    onClick={() => {
                      if (salaId) {
                        navigate(`/scena?salaId=${salaId}`, {
                          state: {
                            nomeSala: s.nome,
                          },
                        });
                      }
                    }}
                    title="Clicca per gestire le scene"
                  >
                    {s.nome}
                  </td>
                  <td className="py-3 text-muted">{s.descrizione}</td>
                  <td className="py-3 text-end">
                    <Button
                      variant="link"
                      className="text-decoration-none p-0 text-primary fw-medium"
                      onClick={() => handleOpenEdit(s)}
                    >
                      Edit
                    </Button>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={handleClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>
            {salaSelezionata ? "Modifica Sala" : "Aggiungi una nuova Sala"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit} id="salaForm">
            <Form.Group className="mb-3" controlId="nome">
              <Form.Label>Nome Sala</Form.Label>
              <Form.Control
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="descrizione">
              <Form.Label>Descrizione</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="descrizione"
                value={formData.descrizione}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="ordine">
              <Form.Label>Ordine</Form.Label>
              <Form.Control
                type="number"
                name="ordine"
                value={formData.ordine}
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
          <Button variant="primary" type="submit" form="salaForm">
            {salaSelezionata ? "Salva Modifiche" : "Salva Sala"}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default Sala;

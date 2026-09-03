import { useState } from "react";
import { Modal, Form, Button, Container } from "react-bootstrap";
import { Link, useNavigate } from "react-router";

function Registrazione() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    nome: "",
    cognome: "",
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:3001/utenti", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Errore durante la registrazione");
      }

      const data = await response.json();
      console.log("Registrazione avvenuta con successo:", data);
      alert("Registrazione completata con successo!");

      // Reindirizza l'utente alla pagina di login
      navigate("/login");
    } catch (error) {
      console.error("Errore:", error);
      alert(
        "Si è verificato un errore durante la registrazione. Controlla i dati.",
      );
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center">
      <div
        className="modal show w-100"
        style={{ display: "block", position: "initial" }}
      >
        <Modal.Dialog className="shadow-sm">
          <Form className="p-4" onSubmit={handleSubmit}>
            <div className="mb-4">
              <h3 className="fw-bold">Registrazione</h3>
              <p className="text-muted mb-0">
                Hai già un profilo? <Link to="/login">Accedi</Link>
              </p>
            </div>

            <Form.Group className="mb-4">
              <Form.Label>Nome</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Mario"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
              />
            </Form.Group>

            <Form.Group className="mb-4">
              <Form.Label>Cognome</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Rossi"
                name="cognome"
                value={formData.cognome}
                onChange={handleChange}
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control
                required
                type="email"
                placeholder="nome@esempio.com"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
            </Form.Group>

            <Form.Group className="mb-4" controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                required
                type="password"
                placeholder="********"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            </Form.Group>

            <Button variant="dark" type="submit" className="w-100 py-2">
              Registrati
            </Button>
          </Form>
        </Modal.Dialog>
      </div>
    </Container>
  );
}

export default Registrazione;

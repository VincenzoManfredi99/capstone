import { useState } from "react";
import { Modal, Form, Button, Container } from "react-bootstrap";
import { Link, useNavigate } from "react-router";

function Login() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:3001/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Credenziali non valide");
      }

      const data = await response.json();
      console.log("Login effettuato con successo:", data);

      const token = data.accessToken || data.token;
      localStorage.setItem("token", token);

      try {
        const base64Url = token.split(".")[1];
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
        const jsonPayload = decodeURIComponent(
          atob(base64)
            .split("")
            .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
            .join(""),
        );
        const decoded = JSON.parse(jsonPayload);
        if (decoded.sub) {
          localStorage.setItem("userId", decoded.sub);
        }
      } catch (err) {
        console.error("Errore nell'estrazione dell'ID dal token:", err);
      }

      alert("Login effettuato con successo!");
      navigate("/museo");
    } catch (error) {
      console.error("Errore di login:", error);
      alert("Email o password errati.");
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
              <h3 className="fw-bold">Login</h3>
              <p className="text-muted mb-0">
                Sei nuovo su Museum? <Link to="/registrazione">Registrati</Link>
              </p>
            </div>

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
              Accedi
            </Button>
          </Form>
        </Modal.Dialog>
      </div>
    </Container>
  );
}

export default Login;

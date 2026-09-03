import { Button, Nav } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";
import MuseumLogo from "../assets/MuseumLogo.png";
import { Link } from "react-router";

function MyNavbar() {
  return (
    <Navbar expand="lg" className="bg-body-tertiary p-3 m-0">
      <Container>
        <Navbar.Brand href="/home">
          <img src={MuseumLogo} style={{ width: "250px", height: "60px" }} />
        </Navbar.Brand>
        <Nav className="ms-auto d-flex flex-row gap-2 align-items-center">
          <Nav.Link as={Link} to="/info">
            <Button variant="outline-dark">Per i Musei</Button>
          </Nav.Link>
          <Nav.Link as={Link} to="/login">
            <Button variant="outline-dark">Login</Button>
          </Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
}

export default MyNavbar;

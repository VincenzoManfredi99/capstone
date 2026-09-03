import { Carousel, Image } from "react-bootstrap";
import MuseoFoto1 from "../assets/FotoMuseo1.png";
import MuseoFoto2 from "../assets/FotoMuseo2.png";
import MuseoFoto3 from "../assets/FotoMuseo3.png";
import MuseoFoto4 from "../assets/FotoMuseo4.png";

function Home() {
  return (
    <div className="w-75 my-4 mx-auto overflow-hidden rounded-3 shadow">
      <div className="w-100 overflow-hidden">
        <Carousel fade interval={5000} pause="hover" wrap={true}>
          <Carousel.Item>
            <Image
              className="d-block w-100 object-fit-cover"
              style={{ height: "850px" }}
              src={MuseoFoto1}
              alt="Prima slide"
            />
            <Carousel.Caption>
              <h3 style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Esplora le sale in alta definizione
              </h3>
              <p style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Porta i visitatori dentro il tuo museo con tour panoramici a
                360° fluidi e immersivi, accessibili da qualsiasi dispositivo.
              </p>
            </Carousel.Caption>
          </Carousel.Item>

          <Carousel.Item>
            <Image
              className="d-block w-100 object-fit-cover"
              style={{ height: "850px" }}
              src={MuseoFoto2}
              alt="Seconda slide"
            />
            <Carousel.Caption>
              <h3 style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Valorizza ogni opera con gli Hotspot
              </h3>
              <p style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Inserisci punti interattivi sui dipinti e sulle sculture per
                svelare dettagli nascosti, curiosità storiche e schede di
                approfondimento.
              </p>
            </Carousel.Caption>
          </Carousel.Item>

          <Carousel.Item>
            <Image
              className="d-block w-100 object-fit-cover"
              style={{ height: "850px" }}
              src={MuseoFoto3}
              alt="Terza slide"
            />
            <Carousel.Caption>
              <h3 style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Multimedia e modelli 3D interattivi
              </h3>
              <p
                className="fw-bold"
                style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}
              >
                Arricchisci l'esperienza con audioguide immersive e permetti
                agli utenti di ruotare ed esaminare i modelli 3D delle opere da
                vicino.
              </p>
            </Carousel.Caption>
          </Carousel.Item>

          <Carousel.Item>
            <Image
              className="d-block w-100 object-fit-cover"
              style={{ height: "850px" }}
              src={MuseoFoto4}
              alt="Quarta slide"
            />
            <Carousel.Caption>
              <h3 style={{ textShadow: "2px 2px 4px rgba(0,0,0,0.8)" }}>
                Dal digitale al merchandising reale
              </h3>
              <p
                className="fw-bold"
                style={{ textShadow: "2px 2px 2px rgba(0,0,0,0.8)" }}
              >
                Sfrutta i modelli digitali per creare stampe 3D e souvenir
                unici, offrendo ai visitatori un ricordo tangibile nel bookshop
                del museo.
              </p>
            </Carousel.Caption>
          </Carousel.Item>
        </Carousel>
      </div>
    </div>
  );
}

export default Home;

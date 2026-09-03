import { Row, Col, Button, Container } from "react-bootstrap";
import Immagine1 from "../assets/Immagine1.png";
import Immagine2 from "../assets/Immagine2.jpg";
import icona1 from "../assets/icona1-Photoroom.png";
import icona2 from "../assets/icona 2-Photoroom.png";
import icona3 from "../assets/icona 3-Photoroom.png";
import Faq from "./Faq";

function PaginaInfo() {
  return (
    <>
      <Container>
        <Row className="px-2">
          <Col xs={12} md={3} className="p-0">
            <img
              src={Immagine1}
              alt="Foto Corridoio Museo"
              className="img-fluid"
            />
          </Col>
          <Col xs={12} md={6} className="text-center p-0">
            <h1 className="fw-bold mb-3">Digitalizza il tuo museo!</h1>
            <h5>
              Porta il tuo patrimonio culturale ovunque nel mondo. Su Museum
              puoi trasformare le tue sale in un'esperienza immersiva. Unisciti
              alla rete di musei che innovano.
            </h5>
            <Button variant="success">Inizia ora!</Button>
          </Col>
          <Col xs={12} md={3} className="p-0 me-0">
            <img src={Immagine2} alt="Foto Opera Vaso" className="img-fluid" />
          </Col>
        </Row>
        <h4 className="m-5 text-center">Esplora senza confini</h4>

        {/*Tre card*/}

        <Row className="justify-content-between px-3 g-2">
          <Col xs={12} md={4} className="px-2">
            <div
              className="text-center rounded-4 p-2 h-100"
              style={{ backgroundColor: "#caffe5" }}
            >
              <img
                src={icona1}
                alt="Icona telefono"
                className="img-fluid mb-3"
                style={{ width: "65px", height: "70px" }}
              />
              <h4>Tour a 360° immersivi</h4>
              <p>
                Fai camminare i visitatori virtualmente tra le sale con foto
                panoramiche ad alta definizione.
              </p>
            </div>
          </Col>

          <Col xs={12} md={4} className="px-2">
            <div
              className="text-center rounded-4 p-2 h-100"
              style={{ backgroundColor: "#91e3bb" }}
            >
              <img
                src={icona2}
                alt="Icona quadro"
                className="img-fluid mb-3"
                style={{ width: "70px", height: "70px" }}
              />
              <h4>Hotspot interattivi</h4>
              <p>
                Posiziona punti di interesse sulle opere d'arte per permettere
                al visitatore di esaminarle da vicino e scoprire i dettagli
                nascosti.
              </p>
            </div>
          </Col>

          <Col xs={12} md={4} className="px-2">
            <div
              className="text-center  rounded-4 p-2 h-100"
              style={{ backgroundColor: "#43d48e" }}
            >
              <img
                src={icona3}
                alt="Icona Multidiale"
                className="img-fluid mb-3"
                style={{ width: "110px", height: "70px" }}
              />
              <h4>Multimedialità e 3D</h4>
              <p>
                Arricchisci l'esperienza con descrizioni testuali, tracce audio
                e modelli 3D interattivi per un'immersione totale.
              </p>
            </div>
          </Col>
        </Row>

        {/* Gadget */}

        <h2 className="m-3 mt-5 text-center">
          Dallo schermo al palmo della mano
        </h2>
        <h5 className="text-center">
          Trasforma le opere del tuo museo in prodotti unici da vendere ai
          visitatori. Sfruttando i modelli 3D ad alta definizione realizzati per
          il tour, puoi dare vita a una linea esclusiva di gadget e souvenir da
          offrire sia nel bookshop fisico che online.
        </h5>
        <ul className="my-4">
          <li className="my-2">
            <span className="fw-bold">Nuovi canali di guadagno:</span> Crea una
            collezione di repliche in miniatura pronte per la vendita,
            valorizzando il patrimonio artistico in modo innovativo.
          </li>
          <li className="my-2">
            <span className="fw-bold">
              Zero sprechi con la produzione on-demand:
            </span>
            Grazie alla stampa 3D, puoi realizzare i gadget su ordinazione senza
            dover gestire magazzini o scorte invendute.
          </li>
          <li className="my-2">
            <span className="fw-bold">Ricordi memorabili:</span>
            Offri a turisti e appassionati l'opportunità di portare a casa un
            pezzo autentico della tua esposizione.
          </li>
        </ul>

        {/* FAQ */}

        <h2 className="m-3 my-5">Scopri gli aspetti principali di Museum</h2>
        <Row className="justify-content-center">
          <Col xs={9}>
            <Faq
              domanda="Che tipo di museo può aderire alla piattaforma?"
              risposta="La nostra piattaforma è aperta a qualsiasi spazio espositivo: musei d'arte, archeologici, scientifici, gallerie d'arte contemporanea e case museo. Che tu abbia una collezione sterminata o una piccola mostra temporanea, valorizziamo ogni opera."
            />
            <Faq
              domanda="Ho bisogno di attrezzature particolari per le foto a 360°?"
              risposta="Non preoccuparti! Puoi occupartene tu con una telecamera a 360 gradi o puoi affidarti al nostro team di esperti che verrà direttamente in sede per realizzare le riprese panoramiche professionali e scansionare i modelli 3D."
            />
            <Faq
              domanda="Quanto tempo richiede l'allestimento del museo digitale?"
              risposta="Dipende dalla grandezza del museo e dal numero di opere da censire. La configurazione della piattaforma è immediata: una volta caricate le foto a 360 gradi, potrai inserire gradualmente gli hotspot, i file audio e i modelli 3D in autonomia o con il nostro supporto passo dopo passo."
            />
            <Faq
              domanda="Come posso ricevere assistenza se ho dubbi?"
              risposta="Puoi contattarci in qualsiasi momento tramite il nostro modulo di supporto dedicato ai partner culturali."
            />
          </Col>
        </Row>
      </Container>
    </>
  );
}

export default PaginaInfo;

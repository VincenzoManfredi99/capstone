import { useState, useEffect, useCallback, useRef } from "react";
import { Container, Button, Alert, Card, ListGroup } from "react-bootstrap";
import { useLocation, useSearchParams } from "react-router";

import { ChoiceModal } from "./ChoiceModalProps";
import { MovimentoModal } from "./MovimentoModal";
import { OperaModal } from "./OperaModalProps";
import { ViewOperaModal } from "./ViewOperaModal";
import { UploadScenaModal } from "./UploadScenaModal";
import { AssetModal } from "./AssetModal";

interface ScenaItem {
  id?: string;
  _id?: string;
  foto360: string;
  salaId?: string;
  sala_id?: string;
  sala?: string | { id: string };
}

interface HotspotItem {
  id?: string;
  tipo: "MOVIMENTO" | "OPERA";
  pitch: number;
  yaw: number;
  targetScenaId?: string;
  target_scena_id?: string;
  targetScena?: { id?: string; _id?: string } | string;
  opera?: {
    id?: string;
    _id?: string;
    titolo?: string;
    titolo_opera?: string;
    descrizione?: string;
    descrizione_opera?: string;
    url_audio?: string;
    urlAudio?: string;
  };
}

interface LocationState {
  nomeSala?: string;
}

function Scena() {
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const salaId = searchParams.get("salaId");

  const [sceneList, setSceneList] = useState<ScenaItem[]>([]);
  const [scenaCorrente, setScenaCorrente] = useState<ScenaItem | null>(null);
  const [hotspotsCorrenti, setHotspotsCorrenti] = useState<HotspotItem[]>([]);

  const [showUploadModal, setShowUploadModal] = useState<boolean>(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const [isAddingHotspot, setIsAddingHotspot] = useState<boolean>(false);
  const [tempCoords, setTempCoords] = useState<{
    pitch: number;
    yaw: number;
  } | null>(null);
  const [showChoiceModal, setShowChoiceModal] = useState<boolean>(false);

  const [showMovimentoModal, setShowMovimentoModal] = useState<boolean>(false);
  const [targetScenaId, setTargetScenaId] = useState<string>("");

  const [showOperaModal, setShowOperaModal] = useState<boolean>(false);
  const [titoloOpera, setTitoloOpera] = useState<string>("");
  const [descrizioneOpera, setDescrizioneOpera] = useState<string>("");
  const [urlAudioOpera, setUrlAudioOpera] = useState<string>("");

  const [showViewOperaModal, setShowViewOperaModal] = useState<boolean>(false);
  const [operaSelezionata, setOperaSelezionata] = useState<{
    id?: string;
    titolo?: string;
    descrizione?: string;
    url_audio?: string;
  } | null>(null);

  // Stati per la gestione degli asset dell'opera
  const [showAssetModal, setShowAssetModal] = useState<boolean>(false);
  const [operaPerAsset, setOperaPerAsset] = useState<any | null>(null);

  const viewerRef = useRef<HTMLDivElement>(null);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const viewerInstanceRef = useRef<any>(null);

  const state = location.state as LocationState | null;
  const nomeSala = state?.nomeSala || "Sala";

  const fetchScene = useCallback(() => {
    const token = localStorage.getItem("token");
    if (!token || !salaId) return;

    fetch("http://localhost:3001/scene", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data: unknown) => {
        const rawArray = Array.isArray(data)
          ? data
          : (data as { content?: ScenaItem[] })?.content || [];

        const sceneFiltrate = rawArray.filter((scena: ScenaItem) => {
          const idCollegato =
            scena.salaId ||
            scena.sala_id ||
            (typeof scena.sala === "object" ? scena.sala?.id : scena.sala);
          return String(idCollegato) === String(salaId);
        });

        setSceneList(sceneFiltrate);
        if (sceneFiltrate.length > 0 && !scenaCorrente) {
          setScenaCorrente(sceneFiltrate[0]);
        }
      })
      .catch((err) => console.error("Errore nel recupero delle scene:", err));
  }, [salaId, scenaCorrente]);

  const fetchHotspots = useCallback(async (idScena: string) => {
    const token = localStorage.getItem("token");
    if (!token) return;

    setHotspotsCorrenti([]);

    try {
      const [resHotspot, resOpere] = await Promise.all([
        fetch(`http://localhost:3001/hotspot?scenaId=${idScena}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        fetch(`http://localhost:3001/opere`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ]);

      const hotspotsData = await resHotspot.json();
      const opereData = await resOpere.json();

      const listaHotspots = Array.isArray(hotspotsData)
        ? hotspotsData
        : hotspotsData.content || [];
      const listaOpere = Array.isArray(opereData)
        ? opereData
        : opereData.content || [];

      const hotspotsConOperaMappati = listaHotspots.map((hs: any) => {
        if (hs.tipo === "OPERA") {
          const hsId = hs.id || hs._id;

          const operaTrovata = listaOpere.find((op: any) => {
            const hId =
              op.hotspot?.id ||
              op.hotspot?._id ||
              op.hotspot ||
              op.hotspotId?.id ||
              op.hotspotId?._id ||
              op.hotspotId;

            return String(hId) === String(hsId);
          });

          if (operaTrovata) {
            return {
              ...hs,
              opera: operaTrovata,
            };
          }
        }
        return hs;
      });

      setHotspotsCorrenti(hotspotsConOperaMappati);
    } catch (err) {
      console.error("Errore nel recupero di hotspot e opere:", err);
    }
  }, []);

  useEffect(() => {
    if (salaId) fetchScene();
  }, [salaId, fetchScene]);

  useEffect(() => {
    const idScena = scenaCorrente?.id || scenaCorrente?._id;
    if (idScena) {
      fetchHotspots(idScena);
    } else {
      setHotspotsCorrenti([]);
    }
  }, [scenaCorrente, fetchHotspots]);

  useEffect(() => {
    if (scenaCorrente?.foto360 && viewerRef.current) {
      viewerRef.current.innerHTML = "";

      const formattedHotspots = hotspotsCorrenti.map((hs) => {
        let targetId = hs.targetScenaId || hs.target_scena_id;
        if (!targetId && hs.targetScena) {
          targetId =
            typeof hs.targetScena === "object"
              ? hs.targetScena.id || hs.targetScena._id
              : hs.targetScena;
        }

        if (hs.tipo === "MOVIMENTO") {
          return {
            pitch: hs.pitch,
            yaw: hs.yaw,
            type: "info",
            text: "🚀 Vai alla prossima scena",
            clickHandlerFunc: () => {
              const prossimaScena = sceneList.find(
                (s) => String(s.id || s._id) === String(targetId),
              );
              if (prossimaScena) {
                setScenaCorrente(prossimaScena);
              } else {
                alert("Scena di destinazione non trovata");
              }
            },
          };
        } else {
          const operaData = hs.opera || {};
          const titoloOperaTesto =
            operaData.titolo || operaData.titolo_opera || "Opera d'arte";
          const descrizioneTesto =
            operaData.descrizione ||
            operaData.descrizione_opera ||
            "Nessuna descrizione disponibile.";
          const audioTesto = operaData.url_audio || operaData.urlAudio;

          return {
            pitch: hs.pitch,
            yaw: hs.yaw,
            type: "info",
            text: `🖼️ ${titoloOperaTesto}`,
            clickHandlerFunc: () => {
              setOperaSelezionata({
                id: operaData.id || operaData._id,
                titolo: titoloOperaTesto,
                descrizione: descrizioneTesto,
                url_audio: audioTesto,
              });
              setShowViewOperaModal(true);
            },
          };
        }
      });

      // @ts-expect-error pannellum caricato da CDN globale
      if (window.pannellum) {
        // @ts-expect-error pannellum caricato da CDN globale
        viewerInstanceRef.current = window.pannellum.viewer(viewerRef.current, {
          type: "equirectangular",
          panorama: scenaCorrente.foto360,
          autoLoad: true,
          hfov: 110,
          hotSpots: formattedHotspots,
        });
      }
    }
  }, [scenaCorrente, hotspotsCorrenti, sceneList]);

  const handleConfermaPuntoCentrale = () => {
    if (viewerInstanceRef.current) {
      const pitch = viewerInstanceRef.current.getPitch();
      const yaw = viewerInstanceRef.current.getYaw();
      setTempCoords({ pitch, yaw });
      setShowChoiceModal(true);
      setIsAddingHotspot(false);
    }
  };

  const handleSalvaHotspot = async (
    tipo: "MOVIMENTO" | "OPERA",
    extraData = {},
  ) => {
    const token = localStorage.getItem("token");
    const idScena = scenaCorrente?.id || scenaCorrente?._id;

    if (!token || !idScena || !tempCoords) {
      alert(
        "Errore: Impossibile determinare la scena corrente o le coordinate.",
      );
      return;
    }

    try {
      const payloadHotspot = {
        tipo,
        pitch: tempCoords.pitch,
        yaw: tempCoords.yaw,
        scenaId: idScena,
        ...extraData,
      };

      const resHotspot = await fetch("http://localhost:3001/hotspot", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payloadHotspot),
      });

      if (!resHotspot.ok) {
        throw new Error("Errore nel salvataggio dell'hotspot");
      }

      const hotspotCreato = await resHotspot.json();
      const hotspotIdFinal = hotspotCreato.id || hotspotCreato._id;

      if (tipo === "OPERA") {
        const payloadOpera = {
          titolo: titoloOpera,
          descrizione: descrizioneOpera,
          url_audio: urlAudioOpera,
          hotspotId: hotspotIdFinal,
        };

        const resOpera = await fetch("http://localhost:3001/opere", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payloadOpera),
        });

        if (!resOpera.ok) {
          throw new Error(
            "Errore durante la creazione dell'opera nel database",
          );
        }
      }

      fetchHotspots(idScena);
      setShowMovimentoModal(false);
      setShowOperaModal(false);
      setTempCoords(null);
      setTargetScenaId("");
      setTitoloOpera("");
      setDescrizioneOpera("");
      setUrlAudioOpera("");
    } catch (err) {
      console.error("Errore di rete o di salvataggio:", err);
      alert("Si è verificato un errore durante il salvataggio.");
    }
  };

  const handleUploadScena = (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!selectedFile) return;

    const formData = new FormData();
    formData.append("foto360", selectedFile);
    if (salaId) formData.append("salaId", salaId);

    fetch("http://localhost:3001/scene", {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
      body: formData,
    })
      .then((res) => {
        if (res.ok) {
          setShowUploadModal(false);
          setSelectedFile(null);
          fetchScene();
        } else {
          alert("Errore nel caricamento della scena 360");
        }
      })
      .catch((err) => console.error("Errore di rete:", err));
  };

  return (
    <Container className="py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Visualizzatore 360: Sala {nomeSala}</h2>
        <div className="d-flex gap-2">
          {scenaCorrente && (
            <Button
              variant={isAddingHotspot ? "danger" : "outline-primary"}
              onClick={() => setIsAddingHotspot(!isAddingHotspot)}
            >
              {isAddingHotspot ? "Annulla Inserimento" : "📍 Aggiungi Hotspot"}
            </Button>
          )}
          <Button variant="primary" onClick={() => setShowUploadModal(true)}>
            Carica Foto 360 da PC
          </Button>
        </div>
      </div>

      {isAddingHotspot && (
        <Alert
          variant="info"
          className="d-flex justify-content-between align-items-center"
        >
          <span>
            🎯 <strong>Modalità inserimento attiva:</strong> Muoviti nella foto
            360 e posiziona al centro dello schermo il punto in cui vuoi
            inserire l'hotspot, poi clicca sul pulsante a destra.
          </span>
          <Button
            variant="success"
            size="sm"
            onClick={handleConfermaPuntoCentrale}
          >
            Conferma qui il punto
          </Button>
        </Alert>
      )}

      {sceneList.length > 1 && (
        <div className="mb-3 d-flex gap-2 align-items-center">
          <span className="text-muted small fw-semibold">
            Cambia vista scena:
          </span>
          {sceneList.map((scena, index) => (
            <Button
              key={scena.id || scena._id || index}
              variant={scenaCorrente?.id === scena.id ? "dark" : "outline-dark"}
              size="sm"
              onClick={() => setScenaCorrente(scena)}
            >
              Scena {index + 1}
            </Button>
          ))}
        </div>
      )}

      {scenaCorrente ? (
        <div
          ref={viewerRef}
          className="border rounded shadow-sm overflow-hidden position-relative mb-4"
          style={{ width: "100%", height: "550px" }}
        />
      ) : (
        <div className="text-center py-5 border rounded bg-light text-muted mb-4">
          <h4>Nessuna foto 360 caricata per questa sala.</h4>
          <p className="mb-0">
            Clicca su "Carica Foto 360 da PC" per iniziare.
          </p>
        </div>
      )}

      {/* Sezione Lista Opere Collegate alla Scena */}
      <Card className="shadow-sm">
        <Card.Header className="bg-white py-3">
          <h5 className="mb-0">🖼️ Opere d'arte in questa Scena</h5>
        </Card.Header>
        <ListGroup variant="flush">
          {hotspotsCorrenti.filter((hs) => hs.tipo === "OPERA" && hs.opera)
            .length === 0 ? (
            <ListGroup.Item className="text-muted text-py-3 text-center">
              Nessuna opera registrata tramite hotspot in questa scena.
            </ListGroup.Item>
          ) : (
            hotspotsCorrenti
              .filter((hs) => hs.tipo === "OPERA" && hs.opera)
              .map((hs, idx) => {
                const op = hs.opera!;
                const titolo = op.titolo || op.titolo_opera || "Senza titolo";
                const descrizione =
                  op.descrizione ||
                  op.descrizione_opera ||
                  "Nessuna descrizione";

                return (
                  <ListGroup.Item
                    key={hs.id || hs._id || idx}
                    className="d-flex justify-content-between align-items-center py-3"
                  >
                    <div>
                      <h6 className="mb-1 fw-bold">{titolo}</h6>
                      <p className="mb-0 text-muted small">{descrizione}</p>
                    </div>
                    <Button
                      variant="outline-secondary"
                      size="sm"
                      onClick={() => {
                        setOperaPerAsset(op);
                        setShowAssetModal(true);
                      }}
                    >
                      📁 Gestisci Asset (Foto/3D)
                    </Button>
                  </ListGroup.Item>
                );
              })
          )}
        </ListGroup>
      </Card>

      <ChoiceModal
        show={showChoiceModal}
        onHide={() => setShowChoiceModal(false)}
        tempCoords={tempCoords}
        onSelectMovimento={() => {
          setShowChoiceModal(false);
          setShowMovimentoModal(true);
        }}
        onSelectOpera={() => {
          setShowChoiceModal(false);
          setShowOperaModal(true);
        }}
      />

      <MovimentoModal
        show={showMovimentoModal}
        onHide={() => setShowMovimentoModal(false)}
        sceneList={sceneList}
        scenaCorrente={scenaCorrente}
        targetScenaId={targetScenaId}
        setTargetScenaId={setTargetScenaId}
        onSave={() => handleSalvaHotspot("MOVIMENTO", { targetScenaId })}
      />

      <OperaModal
        show={showOperaModal}
        onHide={() => setShowOperaModal(false)}
        titoloOpera={titoloOpera}
        setTitoloOpera={setTitoloOpera}
        descrizioneOpera={descrizioneOpera}
        setDescrizioneOpera={setDescrizioneOpera}
        urlAudioOpera={urlAudioOpera}
        setUrlAudioOpera={setUrlAudioOpera}
        onSave={() => handleSalvaHotspot("OPERA")}
      />

      <ViewOperaModal
        show={showViewOperaModal}
        onHide={() => setShowViewOperaModal(false)}
        operaSelezionata={operaSelezionata}
      />

      <AssetModal
        show={showAssetModal}
        onHide={() => setShowAssetModal(false)}
        operaSelezionata={operaPerAsset}
        onSuccess={() => {
          const idScena = scenaCorrente?.id || scenaCorrente?._id;
          if (idScena) fetchHotspots(idScena);
        }}
      />

      <UploadScenaModal
        show={showUploadModal}
        onHide={() => setShowUploadModal(false)}
        onSubmit={handleUploadScena}
        onFileChange={(e) => {
          const target = e.target as HTMLInputElement;
          if (target.files && target.files[0]) {
            setSelectedFile(target.files[0]);
          }
        }}
      />
    </Container>
  );
}

export default Scena;

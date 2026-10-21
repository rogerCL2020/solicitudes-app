
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ListadoSolicitudes from '../features/solicitudes/pages/ListadoSolicitudes';
import DetalleSolicitud from '../features/solicitudes/pages/DetalleSolicitud';
import RegistrarSolicitud from '../features/solicitudes/pages/RegistrarSolicitud';



export default function App() {
  return (
    <BrowserRouter>
      <header className="app-header">
        <h1>DINET | Gestión de solicitudes</h1>
        <p>Plataforma de administración y seguimiento</p>
      </header>

      <Routes>
        <Route path="/" element={<Navigate to="/solicitudes" replace />} />
        <Route path="/solicitudes" element={<ListadoSolicitudes />} />
        <Route path="/solicitudes/nueva" element={<RegistrarSolicitud />} />
        <Route path="/solicitudes/:codigo" element={<DetalleSolicitud />} />
      </Routes>
    </BrowserRouter>
  );
}

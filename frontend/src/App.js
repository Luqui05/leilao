import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import RecoverPassword from "./pages/RecoverPassword";
import ChangePassword from "./pages/ChangePassword";
import Home from "./pages/Home";
import CategoriasList from "./pages/CategoriasList";
import PermissoesUsuariosList from "./pages/PermissoesUsuariosList";
import LeiloesList from "./pages/LeilõesList";
import ProtectedRoute from "./components/layout/ProtectedRoute";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Register />} />
      <Route path="/recuperar-senha" element={<RecoverPassword />} />
      <Route path="/alterar-senha" element={<ChangePassword />} />
      <Route
        path="/home"
        element={
          <ProtectedRoute>
            <Home />
          </ProtectedRoute>
        }
      />
      <Route
        path="/categorias"
        element={
          <ProtectedRoute>
            <CategoriasList />
          </ProtectedRoute>
        }
      />
      <Route
        path="/perfis"
        element={
          <ProtectedRoute>
            <PermissoesUsuariosList />
          </ProtectedRoute>
        }
      />
      <Route
        path="/leiloes"
        element={
          <ProtectedRoute>
            <LeiloesList />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;

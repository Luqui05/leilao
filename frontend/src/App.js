import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import RecoverPassword from './pages/RecoverPassword';
import ChangePassword from './pages/ChangePassword';
import Home from './pages/Home';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Register />} />
      <Route path="/recuperar-senha" element={<RecoverPassword />} />
      <Route path="/alterar-senha" element={<ChangePassword />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
}

export default App;

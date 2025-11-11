import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

export default function Home() {
  const navigate = useNavigate();

  const logout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="p-4">
      <h2>Bem-vindo ao sistema</h2>
      <p>Você está autenticado. Use o menu para navegar.</p>
      <Button label="Sair" icon="pi pi-sign-out" onClick={logout} />
    </div>
  );
}

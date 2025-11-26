import { Button } from "primereact/button";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";

export default function Home() {
  const navigate = useNavigate();

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  return (
    <div className="p-4">
      <h2 className="mb-3">Bem-vindo ao sistema</h2>
      <p className="mb-4">Você está autenticado. Selecione uma opção:.</p>
      <div className="flex gap-2 mb-4">
        <Button
          label="Categorias"
          icon="pi pi-list"
          onClick={() => navigate("/categorias")}
        />
        <Button
          label="Permissões"
          icon="pi pi-users"
          onClick={() => navigate("/perfis")}
        />
        <Button
          label="Sair"
          icon="pi pi-sign-out"
          className="p-button-danger"
          onClick={logout}
        />
      </div>
    </div>
  );
}

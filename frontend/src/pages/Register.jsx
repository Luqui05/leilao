import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import authService from "../services/authService";

export default function Register() {
  const navigate = useNavigate();
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [errors, setErrors] = useState({ nome: "", email: "", senha: "" });

  const validar = () => {
    const next = { nome: "", email: "", senha: "" };
    if (!nome) next.nome = "Informe o nome.";
    if (!email) next.email = "Informe o e-mail.";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email))
      next.email = "E-mail inválido.";
    if (!senha) next.senha = "Informe a senha.";
    setErrors(next);
    return !next.nome && !next.email && !next.senha;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!validar()) return;
    setSubmitting(true);
    try {
      // integra com backend: cria Pessoa (nome, email, senha)
      await authService.register({ nome, email, senha });
      // sucesso
      alert('Cadastro realizado com sucesso. Verifique seu e-mail (se aplicável).');
      navigate('/login');
    } catch (err) {
      // mostra erro amigável (network/server)
      alert(err?.message || 'Falha ao efetuar cadastro.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="surface-ground min-h-screen flex align-items-center justify-content-center p-3">
      <div className="w-full" style={{ maxWidth: 480 }}>
        <Card title="Novo Cadastro" className="shadow-2">
          <form onSubmit={onSubmit} className="p-fluid register-form form-with-left-icons">
            <div className="field mb-3">
              <label htmlFor="nome" className="block mb-2">
                Nome
              </label>
              <InputText
                id="nome"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className={errors.nome ? "p-invalid w-full" : "w-full"}
                placeholder="Seu nome completo"
                autoComplete="name"
              />
              {errors.nome && <small className="p-error">{errors.nome}</small>}
            </div>

            <div className="field mb-3">
              <label htmlFor="email" className="block mb-2">
                E-mail
              </label>
              <span className="p-input-icon-left w-full">
                <i className="pi pi-envelope" />
                <InputText
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className={errors.email ? "p-invalid w-full" : "w-full"}
                  placeholder="seuemail@exemplo.com"
                  autoComplete="email"
                />
              </span>
              {errors.email && (
                <small className="p-error">{errors.email}</small>
              )}
            </div>

            <div className="field mb-3">
              <label htmlFor="senha" className="block mb-2">Senha</label>
              <InputText
                id="senha"
                type="password"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                className={errors.senha ? "p-invalid w-full" : "w-full"}
                placeholder="Sua senha"
                autoComplete="new-password"
              />
              {errors.senha && <small className="p-error">{errors.senha}</small>}
            </div>

            <div className="flex flex-column sm:flex-row gap-2 justify-content-between mt-2">
              <Button
                type="button"
                label="Cancelar"
                icon="pi pi-times"
                className="p-button-text"
                onClick={() => navigate("/login")}
              />
              <Button
                type="submit"
                label="Cadastrar-se"
                icon="pi pi-user-plus"
                loading={submitting}
              />
            </div>
          </form>
        </Card>
      </div>
    </div>
  );
}

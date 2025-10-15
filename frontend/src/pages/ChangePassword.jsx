import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Password } from "primereact/password";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function ChangePassword() {
  const navigate = useNavigate();
  const { search } = useLocation();

  const getEmailFromQuery = () => {
    const params = new URLSearchParams(search);
    return params.get("email") || "";
  };

  const [email, setEmail] = useState(getEmailFromQuery());
  const [codigo, setCodigo] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmar, setConfirmar] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [errors, setErrors] = useState({
    email: "",
    codigo: "",
    senha: "",
    confirmar: "",
  });

  const strongPasswordMsg =
    "A senha deve ter no mínimo 6 caracteres e conter 1 maiúscula, 1 minúscula, 1 número e 1 caractere especial.";

  const isEmailValid = (v) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
  const isStrongPassword = (v) =>
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{6,}$/.test(v);

  const computeErrors = (next = {}) => {
    const e = {
      email: "",
      codigo: "",
      senha: "",
      confirmar: "",
    };

    const ve = next.email ?? email;
    const vc = next.codigo ?? codigo;
    const vs = next.senha ?? senha;
    const vcf = next.confirmar ?? confirmar;

    if (!ve) e.email = "Informe o e-mail.";
    else if (!isEmailValid(ve)) e.email = "E-mail inválido.";

    if (!vc) e.codigo = "Informe o código.";

    if (!vs) e.senha = "Informe a nova senha.";
    else if (!isStrongPassword(vs)) e.senha = strongPasswordMsg;

    if (!vcf) e.confirmar = "Confirme a nova senha.";
    else if (vs && vcf && vs !== vcf) e.confirmar = "As senhas não coincidem.";

    return e;
  };

  useEffect(() => {
    setErrors(computeErrors({}));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [email, codigo, senha, confirmar]);

  const isFormValid = (e) => !e.email && !e.codigo && !e.senha && !e.confirmar;

  const onSubmit = async (evt) => {
    evt.preventDefault();
    const e = computeErrors({});
    setErrors(e);
    if (!isFormValid(e)) return;

    setSubmitting(true);
    try {
      // TODO: integrar com backend: POST /auth/change-password { email, codigo, senha }
      // await api.post('/auth/change-password', { email, codigo, novaSenha: senha });
      alert("Senha alterada com sucesso.");
      navigate("/login");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="surface-ground min-h-screen flex align-items-center justify-content-center p-3">
      <div className="w-full" style={{ maxWidth: 480 }}>
        <Card title="Alterar Senha" className="shadow-2">
          <form onSubmit={onSubmit} className="p-fluid form-with-left-icons">
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
              <label htmlFor="codigo" className="block mb-2">
                Código
              </label>
              <span className="p-input-icon-left w-full">
                <i className="pi pi-shield" />
                <InputText
                  id="codigo"
                  value={codigo}
                  onChange={(e) => setCodigo(e.target.value)}
                  className={errors.codigo ? "p-invalid w-full" : "w-full"}
                  placeholder="Código recebido por e-mail"
                  autoComplete="one-time-code"
                />
              </span>
              {errors.codigo && (
                <small className="p-error">{errors.codigo}</small>
              )}
            </div>

            <div className="field mb-3">
              <label htmlFor="senha" className="block mb-2">
                Nova Senha
              </label>
              <span className="p-input-icon-left w-full">
                <i className="pi pi-lock" />
                <Password
                  id="senha"
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  feedback={false}
                  toggleMask
                  inputClassName={errors.senha ? "p-invalid w-full" : "w-full"}
                  placeholder="Nova senha"
                  inputProps={{ autoComplete: "new-password" }}
                />
              </span>
              {errors.senha && (
                <small className="p-error">{errors.senha}</small>
              )}
            </div>

            <div className="field mb-3">
              <label htmlFor="confirmar" className="block mb-2">
                Confirmar Senha
              </label>
              <span className="p-input-icon-left w-full">
                <i className="pi pi-lock" />
                <Password
                  id="confirmar"
                  value={confirmar}
                  onChange={(e) => setConfirmar(e.target.value)}
                  feedback={false}
                  toggleMask
                  inputClassName={
                    errors.confirmar ? "p-invalid w-full" : "w-full"
                  }
                  placeholder="Repita a nova senha"
                  inputProps={{ autoComplete: "new-password" }}
                />
              </span>
              {errors.confirmar && (
                <small className="p-error">{errors.confirmar}</small>
              )}
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
                label="Alterar senha"
                icon="pi pi-check"
                loading={submitting}
              />
            </div>
          </form>
        </Card>
      </div>
    </div>
  );
}

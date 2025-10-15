import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function RecoverPassword() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [errors, setErrors] = useState({ email: "" });

  const validar = () => {
    const next = { email: "" };
    if (!email) next.email = "Informe o e-mail.";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email))
      next.email = "E-mail inválido.";
    setErrors(next);
    return !next.email;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!validar()) return;
    setSubmitting(true);
    try {
      // TODO: integrar com backend: POST /auth/recover (ou equivalente)
      // await api.post('/auth/recover', { email });
      alert(
        "Se o e-mail existir, enviaremos instruções para recuperação de senha."
      );
      navigate("/login");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="surface-ground min-h-screen flex align-items-center justify-content-center p-3">
      <div className="w-full" style={{ maxWidth: 480 }}>
        <Card title="Recuperar Senha" className="shadow-2">
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
                label="Recuperar senha"
                icon="pi pi-key"
                loading={submitting}
              />
            </div>
          </form>
        </Card>
      </div>
    </div>
  );
}

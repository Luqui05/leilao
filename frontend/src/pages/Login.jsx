import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Password } from "primereact/password";
import { Button } from "primereact/button";

export default function Login() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const [errors, setErrors] = useState({ email: "", senha: "" });

    const strongPasswordMsg =
        "A senha deve ter no mínimo 6 caracteres e conter pelo menos 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial.";

    const isStrongPassword = (value) => {
        // Pelo menos 6, 1 maiúscula, 1 minúscula, 1 número, 1 especial
        return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{6,}$/.test(
            value
        );
    };

    const validar = () => {
        const next = { email: "", senha: "" };
        if (!email) next.email = "Informe o e-mail.";
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email))
            next.email = "E-mail inválido.";
        if (!senha) next.senha = "Informe a senha.";
        else if (!isStrongPassword(senha)) next.senha = strongPasswordMsg;
        setErrors(next);
        return !next.email && !next.senha;
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        if (!validar()) return;
        setSubmitting(true);
        try {
            // TODO: integrar com backend: POST /auth/login e armazenar token
            // Exemplo futuro:
            // const { data } = await api.post('/auth/login', { email, senha });
            // localStorage.setItem('token', data.token);
            // navigate('/'); // redirecionar conforme fluxo

            // Por enquanto, apenas navega e simula sucesso
            setTimeout(() => navigate("/"), 400);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="surface-ground min-h-screen flex align-items-center justify-content-center p-3">
            <div className="w-full" style={{ maxWidth: 420 }}>
                <Card title="Acesso ao Sistema" className="shadow-2">
                    <form onSubmit={onSubmit} className="p-fluid login-form">
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
                                    className={
                                        errors.email ? "p-invalid w-full" : "w-full"
                                    }
                                    placeholder="seuemail@exemplo.com"
                                    autoComplete="username"
                                />
                            </span>
                            {errors.email && (
                                <small className="p-error">{errors.email}</small>
                            )}
                        </div>

                        <div className="field mb-3">
                            <label htmlFor="senha" className="block mb-2">
                                Senha
                            </label>
                            <span className="p-input-icon-left w-full">
                                <i className="pi pi-lock" />
                                <Password
                                    id="senha"
                                    value={senha}
                                    onChange={(e) => setSenha(e.target.value)}
                                    feedback={false}
                                    toggleMask
                                    inputClassName={
                                        errors.senha ? "p-invalid w-full" : "w-full"
                                    }
                                    placeholder="Sua senha"
                                    inputProps={{
                                        autoComplete: "current-password",
                                    }}
                                />
                            </span>
                            {errors.senha && (
                                <small className="p-error">{errors.senha}</small>
                            )}
                        </div>

                        <Button
                            type="submit"
                            label="Entrar"
                            icon="pi pi-sign-in"
                            className="mb-3"
                            loading={submitting}
                        />

                        <div className="flex flex-column sm:flex-row gap-2 justify-content-between">
                            <Button
                                type="button"
                                label="Cadastrar-se"
                                link
                                onClick={() => navigate("/cadastro")}
                                icon="pi pi-user-plus"
                            />
                            <Button
                                type="button"
                                label="Recuperar senha"
                                link
                                onClick={() => navigate("/recuperar-senha")}
                                icon="pi pi-key"
                            />
                        </div>
                    </form>
                </Card>
            </div>
        </div>
    );
}

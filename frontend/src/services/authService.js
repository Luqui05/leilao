import api from "./api";

const authService = {
  login: async (email, senha) => {
    try {
      const { data } = await api.post("/auth/login", { email, senha });
      // backend returns { token }
      if (data && data.token) {
        localStorage.setItem("token", data.token);
      }
      return data;
    } catch (err) {
      // network error (no response)
      if (!err.response) {
        throw new Error(
          `Network Error: não foi possível conectar ao servidor em ${api.defaults.baseURL}. Verifique se o backend está em execução e se a URL está correta.`
        );
      }
      // normalize error
      if (err.response && err.response.data)
        throw new Error(
          err.response.data.message || JSON.stringify(err.response.data)
        );
      throw new Error(err.message || "Erro desconhecido ao autenticar");
    }
  },

  logout: () => {
    localStorage.removeItem("token");
  },

  register: async (pessoa) => {
    try {
      // pessoa: { nome, email, senha }
      const { data } = await api.post("/api/pessoas", pessoa);
      return data;
    } catch (err) {
      if (!err.response) {
        throw new Error(
          `Network Error: não foi possível conectar ao servidor em ${api.defaults.baseURL}. Verifique se o backend está em execução e se a URL está correta.`
        );
      }
      if (err.response && err.response.data)
        throw new Error(
          err.response.data.message || JSON.stringify(err.response.data)
        );
      throw new Error(err.message || "Erro desconhecido ao cadastrar");
    }
  },

  recover: async (email) => {
    try {
      await api.post("/auth/recover", { email });
    } catch (err) {
      if (!err.response) {
        throw new Error(
          `Network Error: não foi possível conectar ao servidor em ${api.defaults.baseURL}. Verifique se o backend está em execução e se a URL está correta.`
        );
      }
      if (err.response && err.response.data)
        throw new Error(
          err.response.data.message || JSON.stringify(err.response.data)
        );
      throw new Error(err.message || "Erro desconhecido ao recuperar senha");
    }
  },

  changePasswordWithCode: async ({ email, codigo, novaSenha }) => {
    try {
      await api.post("/auth/change-password-with-code", {
        email,
        codigo,
        novaSenha,
      });
    } catch (err) {
      if (!err.response) {
        throw new Error(
          `Network Error: não foi possível conectar ao servidor em ${api.defaults.baseURL}. Verifique se o backend está em execução e se a URL está correta.`
        );
      }
      if (err.response && err.response.data)
        throw new Error(
          err.response.data.message || JSON.stringify(err.response.data)
        );
      throw new Error(err.message || "Erro desconhecido ao alterar senha");
    }
  },

  isAdmin: () => {
    const token = localStorage.getItem("token");
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      console.log("Token payload:", payload); // DEBUG
      return (
        payload.roles &&
        Array.isArray(payload.roles) &&
        payload.roles.includes("ROLE_ADMIN")
      );
    } catch (e) {
      console.error("Erro ao decodificar token:", e);
      return false;
    }
  },

  getCurrentUser: async () => {
    try {
      const { data } = await api.get("/auth/me");
      return data;
    } catch (err) {
      return null;
    }
  },
};

export default authService;

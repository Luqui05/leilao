import api from "./api";

const pessoaPerfilService = {
  list: async () => {
    try {
      const { data } = await api.get("/api/pessoas-perfis");
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao listar permissões"
      );
    }
  },

  getById: async (id) => {
    try {
      const { data } = await api.get(`/api/pessoas-perfis/${id}`);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao buscar permissão"
      );
    }
  },

  create: async (payload) => {
    try {
      const { data } = await api.post("/api/pessoas-perfis", payload);
      return data;
    } catch (err) {
      if (err.response?.status === 400) {
        const errorMsg = err.response?.data?.message || err.response?.data;
        if (
          typeof errorMsg === "string" &&
          errorMsg.includes("já possui este perfil")
        ) {
          throw new Error("Este usuário já possui este perfil atribuído.");
        }
        throw new Error(errorMsg || "Dados inválidos.");
      }
      if (err.response?.status === 403) {
        throw new Error(
          "Você não tem permissão para criar esta atribuição de perfil."
        );
      }
      if (err.response?.status === 404) {
        throw new Error("Usuário ou perfil não encontrado.");
      }
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao criar permissão"
      );
    }
  },

  update: async (id, payload) => {
    try {
      const { data } = await api.patch(`/api/pessoas-perfis/${id}`, payload);
      return data;
    } catch (err) {
      if (err.response?.status === 403) {
        throw new Error(
          "Você não tem permissão para editar esta atribuição de perfil."
        );
      }
      if (err.response?.status === 404) {
        throw new Error("Permissão não encontrada.");
      }
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao atualizar permissão"
      );
    }
  },

  remove: async (id) => {
    try {
      await api.delete(`/api/pessoas-perfis/${id}`);
    } catch (err) {
      if (err.response?.status === 403) {
        throw new Error(
          "Você não tem permissão para remover esta atribuição de perfil."
        );
      }
      if (err.response?.status === 404) {
        throw new Error("Permissão não encontrada.");
      }
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao remover permissão"
      );
    }
  },
};

export default pessoaPerfilService;

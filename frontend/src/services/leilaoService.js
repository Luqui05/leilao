import api from "./api";

const leilaoService = {
  listPaginated: async (params = {}) => {
    try {
      const response = await api.get("/api/leiloes", { params });
      return response.data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao listar leilões"
      );
    }
  },

  list: async () => {
    try {
      const { data } = await api.get("/api/leiloes/todos");
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao listar leilões"
      );
    }
  },

  getById: async (id) => {
    try {
      const { data } = await api.get(`/api/leiloes/${id}`);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao buscar leilão"
      );
    }
  },

  create: async (payload) => {
    try {
      const { data } = await api.post("/api/leiloes", payload);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao criar leilão"
      );
    }
  },

  update: async (id, payload) => {
    try {
      const { data } = await api.patch(`/api/leiloes/${id}`, payload);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao atualizar categoria"
      );
    }
  },

  remove: async (id) => {
    try {
      await api.delete(`/api/leiloes/${id}`);
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao remover categoria"
      );
    }
  },
};

export default leilaoService;

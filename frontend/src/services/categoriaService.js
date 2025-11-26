import api from "./api";

const categoriaService = {
  listPaginated: async (params = {}) => {
    const response = await api.get("/api/categorias", { params });
    return response.data;
  },

  list: async () => {
    try {
      const { data } = await api.get("/api/categorias/todas");
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao listar categorias"
      );
    }
  },

  getById: async (id) => {
    try {
      const { data } = await api.get(`/api/categorias/${id}`);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao buscar categoria"
      );
    }
  },

  create: async (payload) => {
    try {
      const { data } = await api.post("/api/categorias", payload);
      return data;
    } catch (err) {
      throw new Error(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Erro ao criar categoria"
      );
    }
  },

  update: async (id, payload) => {
    try {
      const { data } = await api.patch(`/api/categorias/${id}`, payload);
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
      await api.delete(`/api/categorias/${id}`);
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

export default categoriaService;

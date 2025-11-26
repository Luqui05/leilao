import api from "./api";

const pessoaService = {
  listPaginated: async (params = {}) => {
    const response = await api.get("/api/pessoas", { params });
    return response.data;
  },

  list: async () => {
    // Busca todas sem paginação (para dropdowns)
    const response = await api.get("/api/pessoas", {
      params: { size: 1000 }, // pega muitas pra não paginar
    });
    return response.data.content || [];
  },

  getById: async (id) => {
    const response = await api.get(`/api/pessoas/${id}`);
    return response.data;
  },

  update: async (id, data) => {
    const response = await api.patch(`/api/pessoas/${id}`, data);
    return response.data;
  },

  remove: async (id) => {
    await api.delete(`/api/pessoas/${id}`);
  },
};

export default pessoaService;

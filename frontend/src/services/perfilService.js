import api from "./api";

const perfilService = {
  list: async () => {
    const { data } = await api.get("/api/perfis");
    return data;
  },

  getById: async (id) => {
    const { data } = await api.get(`/api/perfis/${id}`);
    return data;
  },

  create: async (payload) => {
    const { data } = await api.post("/api/perfis", payload);
    return data;
  },

  update: async (id, payload) => {
    const { data } = await api.patch(`/api/perfis/${id}`, payload);
    return data;
  },

  remove: async (id) => {
    await api.delete(`/api/perfis/${id}`);
  },
};

export default perfilService;

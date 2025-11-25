import api from "./api";

const pessoaPerfilService = {
  list: async () => {
    const { data } = await api.get("/api/pessoas-perfis");
    return data;
  },

  getById: async (id) => {
    const { data } = await api.get(`/api/pessoas-perfis/${id}`);
    return data;
  },

  create: async (payload) => {
    const { data } = await api.post("/api/pessoas-perfis", payload);
    return data;
  },

  update: async (id, payload) => {
    const { data } = await api.patch(`/api/pessoas-perfis/${id}`, payload);
    return data;
  },

  remove: async (id) => {
    await api.delete(`/api/pessoas-perfis/${id}`);
  },
};

export default pessoaPerfilService;

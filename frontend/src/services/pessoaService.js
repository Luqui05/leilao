import api from "./api";

const pessoaService = {
  list: async () => {
    const { data } = await api.get("/api/pessoas");
    return data;
  },

  getById: async (id) => {
    const { data } = await api.get(`/api/pessoas/${id}`);
    return data;
  },
};

export default pessoaService;

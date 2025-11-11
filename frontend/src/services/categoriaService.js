import api from './api';

const categoriaService = {
  list: async () => {
    const { data } = await api.get('/api/categorias');
    return data;
  },
  create: async (payload) => {
    const { data } = await api.post('/api/categorias', payload);
    return data;
  },
  remove: async (id) => {
    await api.delete(`/api/categorias/${id}`);
  },
};

export default categoriaService;

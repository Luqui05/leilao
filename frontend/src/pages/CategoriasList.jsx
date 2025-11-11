import { useEffect, useState } from 'react';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import categoriaService from '../services/categoriaService';

export default function CategoriasList() {
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [nome, setNome] = useState('');
  const [observacao, setObservacao] = useState('');

  const carregar = async () => {
    setLoading(true);
    try {
      const data = await categoriaService.list();
      setItens(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { carregar(); }, []);

  const criar = async () => {
    if (!nome?.trim()) return alert('Informe o nome da categoria.');
    try {
      await categoriaService.create({ nome, observacao });
      setNome('');
      setObservacao('');
      await carregar();
    } catch (err) {
      alert(err?.message || 'Falha ao criar categoria.');
    }
  };

  const excluir = async (id) => {
    if (!window.confirm('Excluir categoria?')) return;
    try {
      await categoriaService.remove(id);
      await carregar();
    } catch (err) {
      alert(err?.message || 'Falha ao excluir categoria.');
    }
  };

  return (
    <div className="p-3">
      <Card title="Categorias">
        <div className="grid gap-2 mb-3">
          <div className="col-12 md:col-4">
            <label className="block mb-2">Nome</label>
            <InputText value={nome} onChange={(e) => setNome(e.target.value)} className="w-full" />
          </div>
          <div className="col-12 md:col-6">
            <label className="block mb-2">Observação</label>
            <InputText value={observacao} onChange={(e) => setObservacao(e.target.value)} className="w-full" />
          </div>
          <div className="col-12 md:col-2 flex align-items-end">
            <Button label="Adicionar" icon="pi pi-plus" onClick={criar} />
          </div>
        </div>

        {loading ? (
          <p>Carregando...</p>
        ) : itens.length === 0 ? (
          <p>Nenhuma categoria.</p>
        ) : (
          <ul className="list-none p-0 m-0">
            {itens.map((c) => (
              <li key={c.id} className="flex justify-content-between align-items-center py-2 border-bottom-1 surface-border">
                <div>
                  <strong>{c.nome}</strong>
                  {c.observacao ? <span className="ml-2">— {c.observacao}</span> : null}
                </div>
                <Button icon="pi pi-trash" className="p-button-text p-button-danger" onClick={() => excluir(c.id)} />
              </li>
            ))}
          </ul>
        )}
      </Card>
    </div>
  );
}

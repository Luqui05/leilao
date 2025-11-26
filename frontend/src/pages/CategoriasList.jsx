import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import categoriaService from "../services/categoriaService";
import authService from "../services/authService";

export default function CategoriasList() {
  const navigate = useNavigate();
  const [itens, setItens] = useState([]);
  const [itensFiltrados, setItensFiltrados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  const [showDialog, setShowDialog] = useState(false);
  const [showDetailDialog, setShowDetailDialog] = useState(false);
  const [editando, setEditando] = useState(null);
  const [detalhe, setDetalhe] = useState(null);
  const [nome, setNome] = useState("");
  const [observacao, setObservacao] = useState("");
  const [errors, setErrors] = useState({ nome: "", observacao: "" });

  const [filtroGlobal, setFiltroGlobal] = useState("");

  const carregar = async () => {
    setLoading(true);
    try {
      const data = await categoriaService.list();
      setItens(data || []);
      setItensFiltrados(data || []);
    } catch (err) {
      alert(err?.message || "Falha ao carregar categorias.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregar();
  }, []);

  useEffect(() => {
    if (!filtroGlobal.trim()) {
      setItensFiltrados(itens);
    } else {
      const termo = filtroGlobal.toLowerCase();
      const filtrados = itens.filter(
        (item) =>
          item.nome?.toLowerCase().includes(termo) ||
          item.observacao?.toLowerCase().includes(termo)
      );
      setItensFiltrados(filtrados);
    }
  }, [filtroGlobal, itens]);

  const abrirDialogNovo = () => {
    setEditando(null);
    setNome("");
    setObservacao("");
    setErrors({ nome: "", observacao: "" });
    setShowDialog(true);
  };

  const abrirDialogEditar = (item) => {
    setEditando(item);
    setNome(item.nome || "");
    setObservacao(item.observacao || "");
    setErrors({ nome: "", observacao: "" });
    setShowDialog(true);
  };

  const abrirDialogDetalhe = async (item) => {
    try {
      const data = await categoriaService.getById(item.id);
      setDetalhe(data);
      setShowDetailDialog(true);
    } catch (err) {
      alert(err?.message || "Falha ao carregar detalhes.");
    }
  };

  const validar = () => {
    const next = { nome: "", observacao: "" };
    if (!nome?.trim()) next.nome = "Informe o nome da categoria.";
    setErrors(next);
    return !next.nome;
  };

  const salvar = async () => {
    if (!validar()) return;

    setSubmitting(true);
    try {
      const payload = {
        nome: nome.trim(),
        observacao: observacao?.trim() || null,
      };

      if (editando) {
        await categoriaService.update(editando.id, payload);
      } else {
        await categoriaService.create(payload);
      }

      setShowDialog(false);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao salvar categoria.");
    } finally {
      setSubmitting(false);
    }
  };

  const excluir = async (id) => {
    if (!window.confirm("Excluir esta categoria?")) return;

    try {
      await categoriaService.remove(id);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao excluir categoria.");
    }
  };

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  const acoesTemplate = (rowData) => {
    return (
      <div className="flex gap-2">
        <Button
          icon="pi pi-eye"
          className="p-button-rounded p-button-text p-button-info"
          onClick={() => abrirDialogDetalhe(rowData)}
          tooltip="Visualizar"
        />
        <Button
          icon="pi pi-pencil"
          className="p-button-rounded p-button-text p-button-warning"
          onClick={() => abrirDialogEditar(rowData)}
          tooltip="Editar"
        />
        <Button
          icon="pi pi-trash"
          className="p-button-rounded p-button-text p-button-danger"
          onClick={() => excluir(rowData.id)}
          tooltip="Remover"
        />
      </div>
    );
  };

  const observacaoTemplate = (rowData) => {
    return (
      <span className="text-color-secondary">
        {rowData.observacao || "—"}
      </span>
    );
  };

  const header = (
    <div className="flex flex-column md:flex-row md:justify-content-between gap-2">
      <h3 className="m-0">Lista de Categorias</h3>
      <span className="p-input-icon-left">
        <i className="pi pi-search" />
        <InputText
          value={filtroGlobal}
          onChange={(e) => setFiltroGlobal(e.target.value)}
          placeholder="Buscar categoria..."
          className="w-full md:w-20rem"
        />
      </span>
    </div>
  );

  const dialogFooter = (
    <div>
      <Button
        label="Cancelar"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setShowDialog(false)}
        disabled={submitting}
      />
      <Button
        label="Salvar"
        icon="pi pi-check"
        onClick={salvar}
        loading={submitting}
      />
    </div>
  );

  const detailFooter = (
    <Button
      label="Fechar"
      icon="pi pi-times"
      onClick={() => setShowDetailDialog(false)}
    />
  );

  return (
    <div className="p-4">
      <Card title="Gerenciar Categorias">
        <div className="flex justify-content-between mb-3">
          <div className="flex gap-2">
            <Button
              label="Nova Categoria"
              icon="pi pi-plus"
              onClick={abrirDialogNovo}
            />
            <Button
              label="Voltar"
              icon="pi pi-arrow-left"
              className="p-button-secondary"
              onClick={() => navigate("/home")}
            />
          </div>
          <Button
            label="Sair"
            icon="pi pi-sign-out"
            className="p-button-danger"
            onClick={logout}
          />
        </div>

        <DataTable
          value={itensFiltrados}
          loading={loading}
          header={header}
          emptyMessage="Nenhuma categoria encontrada."
          paginator
          rows={10}
          rowsPerPageOptions={[5, 10, 25, 50]}
          stripedRows
          sortMode="multiple"
          removableSort
        >
          <Column
            field="id"
            header="ID"
            sortable
            style={{ width: "80px" }}
          />
          <Column
            field="nome"
            header="Nome"
            sortable
          />
          <Column
            field="observacao"
            header="Observação"
            body={observacaoTemplate}
            sortable
          />
          <Column
            header="Ações"
            body={acoesTemplate}
            style={{ width: "180px" }}
          />
        </DataTable>
      </Card>

      <Dialog
        header={editando ? "Editar Categoria" : "Nova Categoria"}
        visible={showDialog}
        style={{ width: "500px" }}
        onHide={() => setShowDialog(false)}
        footer={dialogFooter}
      >
        <div className="p-fluid">
          <div className="field mb-3">
            <label htmlFor="nome" className="block mb-2">
              Nome *
            </label>
            <InputText
              id="nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className={errors.nome ? "p-invalid w-full" : "w-full"}
              placeholder="Nome da categoria"
              autoFocus
            />
            {errors.nome && <small className="p-error">{errors.nome}</small>}
          </div>

          <div className="field mb-3">
            <label htmlFor="observacao" className="block mb-2">
              Observação
            </label>
            <InputTextarea
              id="observacao"
              value={observacao}
              onChange={(e) => setObservacao(e.target.value)}
              rows={4}
              className="w-full"
              placeholder="Descrição ou observação sobre a categoria (opcional)"
            />
          </div>
        </div>
      </Dialog>

      <Dialog
        header="Detalhes da Categoria"
        visible={showDetailDialog}
        style={{ width: "500px" }}
        onHide={() => setShowDetailDialog(false)}
        footer={detailFooter}
      >
        {detalhe && (
          <div className="p-fluid">
            <div className="field mb-3">
              <label className="block mb-2 font-semibold">ID</label>
              <p className="m-0">{detalhe.id}</p>
            </div>

            <div className="field mb-3">
              <label className="block mb-2 font-semibold">Nome</label>
              <p className="m-0">{detalhe.nome}</p>
            </div>

            <div className="field mb-3">
              <label className="block mb-2 font-semibold">Observação</label>
              <p className="m-0 text-color-secondary">
                {detalhe.observacao || "Nenhuma observação"}
              </p>
            </div>
          </div>
        )}
      </Dialog>
    </div>
  );
}

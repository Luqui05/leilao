import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { Tag } from "primereact/tag";
import { Checkbox } from "primereact/checkbox";
import { Message } from "primereact/message";
import pessoaService from "../services/pessoaService";
import authService from "../services/authService";

export default function PessoasList() {
  const navigate = useNavigate();
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [totalRecords, setTotalRecords] = useState(0);

  const [lazyParams, setLazyParams] = useState({
    first: 0,
    rows: 10,
    page: 0,
    sortField: "id",
    sortOrder: 1,
  });

  const [filtroTermo, setFiltroTermo] = useState("");
  const [showDialog, setShowDialog] = useState(false);
  const [showDetailDialog, setShowDetailDialog] = useState(false);
  const [editando, setEditando] = useState(null);
  const [detalhe, setDetalhe] = useState(null);

  // Form
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [ativo, setAtivo] = useState(true);
  const [errors, setErrors] = useState({});

  // Validação em tempo real
  useEffect(() => {
    const newErrors = {};
    if (nome && nome.trim().length < 3) {
      newErrors.nome = "O nome deve ter no mínimo 3 caracteres";
    }
    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      newErrors.email = "E-mail inválido";
    }
    setErrors(newErrors);
  }, [nome, email]);

  const carregar = async () => {
    setLoading(true);
    try {
      const params = {
        page: lazyParams.page,
        size: lazyParams.rows,
        sort: `${lazyParams.sortField},${lazyParams.sortOrder === 1 ? "asc" : "desc"}`,
        termo: filtroTermo || undefined,
      };

      const data = await pessoaService.listPaginated(params);
      setItens(data.content || []);
      setTotalRecords(data.totalElements || 0);
    } catch (err) {
      alert(err?.message || "Falha ao carregar pessoas.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregar();
  }, [lazyParams, filtroTermo]);

  const onPage = (event) => {
    setLazyParams({
      ...lazyParams,
      first: event.first,
      rows: event.rows,
      page: event.page,
    });
  };

  const onSort = (event) => {
    setLazyParams({
      ...lazyParams,
      sortField: event.sortField || "id",
      sortOrder: event.sortOrder || 1,
    });
  };

  const abrirDialogEditar = (item) => {
    setEditando(item);
    setNome(item.nome || "");
    setEmail(item.email || "");
    setAtivo(item.ativo ?? true);
    setErrors({});
    setShowDialog(true);
  };

  const abrirDialogDetalhe = async (item) => {
    try {
      const data = await pessoaService.getById(item.id);
      setDetalhe(data);
      setShowDetailDialog(true);
    } catch (err) {
      alert(err?.message || "Falha ao carregar detalhes.");
    }
  };

  const validar = () => {
    const newErrors = {};

    if (!nome?.trim()) {
      newErrors.nome = "Informe o nome.";
    } else if (nome.trim().length < 3) {
      newErrors.nome = "O nome deve ter no mínimo 3 caracteres";
    }

    if (!email?.trim()) {
      newErrors.email = "Informe o e-mail.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      newErrors.email = "E-mail inválido";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const salvar = async () => {
    if (!validar()) return;

    setSubmitting(true);
    try {
      const payload = {
        nome: nome.trim(),
        email: email.trim(),
        ativo: ativo,
      };

      await pessoaService.update(editando.id, payload);
      setShowDialog(false);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao salvar pessoa.");
    } finally {
      setSubmitting(false);
    }
  };

  const excluir = async (id) => {
    if (!window.confirm("Excluir esta pessoa?")) return;

    try {
      await pessoaService.remove(id);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao excluir pessoa.");
    }
  };

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  const ativoTemplate = (rowData) => {
    return rowData.ativo ? (
      <Tag value="Ativo" severity="success" />
    ) : (
      <Tag value="Inativo" severity="danger" />
    );
  };

  const perfisTemplate = (rowData) => {
    return rowData.perfis?.map((p) => p.tipo).join(", ") || "—";
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

  const header = (
    <div className="flex flex-column md:flex-row md:justify-content-between gap-2">
      <h3 className="m-0">Lista de Pessoas</h3>
      <span className="p-input-icon-left">
        <i className="pi pi-search" />
        <InputText
          value={filtroTermo}
          onChange={(e) => setFiltroTermo(e.target.value)}
          placeholder="Buscar por nome ou e-mail..."
          className="w-full md:w-25rem"
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
        disabled={Object.keys(errors).length > 0}
      />
    </div>
  );

  const detailFooter = (
    <Button label="Fechar" icon="pi pi-times" onClick={() => setShowDetailDialog(false)} />
  );

  const emptyMessage = () => (
    <div className="text-center p-4">
      <i className="pi pi-inbox" style={{ fontSize: "3rem", color: "#ccc" }}></i>
      <p className="text-color-secondary mt-3">
        {filtroTermo ? "Nenhuma pessoa encontrada para este filtro." : "Nenhuma pessoa cadastrada."}
      </p>
    </div>
  );

  return (
    <div className="p-4">
      <Card title="Gerenciar Pessoas">
        <div className="flex justify-content-between mb-3">
          <div className="flex gap-2">
            <Button
              label="Voltar"
              icon="pi pi-arrow-left"
              className="p-button-secondary"
              onClick={() => navigate("/home")}
            />
          </div>
          <Button label="Sair" icon="pi pi-sign-out" className="p-button-danger" onClick={logout} />
        </div>

        <DataTable
          value={itens}
          loading={loading}
          header={header}
          emptyMessage={emptyMessage()}
          lazy
          paginator
          first={lazyParams.first}
          rows={lazyParams.rows}
          totalRecords={totalRecords}
          onPage={onPage}
          onSort={onSort}
          sortField={lazyParams.sortField}
          sortOrder={lazyParams.sortOrder}
          rowsPerPageOptions={[5, 10, 25, 50]}
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} pessoas"
          stripedRows
        >
          <Column field="id" header="ID" sortable style={{ width: "80px" }} />
          <Column field="nome" header="Nome" sortable />
          <Column field="email" header="E-mail" sortable />
          <Column field="ativo" header="Status" body={ativoTemplate} sortable style={{ width: "120px" }} />
          <Column field="perfis" header="Perfis" body={perfisTemplate} style={{ width: "180px" }} />
          <Column header="Ações" body={acoesTemplate} style={{ width: "180px" }} />
        </DataTable>
      </Card>

      <Dialog
        header="Editar Pessoa"
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
              placeholder="Nome completo"
            />
            {errors.nome && <Message severity="error" text={errors.nome} className="mt-2" />}
          </div>

          <div className="field mb-3">
            <label htmlFor="email" className="block mb-2">
              E-mail *
            </label>
            <InputText
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className={errors.email ? "p-invalid w-full" : "w-full"}
              placeholder="email@exemplo.com"
            />
            {errors.email && <Message severity="error" text={errors.email} className="mt-2" />}
          </div>

          <div className="field mb-3">
            <div className="flex align-items-center">
              <Checkbox inputId="ativo" checked={ativo} onChange={(e) => setAtivo(e.checked)} />
              <label htmlFor="ativo" className="ml-2">
                Ativo
              </label>
            </div>
          </div>
        </div>
      </Dialog>

      <Dialog
        header="Detalhes da Pessoa"
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
              <label className="block mb-2 font-semibold">E-mail</label>
              <p className="m-0">{detalhe.email}</p>
            </div>

            <div className="field mb-3">
              <label className="block mb-2 font-semibold">Status</label>
              {ativoTemplate(detalhe)}
            </div>

            <div className="field mb-3">
              <label className="block mb-2 font-semibold">Perfis</label>
              <div className="flex gap-2">
                {detalhe.perfis?.map((p) => (
                  <Tag key={p.id} value={p.tipo} />
                ))}
              </div>
            </div>
          </div>
        )}
      </Dialog>
    </div>
  );
}
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import { Dropdown } from "primereact/dropdown";
import { Calendar } from "primereact/calendar";
import { InputNumber } from "primereact/inputnumber";
import { Message } from "primereact/message";
import { Tag } from "primereact/tag";
import leilaoService from "../services/leilaoService";
import categoriaService from "../services/categoriaService";
import authService from "../services/authService";

export default function LeiloesList() {
  const navigate = useNavigate();
  const [itens, setItens] = useState([]);
  const [categorias, setCategorias] = useState([]);
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
  const [filtroStatus, setFiltroStatus] = useState(null);
  const [filtroCategoria, setFiltroCategoria] = useState(null);
  const [filtroDataInicio, setFiltroDataInicio] = useState(null);
  const [filtroDataFim, setFiltroDataFim] = useState(null);

  const [showDialog, setShowDialog] = useState(false);
  const [showDetailDialog, setShowDetailDialog] = useState(false);
  const [editando, setEditando] = useState(null);
  const [detalhe, setDetalhe] = useState(null);

  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [descricaoDetalhada, setDescricaoDetalhada] = useState("");
  const [dataHoraInicio, setDataHoraInicio] = useState(null);
  const [dataHoraFim, setDataHoraFim] = useState(null);
  const [status, setStatus] = useState("ABERTO");
  const [observacao, setObservacao] = useState("");
  const [valorIncremento, setValorIncremento] = useState(1.0);
  const [lanceMinimo, setLanceMinimo] = useState(1.0);
  const [categoriaId, setCategoriaId] = useState(null);

  const [errors, setErrors] = useState({});

  const statusOptions = [
    { label: "Todos", value: null },
    { label: "Aberto", value: "ABERTO" },
    { label: "Encerrado", value: "ENCERRADO" },
    { label: "Cancelado", value: "CANCELADO" },
    { label: "Em análise", value: "EM_ANALISE" },
  ];

  const statusOptionsForm = [
    { label: "Aberto", value: "ABERTO" },
    { label: "Encerrado", value: "ENCERRADO" },
    { label: "Cancelado", value: "CANCELADO" },
    { label: "Em análise", value: "EM_ANALISE" },
  ];

  useEffect(() => {
    const newErrors = {};
    if (titulo && titulo.trim().length < 5) {
      newErrors.titulo = "O título deve ter no mínimo 5 caracteres";
    }
    if (descricao && descricao.trim().length < 10) {
      newErrors.descricao = "A descrição deve ter no mínimo 10 caracteres";
    }
    if (valorIncremento && valorIncremento <= 0) {
      newErrors.valorIncremento = "O valor de incremento deve ser positivo";
    }
    if (lanceMinimo && lanceMinimo <= 0) {
      newErrors.lanceMinimo = "O lance mínimo deve ser positivo";
    }
    if (dataHoraInicio && dataHoraFim && dataHoraInicio >= dataHoraFim) {
      newErrors.dataHoraFim = "A data fim deve ser posterior à data início";
    }
    setErrors(newErrors);
  }, [
    titulo,
    descricao,
    valorIncremento,
    lanceMinimo,
    dataHoraInicio,
    dataHoraFim,
  ]);

  useEffect(() => {
    const carregarCategorias = async () => {
      try {
        const data = await categoriaService.list();
        setCategorias(data || []);
      } catch (err) {
        console.error("Erro ao carregar categorias:", err);
      }
    };
    carregarCategorias();
  }, []);

  const carregar = async () => {
    setLoading(true);
    try {
      const params = {
        page: lazyParams.page,
        size: lazyParams.rows,
        sort: `${lazyParams.sortField},${
          lazyParams.sortOrder === 1 ? "asc" : "desc"
        }`,
        termo: filtroTermo || undefined,
        status: filtroStatus || undefined,
        categoriaId: filtroCategoria || undefined,
        dataInicio: filtroDataInicio
          ? filtroDataInicio.toISOString()
          : undefined,
        dataFim: filtroDataFim ? filtroDataFim.toISOString() : undefined,
      };

      const data = await leilaoService.listPaginated(params);
      setItens(data.content || []);
      setTotalRecords(data.totalElements || 0);
    } catch (err) {
      alert(err?.message || "Falha ao carregar leilões.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregar();
  }, [
    lazyParams,
    filtroTermo,
    filtroStatus,
    filtroCategoria,
    filtroDataInicio,
    filtroDataFim,
  ]);

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

  const limparFiltros = () => {
    setFiltroTermo("");
    setFiltroStatus(null);
    setFiltroCategoria(null);
    setFiltroDataInicio(null);
    setFiltroDataFim(null);
  };

  const abrirDialogNovo = () => {
    setEditando(null);
    setTitulo("");
    setDescricao("");
    setDescricaoDetalhada("");
    setDataHoraInicio(null);
    setDataHoraFim(null);
    setStatus("ABERTO");
    setObservacao("");
    setValorIncremento(1.0);
    setLanceMinimo(1.0);
    setCategoriaId(null);
    setErrors({});
    setShowDialog(true);
  };

  const abrirDialogEditar = (item) => {
    setEditando(item);
    setTitulo(item.titulo || "");
    setDescricao(item.descricao || "");
    setDescricaoDetalhada(item.descricaoDetalhada || "");
    setDataHoraInicio(
      item.dataHoraInicio ? new Date(item.dataHoraInicio) : null
    );
    setDataHoraFim(item.dataHoraFim ? new Date(item.dataHoraFim) : null);
    setStatus(item.status || "ABERTO");
    setObservacao(item.observacao || "");
    setValorIncremento(item.valorIncremento || 1.0);
    setLanceMinimo(item.lanceMinimo || 1.0);
    setCategoriaId(item.categoria?.id || null);
    setErrors({});
    setShowDialog(true);
  };

  const abrirDialogDetalhe = async (item) => {
    try {
      const data = await leilaoService.getById(item.id);
      setDetalhe(data);
      setShowDetailDialog(true);
    } catch (err) {
      alert(err?.message || "Falha ao carregar detalhes.");
    }
  };

  // validação do formulário de criação do leilão
  const validar = () => {
    const newErrors = {};

    if (!titulo?.trim()) {
      newErrors.titulo = "Informe o título do leilão.";
    } else if (titulo.trim().length < 5) {
      newErrors.titulo = "O título deve ter no mínimo 5 caracteres";
    }

    if (!descricao?.trim()) {
      newErrors.descricao = "Informe a descrição do leilão.";
    } else if (descricao.trim().length < 10) {
      newErrors.descricao = "A descrição deve ter no mínimo 10 caracteres";
    }

    if (!dataHoraInicio) {
      newErrors.dataHoraInicio = "Informe a data/hora de início.";
    }

    if (!dataHoraFim) {
      newErrors.dataHoraFim = "Informe a data/hora de término.";
    } else if (dataHoraInicio && dataHoraInicio >= dataHoraFim) {
      newErrors.dataHoraFim = "A data fim deve ser posterior à data início";
    }

    if (!categoriaId) {
      newErrors.categoriaId = "Selecione uma categoria.";
    }

    if (!valorIncremento || valorIncremento <= 0) {
      newErrors.valorIncremento = "O valor de incremento deve ser positivo";
    }

    if (!lanceMinimo || lanceMinimo <= 0) {
      newErrors.lanceMinimo = "O lance mínimo deve ser positivo";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const salvar = async () => {
    if (!validar()) return;

    setSubmitting(true);
    try {
      const usuarioAtual = authService.getCurrentUser();

      const payload = {
        titulo: titulo.trim(),
        descricao: descricao.trim(),
        descricaoDetalhada: descricaoDetalhada?.trim() || null,
        dataHoraInicio: formatDateTimeForBackend(dataHoraInicio),
        dataHoraFim: formatDateTimeForBackend(dataHoraFim),
        status: status,
        observacao: observacao?.trim() || null,
        valorIncremento: valorIncremento,
        lanceMinimo: lanceMinimo,
        categoriaId: categoriaId,
        // publicadorId -> definido automaticamente pelo backend
      };

      if (editando) {
        await leilaoService.update(editando.id, payload);
      } else {
        await leilaoService.create(payload);
      }

      setShowDialog(false);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao salvar leilão.");
    } finally {
      setSubmitting(false);
    }
  };

  const formatDateTimeForBackend = (date) => {
    if (!date) return null;

    // Formata como YYYY-MM-DDTHH:mm:ss sem conversão de timezone
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  };

  const excluir = async (id) => {
    if (!window.confirm("Excluir este leilão?")) return;

    try {
      await leilaoService.remove(id);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao excluir leilão.");
    }
  };

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  const statusTemplate = (rowData) => {
    const severity =
      {
        ABERTO: "success",
        ENCERRADO: "danger",
        CANCELADO: "warning",
        EM_ANALISE: "info",
      }[rowData.status] || "secondary";

    return <Tag value={rowData.status} severity={severity} />;
  };

  const categoriaTemplate = (rowData) => {
    return rowData.categoria?.nome || "—";
  };

  const valorTemplate = (rowData, field) => {
    const valor = rowData[field];
    return valor ? `R$ ${valor.toFixed(2)}` : "—";
  };

  const dataTemplate = (rowData, field) => {
    const data = rowData[field];
    if (!data) return "—";
    return new Date(data).toLocaleString("pt-BR");
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
    <div className="flex flex-column gap-3">
      <div className="flex flex-column md:flex-row md:justify-content-between gap-2">
        <h3 className="m-0">Lista de Leilões</h3>
        <span className="p-input-icon-left">
          <InputText
            value={filtroTermo}
            onChange={(e) => setFiltroTermo(e.target.value)}
            placeholder="Buscar por título ou descrição..."
            className="w-full md:w-25rem"
          />
        </span>
      </div>

      <div className="grid">
        <div className="col-12 md:col-3">
          <Dropdown
            value={filtroStatus}
            options={statusOptions}
            onChange={(e) => setFiltroStatus(e.value)}
            placeholder="Filtrar por Status"
            className="w-full"
            showClear
          />
        </div>

        <div className="col-12 md:col-3">
          <Dropdown
            value={filtroCategoria}
            options={categorias.map((c) => ({ label: c.nome, value: c.id }))}
            onChange={(e) => setFiltroCategoria(e.value)}
            placeholder="Filtrar por Categoria"
            className="w-full"
            showClear
            filter
          />
        </div>

        <div className="col-12 md:col-2">
          <Calendar
            value={filtroDataInicio}
            onChange={(e) => setFiltroDataInicio(e.value)}
            placeholder="Data Início"
            showIcon
            showTime
            hourFormat="24"
            className="w-full"
          />
        </div>

        <div className="col-12 md:col-2">
          <Calendar
            value={filtroDataFim}
            onChange={(e) => setFiltroDataFim(e.value)}
            placeholder="Data Fim"
            showIcon
            showTime
            hourFormat="24"
            className="w-full"
          />
        </div>

        <div className="col-12 md:col-2">
          <Button
            label="Limpar Filtros"
            icon="pi pi-filter-slash"
            className="p-button-outlined w-full"
            onClick={limparFiltros}
          />
        </div>
      </div>
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
    <Button
      label="Fechar"
      icon="pi pi-times"
      onClick={() => setShowDetailDialog(false)}
    />
  );

  const emptyMessage = () => (
    <div className="text-center p-4">
      <i
        className="pi pi-inbox"
        style={{ fontSize: "3rem", color: "#ccc" }}
      ></i>
      <p className="text-color-secondary mt-3">
        {filtroTermo ||
        filtroStatus ||
        filtroCategoria ||
        filtroDataInicio ||
        filtroDataFim
          ? "Nenhum leilão encontrado para estes filtros."
          : "Nenhum leilão cadastrado."}
      </p>
    </div>
  );

  return (
    <div className="p-4">
      <Card title="Gerenciar Leilões">
        <div className="flex justify-content-between mb-3">
          <div className="flex gap-2">
            <Button
              label="Novo Leilão"
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
          currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} leilões"
          stripedRows
        >
          <Column field="id" header="ID" sortable style={{ width: "80px" }} />
          <Column field="titulo" header="Título" sortable />
          <Column
            field="status"
            header="Status"
            body={statusTemplate}
            sortable
            style={{ width: "120px" }}
          />
          <Column
            field="categoria"
            header="Categoria"
            body={categoriaTemplate}
            sortable
          />
          <Column
            field="lanceMinimo"
            header="Lance Mínimo"
            body={(row) => valorTemplate(row, "lanceMinimo")}
            sortable
            style={{ width: "140px" }}
          />
          <Column
            field="dataHoraInicio"
            header="Início"
            body={(row) => dataTemplate(row, "dataHoraInicio")}
            sortable
            style={{ width: "180px" }}
          />
          <Column
            header="Ações"
            body={acoesTemplate}
            style={{ width: "180px" }}
          />
        </DataTable>
      </Card>

      <Dialog
        header={editando ? "Editar Leilão" : "Novo Leilão"}
        visible={showDialog}
        style={{ width: "700px" }}
        onHide={() => setShowDialog(false)}
        footer={dialogFooter}
        maximizable
      >
        <div className="p-fluid grid">
          <div className="field col-12">
            <label htmlFor="titulo" className="block mb-2">
              Título *
            </label>
            <InputText
              id="titulo"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              className={errors.titulo ? "p-invalid w-full" : "w-full"}
              placeholder="Título do leilão"
            />
            {errors.titulo && (
              <Message severity="error" text={errors.titulo} className="mt-2" />
            )}
          </div>

          <div className="field col-12">
            <label htmlFor="descricao" className="block mb-2">
              Descrição *
            </label>
            <InputTextarea
              id="descricao"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              rows={3}
              className={errors.descricao ? "p-invalid w-full" : "w-full"}
              placeholder="Descrição resumida do leilão"
            />
            {errors.descricao && (
              <Message
                severity="error"
                text={errors.descricao}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12">
            <label htmlFor="descricaoDetalhada" className="block mb-2">
              Descrição Detalhada
            </label>
            <InputTextarea
              id="descricaoDetalhada"
              value={descricaoDetalhada}
              onChange={(e) => setDescricaoDetalhada(e.target.value)}
              rows={4}
              className="w-full"
              placeholder="Informações adicionais (opcional)"
            />
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="dataHoraInicio" className="block mb-2">
              Data/Hora Início *
            </label>
            <Calendar
              id="dataHoraInicio"
              value={dataHoraInicio}
              onChange={(e) => setDataHoraInicio(e.value)}
              showIcon
              showTime
              hourFormat="24"
              className={errors.dataHoraInicio ? "p-invalid w-full" : "w-full"}
            />
            {errors.dataHoraInicio && (
              <Message
                severity="error"
                text={errors.dataHoraInicio}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="dataHoraFim" className="block mb-2">
              Data/Hora Fim *
            </label>
            <Calendar
              id="dataHoraFim"
              value={dataHoraFim}
              onChange={(e) => setDataHoraFim(e.value)}
              showIcon
              showTime
              hourFormat="24"
              className={errors.dataHoraFim ? "p-invalid w-full" : "w-full"}
            />
            {errors.dataHoraFim && (
              <Message
                severity="error"
                text={errors.dataHoraFim}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="status" className="block mb-2">
              Status *
            </label>
            <Dropdown
              id="status"
              value={status}
              options={statusOptionsForm}
              onChange={(e) => setStatus(e.value)}
              className="w-full"
            />
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="categoria" className="block mb-2">
              Categoria *
            </label>
            <Dropdown
              id="categoria"
              value={categoriaId}
              options={categorias.map((c) => ({ label: c.nome, value: c.id }))}
              onChange={(e) => setCategoriaId(e.value)}
              className={errors.categoriaId ? "p-invalid w-full" : "w-full"}
              placeholder="Selecione uma categoria"
              filter
            />
            {errors.categoriaId && (
              <Message
                severity="error"
                text={errors.categoriaId}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="valorIncremento" className="block mb-2">
              Valor Incremento *
            </label>
            <InputNumber
              id="valorIncremento"
              value={valorIncremento}
              onValueChange={(e) => setValorIncremento(e.value)}
              mode="currency"
              currency="BRL"
              locale="pt-BR"
              className={errors.valorIncremento ? "p-invalid w-full" : "w-full"}
              minFractionDigits={2}
            />
            {errors.valorIncremento && (
              <Message
                severity="error"
                text={errors.valorIncremento}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12 md:col-6">
            <label htmlFor="lanceMinimo" className="block mb-2">
              Lance Mínimo *
            </label>
            <InputNumber
              id="lanceMinimo"
              value={lanceMinimo}
              onValueChange={(e) => setLanceMinimo(e.value)}
              mode="currency"
              currency="BRL"
              locale="pt-BR"
              className={errors.lanceMinimo ? "p-invalid w-full" : "w-full"}
              minFractionDigits={2}
            />
            {errors.lanceMinimo && (
              <Message
                severity="error"
                text={errors.lanceMinimo}
                className="mt-2"
              />
            )}
          </div>

          <div className="field col-12">
            <label htmlFor="observacao" className="block mb-2">
              Observação
            </label>
            <InputTextarea
              id="observacao"
              value={observacao}
              onChange={(e) => setObservacao(e.target.value)}
              rows={3}
              className="w-full"
              placeholder="Observações adicionais (opcional)"
            />
          </div>
        </div>
      </Dialog>

      {/* Dialog de Detalhes */}
      <Dialog
        header="Detalhes do Leilão"
        visible={showDetailDialog}
        style={{ width: "600px" }}
        onHide={() => setShowDetailDialog(false)}
        footer={detailFooter}
      >
        {detalhe && (
          <div className="p-fluid">
            <div className="grid">
              <div className="col-6">
                <label className="block mb-2 font-semibold">ID</label>
                <p className="m-0">{detalhe.id}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">Status</label>
                {statusTemplate(detalhe)}
              </div>
              <div className="col-12">
                <label className="block mb-2 font-semibold">Título</label>
                <p className="m-0">{detalhe.titulo}</p>
              </div>
              <div className="col-12">
                <label className="block mb-2 font-semibold">Descrição</label>
                <p className="m-0">{detalhe.descricao}</p>
              </div>
              {detalhe.descricaoDetalhada && (
                <div className="col-12">
                  <label className="block mb-2 font-semibold">
                    Descrição Detalhada
                  </label>
                  <p className="m-0">{detalhe.descricaoDetalhada}</p>
                </div>
              )}
              <div className="col-6">
                <label className="block mb-2 font-semibold">Categoria</label>
                <p className="m-0">{detalhe.categoria?.nome || "—"}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">Publicador</label>
                <p className="m-0">{detalhe.publicador?.nome || "—"}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">
                  Data/Hora Início
                </label>
                <p className="m-0">{dataTemplate(detalhe, "dataHoraInicio")}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">
                  Data/Hora Fim
                </label>
                <p className="m-0">{dataTemplate(detalhe, "dataHoraFim")}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">Lance Mínimo</label>
                <p className="m-0">{valorTemplate(detalhe, "lanceMinimo")}</p>
              </div>
              <div className="col-6">
                <label className="block mb-2 font-semibold">
                  Valor Incremento
                </label>
                <p className="m-0">
                  {valorTemplate(detalhe, "valorIncremento")}
                </p>
              </div>
              {detalhe.observacao && (
                <div className="col-12">
                  <label className="block mb-2 font-semibold">Observação</label>
                  <p className="m-0 text-color-secondary">
                    {detalhe.observacao}
                  </p>
                </div>
              )}
            </div>
          </div>
        )}
      </Dialog>
    </div>
  );
}

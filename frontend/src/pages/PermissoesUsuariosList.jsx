import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { Tag } from "primereact/tag";
import pessoaPerfilService from "../services/pessoaPerfilService";
import pessoaService from "../services/pessoaService";
import authService from "../services/authService";

export default function PermissoesUsuariosList() {
  const navigate = useNavigate();
  const [itens, setItens] = useState([]);
  const [pessoas, setPessoas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showDialog, setShowDialog] = useState(false);
  const [editando, setEditando] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Campos do formulário
  const [pessoaId, setPessoaId] = useState(null);
  const [perfilTipo, setPerfilTipo] = useState(null);
  const [errors, setErrors] = useState({ pessoaId: "", perfilTipo: "" });

  const [currentUser, setCurrentUser] = useState(null);
  const isAdmin = authService.isAdmin();

  // perfis base (use value=id do perfil no seu BD)
  const PERFIS_BASE = [
    { label: "ADMIN", value: 1 }, // ID 1
    { label: "COMPRADOR", value: 2 }, // ID 2
    { label: "VENDEDOR", value: 3 }, // ID 3
  ];

  const carregar = async () => {
    setLoading(true);
    try {
      const data = await pessoaPerfilService.list(); // backend retorna all (admin) ou só do usuário logado
      setItens(data || []);
    } catch (err) {
      alert(err?.message || "Falha ao carregar permissões.");
    } finally {
      setLoading(false);
    }
  };

  const carregarPessoas = async (curUser) => {
    try {
      if (isAdmin) {
        const data = await pessoaService.list();
        setPessoas(
          data.map((p) => ({ label: `${p.nome} (${p.email})`, value: p.id }))
        );
      } else if (curUser) {
        // apenas o próprio usuário
        setPessoas([
          { label: `${curUser.nome} (${curUser.email})`, value: curUser.id },
        ]);
      } else {
        setPessoas([]);
      }
    } catch (err) {
      alert(err?.message || "Falha ao carregar pessoas.");
    }
  };

  useEffect(() => {
    // busca current user e carrega
    (async () => {
      const me = await authService.getCurrentUser();
      setCurrentUser(me);
      await carregarPessoas(me);
      await carregar();
    })();
  }, []);

  // util: verifica se usuário já tem um profile type (por label 'ADMIN'/'COMPRADOR'/'VENDEDOR')
  const userHasProfileType = (tipo) => {
    return itens.some((it) => it.perfil?.tipo === tipo);
  };

  const abrirDialogNovo = () => {
    setEditando(null);
    setPerfilTipo(null);
    setErrors({ pessoaId: "", perfilTipo: "" });

    // se não-admin: predefine pessoaId e remove opções ADMIN
    if (!isAdmin && currentUser) {
      setPessoaId(currentUser.id);
      // se usuário já tem todos perfis permitidos, não abrir
      const allowed = ["COMPRADOR", "VENDEDOR"]; // para não-admins
      const available = allowed.filter((t) => !userHasProfileType(t));
      if (available.length === 0) {
        alert("Você já possui todos os perfis disponíveis.");
        return;
      }
    } else {
      setPessoaId(null);
    }

    setShowDialog(true);
  };

  const abrirDialogEditar = (item) => {
    setEditando(item);
    setPessoaId(item.pessoa?.id || null);
    setPerfilTipo(item.perfil?.id || null);
    setErrors({ pessoaId: "", perfilTipo: "" });
    setShowDialog(true);
  };

  const validar = () => {
    const next = { pessoaId: "", perfilTipo: "" };
    if (!pessoaId) next.pessoaId = "Selecione uma pessoa.";
    if (!perfilTipo) next.perfilTipo = "Selecione um perfil.";
    setErrors(next);
    return !next.pessoaId && !next.perfilTipo;
  };

  const salvar = async () => {
    if (!validar()) return;

    // verificação frontend para não-admin
    if (!isAdmin && currentUser) {
      // pessoaId deve ser a própria pessoa
      if (pessoaId !== currentUser.id) {
        alert("Você só pode atribuir permissões para si mesmo.");
        return;
      }
      // perfil escolhido não pode ser ADMIN
      const chosen = PERFIS_BASE.find((p) => p.value === perfilTipo);
      if (!chosen || chosen.label === "ADMIN") {
        alert("Operação não permitida.");
        return;
      }
      // evitar duplicata
      if (userHasProfileType(chosen.label)) {
        alert("Você já possui este perfil.");
        return;
      }
    }

    setSubmitting(true);
    try {
      const payload = {
        pessoa: { id: pessoaId },
        perfil: { id: perfilTipo },
      };

      if (editando) {
        await pessoaPerfilService.update(editando.id, {
          pessoaId: pessoaId,
          perfilId: perfilTipo,
        });
      } else {
        await pessoaPerfilService.create(payload);
      }

      setShowDialog(false);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao salvar permissão.");
    } finally {
      setSubmitting(false);
    }
  };

  const excluir = async (id) => {
    if (!window.confirm("Remover esta permissão do usuário?")) return;

    try {
      await pessoaPerfilService.remove(id);
      await carregar();
    } catch (err) {
      alert(err?.message || "Falha ao remover permissão.");
    }
  };

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  // ações só para admin
  const acoesTemplate = (rowData) => {
    return (
      <div className="flex gap-2">
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

  const pessoaTemplate = (rowData) => {
    return (
      <div>
        <div className="font-semibold">{rowData.pessoa?.nome}</div>
        <small className="text-color-secondary">{rowData.pessoa?.email}</small>
      </div>
    );
  };

  const perfilTemplate = (rowData) => {
    const tipo = rowData.perfil?.tipo;
    let severity = "info";
    if (tipo === "ADMIN") severity = "danger";
    if (tipo === "VENDEDOR") severity = "success";
    if (tipo === "COMPRADOR") severity = "warning";

    return <Tag value={tipo} severity={severity} />;
  };

  // options dinâmicas para o Dropdown de perfis, remove ADMIN se não-admin, remove perfis já atribuídos ao usuário (quando for self)
  const getPerfilOptions = () => {
    let options = PERFIS_BASE.slice(); // clone
    if (!isAdmin) {
      options = options.filter((p) => p.label !== "ADMIN");
      // remove já atribuidos
      const assignedTypes = itens.map((it) => it.perfil?.tipo);
      options = options.filter((p) => !assignedTypes.includes(p.label));
    }
    return options;
  };

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

  return (
    <div className="p-4">
      <Card
        title={
          isAdmin ? "Gerenciar Permissões de Usuários" : "Minhas Permissões"
        }
      >
        <div className="flex justify-content-between mb-3">
          <div className="flex gap-2">
            <Button
              label="Nova Permissão"
              icon="pi pi-plus"
              onClick={abrirDialogNovo}
              disabled={
                !isAdmin &&
                currentUser &&
                ["COMPRADOR", "VENDEDOR"].every((t) => userHasProfileType(t))
              }
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
          emptyMessage="Nenhuma permissão encontrada."
          paginator
          rows={10}
          rowsPerPageOptions={[5, 10, 25]}
          stripedRows
        >
          <Column field="id" header="ID" sortable style={{ width: "80px" }} />
          <Column header="Usuário" body={pessoaTemplate} sortable />
          <Column header="Perfil" body={perfilTemplate} sortable />
          <Column
            header="Ações"
            body={acoesTemplate}
            style={{ width: "120px" }}
          />
        </DataTable>
      </Card>

      <Dialog
        header={editando ? "Editar Permissão" : "Nova Permissão"}
        visible={showDialog}
        style={{ width: "500px" }}
        onHide={() => setShowDialog(false)}
        footer={dialogFooter}
      >
        <div className="p-fluid">
          <div className="field mb-3">
            <label htmlFor="pessoa" className="block mb-2">
              Usuário *
            </label>

            {/* se for admin, pode escolher qualquer pessoa; se não, escolhe apenas a si mesmo (campo desabilitado) */}
            <Dropdown
              id="pessoa"
              value={pessoaId}
              options={pessoas}
              onChange={(e) => setPessoaId(e.value)}
              placeholder="Selecione um usuário"
              className={errors.pessoaId ? "p-invalid w-full" : "w-full"}
              filter
              disabled={!!editando || !isAdmin}
            />
            {errors.pessoaId && (
              <small className="p-error">{errors.pessoaId}</small>
            )}
          </div>

          <div className="field mb-3">
            <label htmlFor="perfil" className="block mb-2">
              Perfil *
            </label>
            <Dropdown
              id="perfil"
              value={perfilTipo}
              options={getPerfilOptions()}
              onChange={(e) => setPerfilTipo(e.value)}
              placeholder="Selecione um perfil"
              className={errors.perfilTipo ? "p-invalid w-full" : "w-full"}
            />
            {errors.perfilTipo && (
              <small className="p-error">{errors.perfilTipo}</small>
            )}
          </div>

          <div className="surface-100 p-3 border-round">
            <p className="text-sm m-0">
              <strong>ADMIN:</strong> Permissão total no sistema
              <br />
              <strong>COMPRADOR:</strong> Dar lances em leilões
              <br />
              <strong>VENDEDOR:</strong> Criar e gerenciar leilões
            </p>
          </div>
        </div>
      </Dialog>
    </div>
  );
}

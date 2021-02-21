package br.com.zup.orangetalents.repository.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class VacinacaoPk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "data_vacinacao")
    private Date dataVacinacao;

    @Column(name = "nome_vacina")
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public VacinacaoPk() {
    }

    public VacinacaoPk(Date dataVacinacao, String nome, Usuario usuario) {
        setDataVacinacao(dataVacinacao);
        setNome(nome);
        setUsuario(usuario);
    }

    public Date getDataVacinacao() {
        return dataVacinacao;
    }

    public void setDataVacinacao(Date dataVacinacao) {
        this.dataVacinacao = dataVacinacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VacinacaoPk that = (VacinacaoPk) o;

        if (getDataVacinacao() != null ? !getDataVacinacao().equals(that.getDataVacinacao()) : that.getDataVacinacao() != null)
            return false;
        if (getNome() != null ? !getNome().equals(that.getNome()) : that.getNome() != null) return false;
        return getUsuario() != null ? getUsuario().equals(that.getUsuario()) : that.getUsuario() == null;
    }

    @Override
    public int hashCode() {
        int result = getDataVacinacao() != null ? getDataVacinacao().hashCode() : 0;
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getUsuario() != null ? getUsuario().hashCode() : 0);
        return result;
    }
}

package br.com.zup.orangetalents.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class VacinacaoRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataVacinacao;

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String emailUsuario;

    public VacinacaoRequest() {
    }

    public VacinacaoRequest(Date dataVacinacao, String emailUsuario, String nome) {
        setDataVacinacao(dataVacinacao);
        setNome(nome);
        setEmailUsuario(emailUsuario);
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

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}

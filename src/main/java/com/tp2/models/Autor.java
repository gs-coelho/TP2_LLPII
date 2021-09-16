package com.tp2.models;

import java.util.Date;

public class Autor {
    private String nome;
    private int codigo;
    private Date dtNasc;
    private String localNasc;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(Date dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getLocalNasc() {
        return localNasc;
    }

    public void setLocalNasc(String localNasc) {
        this.localNasc = localNasc;
    }
}

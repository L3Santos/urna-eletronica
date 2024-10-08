package com.urna.entity;

import com.urna.enuns.TipoPessoa;

import javax.persistence.*;

@Entity
@Table(name = "urn_pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "titulo_eleitor", nullable = false)
    private String tituloEleitor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    public Pessoa() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getTituloEleitor() {
        return tituloEleitor;
    }
    public void setTituloEleitor(String tituloEleitor) {
        this.tituloEleitor = tituloEleitor;
    }
    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }
    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }
}

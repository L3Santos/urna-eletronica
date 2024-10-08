package com.urna.entity;

import com.urna.enuns.TipoCandidato;

import javax.persistence.*;

@Entity
@Table(name = "urn_candidato")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

    @Column(name = "numero", nullable = false, unique = true)
    private int numero;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_candidato", nullable = false)
    private com.urna.enuns.TipoCandidato tipoCandidato;

    @Column(name = "foto_candidato")
    private String fotoCandidato;


    public Candidato() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Pessoa getPessoa() {
        return pessoa;
    }
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public Partido getPartido() {
        return partido;
    }
    public void setPartido(Partido partido) {
        this.partido = partido;
    }
    public TipoCandidato getTipoCandidato() {
        return tipoCandidato;
    }
    public void setTipoCandidato(TipoCandidato tipoCandidato) {
        this.tipoCandidato = tipoCandidato;
    }
    public String getFotoCandidato() {
        return fotoCandidato;
    }
    public void setFotoCandidato(String fotoCandidato) {
        this.fotoCandidato = fotoCandidato;
    }
}

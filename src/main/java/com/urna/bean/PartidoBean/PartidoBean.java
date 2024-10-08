package com.urna.bean.PartidoBean;

import com.urna.entity.Partido;
import com.urna.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class PartidoBean {
    private EntityManager em;

    private List<Partido> partidos;

    public PartidoBean() {
        em = HibernateUtil.getEntityManager();
        partidos = new ArrayList<>();
    }

    public Partido geraNovoPartido(String nome, int numero) throws Exception {
        EntityTransaction flush = em.getTransaction();
        try {
            flush.begin();
            Partido partido = new Partido();
            partido.setNome(nome);
            partido.setNumero(numero);
            em.persist(partido);
            flush.commit();
            return partido;
        } catch (Exception e) {
            if (flush.isActive()) {
                flush.rollback();
            }
            throw new Exception("Erro ao cadastrar novo partido: " + e.getMessage(), e);
        }
    }

    public boolean excluirPartido(String nomeOuNumero) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            StringBuilder query = new StringBuilder();
            query.append("SELECT p ");
            query.append("FROM Partido p ");
            query.append("WHERE p.nome = :nomeOuNumero ");
            query.append("OR p.numero = :numero");

            TypedQuery<Partido> typedQuery = em.createQuery(query.toString(), Partido.class);
            typedQuery.setParameter("nomeOuNumero", nomeOuNumero);

            try {
                Integer numero = Integer.valueOf(nomeOuNumero);
                typedQuery.setParameter("numero", numero);
            } catch (NumberFormatException e) {
                typedQuery.setParameter("numero", null);
            }

            Partido partido = typedQuery.getSingleResult();
            em.remove(partido);
            transaction.commit();
            return true;
        } catch (NoResultException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao excluir partido: " + e.getMessage(), e);
        }
    }

    public List<Partido> listPartidosCadastrado() throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT p ");
            query.append("FROM Partido p ");

            TypedQuery<Partido> partidos = em.createQuery(query.toString(), Partido.class);
            return partidos.getResultList();
        } catch (Error er) {
            throw new Exception("Erro ao listar partidos cadastrados");
        }
    }
}

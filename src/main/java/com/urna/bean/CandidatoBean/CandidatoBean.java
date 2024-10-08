package com.urna.bean.CandidatoBean;

import com.urna.entity.Candidato;
import com.urna.entity.Pessoa;
import com.urna.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class CandidatoBean {
    private EntityManager em;

    public CandidatoBean() {
        em = HibernateUtil.getEntityManager();
    }

    public Pessoa geraNovoPessoa(Pessoa pessoa) throws Exception {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(pessoa);
            transaction.commit();
            return pessoa;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Erro ao cadastrar pessoa: " + e.getMessage(), e);
        }
    }

    public Candidato geraNovoCandidato(Candidato candidato) throws Exception {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(candidato);
            transaction.commit();
            return candidato;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Erro ao cadastrar novo candidato: " + e.getMessage(), e);
        }
    }

    public boolean excluirCandidato(String numeroOuCpf, int numeroCandidato) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            StringBuilder query = new StringBuilder();
            query.append("SELECT c ");
            query.append("FROM Candidato c ");
            query.append("JOIN c.pessoa p ");
            query.append("WHERE p.cpf = :numeroOuCpf ");
            query.append("OR c.numero = :numero");

            TypedQuery<Candidato> typedQuery = em.createQuery(query.toString(), Candidato.class);
            typedQuery.setParameter("numeroOuCpf", numeroOuCpf);

            try {
                Integer numero = Integer.valueOf(numeroCandidato);
                typedQuery.setParameter("numero", numero);
            } catch (NumberFormatException e) {
                typedQuery.setParameter("numero", null);
            }

            Candidato candidato = typedQuery.getSingleResult();
            em.remove(candidato);
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
            throw new RuntimeException("Erro ao excluir candidato: " + e.getMessage(), e);
        }
    }

    public List<Candidato> listCandidatosCadastrados() throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT c ");
            query.append("FROM Candidato c ");

            TypedQuery<Candidato> candidatos = em.createQuery(query.toString(), Candidato.class);
            return candidatos.getResultList();
        } catch (Error er) {
            throw new Exception("Erro ao listar candidatos cadastrados: " + er.getMessage(), er);
        }
    }
}

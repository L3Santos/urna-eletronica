package com.urna;

import com.urna.bean.UrnaBean.UrnaBean;
import com.urna.controller.UrnaController.UrnaController;
import com.urna.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.*;

public class App {
    static UrnaController urnaController = new UrnaController();

    private static JFrame telaPrincipal;
    private static JPanel painelPrincipal;
    private static CardLayout layoutCartao;
    private static UrnaBean urna;

    public static void initApp() {
        telaPrincipal = new JFrame("Urna Eleitoral");
        telaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaPrincipal.setSize(700, 700);
        telaPrincipal.setLocationRelativeTo(null);

        layoutCartao = new CardLayout();
        painelPrincipal = new JPanel(layoutCartao);

        urna = new UrnaBean("001001-01");

        criarPainelInicial();

        telaPrincipal.add(painelPrincipal);
        telaPrincipal.setVisible(true);
    }

    public static void criarPainelInicial() {
        JPanel painelHome = new JPanel();
        painelHome.setLayout(new GridLayout(1, 1));

        JButton botaoAcessarUrna = new JButton("Acessar Urna - Número: " + urna.getCodigoUrna());

        botaoAcessarUrna.setBackground(new Color(0, 102, 204));
        botaoAcessarUrna.setForeground(Color.WHITE);
        botaoAcessarUrna.setFont(new Font("Arial", Font.BOLD, 18));
        painelHome.add(botaoAcessarUrna);

        botaoAcessarUrna.addActionListener(e -> abrirTelaAcesso());

        painelPrincipal.add(painelHome, "Home");
    }

    public static void abrirTelaAcesso() {
        JPanel painelAcesso = new JPanel();
        painelAcesso.setLayout(new BorderLayout());
        painelAcesso.setBackground(new Color(240, 240, 240));

        JLabel tituloEleicao = new JLabel("Eleições 2024 - Ano mais roubado de todos", SwingConstants.CENTER);
        tituloEleicao.setFont(new Font("Arial", Font.BOLD, 28));
        painelAcesso.add(tituloEleicao, BorderLayout.NORTH);

        JPanel painelSelecao = new JPanel(new GridLayout(0, 1));
        JButton botaoAcessarCodigo = new JButton("Acessar por Código");
        botaoAcessarCodigo.setBackground(new Color(0, 102, 204));
        botaoAcessarCodigo.setSize(700, 250);
        botaoAcessarCodigo.setForeground(Color.WHITE);
        botaoAcessarCodigo.setFont(new Font("Arial", Font.BOLD, 16));
        JButton botaoAcessarUsuario = new JButton("Acessar por Usuário");
        botaoAcessarUsuario.setBackground(new Color(0, 102, 204));
        botaoAcessarUsuario.setSize(700, 250);
        botaoAcessarUsuario.setForeground(Color.WHITE);
        botaoAcessarUsuario.setFont(new Font("Arial", Font.BOLD, 16));


        painelSelecao.add(botaoAcessarCodigo);
        painelSelecao.add(botaoAcessarUsuario);
        painelAcesso.add(painelSelecao, BorderLayout.CENTER);

        botaoAcessarCodigo.addActionListener(e -> urnaController.initUrna());
        botaoAcessarUsuario.addActionListener(e -> urnaController.initElicao());

        painelPrincipal.add(painelAcesso, "Acesso");

        layoutCartao.show(painelPrincipal, "Acesso");
    }

    public static void main(String[] args) {
        HibernateUtil.getEntityManager();
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        initApp();
    }
}

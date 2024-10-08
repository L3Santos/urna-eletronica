package com.urna.controller.PartidoController;

import com.urna.bean.PartidoBean.PartidoBean;
import com.urna.entity.Partido;

import javax.swing.*;
import java.awt.*;

public class PartidoController {
    private JFrame telaPartido;
    private JPanel painelPartido;
    private CardLayout layoutPartido;
    private PartidoBean partidoBean;

    public void initPartido() {
        telaPartido = new JFrame("Partido");
        telaPartido.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaPartido.setSize(700, 700);

        layoutPartido = new CardLayout();
        painelPartido = new JPanel(layoutPartido);

        partidoBean = new PartidoBean();

        opcoesPartido();

        telaPartido.add(painelPartido);
        telaPartido.setVisible(true);
    }

    public void opcoesPartido() {
        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new GridLayout(4, 1));

        JButton listarPartidoCadastrado = new JButton("Lista de partidos cadastrados");
        listarPartidoCadastrado.setForeground(Color.WHITE);
        listarPartidoCadastrado.setBackground(new Color(0, 102, 204));
        JButton adicionarPartido = new JButton("Adicionar partido");
        adicionarPartido.setForeground(Color.WHITE);
        adicionarPartido.setBackground(new Color(0, 102, 204));
        JButton excluirPartido = new JButton("Excluir partido");
        excluirPartido.setForeground(Color.WHITE);
        excluirPartido.setBackground(new Color(0, 102, 204));
        JButton voltarMenu = new JButton("Voltar ao Menu");
        voltarMenu.setForeground(Color.WHITE);
        voltarMenu.setBackground(new Color(0, 102, 204));

        listarPartidoCadastrado.addActionListener(e -> {
            try {
                exibirListaPartidos();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        adicionarPartido.addActionListener(e -> {
            try {
                criarPainelCadastroPartido();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        excluirPartido.addActionListener(e -> {
            try {
                criarPainelExclusaoPartido();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        voltarMenu.addActionListener(e -> {telaPartido.dispose();});

        painelOpcoes.add(listarPartidoCadastrado);
        painelOpcoes.add(adicionarPartido);
        painelOpcoes.add(excluirPartido);
        painelOpcoes.add(voltarMenu);

        painelPartido.add(painelOpcoes, "Opcoes");
        layoutPartido.show(painelPartido, "Opcoes");
    }

    private void criarPainelCadastroPartido() {
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(5, 2));

        JLabel labelNome = new JLabel("Nome do Partido:");
        JTextField campoNome = new JTextField();
        JLabel labelNumero = new JLabel("Número do Partido:");
        JTextField campoNumero = new JTextField();

        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBackground(new Color(144, 238, 144));
        botaoCadastrar.setForeground(Color.black);
        botaoCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText();
                String numeroStr = campoNumero.getText();
                int numero = 0;
                numero = Integer.parseInt(numeroStr);
                Partido novoPartido = null;
                novoPartido = partidoBean.geraNovoPartido(nome, numero);

                JOptionPane.showMessageDialog(telaPartido, "Partido " + novoPartido.getNome() + " cadastrado com sucesso!");
                campoNome.setText("");
                campoNumero.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(telaPartido, "Número do partido deve ser um número inteiro.");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.black);
        botaoVoltar.addActionListener(e -> {
            layoutPartido.show(painelPartido, "Opcoes");
        });

        painelCadastro.add(labelNome);
        painelCadastro.add(campoNome);
        painelCadastro.add(labelNumero);
        painelCadastro.add(campoNumero);
        painelCadastro.add(new JLabel());
        painelCadastro.add(botaoCadastrar);
        painelCadastro.add(new JLabel());
        painelCadastro.add(botaoVoltar);

        painelPartido.add(painelCadastro, "Cadastro");
        layoutPartido.show(painelPartido, "Cadastro");
    }

    private void criarPainelExclusaoPartido() {
        JPanel painelExclusao = new JPanel();
        painelExclusao.setLayout(new GridLayout(3, 2));

        JLabel labelNomeOuNumero = new JLabel("Nome ou Número do Partido:");
        JTextField campoNomeOuNumero = new JTextField();

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBackground(new Color(255, 102, 102));
        botaoExcluir.setForeground(Color.black);
        botaoExcluir.addActionListener(e -> {
            String nomeOuNumero = campoNomeOuNumero.getText();

            boolean excluido = partidoBean.excluirPartido(nomeOuNumero);

            if (excluido) {
                JOptionPane.showMessageDialog(telaPartido, "Partido excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(telaPartido, "Partido não encontrado.");
            }

            campoNomeOuNumero.setText("");
        });

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.black);
        botaoVoltar.addActionListener(e -> {
            layoutPartido.show(painelPartido, "Opcoes");
        });

        painelExclusao.add(labelNomeOuNumero);
        painelExclusao.add(campoNomeOuNumero);
        painelExclusao.add(botaoExcluir);
        painelExclusao.add(botaoVoltar);

        painelPartido.add(painelExclusao, "Exclusao");
        layoutPartido.show(painelPartido, "Exclusao");
    }

    private void exibirListaPartidos() throws Exception {
        StringBuilder lista = new StringBuilder("Lista de partidos cadastrados:\n");

        for (Partido partido : partidoBean.listPartidosCadastrado()) {
            lista.append("Nome: ").append(partido.getNome())
                    .append(", Número: ").append(partido.getNumero()).append("\n");
        }

        if (partidoBean.listPartidosCadastrado().isEmpty()) {
            lista.append("Nenhum partido cadastrado.");
        }

        JOptionPane.showMessageDialog(telaPartido, lista.toString());
    }
}

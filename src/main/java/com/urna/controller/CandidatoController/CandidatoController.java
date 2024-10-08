package com.urna.controller.CandidatoController;

import com.urna.bean.CandidatoBean.CandidatoBean;
import com.urna.bean.PartidoBean.PartidoBean;
import com.urna.entity.Candidato;
import com.urna.entity.Partido;
import com.urna.entity.Pessoa;
import com.urna.enuns.TipoCandidato;
import com.urna.enuns.TipoPessoa;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatoController { ;
    static CandidatoBean candidatoBean = new CandidatoBean();
    static PartidoBean partidoBean = new PartidoBean();

    private JFrame telaCandidato;
    private JPanel painelCandidato;
    private CardLayout layoutCandidato;

    public void initCandidato() {
        telaCandidato = new JFrame("Candidato");
        telaCandidato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaCandidato.setSize(700, 700);

        layoutCandidato = new CardLayout();
        painelCandidato = new JPanel(layoutCandidato);

        opcoesCandidato();

        telaCandidato.add(painelCandidato);
        telaCandidato.setVisible(true);
    }

    public void opcoesCandidato() {
        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new GridLayout(4, 1));

        JButton listarCandidato = new JButton("Lista de candidatos");
        listarCandidato.setForeground(Color.WHITE);
        listarCandidato.setBackground(new Color(0, 102, 204));
        JButton adicionarCandidato = new JButton("Adicionar candidato");
        adicionarCandidato.setForeground(Color.WHITE);
        adicionarCandidato.setBackground(new Color(0, 102, 204));
        JButton excluirCandidato = new JButton("Excluir candidato");
        excluirCandidato.setForeground(Color.WHITE);
        excluirCandidato.setBackground(new Color(0, 102, 204));
        JButton voltarMenu = new JButton("Voltar ao menu");
        voltarMenu.setForeground(Color.WHITE);
        voltarMenu.setBackground(new Color(0, 102, 204));

        listarCandidato.addActionListener(e -> {
            try {
                exibirListaCandidatos();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        adicionarCandidato.addActionListener(e -> {
            try {
                criarPainelCadastroCandidato();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        excluirCandidato.addActionListener(e -> {
            try {
                criarPainelExclusaoCandidato();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        voltarMenu.addActionListener(e -> telaCandidato.dispose());

        painelOpcoes.add(listarCandidato);
        painelOpcoes.add(adicionarCandidato);
        painelOpcoes.add(excluirCandidato);
        painelOpcoes.add(voltarMenu);

        painelCandidato.add(painelOpcoes, "Opcoes");
        layoutCandidato.show(painelCandidato, "Opcoes");
    }

    public void criarPainelCadastroCandidato() throws Exception {
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(8, 1));

        JLabel labelNome = new JLabel("Nome");
        JTextField textoNome = new JTextField(15);

        JLabel labelCpf = new JLabel("CPF");
        JTextField textoCpf = new JTextField(15);

        JLabel labelTituloEleitor = new JLabel("Título de Eleitor");
        JTextField textoTituloEleitor = new JTextField(15);

        JLabel labelTipoPessoa = new JLabel("Tipo de Pessoa");
        String[] opcoesTipoPessoa = {"Candidato"};
        JComboBox<String> comboTipoPessoa = new JComboBox<>(opcoesTipoPessoa);

        JLabel labelTipoCandidato = new JLabel("Tipo de Candidato");
        JComboBox<TipoCandidato> comboTipoCandidato = new JComboBox<>(TipoCandidato.values());
        comboTipoCandidato.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoCandidato) {
                    label.setText(((TipoCandidato) value).getDescricao());
                }
                return label;
            }
        });

        JLabel labelNumero = new JLabel("Número do Candidato");
        JTextField textoNumero = new JTextField(15);

        List<Partido> partidos = partidoBean != null ? partidoBean.listPartidosCadastrado() : new ArrayList<>();
        JLabel labelPartidos = new JLabel("Partidos");
        JComboBox<Partido> comboPartidos = new JComboBox<>(partidos.toArray(new Partido[0]));


        JButton cadastrarCandidato = new JButton("Cadastrar candidato");
        cadastrarCandidato.setForeground(Color.black);
        cadastrarCandidato.setBackground(new Color(144, 238, 144));
        cadastrarCandidato.addActionListener(e -> {
            try {
                String nome = textoNome.getText();
                String cpf = textoCpf.getText();
                String tituloEleitor = textoTituloEleitor.getText();
                String tipoPessoa = comboTipoPessoa.getSelectedItem().toString();
                String numero = textoNumero.getText();

                Partido partidoSelecionado = (Partido) comboPartidos.getSelectedItem();
                TipoCandidato tipoCandidatoSelecionado = (TipoCandidato) comboTipoCandidato.getSelectedItem();

                if (partidoSelecionado != null && tipoCandidatoSelecionado != null) {
                    String numeroCompletoDoCandidato = partidoSelecionado.getNumero() + numero;

                    Pessoa pessoa = new Pessoa();
                    pessoa.setNome(nome);
                    pessoa.setCpf(cpf);
                    pessoa.setTituloEleitor(tituloEleitor);
                    if (tipoPessoa.equals(TipoPessoa.CANDIDATO.getDescricao())) {
                        pessoa.setTipoPessoa(TipoPessoa.CANDIDATO);
                    }

                    Candidato candidato = new Candidato();
                    candidato.setPessoa(pessoa);
                    int numeroCompleto = Integer.parseInt(numeroCompletoDoCandidato);
                    candidato.setNumero(numeroCompleto);
                    candidato.setPartido(partidoSelecionado);
                    candidato.setTipoCandidato(tipoCandidatoSelecionado);
                    candidato.setFotoCandidato("INDEFINIDO");

                    candidatoBean.geraNovoPessoa(pessoa);
                    candidatoBean.geraNovoCandidato(candidato);
                    JOptionPane.showMessageDialog(painelCadastro, "Candidato cadastrado com sucesso!");
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar candidato: " + exception.getMessage());
            }
        });

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setForeground(Color.black);
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.addActionListener(e -> layoutCandidato.show(painelCandidato, "Opcoes"));

        painelCadastro.add(labelNome);
        painelCadastro.add(textoNome);
        painelCadastro.add(labelCpf);
        painelCadastro.add(textoCpf);
        painelCadastro.add(labelTituloEleitor);
        painelCadastro.add(textoTituloEleitor);
        painelCadastro.add(labelTipoPessoa);
        painelCadastro.add(comboTipoPessoa);
        painelCadastro.add(labelTipoCandidato);
        painelCadastro.add(comboTipoCandidato);
        painelCadastro.add(labelNumero);
        painelCadastro.add(textoNumero);
        painelCadastro.add(labelPartidos);
        painelCadastro.add(comboPartidos);
        painelCadastro.add(cadastrarCandidato);
        painelCadastro.add(botaoVoltar);

        painelCandidato.add(painelCadastro, "Cadastro");
        layoutCandidato.show(painelCandidato, "Cadastro");
    }


    public void criarPainelExclusaoCandidato() {
        JPanel painelExclusao = new JPanel();
        painelExclusao.setLayout(new GridLayout(3, 2));

        JLabel labelNumero = new JLabel("Número completo do candidato:");
        JTextField campoNumero = new JTextField();

        JLabel labelCpf = new JLabel("CPF do candidato:");
        JTextField campoCpf = new JTextField();

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.setForeground(Color.black);
        botaoExcluir.setBackground(new Color(255, 102, 102));
        botaoExcluir.addActionListener(e -> {
            String cpf = campoCpf.getText();
            String numeroCompletoStr = campoNumero.getText();

            if (cpf.isEmpty() && numeroCompletoStr.isEmpty()) {
                JOptionPane.showMessageDialog(telaCandidato, "Por favor, insira o CPF ou o número do candidato.");
                return;
            }

            Integer numeroCompleto = null;

            // Verifica se o campoNúmero não está vazio e tenta convertê-lo
            if (!numeroCompletoStr.isEmpty()) {
                try {
                    numeroCompleto = Integer.parseInt(numeroCompletoStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(telaCandidato, "Número do candidato deve ser um número válido.");
                    return;
                }
            }

            boolean excluido = candidatoBean.excluirCandidato(cpf, numeroCompleto);

            if (excluido) {
                JOptionPane.showMessageDialog(telaCandidato, "Candidato excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(telaCandidato, "Candidato não encontrado.");
            }

            campoNumero.setText("");
            campoCpf.setText("");
        });

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setForeground(Color.black);
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.addActionListener(e -> layoutCandidato.show(painelCandidato, "Opcoes"));

        painelExclusao.add(labelNumero);
        painelExclusao.add(campoNumero);
        painelExclusao.add(labelCpf);
        painelExclusao.add(campoCpf);
        painelExclusao.add(botaoExcluir);
        painelExclusao.add(botaoVoltar);

        painelCandidato.add(painelExclusao, "Exclusao");
        layoutCandidato.show(painelCandidato, "Exclusao");
    }

    public void exibirListaCandidatos() throws Exception {
        StringBuilder lista = new StringBuilder("Lista de candidatos cadastrados:\n");

        for (Candidato candidato : candidatoBean.listCandidatosCadastrados()) {
            lista.append("Nome: ").append(candidato.getPessoa().getNome())
                    .append(", CPF: ").append(candidato.getPessoa().getCpf())
                    .append(", Número: ").append(candidato.getNumero()).append("\n");
        }

        // Se não houver candidatos cadastrados
        if (candidatoBean.listCandidatosCadastrados().isEmpty()) {
            lista.append("Nenhum candidato cadastrado.");
        }

        JOptionPane.showMessageDialog(telaCandidato, lista.toString());
    }
}
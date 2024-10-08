package com.urna.controller.UrnaController;

import com.urna.bean.UrnaBean.UrnaBean;
import com.urna.controller.CandidatoController.CandidatoController;
import com.urna.controller.PartidoController.PartidoController;
import com.urna.utils.TocaSomUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UrnaController {
    private JFrame telaPrincipal;
    private JPanel painelPrincipal;
    private CardLayout layoutCartao;

    private JFrame telaEleicao;
    private JPanel painelEleicao;
    private CardLayout layoutCartaoEleicao;

    static PartidoController partidoController = new PartidoController();
    static CandidatoController candidatoController = new CandidatoController();
    static TocaSomUtils tocaSomUtils = new TocaSomUtils();
    private UrnaBean urna;

    private Map<String, Integer> votosPorCandidato = new HashMap<>();
    private int totalVotosNulos = 0;
    private int totalVotosBrancos = 0;
    private int totalVotosUrna = 0;
    private int totalEleitores = 5;
    private int totalEleitoresVotos = 0;

    private Boolean isFinalizada = true;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    public void initUrna() {
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

    public void initElicao() {
        telaEleicao = new JFrame("Urna Aduterada - Eleições 2024");
        telaEleicao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaEleicao.setSize(700, 700);
        telaEleicao.setLocationRelativeTo(null);

        layoutCartaoEleicao = new CardLayout();
        painelEleicao = new JPanel(layoutCartaoEleicao);

        if (dataHoraFim == null) {
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Data fim da votação ainda não foi definida");
            JOptionPane.showMessageDialog(painelEleicao, mensagem.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        DateTimeFormatter formatterBrasil = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        if (dataHoraFim != null && dataHoraFim.format(formatterBrasil).compareTo(LocalDateTime.now().format(formatterBrasil)) < 0) {
            isFinalizada = true;
        }

        if (totalEleitoresVotos == totalEleitores || totalEleitoresVotos > totalEleitores) {
            isFinalizada = true;
        }

        if (!isFinalizada) {
            validaTituloCpf();
            telaEleicao.add(painelEleicao);
            telaEleicao.setVisible(true);
        } else {
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Urna ainda não foi iniciada pelo o lider da seção");
            JOptionPane.showMessageDialog(painelEleicao, mensagem.toString(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
            painelEleicao.setVisible(false);
        }
    }

    public void validaTituloCpf() {
        JPanel painelValidaTituloCpf = new JPanel();
        painelValidaTituloCpf.setLayout(new BorderLayout());
        painelValidaTituloCpf.setBackground(new Color(0, 102, 204));

        JTextField campoTitulCpf = new JTextField();
        campoTitulCpf.setHorizontalAlignment(JTextField.CENTER);
        campoTitulCpf.setPreferredSize(new Dimension(200, 30));
        campoTitulCpf.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        JLabel tituloEleicao = new JLabel("Digite seu titulo ou cpf para validação", SwingConstants.CENTER);
        tituloEleicao.setFont(new Font("Arial", Font.BOLD, 28));
        painelValidaTituloCpf.add(tituloEleicao, BorderLayout.NORTH);

        JButton botaoConfirmar = new JButton("Validar Titulo ou Cpf");
        botaoConfirmar.setBackground(new Color(144, 238, 144));
        botaoConfirmar.setForeground(Color.black);

        JPanel painelTituloOuCpf = new JPanel();
        painelTituloOuCpf.setLayout(new GridBagLayout());
        painelTituloOuCpf.add(campoTitulCpf);
        painelValidaTituloCpf.add(painelTituloOuCpf, BorderLayout.CENTER);
        painelValidaTituloCpf.add(botaoConfirmar, BorderLayout.SOUTH);

        botaoConfirmar.addActionListener(e -> {
            if (campoTitulCpf.getText().length() > 7) {
                abrirUrnaVotacao();
            } else {
                JOptionPane.showMessageDialog(telaPrincipal, "Titulo ou Cpf invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelEleicao.add(painelValidaTituloCpf, "Acesso Urna");
        layoutCartaoEleicao.show(painelEleicao, "Acesso Urna");
    }

    public void criarPainelInicial() {
        JPanel painelHome = new JPanel();
        painelHome.setLayout(new GridLayout(1, 1));

        JButton botaoAcessarUrna = new JButton("Acessar Urna - Número: " + urna.getCodigoUrna());

        botaoAcessarUrna.setBackground(new Color(0, 102, 204));
        botaoAcessarUrna.setForeground(Color.WHITE);
        botaoAcessarUrna.setFont(new Font("Arial", Font.BOLD, 18));
        painelHome.add(botaoAcessarUrna);

        botaoAcessarUrna.addActionListener(e -> abrirTelaAcessoUrna());

        painelPrincipal.add(painelHome, "Home");
    }

    public void abrirTelaAcessoUrna() {
        JPanel painelAcessoUrna = new JPanel();
        painelAcessoUrna.setLayout(new BorderLayout());

        JTextField campoCodigo = new JTextField();
        campoCodigo.setHorizontalAlignment(JTextField.CENTER);
        campoCodigo.setPreferredSize(new Dimension(200, 30));
        campoCodigo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton botaoConfirmar = new JButton("Confirmar Código");
        botaoConfirmar.setBackground(new Color(0, 102, 204));
        botaoConfirmar.setForeground(Color.WHITE);
        botaoConfirmar.setFont(new Font("Arial", Font.BOLD, 16));

        painelAcessoUrna.add(new JLabel("Digite o código de ativação da urna: " + urna.getCodigoUrna(), SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel painelCodigo = new JPanel();
        painelCodigo.setLayout(new GridBagLayout());
        painelCodigo.add(campoCodigo);
        painelAcessoUrna.add(painelCodigo, BorderLayout.CENTER);
        painelAcessoUrna.add(botaoConfirmar, BorderLayout.SOUTH);
        painelAcessoUrna.setFont(new Font("Arial", Font.BOLD, 28));

        botaoConfirmar.addActionListener(e -> {
            String codigo = campoCodigo.getText();
            if (urna.habilitarUrna(codigo)) {
                if (isFinalizada) {
                    abrirUrna();
                } else {
                    abrirMenuUrna();
                }
            } else {
                JOptionPane.showMessageDialog(telaPrincipal, "Código inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelPrincipal.add(painelAcessoUrna, "Acesso Urna");
        layoutCartao.show(painelPrincipal, "Acesso Urna");
    }

    public void abrirUrna() {
        JPanel painelUrna = new JPanel();
        painelUrna.setLayout(new BorderLayout());
        painelUrna.setBackground(new Color(240, 240, 240));

        JLabel tituloUrna = new JLabel("Urna de Votação", SwingConstants.CENTER);
        tituloUrna.setFont(new Font("Arial", Font.BOLD, 28));
        painelUrna.add(tituloUrna, BorderLayout.NORTH);

        JLabel infoUrna = new JLabel("Urna número " + urna.getCodigoUrna() + " habilitada com sucesso!", SwingConstants.CENTER);
        infoUrna.setFont(new Font("Arial", Font.PLAIN, 18));
        painelUrna.add(infoUrna, BorderLayout.CENTER);

        JButton botaoAcessarMenu = new JButton("Acessar menu urna");
        botaoAcessarMenu.setBackground(new Color(0, 102, 204));
        botaoAcessarMenu.setForeground(Color.WHITE);
        botaoAcessarMenu.setFont(new Font("Arial", Font.BOLD, 16));
        painelUrna.add(botaoAcessarMenu, BorderLayout.SOUTH);

        botaoAcessarMenu.addActionListener(e -> abrirMenuUrna());

        painelPrincipal.add(painelUrna, "UrnaController");
        layoutCartao.show(painelPrincipal, "UrnaController");
    }

    public void abrirMenuUrna() {
        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new GridLayout(0, 1));
        painelMenu.setBackground(new Color(240, 240, 240));

        JButton cadastrarPartido = new JButton("Menu Partido");
        JButton cadastrarCandidato = new JButton("Menu Candidato");
        JButton abrirEleicao = new JButton("Menu Eleição");

        cadastrarPartido.setBackground(new Color(0, 102, 204));
        cadastrarCandidato.setBackground(new Color(0, 102, 204));
        abrirEleicao.setBackground(new Color(0, 102, 204));

        cadastrarPartido.setForeground(Color.WHITE);
        cadastrarCandidato.setForeground(Color.WHITE);
        abrirEleicao.setForeground(Color.WHITE);

        painelMenu.add(cadastrarPartido);
        painelMenu.add(cadastrarCandidato);
        painelMenu.add(abrirEleicao);

        cadastrarPartido.addActionListener(e -> { partidoController.initPartido(); });
        cadastrarCandidato.addActionListener(e -> { candidatoController.initCandidato(); });
        abrirEleicao.addActionListener(e -> abrirTelaEleicao());

        JButton botaoVoltar = new JButton("Fechar Menu");
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.addActionListener(e -> {telaPrincipal.dispose();});
        painelMenu.add(botaoVoltar);

        painelPrincipal.add(painelMenu, "Menu Urna");
        layoutCartao.show(painelPrincipal, "Menu Urna");
    }

    public void abrirTelaEleicao() {
        JPanel painelEleicao = new JPanel();
        painelEleicao.setLayout(new BorderLayout());
        painelEleicao.setBackground(new Color(240, 240, 240));

        JLabel tituloEleicao = new JLabel("Eleições 2024 - Ano mais roubada de todas (Urna Aduterada)", SwingConstants.CENTER);
        tituloEleicao.setFont(new Font("Arial", Font.BOLD, 28));
        painelEleicao.add(tituloEleicao, BorderLayout.NORTH);

        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new GridLayout(0, 1));

        JButton botaoVisualizarResultadosTotais = new JButton("Visualizar Resultados");
        botaoVisualizarResultadosTotais.setForeground(Color.WHITE);
        botaoVisualizarResultadosTotais.setBackground(new Color(0, 102, 204));
        JButton botaoGerenciamentoUrna = new JButton("Gerenciamento Urna");
        botaoGerenciamentoUrna.setForeground(Color.WHITE);
        botaoGerenciamentoUrna.setBackground(new Color(0, 102, 204));
        JButton botaoVoltar = new JButton("Voltar ao Menu");

        painelOpcoes.add(botaoVisualizarResultadosTotais);
        painelOpcoes.add(botaoGerenciamentoUrna);

        botaoVisualizarResultadosTotais.addActionListener(e -> visualizarResultados());
        botaoGerenciamentoUrna.addActionListener(e -> gerenciamentoUrna());

        botaoVoltar.addActionListener(e -> abrirMenuUrna());
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.WHITE);

        painelEleicao.add(botaoVoltar, BorderLayout.SOUTH);
        painelEleicao.add(painelOpcoes, BorderLayout.CENTER);

        painelPrincipal.add(painelEleicao, "Tela Eleição");
        layoutCartao.show(painelPrincipal, "Tela Eleição");
    }

    public void gerenciamentoUrna() {
        JPanel painelGerenciarUrna = new JPanel();
        painelGerenciarUrna.setLayout(new BorderLayout());
        painelGerenciarUrna.setBackground(new Color(240, 240, 240));

        JLabel tituloEleicao = new JLabel("Eleições 2024 - Ano mais roubado de todos (Urna Adulterada)", SwingConstants.CENTER);
        tituloEleicao.setFont(new Font("Arial", Font.BOLD, 28));
        painelGerenciarUrna.add(tituloEleicao, BorderLayout.NORTH);

        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new GridLayout(0, 1));

        JLabel labelDataHoraInicio = new JLabel("Data e Hora de Início (dd/MM/yyyy HH:mm):");
        JTextField campoDataHoraInicio = new JTextField(16);
        painelOpcoes.add(labelDataHoraInicio);
        painelOpcoes.add(campoDataHoraInicio);

        JLabel labelDataHoraFim = new JLabel("Data e Hora de Fim (dd/MM/yyyy HH:mm):");
        JTextField campoDataHoraFim = new JTextField(16);
        painelOpcoes.add(labelDataHoraFim);
        painelOpcoes.add(campoDataHoraFim);

        JButton botaoInicarVotacao = new JButton("Iniciar Votação");
        JButton botaoFinalizarVotacao = new JButton("Finalizar Votação");
        JButton botaoVoltar = new JButton("Voltar ao Menu");

        painelOpcoes.add(botaoInicarVotacao);
        painelOpcoes.add(botaoFinalizarVotacao);

        botaoInicarVotacao.addActionListener(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dataHoraInicio = LocalDateTime.parse(campoDataHoraInicio.getText(), formatter);
            dataHoraFim = LocalDateTime.parse(campoDataHoraFim.getText(), formatter);
            isFinalizada = false;
            JOptionPane.showMessageDialog(null, "Votação iniciada em: " + dataHoraInicio.format(formatter));
            JOptionPane.showMessageDialog(null, "Votação finalizada em: " + dataHoraFim.format(formatter));
        });
        botaoInicarVotacao.setBackground(new Color(144, 238, 144));

        botaoFinalizarVotacao.addActionListener(e -> {
            isFinalizada = true;
        });
        botaoFinalizarVotacao.setBackground(new Color(255, 102, 102));

        botaoVoltar.addActionListener(e -> abrirMenuUrna());
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.WHITE);

        painelGerenciarUrna.add(botaoVoltar, BorderLayout.SOUTH);
        painelGerenciarUrna.add(painelOpcoes, BorderLayout.CENTER);

        painelPrincipal.add(painelGerenciarUrna, "Tela Eleição");
        layoutCartao.show(painelPrincipal, "Tela Eleição");
    }

    public void abrirUrnaVotacao() {
        JPanel painelUrnaVotacao = new JPanel();
        painelUrnaVotacao.setLayout(new BorderLayout());
        painelUrnaVotacao.setBackground(new Color(240, 240, 240));

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(4, 3, 10, 10));

        JPanel painelCandidato = new JPanel();
        painelCandidato.setLayout(new BoxLayout(painelCandidato, BoxLayout.Y_AXIS));
        painelCandidato.setBorder(BorderFactory.createTitledBorder("Dados do Candidato"));
        painelCandidato.setBackground(new Color(255, 255, 255));

        JLabel cargoCandidatoAtual = new JLabel("Cargo atual: ");
        JLabel cargoCandidato = new JLabel("Para ");
        JLabel nomeCandidato = new JLabel("Nome: ");
        JLabel partidoCandidato = new JLabel("Partido: ");
        JLabel numeroCandidato = new JLabel("Número: ");
        JLabel imagemCandidato = new JLabel();

        painelCandidato.add(cargoCandidatoAtual);
        painelCandidato.add(cargoCandidato);
        painelCandidato.add(nomeCandidato);
        painelCandidato.add(partidoCandidato);
        painelCandidato.add(numeroCandidato);
        painelCandidato.add(imagemCandidato);

        painelUrnaVotacao.add(painelCandidato, BorderLayout.WEST);

        JTextField campoTexto = new JTextField(10);
        campoTexto.setFont(new Font("Arial", Font.PLAIN, 18));
        campoTexto.setHorizontalAlignment(JTextField.CENTER);

        painelUrnaVotacao.add(campoTexto, BorderLayout.NORTH);

        String[][][] candidatosPorCargo = {
                {
                        {"33001", "Zé Sapão", "Movimento dos Sem Tatuagem (MST)", "src/main/resources/candidatos/governador/ZeSapao.png", "Governador"},
                        {"70001", "José Buscossos", "Partido dos Macacos Politicamente Corretos (PMPC)", "src/main/resources/candidatos/governador/JoseBuscossos.png", "Governador"},
                        {"25001", "Josefino Esquizofrenius", "Partido dos Sem Wi-Fi (PSW)", "src/main/resources/candidatos/governador/JosefinoEsquizofrenius.png", "Governador"},
                        {"45001", "Clyde Cheirasfalto", "Movimento dos Vagabundos (MDV)", "src/main/resources/candidatos/governador/ClydeCheirasfalto.png", "Governador"}
                },
                {
                        {"33002", "Lincon Caco", "Movimento dos Sem Tatuagem (MST)", "src/main/resources/candidatos/deputadoEstadual/LinconCaco.png", "Deputado Estadual"},
                        {"70002", "Livia Pepinos", "Partido dos Macacos Politicamente Corretos (PMPC)", "src/main/resources/candidatos/deputadoEstadual/LiviaPepinos.png", "Deputado Estadual"},
                        {"25002", "Mohamed Mahafá", "Partido dos Sem Wi-Fi (PSW)", "src/main/resources/candidatos/deputadoEstadual/MohamedMahafa.png", "Deputado Estadual"},
                        {"45002", "Sheetosvisdo Silva", "Movimento dos Vagabundos (MDV)", "src/main/resources/candidatos/deputadoEstadual/SheetosvisdoSilva.png", "Deputado Estadual"}
                },
                {
                        {"33003", "Carlos Primatus", "Movimento dos Sem Tatuagem (MST)", "src/main/resources/candidatos/senador/CarlosPrimatus.png", "Senador"},
                        {"70003", "Dog Shelby", "Partido dos Macacos Politicamente Corretos (PMPC)", "src/main/resources/candidatos/senador/DogShelby.png", "Senador"},
                        {"25003", "Fernando Mangabeira", "Partido dos Sem Wi-Fi (PSW)", "src/main/resources/candidatos/senador/FernandoMangabeira.png", "Senador"},
                        {"45003", "Humberto Linguinha", "Movimento dos Vagabundos (MDV)", "src/main/resources/candidatos/senador/HumbertoLinguinha.png", "Senador"}
                },
                {
                        {"3301", "Leonardo Sorrisinho", "Movimento dos Sem Tatuagem (MST)", "src/main/resources/candidatos/deputadoFederal/LeonardoSorrisinho.png", "Deputado Federal"},
                        {"7001", "Lião Cabecinha", "Partido dos Macacos Politicamente Corretos (PMPC)", "src/main/resources/deputadoFederal/senador/LiaoCabecinha.png", "Deputado Federal"},
                        {"2501", "Thiago Tremedeira", "Partido dos Sem Wi-Fi (PSW)", "src/main/resources/candidatos/deputadoFederal/ThiagoTremedeira.png", "Deputado Federal"},
                        {"4501", "Victor Pensantus", "Movimento dos Vagabundos (MDV)", "src/main/resources/candidatos/deputadoFederal/VictorPensantus.png", "Deputado Federal"}
                },
                {
                        {"33", "Adamastor Xinim", "Movimento dos Sem Tatuagem (MST)", "src/main/resources/candidatos/presidente/AdamastorXinim.png", "Presidente"},
                        {"70", "Alberto Tigroni", "Partido dos Macacos Politicamente Corretos (PMPC)", "src/main/resources/candidatos/presidente/AlbertoTigroni.png", "Presidente"},
                        {"25", "Capi Jhonson", "Partido dos Sem Wi-Fi (PSW)", "src/main/resources/candidatos/presidente/CapiJhonson.png", "Presidente"},
                        {"45", "Kaike Queijovisk", "Movimento dos Vagabundos (MDV)", "src/main/resources/candidatos/presidente/KaikeQueijovisk.png", "Presidente"}
                }
        };

        Map<String, Integer> votosValidos = new HashMap<>();
        Map<String, Integer> votosNulos = new HashMap<>();
        Map<String, Integer> votosBrancos = new HashMap<>();
        final int[] totalVotos = {0};
        final int[] cargoAtual = {0};

        Runnable atualizarCandidato = () -> {
            if (cargoAtual[0] < candidatosPorCargo.length) {
                String[][] candidatos = candidatosPorCargo[cargoAtual[0]];
                String codigo = campoTexto.getText();
                boolean candidatoEncontrado = false;

                cargoCandidatoAtual.setText("Cargo atual: " + candidatos[0][4]);

                for (String[] candidato : candidatos) {
                    if (codigo.equals(candidato[0])) {
                        cargoCandidato.setText("Para " + candidato[4]);
                        nomeCandidato.setText("Nome: " + candidato[1]);
                        partidoCandidato.setText("Partido: " + candidato[2]);
                        numeroCandidato.setText("Número: " + candidato[0]);
                        imagemCandidato.setIcon(new ImageIcon(candidato[3]));
                        candidatoEncontrado = true;
                        break;
                    }
                }
                if (!candidatoEncontrado) {
                    nomeCandidato.setText("Nome: ");
                    partidoCandidato.setText("Partido: ");
                    numeroCandidato.setText("Número: ");
                    imagemCandidato.setIcon(null);
                }
            }
        };
        atualizarCandidato.run();
        campoTexto.addActionListener(e -> atualizarCandidato.run());

        for (int i = 1; i <= 9; i++) {
            JButton botaoNumero = new JButton(String.valueOf(i));
            botaoNumero.setBackground(new Color(0, 102, 204));
            botaoNumero.setForeground(Color.WHITE);
            botaoNumero.addActionListener(e -> {
                campoTexto.setText(campoTexto.getText() + botaoNumero.getText());
                atualizarCandidato.run();
                tocaSomUtils.tocarSom("SomTecla.wav");
            });
            painelBotoes.add(botaoNumero);
        }

        JButton botaoZero = new JButton("0");
        botaoZero.setBackground(new Color(0, 102, 204));
        botaoZero.setForeground(Color.WHITE);
        botaoZero.addActionListener(e -> {campoTexto.setText(campoTexto.getText() + "0"); atualizarCandidato.run(); tocaSomUtils.tocarSom("SomTecla.wav");});
        painelBotoes.add(botaoZero);

        painelUrnaVotacao.add(painelBotoes, BorderLayout.CENTER);

        JPanel painelAcoes = new JPanel();
        painelAcoes.setLayout(new GridLayout(3, 1, 10, 10));

        JButton botaoConfirmar = new JButton("Confirmar");
        JButton botaoAnular = new JButton("Anular");
        JButton botaoBranco = new JButton("Branco");

        botaoConfirmar.setBackground(new Color(144, 238, 144));
        botaoAnular.setBackground(new Color(255, 102, 102));
        botaoBranco.setBackground(new Color(255, 255, 153));

        botaoConfirmar.addActionListener(e -> {
            String codigo = campoTexto.getText();

            if (codigo != null || !codigo.equals("")) {
                if (cargoAtual[0] < candidatosPorCargo.length) {
                    String[][] candidatos = candidatosPorCargo[cargoAtual[0]];
                    boolean candidatoEncontrado = false;
                    for (String[] candidato : candidatos) {
                        if (codigo.equals(  candidato[0])) {
                            votosPorCandidato.put(candidato[1], votosPorCandidato.getOrDefault(candidato[1], 0) + 1);

                            JOptionPane.showMessageDialog(painelUrnaVotacao, "Voto confirmado para " + candidato[1]);

                            votosValidos.put(candidato[1], votosValidos.getOrDefault(candidato[1], 0) + 1);
                            candidatoEncontrado = true;
                            totalVotos[0]++;
                            break;
                        }
                    }
                    if (!candidatoEncontrado) {
                        JOptionPane.showMessageDialog(painelUrnaVotacao, "Candidato não encontrado.");
                    }

                    if (candidatoEncontrado) {
                        totalVotosUrna += 1;
                        cargoAtual[0]++;
                        if (cargoAtual[0] < candidatosPorCargo.length) {
                            campoTexto.setText("");
                            atualizarCandidato.run();
                            tocaSomUtils.tocarSom("SomVotar.wav");
                        } else {
                            totalEleitoresVotos += 1;
                            telaEleicao.dispose();
                            initElicao();;
                        }
                    }
                }
            }
        });

        botaoAnular.addActionListener(e -> {
            totalVotosNulos += 1;
            totalVotosUrna += 1;
            votosNulos.put("Nulo", votosNulos.getOrDefault("Nulo", 0) + 1);
            JOptionPane.showMessageDialog(painelUrnaVotacao, "Voto anulado.");

            cargoAtual[0]++;
            if (cargoAtual[0] < candidatosPorCargo.length) {
                campoTexto.setText("");
                atualizarCandidato.run();
                tocaSomUtils.tocarSom("SomVotar.wav");
            } else {
                totalEleitoresVotos += 1;
                telaEleicao.dispose();
                initElicao();
            }
        });

        botaoBranco.addActionListener(e -> {
            totalVotosBrancos += 1;
            totalVotosUrna += 1;
            votosBrancos.put("Branco", votosBrancos.getOrDefault("Branco", 0) + 1);
            JOptionPane.showMessageDialog(painelUrnaVotacao, "Voto em branco.");

            cargoAtual[0]++;
            if (cargoAtual[0] < candidatosPorCargo.length) {
                campoTexto.setText("");
                atualizarCandidato.run();
                tocaSomUtils.tocarSom("SomVotar.wav");
            } else {
                totalEleitoresVotos += 1;
                telaEleicao.dispose();
                initElicao();
            }
        });

        painelAcoes.add(botaoConfirmar);
        painelAcoes.add(botaoAnular);
        painelAcoes.add(botaoBranco);

        painelUrnaVotacao.add(painelAcoes, BorderLayout.SOUTH);

        painelEleicao.add(painelUrnaVotacao, "Votação");
        layoutCartaoEleicao.show(painelEleicao, "Votação");
    }

    public void visualizarResultados() {
        if (dataHoraFim == null) {
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Data de início e Data de fim da votação ainda não foram definidas.");
            JOptionPane.showMessageDialog(painelEleicao, mensagem.toString(), "Erro", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder resultados = new StringBuilder();
            DateTimeFormatter formatterBrasil = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Adiciona informações de data ao relatório
            resultados.append("Data da geração do relatório: ").append(LocalDateTime.now().format(formatterBrasil)).append("\n");
            resultados.append("Data do início da votação: ").append(dataHoraInicio.format(formatterBrasil)).append("\n");
            resultados.append("Data do fim da votação: ").append(dataHoraFim.format(formatterBrasil)).append("\n");

            // Resultados de eleições majoritárias
            resultados.append("\nResultados da Eleição Majoritária\n\n");
            for (Map.Entry<String, Integer> entry : votosPorCandidato.entrySet()) {
                resultados.append("Candidato: ").append(entry.getKey()).append(" - Votos: ").append(entry.getValue()).append("\n");
            }

            // Cálculo dos resultados proporcionais
            resultados.append("\nResultados da Eleição Proporcional\n\n");
            int totalVotos = totalVotosUrna - totalVotosBrancos - totalVotosNulos;
            for (Map.Entry<String, Integer> entry : votosPorCandidato.entrySet()) {
                String candidato = entry.getKey();
                int votosCandidato = entry.getValue();

                // Calcula a proporção de votos do candidato em relação ao total
                double proporcao = (double) votosCandidato / totalVotos * 100;
                resultados.append("Candidato: ").append(candidato).append(" - Votos: ").append(votosCandidato).append(" - Proporção: ")
                        .append(String.format("%.2f", proporcao)).append("%\n");
            }

            // Adiciona informações sobre votos brancos, nulos e total de votos
            resultados.append("\nVotos Brancos: ").append(totalVotosBrancos).append("\n");
            resultados.append("Votos Nulos: ").append(totalVotosNulos).append("\n");resultados.append("Total de Votos: ").append(totalVotosUrna);

            // Exibe o relatório em uma caixa de diálogo
            JOptionPane.showMessageDialog(telaPrincipal, resultados.toString(), "Relatório (urna adulterada)", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

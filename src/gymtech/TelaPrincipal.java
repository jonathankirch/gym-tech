package gymtech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaPrincipal extends JFrame {

    private JPanel painelConteudo;
    private CardLayout cardLayout;
    private String usuarioLogado;
    private String perfilLogado;
    private JMenuItem mUsuarios;
    private TelaMensalidade telaMensalidades;
    private TelaDashboard telaDashboard;
    private TelaMatricula telaMatricula;

    // cores
    static final Color COR_FUNDO = new Color(14, 27, 42);
    static final Color COR_SIDEBAR = new Color(10, 20, 31);
    static final Color COR_PAINEL = new Color(18, 34, 53);
    static final Color COR_BOTAO = new Color(122, 140, 46);
    static final Color COR_AZUL = new Color(42, 78, 108);
    static final Color COR_TEXTO = new Color(232, 226, 214);
    static final Color COR_CINZA = new Color(160, 152, 136);
    static final Color COR_BORDA = new Color(42, 78, 108);

    public TelaPrincipal(String usuario, String perfil) {
        this.usuarioLogado = usuario;
        this.perfilLogado = perfil;

        setTitle("GymTech - Sistema de Gestão");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // menu bar
        criarMenuBar();

        // layout principal
        JPanel painelGeral = new JPanel(new BorderLayout());
        painelGeral.setBackground(COR_FUNDO);
        setContentPane(painelGeral);

        // sidebar à esquerda
        JPanel sidebar = criarSidebar();
        painelGeral.add(sidebar, BorderLayout.WEST);

        // área do conteúdo principal
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(COR_PAINEL);

        telaDashboard = new TelaDashboard();
        painelConteudo.add(telaDashboard, "dashboard");
        painelConteudo.add(new TelaAluno(), "alunos");
        painelConteudo.add(new TelaPlano(), "planos");
        telaMensalidades = new TelaMensalidade(telaDashboard);
        painelConteudo.add(telaMensalidades, "mensalidades");
        telaMatricula = new TelaMatricula(telaDashboard);
        painelConteudo.add(telaMatricula, "matriculas");
        painelConteudo.add(new TelaRelatorio(), "relatorios");

        painelGeral.add(painelConteudo, BorderLayout.CENTER);

        // barra de status no rodapé
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(COR_SIDEBAR);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA));
        rodape.setPreferredSize(new Dimension(0, 28));

        JLabel lblRodape = new JLabel("  GymTech v2.0  |  Usuário: " + usuarioLogado);
        lblRodape.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRodape.setForeground(COR_CINZA);
        rodape.add(lblRodape, BorderLayout.WEST);

        painelGeral.add(rodape, BorderLayout.SOUTH);
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(COR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COR_BORDA));

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        // logo no topo da sidebar
        JLabel lblLogo = new JLabel("GymTech", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(COR_TEXTO);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(24, 10, 6, 10));
        topo.add(lblLogo);

        JLabel lblSub = new JLabel("Gestão de Academia", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 10));
        lblSub.setForeground(COR_CINZA);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        topo.add(lblSub);

        sidebar.add(Box.createVerticalStrut(18));

        JSeparator sep = new JSeparator();
        sep.setForeground(COR_BORDA);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        topo.add(sep);

        topo.add(Box.createVerticalStrut(10));

        // botões de navegação
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        menu.add(criarBotaoMenu("Dashboard", "dashboard"));
        menu.add(Box.createVerticalStrut(6));
        menu.add(criarBotaoMenu("Alunos", "alunos"));
        menu.add(Box.createVerticalStrut(6));
        menu.add(criarBotaoMenu("Planos", "planos"));
        menu.add(Box.createVerticalStrut(6));
        menu.add(criarBotaoMenu("Matrículas", "matriculas"));
        menu.add(Box.createVerticalStrut(6));
        menu.add(criarBotaoMenu("Mensalidades", "mensalidades"));
        menu.add(Box.createVerticalStrut(6));
        menu.add(criarBotaoMenu("Relatórios", "relatorios"));
        menu.add(Box.createVerticalGlue());

        // botão sair
        JButton btnSair = new JButton("  Sair do Sistema");
        btnSair.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSair.setForeground(new Color(200, 80, 60));
        btnSair.setBackground(COR_SIDEBAR);
        btnSair.setBorderPainted(false);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setHorizontalAlignment(SwingConstants.LEFT);
        btnSair.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(
                        TelaPrincipal.this,
                        "Deseja sair do sistema?",
                        "Sair",
                        JOptionPane.YES_NO_OPTION
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    dispose();
                    new TelaLogin().setVisible(true);
                }
            }
        });
        sidebar.add(btnSair, BorderLayout.SOUTH);
        sidebar.add(topo, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);

        return sidebar;
    }

    private JButton criarBotaoMenu(String texto, String pagina) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(COR_CINZA);
        btn.setBackground(COR_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COR_AZUL);
                btn.setForeground(COR_TEXTO);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(COR_SIDEBAR);
                btn.setForeground(COR_CINZA);
            }
        });

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("mensalidades".equals(pagina)) {
                    telaMensalidades.atualizarDados();
                }
                if ("matriculas".equals(pagina)) {
                    telaMatricula.carregarTabelaFiltrada();
                }
                cardLayout.show(painelConteudo, pagina);
            }
        });
        return btn;
    }

    private void criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COR_SIDEBAR);

        JMenu mArquivo = new JMenu("Arquivo");
        mArquivo.setForeground(COR_TEXTO);
        mArquivo.add(criarItem("Sair", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TelaLogin().setVisible(true);
            }
        }));

        JMenu mCadastros = new JMenu("Cadastros");
        mCadastros.setForeground(COR_TEXTO);
        mCadastros.add(criarItem("Alunos", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(painelConteudo, "alunos");
            }
        }));
        mCadastros.add(criarItem("Planos", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(painelConteudo, "planos");
            }
        }));
        mCadastros.add(criarItem("Matrículas", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(painelConteudo, "matriculas");
            }
        }));

        JMenu mFinanceiro = new JMenu("Financeiro");
        mFinanceiro.setForeground(COR_TEXTO);
        mFinanceiro.add(criarItem("Mensalidades", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(painelConteudo, "mensalidades");
            }
        }));

        JMenu mAjuda = new JMenu("Ajuda");
        mAjuda.setForeground(COR_TEXTO);
        mAjuda.add(criarItem("Sobre", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(TelaPrincipal.this,
                        "GymTech - Sistema de Gestão de Academia\nVersão 2.0\n\nDesenvolvido em Java Swing",
                        "Sobre", JOptionPane.INFORMATION_MESSAGE);
            }
        }));

        menuBar.add(mArquivo);
        menuBar.add(mCadastros);
        menuBar.add(mFinanceiro);
        menuBar.add(mAjuda);

        setJMenuBar(menuBar);
    }

    private JMenuItem criarItem(String texto, ActionListener acao) {
        JMenuItem item = new JMenuItem(texto);
        item.setBackground(COR_SIDEBAR);
        item.setForeground(COR_TEXTO);
        item.addActionListener(acao);
        return item;
    }
}

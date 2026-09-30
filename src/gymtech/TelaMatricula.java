package gymtech;

import gymtech.dao.AlunoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;
import gymtech.dao.MatriculaDAO;
import gymtech.dao.MensalidadeDAO;
import gymtech.entidade.Matricula;
import gymtech.util.ConexaoDB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaMatricula extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbFiltro;
    private final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JComboBox<String> cmbAluno;
    private JComboBox<String> cmbPlano;
    private JComboBox<String> cmbPeriodo;
    private List<Integer> listaIdAluno = new ArrayList<>();
    private List<Integer> listaIdPlano = new ArrayList<>();
    private TelaDashboard dashboard;
    private JTextField campoBusca;

    public TelaMatricula(TelaDashboard dashboard) {
        this.dashboard = dashboard;
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());
        cmbAluno = new JComboBox<>();
        cmbPlano = new JComboBox<>();
        carregarAlunos();
        carregarPlanos();

        // topo
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(18, 34, 53));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Controle de Matrículas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JButton btnNovo = criarBotao("Nova Matrícula", new Color(122, 140, 46));
        JButton btnRenovar = criarBotao("Renovar Matrícula", new Color(42, 78, 108));
        JButton btnCancelar = criarBotao("Cancelar Matrícula", new Color(180, 60, 50));

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirNovaMatricula();
            }
        });

        btnRenovar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma matrícula.");
                return;
            }

            abrirRenovacao(linha);
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione uma matrícula.");
                    return;
                }
                int idMatricula = (int) modelo.getValueAt(linha, 0);
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Deseja cancelar esta matrícula?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    MatriculaDAO daoMat = new MatriculaDAO();
                    MensalidadeDAO daoMen = new MensalidadeDAO();
                    daoMat.cancelarMatricula(idMatricula);
                    daoMen.cancelarMensalidadesPorMatricula(idMatricula);
                    carregarTabelaFiltrada();
                    JOptionPane.showMessageDialog(null, "Matrícula cancelada com sucesso!");
                }
            }
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnRenovar);
        painelBotoes.add(btnCancelar);
        painelTopo.add(painelBotoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelFiltros.setBackground(new Color(18, 34, 53));
        painelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel lblStatus = new JLabel("Filtrar por status:");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(160, 152, 136));
        painelFiltros.add(lblStatus);

        String[] opcoes = {"Todos", "Ativa", "Encerrada", "Cancelada"};
        cmbFiltro = new JComboBox<>(opcoes);
        cmbFiltro.setBackground(new Color(14, 27, 42));
        cmbFiltro.setForeground(new Color(232, 226, 214));
        painelFiltros.add(cmbFiltro);

        cmbFiltro.addActionListener(e -> carregarTabelaFiltrada());

        JLabel lblBusca = new JLabel("Buscar Aluno:");
        lblBusca.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBusca.setForeground(new Color(160, 152, 136));
        painelFiltros.add(lblBusca);

        campoBusca = criarCampo();
        campoBusca.setPreferredSize(new Dimension(180, 28));
        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                carregarTabelaFiltrada();
            }
        });
        painelFiltros.add(campoBusca);

        add(painelFiltros, BorderLayout.AFTER_LAST_LINE);

        // tabela
        String[] colunas = {"ID", "Aluno", "Plano", "Início", "Fim", "Status"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };
        tabela = new JTable(modelo);
        tabela.setAutoCreateRowSorter(true);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2
                        && tabela.getSelectedRow() != -1) {
                    abrirRenovacao(
                            tabela.getSelectedRow()
                    );
                }
            }
        });

        tabela.setBackground(new Color(22, 42, 61));
        tabela.setForeground(new Color(232, 226, 214));
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setGridColor(new Color(42, 78, 108));
        tabela.setSelectionBackground(new Color(42, 78, 108));
        tabela.setSelectionForeground(new Color(232, 226, 214));

        tabela.getTableHeader().setBackground(new Color(14, 27, 42));
        tabela.getTableHeader().setForeground(new Color(122, 140, 46));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(new Color(18, 34, 53));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(22, 42, 61));

        add(scroll, BorderLayout.CENTER);
        carregarTabelaFiltrada();
    }

    private void abrirNovaMatricula() {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Nova Matrícula",
                true
        );

        dialog.setSize(550, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cmbAluno = new JComboBox<>();
        cmbAluno.setBackground(new Color(14, 27, 42));
        cmbAluno.setForeground(new Color(232, 226, 214));
        cmbPlano = new JComboBox<>();
        cmbPlano.setBackground(new Color(14, 27, 42));
        cmbPlano.setForeground(new Color(232, 226, 214));

        carregarAlunos();
        carregarPlanos();

        JTextField tfInicio = criarCampo();
        JTextField tfFim = criarCampo();

        tfInicio.setText(LocalDate.now().format(FORMATADOR));
        tfFim.setEditable(false);

        String[] periodos = {"Mensal", "Trimestral", "Semestral", "Anual", "Aula Avulsa"};
        cmbPeriodo = new JComboBox<>(periodos);
        cmbPeriodo.setBackground(new Color(14, 27, 42));
        cmbPeriodo.setForeground(new Color(232, 226, 214));

        cmbPeriodo.addActionListener(e -> {
            try {
                LocalDate inicio = LocalDate.parse(tfInicio.getText().trim(), FORMATADOR);
                String periodo = cmbPeriodo.getSelectedItem().toString();
                int meses;
                LocalDate fim;
                if (periodo.equals("Aula Avulsa")) {
                    meses = 0;
                    fim = inicio;
                } else {
                    meses = mesesDoPeriodo(periodo);
                    fim = inicio.plusMonths(meses);
                }
                tfFim.setText(fim.format(FORMATADOR));
            } catch (Exception ex) {
                tfFim.setText("");
            }
        });

        cmbPeriodo.setSelectedIndex(0);
        tfFim.setText(
                LocalDate.now().plusMonths(1).format(FORMATADOR)
        );

        form.add(criarLabel("Aluno (ID):"));
        form.add(cmbAluno);

        form.add(criarLabel("Plano (ID):"));
        form.add(cmbPlano);

        form.add(criarLabel("Data de Início:"));
        form.add(tfInicio);

        form.add(criarLabel("Periodicidade:"));
        form.add(cmbPeriodo);

        form.add(criarLabel("Data de Encerramento:"));
        form.add(tfFim);

        dialog.add(form, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setBackground(new Color(14, 27, 42));

        JButton btnCancelar = criarBotao("Cancelar", new Color(42, 78, 108));
        JButton btnSalvar = criarBotao("Salvar", new Color(122, 140, 46));

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSalvar.addActionListener(e -> {

            try {
                int idAluno = listaIdAluno.get(cmbAluno.getSelectedIndex());
                int idPlano = listaIdPlano.get(cmbPlano.getSelectedIndex());

                String periodo = cmbPeriodo.getSelectedItem().toString();
                LocalDate inicio = LocalDate.parse(tfInicio.getText().trim(), FORMATADOR);

                int meses;
                LocalDate fim;

                if (periodo.equals("Aula Avulsa")) {
                    meses = 0;
                    fim = inicio; // mesmo dia
                } else {
                    meses = mesesDoPeriodo(periodo);
                    fim = inicio.plusMonths(meses);
                }
                Matricula m = new Matricula();
                m.setIdAluno(idAluno);
                m.setIdPlano(idPlano);
                m.setPeriodoMeses(meses);
                m.setDataInicio(inicio);
                m.setDataFim(fim);
                m.setStatus("Ativa");

                int idMatricula = new MatriculaDAO().inserir(m);
                dashboard.atualizarDashboard();

                BigDecimal valorPlano = buscarValorPlano(idPlano);

                int qtdParcelas = (periodo.equals("Aula Avulsa")) ? 1 : meses;

                new MensalidadeDAO().gerarMensalidadesDaMatricula(
                        idMatricula,
                        idAluno,
                        idPlano,
                        valorPlano,
                        inicio,
                        qtdParcelas,
                        periodo
                );

                JOptionPane.showMessageDialog(dialog,
                        "Matrícula criada com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarTabelaFiltrada();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao salvar: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
        dashboard.atualizarDashboard();
    }

    private void abrirRenovacao(int linha) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Renovar Matrícula",
                true
        );

        dialog.setSize(550, 320);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfAluno = criarCampo();
        JTextField tfPlano = criarCampo();
        JTextField tfInicio = criarCampo();
        JTextField tfFim = criarCampo();

        int idMatricula = (int) modelo.getValueAt(linha, 0);

        Matricula m = new MatriculaDAO()
                .listar()
                .stream()
                .filter(x -> x.getIdMatricula() == idMatricula)
                .findFirst()
                .orElse(null);

        if (m == null) {
            JOptionPane.showMessageDialog(this, "Matrícula não encontrada.");
            return;
        }

        tfAluno.setText(m.getAluno());
        tfPlano.setText(m.getPlano());

        tfAluno.setEditable(false);
        tfPlano.setEditable(false);
        tfInicio.setEditable(false);
        tfFim.setEditable(false);

        LocalDate inicio = m.getDataFim();
        int meses = m.getPeriodoMeses();
        LocalDate fim;
        if (meses == 0) {
            fim = inicio;
        } else {
            fim = inicio.plusMonths(meses);
        }

        tfInicio.setText(inicio.format(FORMATADOR));
        tfFim.setText(fim.format(FORMATADOR));

        JButton renovar = criarBotao("Renovar", new Color(122, 140, 46));
        JButton cancelar = criarBotao("Cancelar", new Color(42, 78, 108));

        cancelar.addActionListener(e -> dialog.dispose());

        renovar.addActionListener(e -> {
            try {

                MatriculaDAO dao = new MatriculaDAO();

                dao.renovarMatriculaCompleta(
                        m.getIdMatricula(),
                        inicio,
                        fim
                );

                BigDecimal valorPlano = buscarValorPlano(m.getIdPlano());

                String periodo = periodoPorMeses(meses);

                new MensalidadeDAO().gerarMensalidadesDaMatricula(
                        m.getIdMatricula(),
                        m.getIdAluno(),
                        m.getIdPlano(),
                        valorPlano,
                        inicio,
                        meses,
                        periodo
                );

                carregarTabelaFiltrada();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        form.add(criarLabel("Aluno"));
        form.add(tfAluno);

        form.add(criarLabel("Plano"));
        form.add(tfPlano);

        form.add(criarLabel("Início"));
        form.add(tfInicio);

        form.add(criarLabel("Fim"));
        form.add(tfFim);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(18, 34, 53));
        botoes.add(cancelar);
        botoes.add(renovar);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
        dashboard.atualizarDashboard();
    }

    private void carregarAlunos() {
        listaIdAluno.clear();
        cmbAluno.removeAllItems();
        String sql = "SELECT id_aluno, nome FROM aluno WHERE status='Ativo' ORDER BY nome ASC";
        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) {
                listaIdAluno.add(r.getInt("id_aluno"));
                cmbAluno.addItem(r.getString("nome"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void carregarPlanos() {
        listaIdPlano.clear();
        cmbPlano.removeAllItems();
        String sql = "SELECT id_plano, nome FROM plano WHERE status='Ativo' ORDER BY id_plano ASC";
        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) {
                listaIdPlano.add(r.getInt("id_plano"));
                cmbPlano.addItem(r.getString("nome"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private int mesesDoPeriodo(String periodo) {
        switch (periodo) {
            case "Mensal":
                return 1;
            case "Trimestral":
                return 3;
            case "Semestral":
                return 6;
            case "Anual":
                return 12;
            case "Aula Avulsa":
                return 0;
            default:
                return 1;
        }
    }

    private String periodoPorMeses(int meses) {

        switch (meses) {
            case 0:
                return "Aula Avulsa";
            case 1:
                return "Mensal";
            case 3:
                return "Trimestral";
            case 6:
                return "Semestral";
            case 12:
                return "Anual";
            default:
                return "Mensal";
        }
    }

    private BigDecimal buscarValorPlano(int idPlano) {

        String sql = "SELECT valor_mensal FROM plano WHERE id_plano = ?";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql)) {

            s.setInt(1, idPlano);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("valor_mensal");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    public void carregarTabelaFiltrada() {
        new MatriculaDAO().atualizarStatusAutomatico();
        modelo.setRowCount(0);
        String filtro = cmbFiltro.getSelectedItem().toString();
        String busca = campoBusca.getText();
        if (busca == null) {
            busca = "";
        }
        busca = busca.toLowerCase().trim();

        for (Matricula m : new MatriculaDAO().listar()) {
            boolean okStatus = filtro.equals("Todos") || m.getStatus().equalsIgnoreCase(filtro);
            boolean okBusca = busca.isEmpty() || (m.getAluno() != null && m.getAluno().toLowerCase().contains(busca));

            if (okStatus && okBusca) {
                modelo.addRow(new Object[]{
                    m.getIdMatricula(),
                    m.getAluno(),
                    m.getPlano(),
                    m.getDataInicio().format(FORMATADOR),
                    m.getDataFim().format(FORMATADOR),
                    m.getStatus()
                });
            }
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(160, 152, 136));
        return lbl;
    }

    private JTextField criarCampo() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(14, 27, 42));
        tf.setForeground(new Color(232, 226, 214));
        tf.setCaretColor(new Color(232, 226, 214));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 78, 108), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(cor);
        btn.setForeground(new Color(232, 226, 214));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }
}

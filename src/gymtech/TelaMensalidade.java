package gymtech;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import gymtech.dao.MensalidadeDAO;
import gymtech.entidade.Mensalidade;
import gymtech.util.Formatacao;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.TreeSet;

public class TelaMensalidade extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbFiltro;
    private JComboBox<String> cmbMes;
    private final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private TelaDashboard dashboard;
    private JTextField campoBusca;

    public TelaMensalidade(TelaDashboard dashboard) {
        this.dashboard = dashboard;
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(18, 34, 53));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Controle de Mensalidades");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JButton btnRegistrar = criarBotao("Registrar Pagamento", new Color(122, 140, 46));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha == -1) {
                    JOptionPane.showMessageDialog(TelaMensalidade.this,
                            "Selecione uma mensalidade.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if ("Pago".equals(modelo.getValueAt(linha, 6).toString())) {
                    JOptionPane.showMessageDialog(TelaMensalidade.this,
                            "Esta mensalidade já está paga!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                abrirRegistroPagamento(linha);
            }
        });

        painelTopo.add(btnRegistrar, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelFiltros.setBackground(new Color(18, 34, 53));
        painelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel lblStatus = new JLabel("Filtrar por status:");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(160, 152, 136));
        painelFiltros.add(lblStatus);

        String[] opcoes = {"Todos", "Pendente", "Paga", "Vencida", "Cancelada"};
        cmbFiltro = new JComboBox<>(opcoes);
        cmbFiltro.setBackground(new Color(14, 27, 42));
        cmbFiltro.setForeground(new Color(232, 226, 214));
        painelFiltros.add(cmbFiltro);

        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMes.setForeground(new Color(160, 152, 136));
        painelFiltros.add(lblMes);

        cmbMes = new JComboBox<>();

        cmbMes.setBackground(new Color(14, 27, 42));
        cmbMes.setForeground(new Color(232, 226, 214));

        painelFiltros.add(cmbMes);

        carregarMeses();
        cmbMes.setBackground(new Color(14, 27, 42));
        cmbMes.setForeground(new Color(232, 226, 214));
        painelFiltros.add(cmbMes);

        cmbFiltro.addActionListener(e -> carregarTabelaFiltrada());
        cmbMes.addActionListener(e -> carregarTabelaFiltrada());

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
        String[] colunas = {"ID", "Aluno", "Plano", "Parcela", "Valor", "Vencimento", "Status"};
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
                    case 4:
                        return java.math.BigDecimal.class;
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
                if (e.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                    int linha = tabela.getSelectedRow();
                    if ("Pago".equals(modelo.getValueAt(linha, 6).toString())) {
                        JOptionPane.showMessageDialog(
                                TelaMensalidade.this,
                                "Esta mensalidade já está paga!"
                        );
                        return;
                    }
                    abrirRegistroPagamento(linha);
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

        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(new Color(18, 34, 53));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(22, 42, 61));

        add(scroll, BorderLayout.CENTER);
        carregarTabelaFiltrada();
    }

    private void abrirRegistroPagamento(int linha) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Registrar Pagamento", true);
        dialog.setSize(340, 260);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String nomeAluno = modelo.getValueAt(linha, 1).toString();
        String valor = modelo.getValueAt(linha, 3).toString();

        JLabel lblNomeAluno = new JLabel(nomeAluno);
        lblNomeAluno.setFont(new Font("Arial", Font.BOLD, 13));
        lblNomeAluno.setForeground(new Color(232, 226, 214));

        JLabel lblValorMens = new JLabel(valor);
        lblValorMens.setFont(new Font("Arial", Font.BOLD, 13));
        lblValorMens.setForeground(new Color(122, 140, 46));

        String dataHoje = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JTextField tfData = new JTextField(dataHoje);
        tfData.setBackground(new Color(14, 27, 42));
        tfData.setForeground(new Color(232, 226, 214));
        tfData.setCaretColor(new Color(232, 226, 214));
        tfData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 78, 108), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        String[] formas = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
        JComboBox<String> cmbForma = new JComboBox<String>(formas);
        cmbForma.setBackground(new Color(14, 27, 42));
        cmbForma.setForeground(new Color(232, 226, 214));

        form.add(criarLabel("Aluno:"));
        form.add(lblNomeAluno);
        form.add(criarLabel("Valor:"));
        form.add(lblValorMens);
        form.add(criarLabel("Data de Pagamento:"));
        form.add(tfData);
        form.add(criarLabel("Forma de Pagamento:"));
        form.add(cmbForma);

        dialog.add(form, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setBackground(new Color(14, 27, 42));

        JButton btnCancelar = criarBotao("Cancelar", new Color(42, 78, 108));
        JButton btnConfirmar = criarBotao("Confirmar", new Color(122, 140, 46));

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idMensalidade
                        = Integer.parseInt(
                                modelo.getValueAt(
                                        linha,
                                        0
                                ).toString()
                        );
                MensalidadeDAO dao
                        = new MensalidadeDAO();
                boolean sucesso
                        = dao.registrarPagamento(
                                idMensalidade
                        );
                if (sucesso) {
                    atualizarDados();
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Pagamento registrado!"
                    );
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Erro ao registrar."
                    );
                }
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnConfirmar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
        dashboard.atualizarDashboard();
    }

    private void carregarTabelaFiltrada() {
        new MensalidadeDAO().atualizarMensalidadesVencidas();
        modelo.setRowCount(0);

        Object statusSelecionado = cmbFiltro.getSelectedItem();
        Object mesSelecionado = cmbMes.getSelectedItem();

        if (statusSelecionado == null || mesSelecionado == null) {
            return;
        }

        String status = statusSelecionado.toString();
        String mes = mesSelecionado.toString();
        String busca = campoBusca.getText();
        if (busca == null) {
            busca = "";
        }
        busca = busca.toLowerCase().trim();

        MensalidadeDAO dao = new MensalidadeDAO();

        for (Mensalidade m : dao.listar()) {

            boolean okStatus
                    = status.equals("Todos")
                    || m.getStatus().equalsIgnoreCase(status);

            boolean okMes = true;

            if (!mes.equals("Todos")) {

                LocalDate data = m.getVencimento();

                String textoMes
                        = data.getMonth()
                                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

                textoMes
                        = textoMes.substring(0, 1).toUpperCase()
                        + textoMes.substring(1)
                        + "/"
                        + data.getYear();

                okMes = textoMes.equals(mes);
            }

            boolean okBusca = busca.isEmpty() || (m.getAluno() != null && m.getAluno().toLowerCase().contains(busca));

            if (okStatus && okMes && okBusca) {
                modelo.addRow(new Object[]{
                    m.getIdMensalidade(),
                    m.getAluno(),
                    m.getPlano(),
                    m.getParcela() + "/" + m.getTotalParcelas(),
                    Formatacao.formatarMoeda(m.getValor()),
                    m.getVencimento().format(FORMATADOR),
                    m.getStatus()
                });
            }
        }
    }

    private void carregarMeses() {
        cmbMes.removeAllItems();
        cmbMes.addItem("Todos");
        TreeSet<LocalDate> meses
                = new TreeSet<>();
        MensalidadeDAO dao
                = new MensalidadeDAO();
        for (Mensalidade m : dao.listar()) {
            LocalDate data
                    = LocalDate.parse(
                            m.getVencimento().toString()
                    );
            meses.add(
                    data.withDayOfMonth(1)
            );
        }
        for (LocalDate data : meses) {
            String nomeMes
                    = data.getMonth()
                            .getDisplayName(
                                    TextStyle.FULL,
                                    new Locale("pt", "BR")
                            );
            String opcao
                    = nomeMes.substring(0, 1).toUpperCase()
                    + nomeMes.substring(1)
                    + "/"
                    + data.getYear();
            cmbMes.addItem(opcao);
        }
        cmbMes.setSelectedIndex(0);
    }

    public void atualizarDados() {
        carregarMeses();
        carregarTabelaFiltrada();
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(160, 152, 136));
        return lbl;
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
}

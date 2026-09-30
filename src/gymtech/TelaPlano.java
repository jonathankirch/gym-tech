package gymtech;

import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import gymtech.dao.PlanoDAO;
import gymtech.entidade.Plano;
import gymtech.util.Formatacao;
import java.time.format.DateTimeFormatter;

public class TelaPlano extends JPanel {

    private Plano planoSelecionado = null;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaPlano() {
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(18, 34, 53));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Gerenciar Planos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setOpaque(false);

        JButton btnNovo = criarBotao("Novo Plano", new Color(122, 140, 46));
        JButton btnEditar = criarBotao("Editar", new Color(42, 78, 108));
        JButton btnExcluir = criarBotao("Excluir", new Color(180, 60, 50));

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormulario();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabela.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(TelaPlano.this,
                            "Selecione um plano para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                abrirFormularioEditar();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha == -1) {
                    JOptionPane.showMessageDialog(TelaPlano.this,
                            "Selecione um plano para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int resposta = JOptionPane.showConfirmDialog(TelaPlano.this,
                        "Excluir o plano selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    int id = (int) modelo.getValueAt(linha, 0);
                    PlanoDAO dao = new PlanoDAO();
                    dao.excluir(id);
                    carregarPlanos();
                }
            }
        });

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelTopo.add(painelBotoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // tabela
        String[] colunas = {"ID", "Nome", "Valor", "Dias de Acesso", "Benefícios", "Status"};
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
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {

                int row = tabela.getSelectedRow();

                planoSelecionado = new Plano();

                planoSelecionado.setIdPlano((int) modelo.getValueAt(row, 0));
                planoSelecionado.setNome(modelo.getValueAt(row, 1).toString());
                planoSelecionado.setValor(Formatacao.limparMoeda(modelo.getValueAt(row, 2).toString()));
                planoSelecionado.setDiasAcesso(modelo.getValueAt(row, 3).toString());
                planoSelecionado.setBeneficios(modelo.getValueAt(row, 4).toString());
                planoSelecionado.setStatus(modelo.getValueAt(row, 5).toString());
            }
        });
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int row = tabela.getSelectedRow();

                    if (row != -1) {

                        planoSelecionado = new Plano();
                        planoSelecionado.setIdPlano((int) modelo.getValueAt(row, 0));
                        planoSelecionado.setNome(modelo.getValueAt(row, 1).toString());
                        planoSelecionado.setValor(Formatacao.limparMoeda(modelo.getValueAt(row, 2).toString()));
                        planoSelecionado.setDiasAcesso(modelo.getValueAt(row, 3).toString());
                        planoSelecionado.setBeneficios(modelo.getValueAt(row, 4).toString());
                        planoSelecionado.setStatus(modelo.getValueAt(row, 5).toString());

                        abrirFormularioEditar();
                    }
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

        tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(new Color(18, 34, 53));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(22, 42, 61));

        add(scroll, BorderLayout.CENTER);
        carregarPlanos();
    }

    private void abrirFormulario() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Cadastro de Plano", true);
        dialog.setSize(380, 340);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfNome = criarCampo();
        JTextField tfValor = criarCampo();
        Formatacao.adicionarMascaraMoeda(tfValor);
        JTextField tfBenef = criarCampo();

        String[] dias = {"2x por semana", "3x por semana", "4x por semana", "5x por semana", "Livre", "Treino único"};
        JComboBox<String> cmbDias = new JComboBox<String>(dias);
        cmbDias.setBackground(new Color(14, 27, 42));
        cmbDias.setForeground(new Color(232, 226, 214));

        String[] statusOpcoes = {"Ativo", "Inativo"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOpcoes);
        cmbStatus.setBackground(new Color(14, 27, 42));
        cmbStatus.setForeground(new Color(232, 226, 214));

        form.add(criarLabel("Nome do Plano:"));
        form.add(tfNome);
        form.add(criarLabel("Valor (R$):"));
        form.add(tfValor);
        form.add(criarLabel("Dias de Acesso:"));
        form.add(cmbDias);
        form.add(criarLabel("Benefícios:"));
        form.add(tfBenef);
        form.add(criarLabel("Status:"));
        form.add(cmbStatus);

        dialog.add(form, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setBackground(new Color(14, 27, 42));

        JButton btnCancelar = criarBotao("Cancelar", new Color(42, 78, 108));
        JButton btnSalvar = criarBotao("Salvar", new Color(122, 140, 46));

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {

                    if (tfNome.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Nome é obrigatório!",
                                "Atenção",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    Plano plano = new Plano();
                    plano.setNome(tfNome.getText());
                    plano.setValor(
                            Formatacao.limparMoeda(tfValor.getText())
                    );
                    plano.setDiasAcesso(
                            cmbDias.getSelectedItem().toString()
                    );
                    plano.setBeneficios(
                            tfBenef.getText()
                    );
                    plano.setStatus(cmbStatus.getSelectedItem().toString());
                    PlanoDAO dao = new PlanoDAO();
                    dao.salvar(plano);
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Plano cadastrado com sucesso!"
                    );
                    carregarPlanos();
                    dialog.dispose();
                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Erro ao salvar plano:\n" + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void carregarPlanos() {
        modelo.setRowCount(0);
        PlanoDAO dao = new PlanoDAO();
        for (Plano plano : dao.listar()) {
            modelo.addRow(new Object[]{
                plano.getIdPlano(),
                plano.getNome(),
                Formatacao.formatarMoeda(plano.getValor()),
                plano.getDiasAcesso(),
                plano.getBeneficios(),
                plano.getStatus()
            });
        }
    }

    private void abrirFormularioEditar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Plano", true);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setSize(380, 340);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField tfNome = criarCampo();
        tfNome.setText(planoSelecionado.getNome());
        JTextField tfValor = criarCampo();
        Formatacao.adicionarMascaraMoeda(tfValor);
        tfValor.setText(Formatacao.formatarMoeda(planoSelecionado.getValor()));
        JTextField tfBenef = criarCampo();
        tfBenef.setText(planoSelecionado.getBeneficios());

        String[] dias = {"Aula avulsa", "2x por semana", "3x por semana", "4x por semana", "5x por semana", "Livre"};
        JComboBox<String> cmbDias = new JComboBox<String>(dias);
        cmbDias.setBackground(new Color(14, 27, 42));
        cmbDias.setForeground(new Color(232, 226, 214));
        cmbDias.setSelectedItem(planoSelecionado.getDiasAcesso());

        String[] statusOpcoes = {"Ativo", "Inativo"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOpcoes);
        cmbStatus.setBackground(new Color(14, 27, 42));
        cmbStatus.setForeground(new Color(232, 226, 214));
        cmbStatus.setSelectedItem(planoSelecionado.getStatus());

        form.add(criarLabel("Nome do Plano:"));
        form.add(tfNome);
        form.add(criarLabel("Valor (R$):"));
        form.add(tfValor);
        form.add(criarLabel("Dias de Acesso:"));
        form.add(cmbDias);
        form.add(criarLabel("Benefícios:"));
        form.add(tfBenef);
        form.add(criarLabel("Status:"));
        form.add(cmbStatus);

        dialog.add(form, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setBackground(new Color(14, 27, 42));

        JButton btnCancelar = criarBotao("Cancelar", new Color(42, 78, 108));
        JButton btnSalvar = criarBotao("Salvar", new Color(122, 140, 46));

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btnSalvar.addActionListener(e -> {
            Plano plano = new Plano();
            plano.setIdPlano(planoSelecionado.getIdPlano());
            plano.setNome(tfNome.getText());
            plano.setValor(Formatacao.limparMoeda(tfValor.getText()));
            plano.setDiasAcesso(cmbDias.getSelectedItem().toString());
            plano.setBeneficios(tfBenef.getText());
            plano.setStatus(cmbStatus.getSelectedItem().toString());

            PlanoDAO dao = new PlanoDAO();
            dao.atualizar(plano);

            JOptionPane.showMessageDialog(dialog, "Atualizado com sucesso!");
            dialog.dispose();
            carregarPlanos();
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
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
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        return btn;
    }
}

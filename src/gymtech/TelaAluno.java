package gymtech;

import gymtech.dao.AlunoDAO;
import gymtech.entidade.Aluno;
import gymtech.util.Formatacao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaAluno extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField campoBusca;
    private int idAlunoSelecionado = -1;
    private JComboBox<String> filtroStatus;
    private java.util.List<Aluno> cacheAlunos = new java.util.ArrayList<>();

    public TelaAluno() {
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());

        // painel do topo com título e botões
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(18, 34, 53));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Gerenciar Alunos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setOpaque(false);

        JButton btnNovo = criarBotao("Novo Aluno", new Color(122, 140, 46));
        JButton btnEditar = criarBotao("Editar", new Color(42, 78, 108));
        JButton btnExcluir = criarBotao("Excluir", new Color(180, 60, 50));

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormulario(false);
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.convertRowIndexToModel(tabela.getSelectedRow());
                if (linha == -1) {
                    JOptionPane.showMessageDialog(TelaAluno.this,
                            "Selecione um aluno para editar."
                    );
                    return;
                }
                int linhaModelo = tabela.convertRowIndexToModel(linha);
                idAlunoSelecionado = (int) modelo.getValueAt(linhaModelo, 0);
                abrirFormulario(true);
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.convertRowIndexToModel(tabela.getSelectedRow());
                if (linha == -1) {
                    JOptionPane.showMessageDialog(TelaAluno.this,
                            "Selecione um aluno para excluir.");
                    return;
                }
                int id = (int) modelo.getValueAt(linha, 0);
                int resposta = JOptionPane.showConfirmDialog(TelaAluno.this,
                        "Deseja realmente excluir este aluno?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    AlunoDAO dao = new AlunoDAO();
                    dao.excluir(id);
                    carregarAlunos();
                }
            }
        });

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelTopo.add(painelBotoes, BorderLayout.EAST);

        add(painelTopo, BorderLayout.NORTH);

        // barra de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelBusca.setBackground(new Color(18, 34, 53));
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel lblBusca = new JLabel("Buscar:");
        lblBusca.setForeground(new Color(160, 152, 136));
        lblBusca.setFont(new Font("Arial", Font.PLAIN, 12));
        painelBusca.add(lblBusca);

        campoBusca = new JTextField(25);
        campoBusca.setBackground(new Color(14, 27, 42));
        campoBusca.setForeground(new Color(232, 226, 214));
        campoBusca.setCaretColor(new Color(232, 226, 214));
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 78, 108), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campoBusca.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarTabela();
            }
        });
        painelBusca.add(campoBusca);

        filtroStatus = new JComboBox<>(new String[]{
            "Todos", "Ativo", "Suspenso", "Inativo"
        });
        filtroStatus.setBackground(new Color(14, 27, 42));
        filtroStatus.setForeground(new Color(232, 226, 214));
        filtroStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filtrarTabela();
            }
        });

        filtroStatus.setBackground(new Color(14, 27, 42));
        filtroStatus.setForeground(new Color(232, 226, 214));
        painelBusca.add(filtroStatus);

        add(painelBusca, BorderLayout.SOUTH);

        String[] colunas = {"ID", "Nome", "Data de Nascimento", "CPF", "Telefone", "E-mail", "Status"};
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
        tabela.setBackground(new Color(22, 42, 61));
        tabela.setForeground(new Color(232, 226, 214));
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setGridColor(new Color(42, 78, 108));
        tabela.setSelectionBackground(new Color(42, 78, 108));
        tabela.setSelectionForeground(new Color(232, 226, 214));
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linha = tabela.convertRowIndexToModel(tabela.getSelectedRow());
                    if (linha != -1) {
                        int linhaModelo = tabela.convertRowIndexToModel(linha);
                        idAlunoSelecionado
                                = (int) modelo.getValueAt(linhaModelo, 0);
                        abrirFormulario(true);
                    }
                }
            }
        });
        tabela.getTableHeader().setBackground(new Color(14, 27, 42));
        tabela.getTableHeader().setForeground(new Color(122, 140, 46));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(new Color(18, 34, 53));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(22, 42, 61));

        add(scroll, BorderLayout.CENTER);
        carregarAlunos();
    }

    private void filtrarTabela() {
        if (cacheAlunos == null) {
            return;
        }
        String busca = campoBusca.getText();
        if (busca == null) {
            busca = "";
        }
        busca = busca.toLowerCase().trim();
        String statusSelecionado = (String) filtroStatus.getSelectedItem();
        if (statusSelecionado == null) {
            statusSelecionado = "Todos";
        }
        modelo.setRowCount(0);
        for (Aluno a : cacheAlunos) {
            if (a == null) {
                continue;
            }
            boolean encontrou
                    = a.getNome().toLowerCase().contains(busca)
                    || a.getCpf().toLowerCase().contains(busca);
            if (!encontrou) {
                continue;
            }
            if (!statusSelecionado.equals("Todos")
                    && !a.getStatus().equals(statusSelecionado)) {
                continue;
            }
            modelo.addRow(new Object[]{
                a.getIdAluno(),
                a.getNome(),
                formatarData(a.getDataNascimento()),
                a.getCpf(),
                a.getTelefone(),
                a.getEmail(),
                a.getStatus()
            });
        }
    }

    private void abrirFormulario(boolean edicao) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                edicao ? "Editar Aluno" : "Novo Aluno", true);
        dialog.setSize(550, 520);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(18, 34, 53));
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(new Color(18, 34, 53));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfNome = criarCampo();
        JTextField tfDataNascimento = criarCampo();
        JTextField tfTelefone = criarCampo();
        JTextField tfEmail = criarCampo();
        JTextField tfCPF = criarCampo();

        Formatacao.adicionarMascaraData(tfDataNascimento);
        Formatacao.adicionarMascaraTelefone(tfTelefone);
        Formatacao.adicionarMascaraCpf(tfCPF);

        String[] status = {"Ativo", "Suspenso", "Inativo"};
        JComboBox<String> cmbStatus = new JComboBox<String>(status);
        cmbStatus.setBackground(new Color(14, 27, 42));
        cmbStatus.setForeground(new Color(232, 226, 214));

        if (edicao && tabela.getSelectedRow() != -1) {
            int linha = tabela.convertRowIndexToModel(tabela.getSelectedRow());
            tfNome.setText(modelo.getValueAt(linha, 1).toString());
            String dataBanco = modelo.getValueAt(linha, 2).toString();
            LocalDate data;
            if (dataBanco.contains("/")) {
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                data = LocalDate.parse(dataBanco, formatoEntrada);
            } else {
                data = LocalDate.parse(dataBanco);
            }
            tfDataNascimento.setText(
                    data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
            tfCPF.setText(modelo.getValueAt(linha, 3).toString());
            tfTelefone.setText(modelo.getValueAt(linha, 4).toString());
            tfEmail.setText(modelo.getValueAt(linha, 5).toString());
            cmbStatus.setSelectedItem(
                    modelo.getValueAt(linha, 6).toString()
            );
        }

        form.add(criarLabel("Nome:"));
        form.add(tfNome);
        form.add(criarLabel("Data Nascimento:"));
        form.add(tfDataNascimento);
        form.add(criarLabel("CPF:"));
        form.add(tfCPF);
        form.add(criarLabel("Telefone:"));
        form.add(tfTelefone);
        form.add(criarLabel("E-mail:"));
        form.add(tfEmail);
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
                if (tfNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nome é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (tfCPF.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "CPF é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (tfTelefone.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Telefone é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    Aluno a = new Aluno();
                    a.setNome(tfNome.getText());
                    String data = tfDataNascimento.getText().trim();
                    String apenasNumeros = Formatacao.limpar(data);
                    if (apenasNumeros.length() != 8) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Data inválida.\nUse o formato dd/MM/yyyy.",
                                "Data inválida",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    try {
                        DateTimeFormatter entrada = DateTimeFormatter.ofPattern("ddMMyyyy");
                        LocalDate nascimento = LocalDate.parse(apenasNumeros, entrada);
                        a.setDataNascimento(nascimento.toString());
                    } catch (java.time.format.DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Data inválida.\n\nDigite uma data existente.\nExemplo: 15/05/2000",
                                "Data inválida",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String telefone = Formatacao.limpar(tfTelefone.getText());
                    if (telefone.length() != 11) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Telefone inválido.\nDigite DDD + número (11 dígitos).",
                                "Telefone inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String telefoneFormatado = Formatacao.formatarTelefone(telefone);
                    a.setTelefone(telefoneFormatado);
                    String email = tfEmail.getText().trim();
                    if (!email.contains("@")) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "E-mail inválido.\n\nO e-mail deve conter o caractere @.\nExemplo: usuario@email.com",
                                "E-mail inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    a.setEmail(email);
                    String cpf = Formatacao.limpar(tfCPF.getText());
                    if (cpf.length() != 11) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "CPF inválido.\nDigite 11 números.",
                                "CPF inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String cpfFormatado = Formatacao.formatarCpf(cpf);
                    a.setCpf(cpfFormatado);
                    a.setStatus(cmbStatus.getSelectedItem().toString());
                    AlunoDAO dao = new AlunoDAO();
                    if (idAlunoSelecionado != -1) {
                        a.setIdAluno(idAlunoSelecionado);
                        dao.atualizar(a);
                    } else {
                        dao.salvar(a);
                    }
                    JOptionPane.showMessageDialog(dialog, "Aluno salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarAlunos();
                    filtrarTabela();
                    dialog.dispose();
                    idAlunoSelecionado = -1;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void carregarAlunos() {
        modelo.setRowCount(0);
        AlunoDAO dao = new AlunoDAO();
        cacheAlunos = dao.listar();
        for (Aluno a : cacheAlunos) {
            modelo.addRow(new Object[]{
                a.getIdAluno(),
                a.getNome(),
                formatarData(a.getDataNascimento()),
                a.getCpf(),
                a.getTelefone(),
                a.getEmail(),
                a.getStatus()
            });
        }
    }

    private String formatarData(String iso) {
        if (iso == null || iso.isEmpty()) {
            return "";
        }

        LocalDate data = LocalDate.parse(iso);
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

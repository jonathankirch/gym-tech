package gymtech;

import gymtech.dao.RelatorioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TelaRelatorio extends JPanel {

    private JComboBox<String> cmbMesI;
    private JComboBox<String> cmbMesF;
    private JComboBox<String> cmbAno;

    public TelaRelatorio() {
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setBackground(new Color(18, 34, 53));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Relatórios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // painel central com opções de relatório
        JPanel painelCentro = new JPanel();
        painelCentro.setBackground(new Color(18, 34, 53));
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // filtro de período
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelFiltro.setBackground(new Color(14, 27, 42));
        painelFiltro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 78, 108), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        painelFiltro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPeriodo.setForeground(new Color(160, 152, 136));
        painelFiltro.add(lblPeriodo);

        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        cmbMesI = new JComboBox<String>(meses);
        cmbMesI.setBackground(new Color(14, 27, 42));
        cmbMesI.setForeground(new Color(232, 226, 214));
        cmbMesI.setSelectedIndex(0);
        painelFiltro.add(cmbMesI);

        JLabel lblAte = new JLabel("até");
        lblAte.setForeground(new Color(160, 152, 136));
        painelFiltro.add(lblAte);

        cmbMesF = new JComboBox<String>(meses);
        cmbMesF.setBackground(new Color(14, 27, 42));
        cmbMesF.setForeground(new Color(232, 226, 214));
        cmbMesF.setSelectedIndex(5);
        painelFiltro.add(cmbMesF);

        String[] anos = {"2026"};
        cmbAno = new JComboBox<String>(anos);
        cmbAno.setBackground(new Color(14, 27, 42));
        cmbAno.setForeground(new Color(232, 226, 214));
        painelFiltro.add(cmbAno);

        painelCentro.add(painelFiltro);
        painelCentro.add(Box.createVerticalStrut(15));

        // botões de relatório
        String[] nomeRelatorios = {
            "Relatório de Alunos com Matrículas Ativas",
            "Relatório de Alunos Inadimplentes",
            "Relatório de Alunos por Plano",
            "Relatório de Aniversariantes do Mês",
            "Relatório de Receita do Mês"
        };

        for (String nome : nomeRelatorios) {
            JPanel linha = new JPanel(new BorderLayout());
            linha.setBackground(new Color(14, 27, 42));
            linha.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(42, 78, 108), 1),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
            ));
            linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

            JLabel lblNome = new JLabel(nome);
            lblNome.setFont(new Font("Arial", Font.PLAIN, 13));
            lblNome.setForeground(new Color(232, 226, 214));
            linha.add(lblNome, BorderLayout.WEST);

            JButton btnGerar = criarBotao("Gerar", new Color(42, 78, 108));
            btnGerar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    int mesInicio = mesParaNumero(cmbMesI.getSelectedItem().toString());
                    int mesFim = mesParaNumero(cmbMesF.getSelectedItem().toString());
                    int ano = Integer.parseInt(cmbAno.getSelectedItem().toString());

                    mostrarRelatorio(nome, mesInicio, mesFim, ano);
                }
            });
            linha.add(btnGerar, BorderLayout.EAST);

            painelCentro.add(linha);
            painelCentro.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(painelCentro);

        scroll.setBorder(
                null);
        scroll.getViewport()
                .setBackground(new Color(18, 34, 53));
        add(scroll, BorderLayout.CENTER);
    }

    private void mostrarRelatorio(String nomeRelatorio, int mesInicio, int mesFim, int ano) {

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                nomeRelatorio, true);

        dialog.setSize(
                700, 400);
        dialog.setLocationRelativeTo(
                this);
        dialog.getContentPane()
                .setBackground(new Color(18, 34, 53));
        dialog.setLayout(
                new BorderLayout());

        String[] colunas;
        Object[][] dadosTabela;

        RelatorioDAO dao = new RelatorioDAO();

        if (nomeRelatorio.contains(
                "Alunos com Matrículas Ativas")) {

            colunas = new String[]{"Aluno", "Plano", "Início", "Fim", "Status"};
            dadosTabela = dao.alunosAtivos(mesInicio, mesFim, ano).toArray(new Object[0][]);

        } else if (nomeRelatorio.contains(
                "Alunos Inadimplentes")) {

            colunas = new String[]{"Nome", "Plano", "Valor", "Vencimento"};
            dadosTabela = dao.inadimplentes(mesInicio, mesFim, ano).toArray(new Object[0][]);

        } else if (nomeRelatorio.contains(
                "Alunos por Plano")) {

            colunas = new String[]{"Aluno", "Plano"};
            dadosTabela = dao.planosPorAlunos(mesInicio, mesFim, ano).toArray(new Object[0][]);

        } else if (nomeRelatorio.contains(
                "Aniversariantes do Mês")) {

            colunas = new String[]{"Aluno", "Data Nascimento"};
            dadosTabela = dao.aniversariantesMes(mesInicio, mesFim, ano).toArray(new Object[0][]);

        } else if (nomeRelatorio.contains(
                "Receita do Mês")) {

            colunas = new String[]{"Plano", "Qtd", "Valor Unit", "Total"};
            dadosTabela = dao.receitaMensal(mesInicio, mesFim, ano).toArray(new Object[0][]);

        } else {

            colunas = new String[]{"Info"};
            dadosTabela = new Object[][]{{"Relatório não encontrado"}};
        }

        DefaultTableModel modelo = new DefaultTableModel(dadosTabela, colunas) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tabela = new JTable(modelo);

        tabela.setAutoCreateRowSorter(
                true);

        tabela.setBackground(
                new Color(22, 42, 61));
        tabela.setForeground(
                new Color(232, 226, 214));
        tabela.setFont(
                new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(
                28);
        tabela.setGridColor(
                new Color(42, 78, 108));
        tabela.setSelectionBackground(
                new Color(42, 78, 108));
        tabela.setSelectionForeground(
                new Color(232, 226, 214));

        tabela.getTableHeader()
                .setBackground(new Color(14, 27, 42));
        tabela.getTableHeader()
                .setForeground(new Color(122, 140, 46));
        tabela.getTableHeader()
                .setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tabela);

        scroll.getViewport().setBackground(new Color(22, 42, 61));
        scroll.setBackground(new Color(22, 42, 61));
        scroll.setBorder(null);

        dialog.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        rodape.setBackground(
                new Color(14, 27, 42));

        JButton btnExportar = criarBotao("Exportar CSV", new Color(122, 140, 46));
        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarTabelaParaCSV(tabela, nomeRelatorio, dialog);
            }
        });
        rodape.add(btnExportar);

        JButton btnFechar = criarBotao("Fechar", new Color(42, 78, 108));

        btnFechar.addActionListener(e
                -> dialog.dispose());

        rodape.add(btnFechar);

        dialog.add(rodape, BorderLayout.SOUTH);

        dialog.setVisible(
                true);
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

    private int mesParaNumero(String mes) {
        switch (mes) {
            case "Janeiro":
                return 1;
            case "Fevereiro":
                return 2;
            case "Março":
                return 3;
            case "Abril":
                return 4;
            case "Maio":
                return 5;
            case "Junho":
                return 6;
            case "Julho":
                return 7;
            case "Agosto":
                return 8;
            case "Setembro":
                return 9;
            case "Outubro":
                return 10;
            case "Novembro":
                return 11;
            case "Dezembro":
                return 12;
            default:
                return 0;
        }
    }

    private void exportarTabelaParaCSV(JTable tabela, String nomeRelatorio, JDialog parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório");
        String defaultFilename = nomeRelatorio.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕçÇ\\s]", "").trim().replace(" ", "_") + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultFilename));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));
        
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(filePath + ".csv");
            }
            
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileToSave), "UTF-8"))) {
                bw.write("\ufeff"); // UTF-8 BOM
                
                DefaultTableModel model = (DefaultTableModel) tabela.getModel();
                int rowCount = model.getRowCount();
                int columnCount = model.getColumnCount();
                
                // Headers
                for (int i = 0; i < columnCount; i++) {
                    bw.write(escaparCSV(model.getColumnName(i)));
                    if (i < columnCount - 1) {
                        bw.write(";");
                    }
                }
                bw.newLine();
                
                // Rows
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < columnCount; c++) {
                        Object val = model.getValueAt(r, c);
                        bw.write(escaparCSV(val == null ? "" : val.toString()));
                        if (c < columnCount - 1) {
                            bw.write(";");
                        }
                    }
                    bw.newLine();
                }
                
                JOptionPane.showMessageDialog(parent, "Relatório exportado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao exportar relatório:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private String escaparCSV(String campo) {
        if (campo == null) return "";
        if (campo.contains("\"") || campo.contains(";") || campo.contains(",") || campo.contains("\n") || campo.contains("\r")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }
}

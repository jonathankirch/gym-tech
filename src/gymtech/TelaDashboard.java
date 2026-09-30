package gymtech;

import javax.swing.*;
import java.awt.*;
import gymtech.dao.DashboardDAO;
import java.text.DecimalFormat;

public class TelaDashboard extends JPanel {

    private DashboardDAO dao = new DashboardDAO();
    private JPanel painelCards;
    private JPanel painelAvisos;

    public TelaDashboard() {
        setBackground(new Color(18, 34, 53));
        setLayout(new BorderLayout());

        // título
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTitulo.setBackground(new Color(18, 34, 53));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(232, 226, 214));

        painelTitulo.add(lblTitulo);
        add(painelTitulo, BorderLayout.NORTH);

        // área central com os cards
        painelCards = new JPanel(new GridLayout(2, 2, 15, 15));
        painelCards.setBackground(new Color(18, 34, 53));
        painelCards.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        carregarCards();
        add(painelCards, BorderLayout.CENTER);

        // avisos no rodapé
        painelAvisos = criarAvisosFixos();
        add(painelAvisos, BorderLayout.SOUTH);
    }

    private JPanel criarCard(String titulo, String valor, Color corFundo) {
        JPanel card = new JPanel();
        card.setBackground(corFundo);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 24));
        lblValor.setForeground(new Color(232, 226, 214));
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblValor);

        card.add(Box.createVerticalStrut(8));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(210, 205, 195));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitulo);

        return card;
    }

    private JLabel criarAviso(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(160, 152, 136));
        return lbl;
    }

    private void carregarCards() {

        painelCards.removeAll();

        DecimalFormat df = new DecimalFormat("#,##0.00");

        int alunosAtivos = dao.contarAlunosAtivos();
        int matriculasAtivas = dao.contarAlunosComMatriculaAtiva();
        int vencidas = dao.contarMensalidadesVencidas();
        double receita = dao.receitaMes();

        painelCards.add(criarCard("Alunos Ativos", String.valueOf(alunosAtivos), new Color(42, 78, 108)));
        painelCards.add(criarCard("Matrículas Ativas", String.valueOf(matriculasAtivas), new Color(60, 120, 60)));
        painelCards.add(criarCard("Mensalidades Vencidas", String.valueOf(vencidas), new Color(180, 60, 50)));
        painelCards.add(criarCard("Receita do Mês", "R$ " + df.format(receita), new Color(122, 140, 46)));

        painelCards.revalidate();
        painelCards.repaint();
    }

    private void carregarAvisos() {

        remove(painelAvisos);

        painelAvisos = criarAvisosFixos();
        add(painelAvisos, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel criarAvisosFixos() {

        JPanel painelAvisos = new JPanel();
        painelAvisos.setBackground(new Color(14, 27, 42));
        painelAvisos.setLayout(new BoxLayout(painelAvisos, BoxLayout.Y_AXIS));
        painelAvisos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(42, 78, 108)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel titulo = new JLabel("Avisos:");
        titulo.setFont(new Font("Arial", Font.BOLD, 13));
        titulo.setForeground(new Color(232, 226, 214));
        painelAvisos.add(titulo);

        int vencidas = dao.contarMensalidadesVencidas();
        int proximasVencer = dao.contarMensalidadesProximasVencimento();
        int matriculasVencendo = dao.contarMatriculasExpirando();

        if (vencidas > 0) {
            painelAvisos.add(criarAviso("- " + vencidas + " mensalidade(s) vencida(s)"));
        }

        if (proximasVencer > 0) {
            painelAvisos.add(criarAviso("- " + proximasVencer + " mensalidade(s) vencem em breve"));
        }

        if (matriculasVencendo > 0) {
            painelAvisos.add(criarAviso("- " + matriculasVencendo + " matrícula(s) perto do fim"));
        }

        if (vencidas == 0 && proximasVencer == 0 && matriculasVencendo == 0) {
            painelAvisos.add(criarAviso("- Sistema em dia, sem pendências"));
        }

        return painelAvisos;
    }

    public void atualizarDashboard() {
        carregarCards();
        carregarAvisos();
    }
}

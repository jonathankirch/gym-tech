package gymtech.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class GymTheme {

    // Paleta de cores GymTech
    public static final Color DARK_NAVY     = new Color(0x0E, 0x1B, 0x2A); // #0E1B2A
    public static final Color STEEL_BLUE    = new Color(0x2A, 0x4E, 0x6C); // #2A4E6C
    public static final Color MILITARY_GREEN= new Color(0x6C, 0x7F, 0x63); // #6C7F63
    public static final Color OLIVE_GREEN   = new Color(0x7A, 0x8C, 0x2E); // #7A8C2E
    public static final Color SAND_BEIGE    = new Color(0xD9, 0xD0, 0xC1); // #D9D0C1

    // Cores derivadas
    public static final Color PANEL_BG      = new Color(0x12, 0x22, 0x35);
    public static final Color SIDEBAR_BG    = new Color(0x0A, 0x14, 0x1F);
    public static final Color CARD_BG       = new Color(0x16, 0x2A, 0x3D);
    public static final Color ACCENT_GREEN  = new Color(0x8A, 0xA0, 0x38);
    public static final Color TEXT_PRIMARY  = new Color(0xE8, 0xE2, 0xD6);
    public static final Color TEXT_SECONDARY= new Color(0xA0, 0x98, 0x88);
    public static final Color BORDER_COLOR  = new Color(0x2A, 0x4E, 0x6C, 180);
    public static final Color INPUT_BG      = new Color(0x0E, 0x1B, 0x2A);
    public static final Color HOVER_BG      = new Color(0x2A, 0x4E, 0x6C, 100);
    public static final Color SUCCESS_COLOR = new Color(0x5A, 0xA0, 0x50);
    public static final Color DANGER_COLOR  = new Color(0xC0, 0x50, 0x40);
    public static final Color WARNING_COLOR = new Color(0xD9, 0xA0, 0x30);

    // Fontes
    public static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD,  22);
    public static final Font FONT_HEADER  = new Font("SansSerif", Font.BOLD,  16);
    public static final Font FONT_SUBHEAD = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN,12);
    public static final Font FONT_LOGO    = new Font("SansSerif", Font.BOLD,  28);

    /** Botão primário (verde oliva) */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, OLIVE_GREEN, TEXT_PRIMARY);
        return btn;
    }

    /** Botão secundário (azul aço) */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, STEEL_BLUE, TEXT_PRIMARY);
        return btn;
    }

    /** Botão de perigo (vermelho) */
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, DANGER_COLOR, TEXT_PRIMARY);
        return btn;
    }

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_SUBHEAD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);

        Color hover = bg.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg);    }
        });
    }

    /** Campo de texto estilizado */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_GREEN);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(STEEL_BLUE, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    /** Senha field estilizado */
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_GREEN);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(STEEL_BLUE, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    /** ComboBox estilizado */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_BODY);
        combo.setBorder(BorderFactory.createLineBorder(STEEL_BLUE, 1));
        return combo;
    }

    /** Label de título */
    public static JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    /** Label normal */
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    /** Label secundário/cinza */
    public static JLabel createSecondaryLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    /** Painel com background padrão */
    public static JPanel createPanel(Color bg) {
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        return panel;
    }

    /** Borda estilizada para cards */
    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(STEEL_BLUE, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    /** Separador horizontal */
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setBackground(PANEL_BG);
        return sep;
    }

    /** Configura o JFrame com estilo padrão */
    public static void applyFrameDefaults(JFrame frame) {
        frame.getContentPane().setBackground(DARK_NAVY);
    }

    /** Estiliza uma tabela */
    public static void styleTable(javax.swing.JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(32);
        table.setSelectionBackground(STEEL_BLUE);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(DARK_NAVY);
        header.setForeground(ACCENT_GREEN);
        header.setFont(FONT_SUBHEAD);
        header.setBorder(BorderFactory.createLineBorder(STEEL_BLUE, 1));
    }

    /** Estiliza JScrollPane */
    public static void styleScrollPane(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(STEEL_BLUE, 1));
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBackground(PANEL_BG);
        scroll.getVerticalScrollBar().setBackground(PANEL_BG);
        scroll.getHorizontalScrollBar().setBackground(PANEL_BG);
    }
}

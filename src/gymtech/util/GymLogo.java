package gymtech.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Logo da GymTech desenhado via Graphics2D — sem necessidade de arquivo externo.
 */
public class GymLogo extends JComponent {

    private final int size;
    private final boolean showText;

    public GymLogo(int size, boolean showText) {
        this.size = size;
        this.showText = showText;
        setPreferredSize(new Dimension(showText ? size * 4 : size, size));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = size / 2;
        int cy = size / 2;
        int r  = (int)(size * 0.42);

        // Fundo circular
        g2.setColor(GymTheme.OLIVE_GREEN);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // Borda
        g2.setColor(GymTheme.SAND_BEIGE);
        g2.setStroke(new BasicStroke(size * 0.045f));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);

        // Haltere — barra horizontal
        int barH  = (int)(size * 0.09);
        int barW  = (int)(size * 0.52);
        int barX  = cx - barW / 2;
        int barY  = cy - barH / 2;
        g2.setColor(GymTheme.SAND_BEIGE);
        g2.fillRoundRect(barX, barY, barW, barH, 4, 4);

        // Haltere — pesos esquerda
        int pw = (int)(size * 0.14);
        int ph = (int)(size * 0.28);
        int px = barX - pw + 2;
        int py = cy - ph / 2;
        g2.fillRoundRect(px, py, pw, ph, 5, 5);
        g2.fillRoundRect(px - (int)(size*0.07), py + ph/4, (int)(size*0.07), ph/2, 4, 4);

        // Haltere — pesos direita
        int px2 = barX + barW - 2;
        g2.fillRoundRect(px2, py, pw, ph, 5, 5);
        g2.fillRoundRect(px2 + pw, py + ph/4, (int)(size*0.07), ph/2, 4, 4);

        if (showText) {
            // Texto "GymTech"
            int tx = size + (int)(size * 0.15);
            g2.setColor(GymTheme.TEXT_PRIMARY);
            g2.setFont(new Font("SansSerif", Font.BOLD, (int)(size * 0.48)));
            g2.drawString("Gym", tx, cy - 2);
            g2.setColor(GymTheme.OLIVE_GREEN);
            FontMetrics fm = g2.getFontMetrics();
            int gymW = fm.stringWidth("Gym");
            g2.drawString("Tech", tx + gymW, cy - 2);

            g2.setColor(GymTheme.TEXT_SECONDARY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, (int)(size * 0.22)));
            g2.drawString("Sistema de Gestão", tx, cy + (int)(size * 0.3));
        }

        g2.dispose();
    }
}

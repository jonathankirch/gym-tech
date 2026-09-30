package gymtech;

import gymtech.util.ConexaoDB;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn
                    = ConexaoDB.conectar();
            System.out.println(
                    "Conectado com sucesso à base de dados da GymTech!"
            );
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TelaLogin tela = new TelaLogin();
                tela.setVisible(true);
            }
        });
    }
}

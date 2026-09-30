package gymtech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import gymtech.dao.UsuarioDAO;
import gymtech.entidade.Usuario;

public class TelaLogin extends JFrame {

    // cores do sistema
    static final Color COR_FUNDO = new Color(14, 27, 42);
    static final Color COR_PAINEL = new Color(22, 42, 61);
    static final Color COR_BOTAO = new Color(122, 140, 46);
    static final Color COR_BOTAO_HOVER = new Color(100, 115, 35);
    static final Color COR_TEXTO = new Color(232, 226, 214);
    static final Color COR_CINZA = new Color(160, 152, 136);
    static final Color COR_CAMPO = new Color(14, 27, 42);
    static final Color COR_BORDA = new Color(42, 78, 108);

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JLabel labelMensagem;

    public TelaLogin() {
        setTitle("GymTech - Login");
        setSize(420, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // painel principal com fundo escuro
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setLayout(new GridBagLayout());
        setContentPane(painelPrincipal);

        // painel do formulário
        JPanel painelForm = new JPanel();
        painelForm.setBackground(COR_PAINEL);
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        painelForm.setPreferredSize(new Dimension(340, 400));

        // título GymTech
        JLabel labelTitulo = new JLabel("GymTech", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitulo.setForeground(COR_TEXTO);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelForm.add(labelTitulo);

        painelForm.add(Box.createVerticalStrut(5));

        JLabel labelSubtitulo = new JLabel("Sistema de Gestão de Academia", SwingConstants.CENTER);
        labelSubtitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        labelSubtitulo.setForeground(COR_CINZA);
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelForm.add(labelSubtitulo);

        painelForm.add(Box.createVerticalStrut(25));

        // separador
        JSeparator separador = new JSeparator();
        separador.setForeground(COR_BORDA);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        painelForm.add(separador);

        painelForm.add(Box.createVerticalStrut(20));

        // label usuário
        JLabel labelUsuario = new JLabel("Usuário:");
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        labelUsuario.setForeground(COR_CINZA);
        labelUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelUsuario.setPreferredSize(new Dimension(999, 20));
        labelUsuario.setMaximumSize(new Dimension(999, 20));

        JPanel linhaUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaUsuario.setBackground(COR_PAINEL);
        linhaUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        linhaUsuario.add(labelUsuario);

        painelForm.add(linhaUsuario);

        // campo usuário
        campoUsuario = new JTextField();
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        campoUsuario.setBackground(COR_CAMPO);
        campoUsuario.setForeground(COR_TEXTO);
        campoUsuario.setCaretColor(COR_TEXTO);
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        painelForm.add(campoUsuario);

        painelForm.add(Box.createVerticalStrut(15));

        // label senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Arial", Font.PLAIN, 12));
        labelSenha.setForeground(COR_CINZA);
        labelSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelSenha.setPreferredSize(new Dimension(999, 20));
        labelSenha.setMaximumSize(new Dimension(999, 20));

        JPanel linhaSenha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaSenha.setBackground(COR_PAINEL);
        linhaSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        linhaSenha.add(labelSenha);

        painelForm.add(linhaSenha);

        // campo senha
        campoSenha = new JPasswordField();
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 13));
        campoSenha.setBackground(COR_CAMPO);
        campoSenha.setForeground(COR_TEXTO);
        campoSenha.setCaretColor(COR_TEXTO);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        campoSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        painelForm.add(campoSenha);

        painelForm.add(Box.createVerticalStrut(22));

        // botão entrar
        JButton botaoEntrar = new JButton("ENTRAR");
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        botaoEntrar.setBackground(COR_BOTAO);
        botaoEntrar.setForeground(COR_TEXTO);
        botaoEntrar.setBorderPainted(false);
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        botaoEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelForm.add(botaoEntrar);

        painelForm.add(Box.createVerticalStrut(12));

        // mensagem de erro/sucesso
        labelMensagem = new JLabel(" ");
        labelMensagem.setFont(new Font("Arial", Font.PLAIN, 11));
        labelMensagem.setForeground(new Color(200, 80, 60));
        labelMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelForm.add(labelMensagem);

        painelPrincipal.add(painelForm);

        // hover no botão
        botaoEntrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botaoEntrar.setBackground(COR_BOTAO_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                botaoEntrar.setBackground(COR_BOTAO);
            }
        });

        // ação do botão
        botaoEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });

        // enter na senha também faz login
        campoSenha.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fazerLogin();
                }
            }
        });
    }

    private void fazerLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            labelMensagem.setText("Preencha usuário e senha!");
            return;
        }

        // verifica credenciais
        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuarioLogado
                    = dao.autenticar(usuario, senha);
            if (usuarioLogado != null) {
                labelMensagem.setForeground(
                        new Color(80, 160, 80));
                labelMensagem.setText(
                        "Login realizado com sucesso!");
                TelaPrincipal telaPrincipal
                        = new TelaPrincipal(
                                usuarioLogado.getNome(),
                                usuarioLogado.getPerfil());
                telaPrincipal.setVisible(true);
                dispose();
            } else {
                labelMensagem.setForeground(
                        new Color(200, 80, 60));
                labelMensagem.setText(
                        "Usuário ou senha incorretos!");
                campoSenha.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            labelMensagem.setForeground(
                    new Color(200, 80, 60));
            labelMensagem.setText(
                    "Erro ao conectar com o banco!");
        }
    }
}

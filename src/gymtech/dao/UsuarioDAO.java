package gymtech.dao;

import gymtech.entidade.Usuario;
import gymtech.util.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    public Usuario autenticar(String login, String senha) {
        String sql
                = "SELECT * FROM usuario "
                + "WHERE login = ? AND senha = ?";
        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setIdUsuario(
                        rs.getInt("id_usuario"));

                usuario.setNome(
                        rs.getString("nome"));

                usuario.setLogin(
                        rs.getString("login"));

                usuario.setPerfil(
                        rs.getString("perfil"));

                return usuario;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

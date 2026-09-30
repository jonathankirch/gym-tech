package gymtech.dao;

import gymtech.entidade.Plano;
import gymtech.util.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PlanoDAO {

    public void salvar(Plano plano) {

        String sql
                = "INSERT INTO plano "
                + "(nome, valor_mensal, dias_acesso, beneficios, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plano.getNome());
            stmt.setBigDecimal(2, plano.getValor());
            stmt.setString(3, plano.getDiasAcesso());
            stmt.setString(4, plano.getBeneficios());
            stmt.setString(5, plano.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar plano:");
            e.printStackTrace();
        }
    }

    public List<Plano> listar() {

        List<Plano> lista = new ArrayList<>();

        String sql = "SELECT * FROM plano ORDER BY id_plano";

        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Plano plano = new Plano();

                plano.setIdPlano(rs.getInt("id_plano"));
                plano.setNome(rs.getString("nome"));
                plano.setValor(rs.getBigDecimal("valor_mensal"));
                plano.setDiasAcesso(rs.getString("dias_acesso"));
                plano.setBeneficios(rs.getString("beneficios"));
                plano.setStatus(rs.getString("status"));

                lista.add(plano);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Plano plano) {

        String sql = "UPDATE plano SET nome=?, valor_mensal=?, dias_acesso=?, beneficios=?, status=? WHERE id_plano=?";
        
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plano.getNome());
            stmt.setBigDecimal(2, plano.getValor());
            stmt.setString(3, plano.getDiasAcesso());
            stmt.setString(4, plano.getBeneficios());
            stmt.setString(5, plano.getStatus());
            stmt.setInt(6, plano.getIdPlano());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM plano WHERE id_plano=?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

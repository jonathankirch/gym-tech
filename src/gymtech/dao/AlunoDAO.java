package gymtech.dao;

import gymtech.entidade.Aluno;
import gymtech.util.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class AlunoDAO {

    public int salvar(Aluno a) {

        String sql = "INSERT INTO aluno "
                + "(nome, data_nascimento, telefone, email, cpf, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(
                sql,
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            stmt.setString(1, a.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(a.getDataNascimento()));
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getEmail());
            stmt.setString(5, a.getCpf());
            stmt.setString(6, a.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Aluno> listar() {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM aluno "
                + "ORDER BY nome";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno a = new Aluno();

                a.setIdAluno(rs.getInt("id_aluno"));
                a.setNome(rs.getString("nome"));
                a.setDataNascimento(rs.getString("data_nascimento").toString());
                a.setTelefone(rs.getString("telefone"));
                a.setEmail(rs.getString("email"));
                a.setCpf(rs.getString("cpf"));
                a.setStatus(rs.getString("status"));
                lista.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(Aluno a) {

        String sql = "UPDATE aluno SET nome=?, data_nascimento=?, telefone=?, email=?, cpf=?, status=? WHERE id_aluno=?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(a.getDataNascimento()));
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getEmail());
            stmt.setString(5, a.getCpf());
            stmt.setString(6, a.getStatus());
            stmt.setInt(7, a.getIdAluno());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM aluno WHERE id_aluno=?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package gymtech.dao;

import gymtech.entidade.Matricula;
import gymtech.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class MatriculaDAO {

    public int inserir(Matricula m) {

        String sql
                = "INSERT INTO matricula "
                + "(id_aluno, id_plano, periodo_meses, data_inicio, data_fim, status) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "RETURNING id_matricula";

        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getIdAluno());
            stmt.setInt(2, m.getIdPlano());
            stmt.setInt(3, m.getPeriodoMeses());
            stmt.setDate(4, Date.valueOf(m.getDataInicio()));
            stmt.setDate(5, Date.valueOf(m.getDataFim()));
            stmt.setString(6, m.getStatus());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_matricula");
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao inserir matrícula: "
                    + e.getMessage(),
                    e
            );
        }

        return 0;
    }

    public List<Matricula> listar() {
        List<Matricula> lista = new ArrayList<>();
        String sql
                = "SELECT "
                + "m.*, "
                + "a.nome AS aluno, "
                + "p.nome AS plano "
                + "FROM matricula m "
                + "INNER JOIN aluno a "
                + "ON m.id_aluno = a.id_aluno "
                + "INNER JOIN plano p "
                + "ON m.id_plano = p.id_plano "
                + "ORDER BY m.data_fim";
        try (
                Connection conn = ConexaoDB.conectar(); PreparedStatement stmt
                = conn.prepareStatement(sql); ResultSet rs
                = stmt.executeQuery()) {
            while (rs.next()) {
                Matricula m
                        = new Matricula();
                m.setIdMatricula(
                        rs.getInt("id_matricula")
                );
                m.setIdAluno(
                        rs.getInt("id_aluno")
                );
                m.setAluno(
                        rs.getString("aluno")
                );
                m.setIdPlano(
                        rs.getInt("id_plano")
                );
                m.setPlano(
                        rs.getString("plano")
                );
                m.setPeriodoMeses(
                        rs.getInt("periodo_meses")
                );
                m.setDataInicio(
                        rs.getDate("data_inicio")
                                .toLocalDate()
                );
                m.setDataFim(
                        rs.getDate("data_fim")
                                .toLocalDate()
                );
                m.setStatus(
                        rs.getString("status")
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void renovarMatricula(int idMatricula, LocalDate inicio, LocalDate fim) {
        String sql = "UPDATE matricula SET data_inicio=?, data_fim=?, status='Ativa' WHERE id_matricula=?";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql)) {

            s.setDate(1, java.sql.Date.valueOf(inicio));
            s.setDate(2, java.sql.Date.valueOf(fim));
            s.setInt(3, idMatricula);

            s.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void renovarMatriculaCompleta(
            int idMatricula,
            LocalDate inicio,
            LocalDate fim
    ) {
        String sql
                = "UPDATE matricula "
                + "SET data_inicio = ?, "
                + "data_fim = ?, "
                + "status = 'Ativa' "
                + "WHERE id_matricula = ?";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql)) {

            s.setDate(1, java.sql.Date.valueOf(inicio));
            s.setDate(2, java.sql.Date.valueOf(fim));
            s.setInt(3, idMatricula);

            s.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarStatusAutomatico() {

        String sql
                = "UPDATE matricula "
                + "SET status = CASE "
                + "WHEN status = 'Cancelada' THEN 'Cancelada' "
                + "WHEN data_fim < CURRENT_DATE THEN 'Encerrada' "
                + "ELSE 'Ativa' END";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql)) {

            s.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Matricula buscarPorId(int id) {

        String sql
                = "SELECT m.*, a.nome AS aluno, p.nome AS plano "
                + "FROM matricula m "
                + "INNER JOIN aluno a ON m.id_aluno = a.id_aluno "
                + "INNER JOIN plano p ON m.id_plano = p.id_plano "
                + "WHERE m.id_matricula = ?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Matricula m = new Matricula();

                m.setIdMatricula(rs.getInt("id_matricula"));
                m.setIdAluno(rs.getInt("id_aluno"));
                m.setAluno(rs.getString("aluno"));
                m.setIdPlano(rs.getInt("id_plano"));
                m.setPlano(rs.getString("plano"));

                m.setPeriodoMeses(rs.getInt("periodo_meses"));
                m.setDataInicio(rs.getDate("data_inicio").toLocalDate());
                m.setDataFim(rs.getDate("data_fim").toLocalDate());
                m.setStatus(rs.getString("status"));

                return m;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void cancelarMatricula(int idMatricula) {

        String sql = "UPDATE matricula SET status = 'Cancelada' WHERE id_matricula = ?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMatricula);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

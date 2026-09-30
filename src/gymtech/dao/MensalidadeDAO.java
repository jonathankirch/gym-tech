package gymtech.dao;

import gymtech.util.ConexaoDB;
import gymtech.entidade.Mensalidade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensalidadeDAO {

    public List<Mensalidade> listar() {

        List<Mensalidade> lista = new ArrayList<>();

        String sql
                = "SELECT "
                + "m.id_mensalidade, "
                + "a.nome AS aluno, "
                + "p.nome AS plano, "
                + "m.valor, "
                + "m.vencimento, "
                + "m.status, "
                + "m.parcela, "
                + "m.total_parcelas "
                + "FROM mensalidade m "
                + "INNER JOIN aluno a ON m.id_aluno = a.id_aluno "
                + "INNER JOIN plano p ON m.id_plano = p.id_plano "
                + "ORDER BY m.vencimento";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Date d = rs.getDate("vencimento");

                Mensalidade m = new Mensalidade(
                        rs.getInt("id_mensalidade"),
                        rs.getString("aluno"),
                        rs.getString("plano"),
                        rs.getDouble("valor"),
                        (d != null ? d.toLocalDate() : null),
                        rs.getString("status"),
                        rs.getInt("parcela"),
                        rs.getInt("total_parcelas")
                );

                lista.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean registrarPagamento(int idMensalidade) {

        String sql
                = "UPDATE mensalidade "
                + "SET status='Paga', data_pagamento=CURRENT_DATE "
                + "WHERE id_mensalidade=?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMensalidade);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void gerarMensalidadesDaMatricula(
            int idMatricula,
            int idAluno,
            int idPlano,
            java.math.BigDecimal valor,
            java.time.LocalDate dataInicio,
            int totalParcelas,
            String periodo
    ) {
        String sql
                = "INSERT INTO mensalidade "
                + "(id_matricula, id_aluno, id_plano, valor, vencimento, status, parcela, total_parcelas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int parcela = 1; parcela <= totalParcelas; parcela++) {

                java.time.LocalDate vencimento;

                if (periodo.equals("Aula Avulsa")) {
                    vencimento = dataInicio;
                } else {
                    vencimento = dataInicio.plusMonths(parcela);
                }

                stmt.setInt(1, idMatricula);
                stmt.setInt(2, idAluno);
                stmt.setInt(3, idPlano);
                stmt.setBigDecimal(4, valor);
                stmt.setDate(5, java.sql.Date.valueOf(vencimento));
                stmt.setString(6, "Pendente");
                stmt.setInt(7, parcela);
                stmt.setInt(8, totalParcelas);

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarMensalidadesVencidas() {

        String sql
                = "UPDATE mensalidade "
                + "SET status = 'Vencida' "
                + "WHERE status = 'Pendente' "
                + "AND vencimento < CURRENT_DATE";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelarMensalidadesPorMatricula(int idMatricula) {

        String sql
                = "UPDATE mensalidade "
                + "SET status = 'Cancelada' "
                + "WHERE id_matricula = ? "
                + "AND status IN ('Pendente', 'Vencida')";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMatricula);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

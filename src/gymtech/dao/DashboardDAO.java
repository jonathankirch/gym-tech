package gymtech.dao;

import gymtech.util.ConexaoDB;
import java.sql.*;

public class DashboardDAO {

    public int contarAlunosAtivos() {
        String sql = "SELECT COUNT(*) FROM aluno WHERE status = 'Ativo'";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int contarAlunosComMatriculaAtiva() {
        String sql = "SELECT COUNT(DISTINCT id_aluno) FROM matricula WHERE status = 'Ativa'";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int contarMensalidadesVencidas() {
        String sql = "SELECT COUNT(*) FROM mensalidade WHERE status = 'Vencida'";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double receitaMes() {
        String sql
                = "SELECT COALESCE(SUM(valor),0) "
                + "FROM mensalidade "
                + "WHERE status = 'Paga' "
                + "AND EXTRACT(MONTH FROM data_pagamento) = EXTRACT(MONTH FROM CURRENT_DATE) "
                + "AND EXTRACT(YEAR FROM data_pagamento) = EXTRACT(YEAR FROM CURRENT_DATE)";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int contarMensalidadesProximasVencimento() {
        String sql
                = "SELECT COUNT(*) FROM mensalidade "
                + "WHERE status = 'Pendente' "
                + "AND vencimento BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days'";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int contarMatriculasExpirando() {
        String sql
                = "SELECT COUNT(*) FROM matricula "
                + "WHERE data_fim BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days'";

        try (Connection c = ConexaoDB.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet rs = s.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}

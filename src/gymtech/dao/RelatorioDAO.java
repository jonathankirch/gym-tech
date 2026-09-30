package gymtech.dao;

import gymtech.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public List<Object[]> alunosAtivos(int mesIni, int mesFim, int ano) {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT a.nome, p.nome, m.data_inicio, m.data_fim, m.status "
                + "FROM matricula m "
                + "JOIN aluno a ON a.id_aluno = m.id_aluno "
                + "JOIN plano p ON p.id_plano = m.id_plano "
                + "WHERE m.status = 'Ativa' "
                + "AND EXTRACT(MONTH FROM m.data_inicio) BETWEEN ? AND ? "
                + "AND EXTRACT(YEAR FROM m.data_inicio) = ?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesIni);
            stmt.setInt(2, mesFim);
            stmt.setInt(3, ano);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getDate(4),
                    rs.getString(5)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Object[]> inadimplentes(int mesIni, int mesFim, int ano) {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT a.nome, p.nome, m.valor, m.vencimento "
                + "FROM mensalidade m "
                + "JOIN aluno a ON a.id_aluno = m.id_aluno "
                + "JOIN plano p ON p.id_plano = m.id_plano "
                + "WHERE m.status = 'Vencida' "
                + "AND EXTRACT(MONTH FROM m.vencimento) BETWEEN ? AND ? "
                + "AND EXTRACT(YEAR FROM m.vencimento) = ?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesIni);
            stmt.setInt(2, mesFim);
            stmt.setInt(3, ano);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getBigDecimal(3),
                    rs.getDate(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Object[]> receitaMensal(int mesIni, int mesFim, int ano) {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT p.nome, "
                + "COUNT(m.id_mensalidade), "
                + "p.valor_mensal, "
                + "(COUNT(m.id_mensalidade) * p.valor_mensal) "
                + "FROM mensalidade m "
                + "JOIN plano p ON p.id_plano = m.id_plano "
                + "WHERE LOWER(m.status) = 'paga' "
                + "AND EXTRACT(MONTH FROM m.data_pagamento) BETWEEN ? AND ? "
                + "AND EXTRACT(YEAR FROM m.data_pagamento) = ? "
                + "GROUP BY p.nome, p.valor_mensal";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesIni);
            stmt.setInt(2, mesFim);
            stmt.setInt(3, ano);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getBigDecimal(3),
                    rs.getBigDecimal(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Object[]> planosPorAlunos(int mesIni, int mesFim, int ano) {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT p.nome, COUNT(m.id_matricula) "
                + "FROM matricula m "
                + "JOIN plano p ON p.id_plano = m.id_plano "
                + "WHERE EXTRACT(MONTH FROM m.data_inicio) BETWEEN ? AND ? "
                + "AND EXTRACT(YEAR FROM m.data_inicio) = ? "
                + "GROUP BY p.nome "
                + "ORDER BY COUNT(m.id_matricula) DESC";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesIni);
            stmt.setInt(2, mesFim);
            stmt.setInt(3, ano);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getInt(2)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Object[]> aniversariantesMes(int mesIni, int mesFim, int ano) {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT nome, data_nascimento "
                + "FROM aluno "
                + "WHERE EXTRACT(MONTH FROM data_nascimento) BETWEEN ? AND ?";

        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesIni);
            stmt.setInt(2, mesFim);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString(1),
                    rs.getDate(2)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

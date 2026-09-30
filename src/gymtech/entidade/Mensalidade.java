package gymtech.entidade;

import java.time.LocalDate;

public class Mensalidade {

    private int idMensalidade;
    private String aluno;
    private String plano;
    private double valor;
    private LocalDate vencimento;
    private String status;
    private int parcela;
    private int totalParcelas;

    public Mensalidade(
            int idMensalidade,
            String aluno,
            String plano,
            double valor,
            LocalDate vencimento,
            String status,
            int parcela,
            int totalParcelas
    ) {

        this.idMensalidade = idMensalidade;
        this.aluno = aluno;
        this.plano = plano;
        this.valor = valor;
        this.vencimento = vencimento;
        this.status = status;
        this.parcela = parcela;
        this.totalParcelas = totalParcelas;
    }

    public int getIdMensalidade() {
        return idMensalidade;
    }

    public String getAluno() {
        return aluno;
    }

    public String getPlano() {
        return plano;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public String getStatus() {
        return status;
    }

    public int getParcela() {
        return parcela;
    }

    public int getTotalParcelas() {
        return totalParcelas;
    }
}

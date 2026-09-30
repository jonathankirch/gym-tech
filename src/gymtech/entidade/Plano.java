package gymtech.entidade;

import java.math.BigDecimal;

public class Plano {

    private int idPlano;
    private String nome;
    private BigDecimal valor;
    private String diasAcesso;
    private String beneficios;
    private String status;

    public int getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(int idPlano) {
        this.idPlano = idPlano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDiasAcesso() {
        return diasAcesso;
    }

    public void setDiasAcesso(String diasAcesso) {
        this.diasAcesso = diasAcesso;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return nome;
    }
}

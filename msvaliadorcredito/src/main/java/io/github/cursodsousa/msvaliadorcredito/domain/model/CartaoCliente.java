package io.github.cursodsousa.msvaliadorcredito.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class CartaoCliente {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public BigDecimal getLimiteLiberado() {
        return limiteLiberado;
    }

    public void setLimiteLiberado(BigDecimal limiteLiberado) {
        this.limiteLiberado = limiteLiberado;
    }
}
package io.github.cursodsousa.msvaliadorcredito.domain.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Data
@Builder
public class SituacaoCliente {
    private DadosCliente cliente;
    private List<CartaoCliente> cartoes;

    public DadosCliente getCliente() {
        return cliente;
    }

    public void setCliente(DadosCliente cliente) {
        this.cliente = cliente;
    }

    public List<CartaoCliente> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoCliente> cartoes) {
        this.cartoes = cartoes;
    }
}
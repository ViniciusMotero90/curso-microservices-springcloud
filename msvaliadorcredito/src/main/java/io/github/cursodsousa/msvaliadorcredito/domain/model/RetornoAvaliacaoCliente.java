package io.github.cursodsousa.msvaliadorcredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class RetornoAvaliacaoCliente {

    private List<CartaoAprovado> cartaoAprovados;

    public List<CartaoAprovado> getCartaoAprovados() {
        return cartaoAprovados;
    }

    public void setCartaoAprovados(List<CartaoAprovado> cartaoAprovados) {
        this.cartaoAprovados = cartaoAprovados;
    }
}
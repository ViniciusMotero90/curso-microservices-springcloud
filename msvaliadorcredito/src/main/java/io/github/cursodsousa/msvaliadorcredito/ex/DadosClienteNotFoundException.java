package io.github.cursodsousa.msvaliadorcredito.ex;

public class DadosClienteNotFoundException extends Exception{
    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontrados para CPF informado.");
    }
}
package io.github.cursodsousa.mscartoes.Services;

import io.github.cursodsousa.mscartoes.domain.ClienteCartao;
import io.github.cursodsousa.mscartoes.infra.repository.ClienteCartaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteCartaoService {
    private final ClienteCartaoRepository repository;

    public ClienteCartaoService(ClienteCartaoRepository repository) {
        this.repository = repository;
    }

    public List<ClienteCartao> listCartoesByCpf(String cpf){
        return repository.findByCpf(cpf);
    }
}
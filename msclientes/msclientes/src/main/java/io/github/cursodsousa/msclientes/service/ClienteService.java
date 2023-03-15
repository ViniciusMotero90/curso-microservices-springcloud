package io.github.cursodsousa.msclientes.service;

import io.github.cursodsousa.msclientes.domain.Cliente;
import io.github.cursodsousa.msclientes.infra.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente save(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> getByCpf(String cpf){
        return clienteRepository.findByCpf(cpf);
    }
}
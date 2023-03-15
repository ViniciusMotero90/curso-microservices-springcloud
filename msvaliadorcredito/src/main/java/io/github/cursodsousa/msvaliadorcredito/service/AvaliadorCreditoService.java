package io.github.cursodsousa.msvaliadorcredito.service;

import feign.FeignException;
import io.github.cursodsousa.msvaliadorcredito.domain.model.*;
import io.github.cursodsousa.msvaliadorcredito.ex.DadosClienteNotFoundException;
import io.github.cursodsousa.msvaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import io.github.cursodsousa.msvaliadorcredito.ex.ErroSolicitacaoCartaoException;
import io.github.cursodsousa.msvaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.cursodsousa.msvaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.cursodsousa.msvaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final SolicitacaoEmissaoCartaoPublisher solicitacaoEmissaoCartaoPublisher;

    public AvaliadorCreditoService(ClienteResourceClient clientesClient, CartoesResourceClient cartoesClient, SolicitacaoEmissaoCartaoPublisher solicitacaoEmissaoCartaoPublisher) {
        this.clientesClient = clientesClient;
        this.cartoesClient = cartoesClient;
        this.solicitacaoEmissaoCartaoPublisher = solicitacaoEmissaoCartaoPublisher;
    }

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {

        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosClientes(cpf);
            ResponseEntity<List<CartaoCliente>> cartaoResponse = cartoesClient.getCartoesByCliente(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cliente((DadosCliente) cartaoResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosClientes(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());

                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

               CartaoAprovado aprovado = new CartaoAprovado();
               aprovado.setCartao(cartao.getNome());
               aprovado.setBandeira(cartao.getBandeira());
               aprovado.setLimiteAprovado(limiteAprovado);

               return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try{
            solicitacaoEmissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}

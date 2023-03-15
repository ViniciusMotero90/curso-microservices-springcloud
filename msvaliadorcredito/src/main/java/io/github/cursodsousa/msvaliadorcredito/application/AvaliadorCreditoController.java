package io.github.cursodsousa.msvaliadorcredito.application;

import io.github.cursodsousa.msvaliadorcredito.domain.model.*;
import io.github.cursodsousa.msvaliadorcredito.ex.DadosClienteNotFoundException;
import io.github.cursodsousa.msvaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import io.github.cursodsousa.msvaliadorcredito.ex.ErroSolicitacaoCartaoException;
import io.github.cursodsousa.msvaliadorcredito.service.AvaliadorCreditoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("valiacoes-credito")
public class AvaliadorCreditoController {
    private final AvaliadorCreditoService avaliadorClienteService;

    public AvaliadorCreditoController(AvaliadorCreditoService avaliadorClienteService) {
        this.avaliadorClienteService = avaliadorClienteService;
    }

    @GetMapping
    public String Status(){
        return "Ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultarSituacaoCliente(@RequestParam("cpf") String cpf){
        try {
            SituacaoCliente situacaoCliente = avaliadorClienteService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorClienteService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartoes")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try{
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorClienteService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch(ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
package io.github.cursodsousa.mscartoes.application;

import io.github.cursodsousa.mscartoes.Services.CartaoService;
import io.github.cursodsousa.mscartoes.Services.ClienteCartaoService;
import io.github.cursodsousa.mscartoes.application.representation.CartaoSaveRequest;
import io.github.cursodsousa.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.cursodsousa.mscartoes.domain.Cartao;
import io.github.cursodsousa.mscartoes.domain.ClienteCartao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
public class CartoesController {

    private final CartaoService service;
    private final ClienteCartaoService clienteCartaoService;

    public CartoesController(CartaoService service, ClienteCartaoService clienteCartaoService) {
        this.service = service;
        this.clienteCartaoService = clienteCartaoService;
    }

    @GetMapping
    public String status(){
        return "Ok";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        Cartao cartao = request.toModel();
        service.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda){
        List<Cartao> cartaos = service.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(cartaos);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
        List<ClienteCartao> clienteCartaos = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = clienteCartaos.stream().map(CartoesPorClienteResponse::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }
}

package Asclepio.Loja.FormaPagamento;

import Asclepio.Loja.FormaPagamento.Controller.LojaFormaPagamentoSwagger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lojas/{lojaId}/formas-pagamento")
public class LojaFormaPagamentoController
        implements LojaFormaPagamentoSwagger {


    private final LojaFormaPagamentoService service;


    public LojaFormaPagamentoController(
            LojaFormaPagamentoService service
    ) {
        this.service = service;
    }


    @Override
    @GetMapping
    public List<LojaFormaPagamentoDTO> listar(
            @PathVariable Long lojaId
    ) {
        return service.listar(lojaId);
    }


    @Override
    @PutMapping
    public void atualizar(
            @PathVariable Long lojaId,
            @RequestBody List<LojaFormaPagamentoDTO> formas
    ) {
        service.atualizar(lojaId, formas);
    }
}
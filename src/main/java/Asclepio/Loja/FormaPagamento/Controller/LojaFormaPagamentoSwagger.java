package Asclepio.Loja.FormaPagamento.Controller;

import Asclepio.Loja.FormaPagamento.LojaFormaPagamentoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

public interface LojaFormaPagamentoSwagger {


    @Operation(summary = "Listar formas de pagamento da loja", description = "Retorna todas as formas de pagamento configuradas para uma loja")
    @ApiResponse(responseCode = "200", description = "Formas de pagamento retornadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Loja não encontrada")
    List<LojaFormaPagamentoDTO> listar(@Parameter(description = "ID da loja", example = "1") Long lojaId);


    @Operation(summary = "Atualizar formas de pagamento", description = "Ativa ou desativa formas de pagamento aceitas pela loja")
    @ApiResponse(responseCode = "200", description = "Configuração atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Loja não encontrada")
    void atualizar(@Parameter(description = "ID da loja", example = "1") Long lojaId,

                   @RequestBody(description = "Lista das formas de pagamento", required = true, content = @Content(examples = @ExampleObject(value = """
                           [
                             {
                               "formaPagamento": "PIX",
                               "ativo": true
                             },
                             {
                               "formaPagamento": "DINHEIRO",
                               "ativo": false
                             },
                             {
                               "formaPagamento": "CARTAO_CREDITO",
                               "ativo": true
                             }
                           ]
                           """))) List<LojaFormaPagamentoDTO> formas);
}
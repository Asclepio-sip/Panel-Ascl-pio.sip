package Asclepio.Usuario.User.dto;

import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestCriarContaDTO(

        // Usuário
        @NotBlank
        String login,

        @NotBlank
        String password,

        @NotBlank
        String email,

        // Empresa
        @NotBlank
        String nomeEmpresa,

        // Loja
        @NotBlank
        String nomeLoja,

        @NotBlank
        String cep,

        String cnpj,

        @NotBlank
        String telefone,

        String textoDescricao,

        @NotNull
        TipoAtendimentoLoja tipoAtendimento,

        // Formas de pagamento
        @NotNull
        List<FormaDePagamento> formasPagamento

) {
}
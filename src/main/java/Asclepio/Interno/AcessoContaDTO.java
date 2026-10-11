package Asclepio.Interno;

import jakarta.validation.constraints.NotNull;

/** Corpo do PUT /interno/contas/{userId}/acesso. */
public record AcessoContaDTO(@NotNull Boolean ativo) {
}

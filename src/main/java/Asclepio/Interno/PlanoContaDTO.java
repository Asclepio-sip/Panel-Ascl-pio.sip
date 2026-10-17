package Asclepio.Interno;

/** Corpo do PUT /interno/contas/{userId}/plano. planoId = id do plano no painel SaaS; null tira o plano. */
public record PlanoContaDTO(Long planoId) {
}

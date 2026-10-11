package Asclepio.Interno;

import java.util.UUID;

/** Resposta do POST /interno/contas. */
public record ContaCriadaDTO(UUID userId, String nome, String email) {
}

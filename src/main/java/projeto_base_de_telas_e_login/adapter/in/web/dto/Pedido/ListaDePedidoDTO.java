package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido;

import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;

import java.time.LocalDateTime;

public class ListaDePedidoDTO {

    private Long id;
    private String nomeCliente;
    private String status;
    private LocalDateTime criadoEm;

    public ListaDePedidoDTO(Long id, String nomeCliente, String status, LocalDateTime criadoEm) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    public static ListaDePedidoDTO fromDomain(Pedido pedido) {
        return new ListaDePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getStatus().name(),
                pedido.getCriadoEm()
        );
    }

    public Long getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public String getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
package projeto_base_de_telas_e_login.domain.repository;


import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;

public interface GeradorPdfPort {
    byte[] gerarPdfPedido(Pedido pedido);

}
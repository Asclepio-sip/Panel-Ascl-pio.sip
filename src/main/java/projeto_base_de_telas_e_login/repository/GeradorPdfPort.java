package projeto_base_de_telas_e_login.repository;


import projeto_base_de_telas_e_login.model.Pedido.Pedido;

public interface GeradorPdfPort {
    byte[] gerarPdfPedido(Pedido pedido);

}
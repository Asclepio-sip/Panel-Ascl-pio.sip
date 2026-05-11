package projeto_base_de_telas_e_login.repository;


import projeto_base_de_telas_e_login.entidade.Pedido;

public interface GeradorPdfPort {
    byte[] gerarPdfPedido(Pedido pedido);

}
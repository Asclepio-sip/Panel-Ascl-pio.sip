package projeto_base_de_telas_e_login.domain.repository;

import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoPorta {

    void save(Pedido pedido);

    List<Pedido> findAll();

    Optional<Pedido> findById(Long id);


    List<Pedido> findByCriadoBetweenOrderByCriadoDesc(
            LocalDateTime inicio,
            LocalDateTime fim
    );


    void atualizarStatus(Long id, StatusDoPedido status);}

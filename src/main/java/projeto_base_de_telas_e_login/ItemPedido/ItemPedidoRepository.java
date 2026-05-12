package projeto_base_de_telas_e_login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_base_de_telas_e_login.entidade.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
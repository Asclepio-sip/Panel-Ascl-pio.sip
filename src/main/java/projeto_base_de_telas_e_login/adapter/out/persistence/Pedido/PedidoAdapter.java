package projeto_base_de_telas_e_login.adapter.out.persistence.Pedido;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.domain.repository.PedidoPorta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PedidoAdapter implements PedidoPorta {

    private  PedidoRepository repository;

    public PedidoAdapter(PedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity(pedido);
        repository.save(entity);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return repository.findById(id)
                .map(PedidoEntity::toDomain);
    }

    @Override
    public List<Pedido> findAll(){
        return repository.findAll().stream().map(PedidoEntity::toDomain).toList();
    }

    @Override
    public List<Pedido> findByCriadoBetweenOrderByCriadoDesc(
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return repository
                .findByCriadoEmBetweenOrderByCriadoEmDesc(inicio, fim)
                .stream()
                .map(PedidoEntity::toDomain)
                .toList();
    }
    @Override
    public void atualizarStatus(Long id, StatusDoPedido status) {
        repository.atualizarStatus(id, status);
    }
}

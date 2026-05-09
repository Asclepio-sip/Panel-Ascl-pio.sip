package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.repository.LojaPorta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LojaAdapter implements LojaPorta {

    private final LojaRepository repository;

    public LojaAdapter(LojaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loja salvar(Loja loja) {
        LojaEntity entity = repository.save(new LojaEntity(loja));
        return entity.toDomain();
    }

    @Override
    public Optional<Loja> buscarPorId(Long id) {
        return repository.findById(id)
                .map(LojaEntity::toDomain);
    }

    @Override
    public Optional<Loja> buscarPorNome(String nome) {
        return repository.findByNome(nome)
                .map(LojaEntity::toDomain);
    }

    @Override
    public List<Loja> listar() {
        return repository.findAll()
                .stream()
                .map(LojaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
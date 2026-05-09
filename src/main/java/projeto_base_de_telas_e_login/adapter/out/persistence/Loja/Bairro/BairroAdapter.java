package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.Bairro;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;
import projeto_base_de_telas_e_login.domain.repository.BairroPorta;

import java.util.List;
import java.util.Optional;

@Component
public class BairroAdapter implements BairroPorta {

    private final BairroRepository repository;

    public BairroAdapter(BairroRepository repository) {
        this.repository = repository;
    }

    @Override
    public Bairro salvar(Bairro bairro) {
        return repository.save(new BairroEntity(bairro)).toDomain();
    }

    @Override
    public List<Bairro> listar() {
        return repository.findAll()
                .stream()
                .map(BairroEntity::toDomain)
                .toList();
    }
    @Override
    public Optional<Bairro> buscarPorNome(String nome) {
        return repository.findByNome(nome).map(BairroEntity::toDomain);
    }

    @Override
    public Optional<Bairro> buscarPorId(Long id) {
        return repository.findById(id)
                .map(BairroEntity::toDomain);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
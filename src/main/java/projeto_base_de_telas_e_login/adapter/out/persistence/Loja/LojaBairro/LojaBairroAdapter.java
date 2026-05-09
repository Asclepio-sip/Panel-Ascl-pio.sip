package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.LojaBairro;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.Bairro.BairroEntity;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.Bairro.BairroRepository;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja.LojaEntity;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja.LojaRepository;
import projeto_base_de_telas_e_login.domain.model.Loja.LojaBairro;
import projeto_base_de_telas_e_login.domain.repository.LojaBairroPort;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LojaBairroAdapter implements LojaBairroPort {

    private final LojaBairroRepository repository;
    private final LojaRepository lojaRepository;
    private final BairroRepository bairroRepository;

    public LojaBairroAdapter(
            LojaBairroRepository repository,
            LojaRepository lojaRepository,
            BairroRepository bairroRepository
    ) {
        this.repository = repository;
        this.lojaRepository = lojaRepository;
        this.bairroRepository = bairroRepository;
    }

    @Override
    public LojaBairro salvar(LojaBairro lojaBairro) {

        LojaEntity lojaEntity = lojaRepository
                .findById(lojaBairro.getLoja().getId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        BairroEntity bairroEntity = bairroRepository
                .findById(lojaBairro.getBairro().getId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        LojaBairroEntity entity =
                new LojaBairroEntity(lojaBairro, lojaEntity, bairroEntity);

        return repository.save(entity).toDomain();
    }

    @Override
    public List<LojaBairro> buscarPorLoja(Long lojaId) {
        return repository.findByLoja_Id(lojaId)
                .stream()
                .map(LojaBairroEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LojaBairro> listarTodos() {
        return repository.findAll()
                .stream()
                .map(LojaBairroEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
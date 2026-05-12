package projeto_base_de_telas_e_login.Loja.Loja;

import org.springframework.stereotype.Service;

import projeto_base_de_telas_e_login.Loja.Loja.dto.CreateLojaRequest;
import projeto_base_de_telas_e_login.Loja.Loja.dto.LojaResponse;

import java.util.List;

@Service
public class LojaService {

    private final LojaRepository repository;

    public LojaService(LojaRepository repository) {
        this.repository = repository;
    }

    public LojaResponse criar(CreateLojaRequest request) {

        Loja loja = new Loja(null, request.nome(), request.tipoAtendimento());

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja salva = repository.save(loja);

        return LojaResponse.fromEntity(salva);
    }

    public List<LojaResponse> listar() {

        return repository.findAll().stream().map(LojaResponse::fromEntity).toList();
    }

    public LojaResponse atualizar(Long id, CreateLojaRequest request) {

        Loja loja = repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        loja.setNome(request.nome());

        loja.setTipoAtendimento(request.tipoAtendimento());

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja atualizada = repository.save(loja);

        return LojaResponse.fromEntity(atualizada);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
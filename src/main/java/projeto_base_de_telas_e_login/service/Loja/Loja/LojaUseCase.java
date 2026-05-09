package projeto_base_de_telas_e_login.service.Loja.Loja;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.dto.loja.Loja.CreateLojaRequest;
import projeto_base_de_telas_e_login.dto.loja.Loja.LojaResponse;
import projeto_base_de_telas_e_login.model.Loja.Loja;
import projeto_base_de_telas_e_login.repository.LojaPorta;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LojaUseCase {

    private final LojaPorta repository;

    public LojaUseCase(LojaPorta repository) {
        this.repository = repository;
    }

    // Criar
    public LojaResponse criar(CreateLojaRequest request) {

        Loja loja = new Loja(
                null,
                request.nome(),
                request.tipoAtendimento()
        );

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja salvo = repository.salvar(loja);

        return LojaResponse.fromDomain(salvo);
    }

    // Listar
    public List<LojaResponse> listar() {
        return repository.listar()
                .stream()
                .map(LojaResponse::fromDomain)
                .collect(Collectors.toList());
    }

    // Atualizar
    public LojaResponse atualizar(Long id, CreateLojaRequest request) {

        Loja loja = new Loja(
                id,
                request.nome(),
                request.tipoAtendimento()
        );

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja atualizado = repository.salvar(loja);

        return LojaResponse.fromDomain(atualizado);
    }

    // Deletar
    public void deletar(Long id) {
        repository.deletar(id);
    }
}
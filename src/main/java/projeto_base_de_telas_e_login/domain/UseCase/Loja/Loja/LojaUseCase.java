package projeto_base_de_telas_e_login.domain.UseCase.Loja.Loja;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja.CreateLojaRequest;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja.LojaResponse;
import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.repository.LojaPorta;

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
                request.getNome(),
                request.getTipoAtendimento()
        );

        loja.configurarFreteGratis(request.getValorMinimoFreteGratis());

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
                request.getNome(),
                request.getTipoAtendimento()
        );

        loja.configurarFreteGratis(request.getValorMinimoFreteGratis());

        Loja atualizado = repository.salvar(loja);

        return LojaResponse.fromDomain(atualizado);
    }

    // Deletar
    public void deletar(Long id) {
        repository.deletar(id);
    }
}
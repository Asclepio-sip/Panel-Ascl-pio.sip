package projeto_base_de_telas_e_login.domain.UseCase.Loja.LojaBairro;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros.LojaBairroResponse;
import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;
import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.model.Loja.LojaBairro;
import projeto_base_de_telas_e_login.domain.repository.BairroPorta;
import projeto_base_de_telas_e_login.domain.repository.LojaBairroPort;
import projeto_base_de_telas_e_login.domain.repository.LojaPorta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LojaBairroUseCase {

    private final LojaBairroPort lojaBairroRepository;
    private final LojaPorta lojaRepository;
    private final BairroPorta bairroRepository;

    public LojaBairroUseCase(
            LojaBairroPort lojaBairroRepository,
            LojaPorta lojaRepository,
            BairroPorta bairroRepository
    ) {
        this.lojaBairroRepository = lojaBairroRepository;
        this.lojaRepository = lojaRepository;
        this.bairroRepository = bairroRepository;
    }

    public LojaBairroResponse criar(CreateLojaBairroRequest request) {

        Loja loja = Optional.ofNullable(request.getLojaId())
                .flatMap(lojaRepository::buscarPorId)
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada"));

        Bairro bairro = Optional.ofNullable(request.getBairroId())
                .flatMap(bairroRepository::buscarPorId)
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado"));

        LojaBairro lb = new LojaBairro(loja, bairro, request.getValorFrete());

        LojaBairro salvo = lojaBairroRepository.salvar(lb);

        return LojaBairroResponse.fromDomain(salvo);
    }

    public List<LojaBairroResponse> listarPorLoja(Long lojaId) {
        return lojaBairroRepository.buscarPorLoja(lojaId)
                .stream()
                .map(LojaBairroResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public List<LojaBairroResponse> listarTodos() {
        return lojaBairroRepository.listarTodos()
                .stream()
                .map(LojaBairroResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public void deletar(Long id) {
        lojaBairroRepository.deletar(id);
    }
}
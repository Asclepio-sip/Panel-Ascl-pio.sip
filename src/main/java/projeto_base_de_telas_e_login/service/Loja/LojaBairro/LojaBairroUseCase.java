package projeto_base_de_telas_e_login.service.Loja.LojaBairro;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.dto.loja.lojabairros.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.dto.loja.lojabairros.LojaBairroResponse;
import projeto_base_de_telas_e_login.model.Loja.Bairro;
import projeto_base_de_telas_e_login.model.Loja.Loja;
import projeto_base_de_telas_e_login.model.Loja.LojaBairro;
import projeto_base_de_telas_e_login.repository.BairroPorta;
import projeto_base_de_telas_e_login.repository.LojaBairroPort;
import projeto_base_de_telas_e_login.repository.LojaPorta;

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

        Loja loja = Optional.ofNullable(request.lojaId())
                .flatMap(lojaRepository::buscarPorId)
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada"));

        Bairro bairro = Optional.ofNullable(request.bairroId())
                .flatMap(bairroRepository::buscarPorId)
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado"));

        LojaBairro lb = new LojaBairro(loja, bairro, request.valorFrete());

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
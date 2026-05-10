package projeto_base_de_telas_e_login.service.Loja.LojaBairro;

import org.springframework.stereotype.Service;

import projeto_base_de_telas_e_login.dto.loja.lojabairros.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.dto.loja.lojabairros.LojaBairroResponse;

import projeto_base_de_telas_e_login.persistence.Loja.Bairro.Bairro;
import projeto_base_de_telas_e_login.persistence.Loja.Bairro.BairroRepository;

import projeto_base_de_telas_e_login.persistence.Loja.loja.Loja;
import projeto_base_de_telas_e_login.persistence.Loja.loja.LojaRepository;

import projeto_base_de_telas_e_login.persistence.Loja.LojaBairro.LojaBairro;
import projeto_base_de_telas_e_login.persistence.Loja.LojaBairro.LojaBairroRepository;

import java.util.List;

@Service
public class LojaBairroService {

    private final LojaBairroRepository lojaBairroRepository;

    private final LojaRepository lojaRepository;

    private final BairroRepository bairroRepository;

    public LojaBairroService(
            LojaBairroRepository lojaBairroRepository,
            LojaRepository lojaRepository,
            BairroRepository bairroRepository
    ) {

        this.lojaBairroRepository =
                lojaBairroRepository;

        this.lojaRepository =
                lojaRepository;

        this.bairroRepository =
                bairroRepository;
    }

    public LojaBairroResponse criar(
            CreateLojaBairroRequest request
    ) {

        Loja loja = lojaRepository
                .findById(request.lojaId())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Loja não encontrada"
                        )
                );

        Bairro bairro = bairroRepository
                .findById(request.bairroId())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Bairro não encontrado"
                        )
                );

        LojaBairro lojaBairro =
                new LojaBairro(
                        null,
                        loja,
                        bairro,
                        request.valorFrete()
                );

        LojaBairro salvo =
                lojaBairroRepository.save(lojaBairro);

        return LojaBairroResponse
                .fromEntity(salvo);
    }

    public List<LojaBairroResponse> listarPorLoja(
            Long lojaId
    ) {

        return lojaBairroRepository
                .findByLoja_Id(lojaId)
                .stream()
                .map(LojaBairroResponse::fromEntity)
                .toList();
    }

    public List<LojaBairroResponse> listarTodos() {

        return lojaBairroRepository
                .findAll()
                .stream()
                .map(LojaBairroResponse::fromEntity)
                .toList();
    }

    public void deletar(Long id) {
        lojaBairroRepository.deleteById(id);
    }
}
package projeto_base_de_telas_e_login.Loja.Bairro;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;

    public BairroService(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    public Bairro criar(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Nome do bairro não pode ser vazio");
        }

        Bairro bairro = new Bairro(null, nome);

        return bairroRepository.save(bairro);
    }

    public List<Bairro> listar() {
        return bairroRepository.findAll();
    }

    public Optional<Bairro> buscarPorId(Long id) {
        return bairroRepository.findById(id);
    }

    public Optional<Bairro> buscarPorNome(String nome) {
        return bairroRepository.findByNome(nome);
    }

    public Bairro atualizar(Long id, String nome) {

        Bairro bairro = bairroRepository.findById(id).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        bairro.setNome(nome);

        return bairroRepository.save(bairro);
    }

    public void deletar(Long id) {
        bairroRepository.deleteById(id);
    }
}
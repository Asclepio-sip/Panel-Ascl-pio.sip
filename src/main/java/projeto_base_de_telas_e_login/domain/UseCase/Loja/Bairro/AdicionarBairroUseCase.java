package projeto_base_de_telas_e_login.domain.UseCase.Loja.Bairro;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;
import projeto_base_de_telas_e_login.domain.repository.BairroPorta;

import java.util.List;
import java.util.Optional;

@Service
public class AdicionarBairroUseCase {
    private final BairroPorta bairroRepositoryPort;

    public AdicionarBairroUseCase(BairroPorta bairroRepositoryPort) {
        this.bairroRepositoryPort = bairroRepositoryPort;
    }

    public Bairro executar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do bairro não pode ser vazio");
        }
        Bairro bairro = new Bairro(null, nome);
        return bairroRepositoryPort.salvar(bairro);
    }

    public List<Bairro> listar() {
        return bairroRepositoryPort.listar();
    }

    public Optional<Bairro> buscarPorId(Long id) {
        return bairroRepositoryPort.buscarPorId(id);
    }

    public Bairro atualizar(Long id, String nome) {
        Bairro bairro = new Bairro(id, nome);
        return bairroRepositoryPort.salvar(bairro); // salvar faz update se existir
    }

    public void deletar(Long id) {
        bairroRepositoryPort.deletar(id);
    }
}
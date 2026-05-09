package projeto_base_de_telas_e_login.domain.repository;

import projeto_base_de_telas_e_login.domain.model.Loja.Loja;

import java.util.List;
import java.util.Optional;

public interface LojaPorta {

    Loja salvar(Loja loja);

    Optional<Loja> buscarPorId(Long id);

    Optional<Loja> buscarPorNome(String nome);

    List<Loja> listar();

    void deletar(Long id);
}
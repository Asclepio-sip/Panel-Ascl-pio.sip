package projeto_base_de_telas_e_login.repository;

import projeto_base_de_telas_e_login.model.Loja.LojaBairro;

import java.util.List;

public interface LojaBairroPort {

    LojaBairro salvar(LojaBairro lojaBairro);

    List<LojaBairro> buscarPorLoja(Long lojaId);

    List<LojaBairro> listarTodos();

    void deletar(Long id);
}
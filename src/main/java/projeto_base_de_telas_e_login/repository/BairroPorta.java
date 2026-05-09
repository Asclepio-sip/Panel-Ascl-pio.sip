package projeto_base_de_telas_e_login.repository;

import projeto_base_de_telas_e_login.model.Loja.Bairro;

import java.util.List;
import java.util.Optional;

public interface BairroPorta {

    Bairro salvar(Bairro bairro);

    List<Bairro> listar();

    Optional<Bairro> buscarPorId(Long id);

    void deletar(Long id);

    Optional<Bairro> buscarPorNome(String nome); // <-- aqui


}

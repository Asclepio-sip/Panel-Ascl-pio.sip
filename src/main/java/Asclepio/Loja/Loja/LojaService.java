package Asclepio.Loja.Loja;

import org.springframework.stereotype.Service;

import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaResponse;

import java.util.List;

@Service
public class LojaService {

    private final LojaRepository repository;

    public LojaService(LojaRepository repository) {
        this.repository = repository;
    }

    public LojaResponse criar(CreateLojaRequest request) {

        Loja loja = new Loja(null, request.nomeLoja(), request.cep(),request.cpnj(),request.telefone(),request.TextoDescricao(),request.tipoAtendimento(),request.imagemUrl());

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja salva = repository.save(loja);

        return LojaResponse.fromEntity(salva);
    }

    public List<LojaResponse> listar() {

        return repository.findAll().stream().map(LojaResponse::fromEntity).toList();
    }

    public LojaResponse atualizar(Long id, CreateLojaRequest request) {

        Loja loja = repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        loja.setNomeLoja(request.nomeLoja());

        loja.setTipoAtendimento(request.tipoAtendimento());

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja atualizada = repository.save(loja);

        return LojaResponse.fromEntity(atualizada);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
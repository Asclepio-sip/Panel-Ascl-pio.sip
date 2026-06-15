package Asclepio.Loja.LojaBairro;

import org.springframework.stereotype.Service;

import Asclepio.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import Asclepio.Loja.LojaBairro.dto.LojaBairroItemResponse;
import Asclepio.Loja.LojaBairro.dto.LojaBairroResponse;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Bairro.BairroRepository;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.LojaRepository;
import Asclepio.Loja.LojaBairro.dto.LojaComBairrosResponse;

import java.util.List;

@Service
public class LojaBairroService {

    private final LojaBairroRepository lojaBairroRepository;

    private final LojaRepository lojaRepository;

    private final BairroRepository bairroRepository;

    public LojaBairroService(LojaBairroRepository lojaBairroRepository, LojaRepository lojaRepository, BairroRepository bairroRepository) {

        this.lojaBairroRepository = lojaBairroRepository;

        this.lojaRepository = lojaRepository;

        this.bairroRepository = bairroRepository;
    }

    public LojaBairroResponse criar(CreateLojaBairroRequest request) {

        Loja loja = lojaRepository.findById(request.lojaId()).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        Bairro bairro = bairroRepository.findById(request.bairroId()).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));


        if (!loja.aceitaEntrega()) {
            throw new RuntimeException("Loja de retirada não aceita bairros");
        }

        LojaBairro lojaBairro = new LojaBairro(null, loja, bairro, request.valorFrete());
        LojaBairro salvo = lojaBairroRepository.save(lojaBairro);

        return LojaBairroResponse.fromEntity(salvo);
    }

    public List<LojaBairroResponse> listarPorLoja(Long lojaId) {

        return lojaBairroRepository.findByLoja_Id(lojaId).stream().map(LojaBairroResponse::fromEntity).toList();
    }

    public List<LojaBairroResponse> listarTodos() {

        return lojaBairroRepository.findAll().stream().map(LojaBairroResponse::fromEntity).toList();
    }

    public void deletar(Long id) {
        lojaBairroRepository.deleteById(id);
    }

    public List<LojaComBairrosResponse> listarLojasComBairros() {

        List<Loja> lojas = lojaRepository.findAll();

        return lojas.stream().map(loja -> {

            List<LojaBairroItemResponse> bairros = loja.getBairros().stream().map(lb -> new LojaBairroItemResponse(lb.getBairro().getId(), lb.getBairro().getNome(), lb.getValorFrete())).toList();

            return new LojaComBairrosResponse(loja.getId(), loja.getNomeLoja(), bairros);
        }).toList();
    }
}
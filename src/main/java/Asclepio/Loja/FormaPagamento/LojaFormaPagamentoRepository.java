package Asclepio.Loja.FormaPagamento;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LojaFormaPagamentoRepository extends JpaRepository<LojaFormaPagamento, UUID> {

    List<LojaFormaPagamento> findAllByLoja(Loja loja);

    List<LojaFormaPagamento> findAllByLoja_IdOrderByFormaPagamento(Long lojaId);

    Optional<LojaFormaPagamento> findByLojaAndFormaPagamento(Loja loja, FormaDePagamento formaPagamento);
}
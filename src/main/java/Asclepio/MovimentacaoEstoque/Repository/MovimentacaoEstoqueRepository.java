package Asclepio.MovimentacaoEstoque.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoUsuarioResponse;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long>, JpaSpecificationExecutor<MovimentacaoEstoque> {

    /** Usuários com pelo menos uma movimentação na empresa (nome; sem nome, o login). */
    @Query("""
            select distinct new Asclepio.MovimentacaoEstoque.dto.MovimentacaoUsuarioResponse(
                u.id, coalesce(u.nome, u.username))
            from MovimentacaoEstoque m
            join m.usuario u
            where m.estoque.loja.empresa.id = :empresaId
            """)
    List<MovimentacaoUsuarioResponse> usuariosComMovimentacao(@Param("empresaId") Long empresaId);
}

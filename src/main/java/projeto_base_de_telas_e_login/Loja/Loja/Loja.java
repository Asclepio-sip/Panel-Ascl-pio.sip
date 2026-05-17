package projeto_base_de_telas_e_login.Loja.Loja;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import projeto_base_de_telas_e_login.Loja.Bairro.Enum.TipoAtendimentoLoja;
import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loja")
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da loja é obrigatório")
    private String nomeLoja;

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "CEP inválido")
    private String cep;

    private String cpnj;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Telefone deve conter 10 ou 11 números"
    )
    private String telefone;

    @Column(columnDefinition = "TEXT")
    private String TextoDescricao;

    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    private TipoAtendimentoLoja tipoAtendimento;

    private BigDecimal valorMinimoFreteGratis;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LojaBairro> bairros = new ArrayList<>();

    public Loja() {
    }

    public Loja(Long id, String nomeLoja, String cep, String cpnj, String telefone, String textoDescricao, TipoAtendimentoLoja tipoAtendimento,String imagemUrl) {
        this.id = id;
        this.nomeLoja = nomeLoja;
        this.cep = cep;
        this.cpnj = cpnj;
        this.telefone = telefone;
        this.TextoDescricao = textoDescricao;
        this.tipoAtendimento = tipoAtendimento;
        this.imagemUrl = imagemUrl;
    }

    public boolean aceitaEntrega() {
        return tipoAtendimento == TipoAtendimentoLoja.ENTREGA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public boolean aceitaRetirada() {
        return tipoAtendimento == TipoAtendimentoLoja.RETIRADA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public void configurarFreteGratis(BigDecimal valorMinimo) {

        if (valorMinimo != null && valorMinimo.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException("Valor mínimo inválido");
        }

        this.valorMinimoFreteGratis = valorMinimo;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCpnj() {
        return cpnj;
    }

    public void setCpnj(String cpnj) {
        this.cpnj = cpnj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTextoDescricao() {
        return TextoDescricao;
    }

    public void setTextoDescricao(String textoDescricao) {
        TextoDescricao = textoDescricao;
    }

    public Long getId() {
        return id;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public TipoAtendimentoLoja getTipoAtendimento() {
        return tipoAtendimento;
    }

    public BigDecimal getValorMinimoFreteGratis() {
        return valorMinimoFreteGratis;
    }

    public List<LojaBairro> getBairros() {
        return bairros;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public void setTipoAtendimento(TipoAtendimentoLoja tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public void setValorMinimoFreteGratis(BigDecimal valorMinimoFreteGratis) {
        this.valorMinimoFreteGratis = valorMinimoFreteGratis;
    }
}
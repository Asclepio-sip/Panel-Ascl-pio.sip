package Asclepio.Loja.Loja;

import Asclepio.Empresa.Empresa;
import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;
import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.UserLoja.UserLoja;
import Asclepio.exception.BusinessException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_LOJA", uniqueConstraints = {@UniqueConstraint(name = "UK_LOJ_EMPRESA_NOME", columnNames = {"LOJ_EMPRESA_ID", "LOJ_NOME"}), @UniqueConstraint(name = "UK_LOJ_EMPRESA_CNPJ", columnNames = {"LOJ_EMPRESA_ID", "LOJ_CNPJ"})})
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOJ_ID")
    private Long id;

    @Column(name = "LOJ_NOME", nullable = false, length = 150)
    private String nomeLoja;

    @Column(name = "LOJ_CEP", nullable = false, length = 9)
    private String cep;

    @Column(name = "LOJ_CNPJ", length = 18)
    private String cnpj;

    @Column(name = "LOJ_TELEFONE", nullable = false, length = 11)
    private String telefone;

    @Column(name = "LOJ_DESCRICAO", columnDefinition = "TEXT")
    private String textoDescricao;

    @Column(name = "LOJ_IMAGEM_URL", columnDefinition = "TEXT")
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOJ_TIPO_ATENDIMENTO", nullable = false, length = 30)
    private TipoAtendimentoLoja tipoAtendimento;

    @Column(name = "LOJ_VALOR_MINIMO_FRETE_GRATIS", precision = 10, scale = 2)
    private BigDecimal valorMinimoFreteGratis;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LojaBairro> bairros = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LOJ_EMPRESA_ID", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoja> usuarios = new ArrayList<>();

    protected Loja() {
    }

    public Loja(Long id, String nomeLoja, String cep, String cnpj, String telefone, String textoDescricao, TipoAtendimentoLoja tipoAtendimento, String imagemUrl, Empresa empresa) {
        this.id = id;
        this.nomeLoja = nomeLoja;
        this.cep = cep;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.textoDescricao = textoDescricao;
        this.tipoAtendimento = tipoAtendimento;
        this.imagemUrl = imagemUrl;
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean aceitaEntrega() {
        return tipoAtendimento == TipoAtendimentoLoja.ENTREGA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public boolean aceitaRetirada() {
        return tipoAtendimento == TipoAtendimentoLoja.RETIRADA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public void configurarFreteGratis(BigDecimal valorMinimo) {
        if (valorMinimo != null && valorMinimo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Valor mínimo de frete grátis inválido");
        }

        this.valorMinimoFreteGratis = valorMinimo;
    }

    public void atualizarDados(String nomeLoja, String cep, String cnpj, String telefone, String textoDescricao, TipoAtendimentoLoja tipoAtendimento, String imagemUrl, BigDecimal valorMinimoFreteGratis) {
        this.nomeLoja = nomeLoja;
        this.cep = cep;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.textoDescricao = textoDescricao;
        this.tipoAtendimento = tipoAtendimento;
        this.imagemUrl = imagemUrl;
        configurarFreteGratis(valorMinimoFreteGratis);
    }

    public Long getId() {
        return id;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public String getCep() {
        return cep;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTextoDescricao() {
        return textoDescricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
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

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setTextoDescricao(String textoDescricao) {
        this.textoDescricao = textoDescricao;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setTipoAtendimento(TipoAtendimentoLoja tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public void setValorMinimoFreteGratis(BigDecimal valorMinimoFreteGratis) {
        this.valorMinimoFreteGratis = valorMinimoFreteGratis;
    }

    public void setBairros(List<LojaBairro> bairros) {
        this.bairros = bairros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Loja loja)) return false;

        return id != null && id.equals(loja.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
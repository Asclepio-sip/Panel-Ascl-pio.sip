package Asclepio.Empresa;

import Asclepio.ClienteEmpresa.ClienteEmpresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_EMPRESA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_EMP_CNPJ",
                        columnNames = "EMP_CNPJ")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "EMP_NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "EMP_CNPJ", length = 18)
    private String cnpj;

    @Column(name = "EMP_ATIVA", nullable = false)
    private Boolean ativa = true;

    @Column(name = "EMP_CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "empresa")
    private List<ClienteEmpresa> clientes;
}
package Asclepio.UserLoja;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.User.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "TB_USER_LOJA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_LOJA",
                        columnNames = {
                                "USL_USER_ID",
                                "USL_LOJA_ID"
                        }
                )
        }
)
public class UserLoja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USL_USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USL_LOJA_ID", nullable = false)
    private Loja loja;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USL_ROLE_ID", nullable = false)
    private Role role;

    public UserLoja() {
    }

    public UserLoja(User user, Loja loja, Role role) {
        this.user = user;
        this.loja = loja;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Loja getLoja() {
        return loja;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
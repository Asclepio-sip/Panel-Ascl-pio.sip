package projeto_base_de_telas_e_login.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import projeto_base_de_telas_e_login.service.User.UserUseCase;
import projeto_base_de_telas_e_login.model.user.UserRole;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserUseCase userUseCase;

    public DataInitializer(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @Override
    public void run(String... args) {

        try {
            userUseCase.createUser(
                    "suporte1",
                    "123",
                    UserRole.ADMIN
            );

            System.out.println("Usuário suporte1 criado com sucesso!");

        } catch (Exception e) {
            System.out.println("Usuário suporte1 já existe.");
        }
    }
}
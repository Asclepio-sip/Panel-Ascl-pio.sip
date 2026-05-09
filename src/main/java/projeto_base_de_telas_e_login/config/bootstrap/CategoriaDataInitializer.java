package projeto_base_de_telas_e_login.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import projeto_base_de_telas_e_login.domain.UseCase.Categoria.CategoriaUseCase;

import java.util.List;

@Configuration
public class CategoriaDataInitializer implements CommandLineRunner {

    private final CategoriaUseCase categoriaUseCase;

    public CategoriaDataInitializer(CategoriaUseCase categoriaUseCase) {
        this.categoriaUseCase = categoriaUseCase;
    }

    @Override
    public void run(String... args) {

        List<String> categoriasPadrao = List.of(
                "Medicamentos",
                "Beleza",
                "Higiene",
                "Infantil",
                "Vitaminas",
                "Promoções"
        );

        categoriasPadrao.forEach(nome -> {
            try {
                categoriaUseCase.criar(nome);
                System.out.println("Categoria criada: " + nome);
            } catch (Exception e) {
                System.out.println("Categoria já existe: " + nome);
            }
        });
    }
}
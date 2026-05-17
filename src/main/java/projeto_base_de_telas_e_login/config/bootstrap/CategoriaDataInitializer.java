package projeto_base_de_telas_e_login.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import projeto_base_de_telas_e_login.Categoria.CategoriaService;

import java.util.List;

@Configuration
public class CategoriaDataInitializer implements CommandLineRunner {

    private final CategoriaService categoriaService;

    public CategoriaDataInitializer(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public void run(String... args) {

        List<String> categoriasPadrao = List.of(
                "Medicamentos",
                "Beleza",
                "Higiene",
                "Infantil",
                "Vitaminas"
        );

        categoriasPadrao.forEach(nome -> {
            try {
                categoriaService.criar(nome);
                System.out.println("Categoria criada: " + nome);
            } catch (Exception e) {
                System.out.println("Categoria já existe: " + nome);
            }
        });
    }
}
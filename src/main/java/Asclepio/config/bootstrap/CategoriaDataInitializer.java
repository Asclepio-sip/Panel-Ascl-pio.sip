package Asclepio.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.dto.CriarCategoria;

import java.util.List;

@Configuration
public class CategoriaDataInitializer implements CommandLineRunner {

    private final CategoriaService categoriaService;

    public CategoriaDataInitializer(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public void run(String... args) {

        List<CriarCategoria> categoriasPadrao = List.of(

                new CriarCategoria("Medicamentos", "Produtos farmacêuticos", "pill", null),

                new CriarCategoria("Beleza", "Produtos de beleza e cosméticos", "sparkles", null),

                new CriarCategoria("Higiene", "Produtos de higiene pessoal", "soap", null),

                new CriarCategoria("Infantil", "Produtos para bebês e crianças", "baby", null),

                new CriarCategoria("Vitaminas", "Vitaminas e suplementos", "heart", null));

        categoriasPadrao.forEach(dto -> {

            try {

                categoriaService.criar(dto);

                System.out.println("Categoria criada: " + dto.nomeCategoria());

            } catch (Exception e) {

                System.out.println("Categoria já existe: " + dto.nomeCategoria());
            }

        });
    }
}
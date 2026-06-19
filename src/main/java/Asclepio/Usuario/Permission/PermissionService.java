package Asclepio.Usuario.Permission;

import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService {

    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {

        this.repository = repository;
    }

    public List<Permission> listarTodos() {
        return repository.findAll();
    }

    public Permission buscarPorId(UUID id) {

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permissão não encontrada"));
    }

}
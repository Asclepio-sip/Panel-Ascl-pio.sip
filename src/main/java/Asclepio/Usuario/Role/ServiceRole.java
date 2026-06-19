package Asclepio.Usuario.Role;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRole {

    private RoleRepository  roleRepository;

    public ServiceRole(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role>listaDeRole(){
        return roleRepository.findAll();
    }



}

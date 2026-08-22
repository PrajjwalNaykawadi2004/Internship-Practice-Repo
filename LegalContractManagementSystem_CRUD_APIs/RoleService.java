package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Role;
import com.legalcontract.legal_contract_management_system.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService
{

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    // CREATE
    public Role createRole(Role role)
    {
        return roleRepository.save(role);
    }

    // READ ALL
    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }

    // READ BY ID
    public Optional<Role> getRoleById(Long id)
    {
        return roleRepository.findById(id);
    }

    // UPDATE
    public Role updateRole(Long id, Role roleDetails)
    {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + id));

        role.setName(roleDetails.getName());

        return roleRepository.save(role);
    }

    // DELETE
    public void deleteRole(Long id)
    {
        if (!roleRepository.existsById(id))
        {
            throw new RuntimeException("Role not found with id: " + id);
        }

        roleRepository.deleteById(id);
    }
}
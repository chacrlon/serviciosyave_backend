package com.serviciosyave.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.serviciosyave.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

    Optional<Role> findByName(String name);

}
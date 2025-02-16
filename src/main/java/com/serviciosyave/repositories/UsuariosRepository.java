package com.serviciosyave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviciosyave.entities.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository <Usuarios, Long>{

}

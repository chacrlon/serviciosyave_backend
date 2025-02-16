package com.serviciosyave.services;

import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviciosyave.entities.Usuarios;
import com.serviciosyave.repositories.UsuariosRepository;

@Service
public class UsuariosService {


	@Autowired 
	private UsuariosRepository ObjetousuariosRepository;
	
	
	
	//Crea
	public Usuarios createNuevoUsuario (Usuarios ObjetodelUsuarios) {
		return ObjetousuariosRepository.save(ObjetodelUsuarios);	
	}
//Lista
	public List<Usuarios> getAllUsuarios(){
		return ObjetousuariosRepository.findAll();
		
	}
	//Busca
	public Optional<Usuarios> getUsuariosbyid(Long id) {
		return ObjetousuariosRepository.findById(id);
	}
	//Actualiza-Modifica
	public Usuarios updateUsuarios (Long id, Usuarios ObjetoServiceUsuarios) {
		ObjetoServiceUsuarios.setId(id);
		return ObjetousuariosRepository.save(ObjetoServiceUsuarios);
		
		
		
	}
	
	//Elimina
	public void deleteUsuarios (Long id) {
	
		
	}
	
}

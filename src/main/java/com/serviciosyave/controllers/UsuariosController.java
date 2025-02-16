package com.serviciosyave.controllers;  

import com.serviciosyave.entities.Usuarios;  
import com.serviciosyave.services.UsuariosService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  

import java.util.List;  
import java.util.Optional;  

@RestController  
@RequestMapping("/api/usuarios")  
public class UsuariosController {  

    @Autowired  
    private UsuariosService usuariosService;  

    // Crear nuevo usuario  
    @PostMapping  
    public ResponseEntity<Usuarios> createUsuario(@RequestBody Usuarios nuevoUsuario) {  
        Usuarios usuarioCreado = usuariosService.createNuevoUsuario(nuevoUsuario);  
        return ResponseEntity.ok(usuarioCreado);  
    }  
    
     

    // Listar todos los usuarios  
    @GetMapping  
    public ResponseEntity<List<Usuarios>> getAllUsuarios() {  
        List<Usuarios> usuarios = usuariosService.getAllUsuarios();  
        return ResponseEntity.ok(usuarios);  
    }  
 
    // Buscar usuario
    @GetMapping("/{id}")  
    public ResponseEntity<Usuarios> getUsuarioById(@PathVariable Long id) {  
        Optional<Usuarios> usuarioOptional = usuariosService.getUsuariosbyid(id);  
        return usuarioOptional.map(ResponseEntity::ok)  
                              .orElseGet(() -> ResponseEntity.notFound().build());  
    }  
    
   

    // Actualizar usuario  
    @PutMapping("/{id}")  
    public ResponseEntity<Usuarios> updateUsuario(@PathVariable Long id, @RequestBody Usuarios usuarioActualizado) {  
        Usuarios actualizado = usuariosService.updateUsuarios(id, usuarioActualizado);  
        return ResponseEntity.ok(actualizado);  
    }  


    // Eliminar usuario  
    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {  
        usuariosService.deleteUsuarios(id);  
        return ResponseEntity.noContent().build();  
    }  
    
  
}
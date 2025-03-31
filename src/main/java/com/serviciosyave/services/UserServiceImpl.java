package com.serviciosyave.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import com.serviciosyave.entities.Role;
import com.serviciosyave.entities.User;
import com.serviciosyave.entities.UserStatus;
import com.serviciosyave.entities.VendorService;
import com.serviciosyave.models.IUser;
import com.serviciosyave.models.UserRequest;
import com.serviciosyave.repositories.RoleRepository;
import com.serviciosyave.repositories.UserRepository;
import com.serviciosyave.repositories.VendorServiceRepository;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository repository;
    private RoleRepository roleRepository;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder;
    // En UserServiceImpl.java  
    @Autowired  
    private VendorServiceRepository vendorServiceRepository; 
    
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService) {
        this.emailService = emailService;
		this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUserEmail(@NonNull String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public void processForgotPassword(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

        repository.save(user);
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        String emailContent = "Haz clic aquí para restablecer tu contraseña: " + resetLink;

        emailService.sendEmail(user.getEmail(), "Recuperación de contraseña", emailContent);
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }
    
    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setLastname(user.getLastname());
            userDb.setName(user.getName());
            userDb.setUsername(user.getUsername());

            userDb.setRoles(getRoles(user));
            return Optional.of(repository.save(userDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
  
    //Actualizar el valor enum del usuario
    @Override  
    @Transactional  
    public Optional<User> updateUserStatus(Long id, UserStatus status) {  
        Optional<User> userOptional = repository.findById(id);  
        if (userOptional.isPresent()) {  
            User userDb = userOptional.get();  
            userDb.setStatus(status);  
            return Optional.of(repository.save(userDb));  
        }  
        return Optional.empty();  
    }

    @Override  
    @Transactional(readOnly = true)  
    public List<VendorService> findServicesByUserId(Long userId) {  
        return vendorServiceRepository.findByUserId(userId); // Cambié a findByUserId  
    }


    //Eso se creo exclusivamente para NegotiationService
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }
}

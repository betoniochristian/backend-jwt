package com.practiceJWT.practiceJWT.Service;

import com.practiceJWT.practiceJWT.Entity.Role;
import com.practiceJWT.practiceJWT.Entity.User;
import com.practiceJWT.practiceJWT.Repository.RoleRepository;
import com.practiceJWT.practiceJWT.Repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService  {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public  UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void registerUser(User user){
        if (userRepository.findByUsername(user.getUsername()) != null){
            throw new RuntimeException("Username already taken");
        }

        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(null);
        user.setVerified(false);

        Optional<Role> adminRole = roleRepository.findByName("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole.orElseThrow(() -> new RuntimeException("ADMIN role not found")));
        user.setRoles(roles);


        userRepository.save(user);

    }

    public boolean authenticate(String username, String password){
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User does not exist in the database");
        }

        // Make sure we're matching the provided password to the hashed password
        if (!bCryptPasswordEncoder.matches(password, user.getPasswordHash())){
            throw new BadCredentialsException("Incorrect password");
        }

        return true;
    }

}

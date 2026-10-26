package br.com.estante.security;

import br.com.estante.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserRepository userRepo;

    public UserDetailsServiceImplementation(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login){
        return new UserDetailsImplementation(userRepo.findByEmailOrUsername(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));
    }
}

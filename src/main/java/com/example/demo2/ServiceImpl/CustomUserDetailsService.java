package com.example.demo2.ServiceImpl;
import com.example.demo2.entity.User_Detail;
import com.example.demo2.repo.userRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final userRepo userRepository;

    public CustomUserDetailsService(userRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User_Detail user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}

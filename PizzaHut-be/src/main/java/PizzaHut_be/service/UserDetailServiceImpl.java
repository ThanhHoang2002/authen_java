package PizzaHut_be.service;

import PizzaHut_be.config.security.UserDetailImpl;
import PizzaHut_be.dao.repository.UserModelRepository;
import PizzaHut_be.model.entity.Client;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserModelRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> userModel = repository.findByEmail(email);

        if (userModel.isPresent()) {
            return new UserDetailImpl(userModel.get());
        }
        return null;
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Optional<Client> userModel = repository.findById(id);

        if (userModel.isPresent()) {
            return new UserDetailImpl(userModel.get());
        }
        return null;
    }
}

package br.com.moreira.desafiopetize.config;

import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.repositories.UserRepository;
import br.com.moreira.desafiopetize.domain.services.UserAuthenticated;
import br.com.moreira.desafiopetize.exceptions.UsernameNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserRepository userRepository;

    public JwtToUserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = userRepository.findByUsername(jwt.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserAuthenticated userDetails = new UserAuthenticated(user);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
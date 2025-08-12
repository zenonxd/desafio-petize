package br.com.moreira.desafiopetize.config;

import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.repositories.UserRepository;
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
        User user = userRepository.findByUsername(jwt.getSubject()).orElse(null);

        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        }

        return null;
    }
}
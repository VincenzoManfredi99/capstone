package vincenzomanfredi.capstone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

        httpSecurity.csrf(csrf -> csrf.disable()); //non serve con i token e complica con il front end

        return httpSecurity.build();
    }
}

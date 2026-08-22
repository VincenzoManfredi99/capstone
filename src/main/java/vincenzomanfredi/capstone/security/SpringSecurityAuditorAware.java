package vincenzomanfredi.capstone.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vincenzomanfredi.capstone.utente.entities.Utente;

import java.util.Optional;
import java.util.UUID;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Utente currentUtente = (Utente) authentication.getPrincipal();
        return Optional.of(currentUtente.getId());
    }
}

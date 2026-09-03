package vincenzomanfredi.capstone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vincenzomanfredi.capstone.exceptions.Unauthorized;
import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.services.UtenteService;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    public TokenFilter(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new Unauthorized("Inserire il token nell'authorization header nel formato Bearer ");

        String accessToken = authHeader.replace("Bearer ", "");
        System.out.println(accessToken);

        this.jwtTools.verifyToken(accessToken);

        UUID userId = this.jwtTools.extractIdFromToken(accessToken);
        Utente authenticatedUser = this.utenteService.findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();
        
        boolean isAuthRoute = path.startsWith("/auth");
        boolean isRegisterRoute = path.equals("/utenti") && method.equals("POST");

        return isAuthRoute || isRegisterRoute;
    }
}

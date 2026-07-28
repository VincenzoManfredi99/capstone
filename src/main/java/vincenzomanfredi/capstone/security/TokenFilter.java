package vincenzomanfredi.capstone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import vincenzomanfredi.capstone.exceptions.Unauthorized;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;

    public TokenFilter(JWTTools jwtTools) {
        this.jwtTools = jwtTools;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new Unauthorized("Inserire il token nell'authorization header nel formato Bearer ");

        String accessToken = authHeader.replace("Bearer ", "");
        System.out.println(accessToken);

        this.jwtTools.verifyToken(accessToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}

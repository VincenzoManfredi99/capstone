package vincenzomanfredi.capstone.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.Unauthorized;
import vincenzomanfredi.capstone.security.login.LoginDTO;
import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.services.UtenteService;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;


    public AuthService(UtenteService utenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String check(LoginDTO body) {
        Utente found = this.utenteService.findByEmail(body.email());

        System.out.println("Password ricevuta: " + body.password());
        System.out.println("Password nel DB: " + found.getPassword());

        if (this.bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new Unauthorized("Credenziali Sbagliate");
        }

    }
}






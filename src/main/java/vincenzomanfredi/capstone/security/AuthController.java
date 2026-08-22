package vincenzomanfredi.capstone.security;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.Validation;
import vincenzomanfredi.capstone.security.login.LoginDTO;
import vincenzomanfredi.capstone.security.login.LoginResponseDTO;
import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.payloads.UtenteDTO;
import vincenzomanfredi.capstone.utente.payloads.UtenteResponseDTO;
import vincenzomanfredi.capstone.utente.services.UtenteService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.check(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UtenteResponseDTO saveUser(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new Validation(errorsList);
        }
        Utente saved = this.utenteService.save(body);
        return new UtenteResponseDTO(saved.getId());
    }
}




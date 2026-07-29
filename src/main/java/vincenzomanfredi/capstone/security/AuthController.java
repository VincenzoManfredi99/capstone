package vincenzomanfredi.capstone.security;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.payloads.AdminDTO;
import vincenzomanfredi.capstone.admin.payloads.AdminResponseDTO;
import vincenzomanfredi.capstone.admin.services.AdminService;
import vincenzomanfredi.capstone.exceptions.Validation;
import vincenzomanfredi.capstone.security.login.LoginDTO;
import vincenzomanfredi.capstone.security.login.LoginResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminService adminService;

    public AuthController(AuthService authService, AdminService adminService) {
        this.authService = authService;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.check(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public AdminResponseDTO saveUser(@RequestBody @Validated AdminDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new Validation(errorsList);
        }
        Admin saved = this.adminService.save(body);
        return new AdminResponseDTO(saved.getId());
    }
}




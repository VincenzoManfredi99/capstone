package vincenzomanfredi.capstone.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.services.AdminService;
import vincenzomanfredi.capstone.exceptions.Unauthorized;
import vincenzomanfredi.capstone.security.login.LoginDTO;

@Service
public class AuthService {

    private final AdminService adminService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;


    public AuthService(AdminService adminService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.adminService = adminService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String check(LoginDTO body) {
        Admin found = this.adminService.findByEmail(body.email());

        System.out.println("Password ricevuta: " + body.password());
        System.out.println("Password nel DB: " + found.getPassword());

        if (this.bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new Unauthorized("Credenziali Sbagliate");
        }

    }
}






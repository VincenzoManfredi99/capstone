package vincenzomanfredi.capstone.security;

import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.services.AdminService;
import vincenzomanfredi.capstone.exceptions.Unauthorized;
import vincenzomanfredi.capstone.security.login.LoginDTO;

@Service
public class AuthService {

    private final AdminService adminService;

    private final JWTTools jwtTools;

    public AuthService(AdminService adminService, JWTTools jwtTools) {
        this.adminService = adminService;
        this.jwtTools = jwtTools;
    }

    public String check(LoginDTO body) {
        Admin found = this.adminService.findByEmail(body.email());

        if (found.getPassword().equals(body.password())) {
            return this.jwtTools.generateToken(found);
            return "TOKEN";
        } else {
            throw new Unauthorized("Credenziali sbagliate");
        }
    }
}

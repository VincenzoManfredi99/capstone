package vincenzomanfredi.capstone.admin.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.payloads.AdminDTO;
import vincenzomanfredi.capstone.admin.services.AdminService;
import vincenzomanfredi.capstone.exceptions.BadRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Admin saveAdmin(@RequestBody @Validated AdminDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.adminService.save(body);
    }

    // Get All
    @GetMapping
    public Page<Admin> getAdmins(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String orderBy) {
        return this.adminService.getAll(page, size, orderBy);
    }

    // Get by Id
    @GetMapping("/{id}")
    public Admin getById(@PathVariable UUID id) {
        return this.adminService.findById(id);
    }

    // Put
    @PutMapping("/{id}")
    public Admin getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated AdminDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.adminService.findByIdAndUpdate(id, body);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.adminService.findByIdAndDelete(id);
    }
}

package hotel.booking.controller;

import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.request.AdminRequest;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDomain>> getListAdmins() {
        return ResponseEntity.ok(userService.getListAdmins());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseByName<String, Object>> getCurrentAdmin() {
        return ResponseEntity.ok(userService.getCurrentAdmin());
    }

    @PutMapping("/changePass/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordRequest passwordRequest){
        userService.changePassword(id, passwordRequest);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDomain> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDomain> registerAdmin(@RequestBody AdminRequest request) {
        return ResponseEntity.ok(userService.registerAdmin( request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDomain> updateAdmin(@PathVariable("id") Long id,@RequestBody AdminRequest request) {
        return ResponseEntity.ok(userService.updateAdmin(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}

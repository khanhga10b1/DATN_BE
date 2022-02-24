package hotel.booking.controller;
import hotel.booking.domain.LoginUser;
import hotel.booking.domain.ResponseDataAPI;
import hotel.booking.domain.UserRegister;
import hotel.booking.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginUser loginUser) {
        Map<String, String> response = new HashMap<>();
        response.put("token", userService.loginWithEmailAndPassword(loginUser));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegister userRegister) {
        userService.registerUser(userRegister);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getAccByEmail")
    public ResponseEntity<Boolean> checkAccount(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.checkAccount(email));
    }
}

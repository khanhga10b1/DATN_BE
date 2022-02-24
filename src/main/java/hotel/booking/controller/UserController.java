package hotel.booking.controller;

import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.UserRegister;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.domain.request.UserRequest;
import hotel.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseByName<String, Object>>  getCurrentAccount() {
        return ResponseEntity.ok(ResponseByName.Builder("account", userService.getCurrentAccount()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProfile(@PathVariable("id") Long userId, @RequestBody UserRequest userRequest) {
        userService.editProfile(userId, userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changePass/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordRequest passwordRequest){
        userService.changePassword(id, passwordRequest);
        return  ResponseEntity.ok().build();
    }




}

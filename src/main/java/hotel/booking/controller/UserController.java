package hotel.booking.controller;

import hotel.booking.domain.ResponseByName;
import hotel.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package hotel.booking.service;

import hotel.booking.domain.LoginUser;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.UserRegister;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.domain.request.UserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    String loginWithEmailAndPassword(LoginUser loginUser);
    UserDomain getCurrentAccount();
    void editProfile(Long userId, UserRequest userRequest);
    void registerUser(UserRegister userRegister);
    Boolean checkAccount(String email);
    void changePassword(Long id,PasswordRequest passwordRequest);
}

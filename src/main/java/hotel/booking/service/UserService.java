package hotel.booking.service;

import hotel.booking.domain.LoginUser;
import hotel.booking.domain.UserDomain;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    String loginWithEmailAndPassword(LoginUser loginUser);
    UserDomain getCurrentAccount();
}

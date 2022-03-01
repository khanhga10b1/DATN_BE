package hotel.booking.service;

import hotel.booking.domain.LoginUser;
import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.UserRegister;
import hotel.booking.domain.request.AdminRequest;
import hotel.booking.domain.request.ChangeStatusRequest;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.domain.request.UserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    Map<String, Object> loginWithEmailAndPassword(LoginUser loginUser);

    UserDomain getCurrentAccount();

    void editProfile(Long userId, UserRequest userRequest);

    void updatePaypal(Long userId, UserRequest userRequest);

    void registerUser(UserRegister userRegister);

    Boolean checkAccount(String email);

    void changePassword(Long id, PasswordRequest passwordRequest);

    List<UserDomain> getListAdmins();

    List<UserDomain> getListUsers();

    ResponseByName<String, Object> getCurrentAdmin();

    UserDomain getUserById(Long id);

    UserDomain registerAdmin( AdminRequest request);

    UserDomain updateAdmin(Long id, AdminRequest request);

    void deleteUser(Long id);

    UserDomain changeStatus(Long userId, ChangeStatusRequest request);
}

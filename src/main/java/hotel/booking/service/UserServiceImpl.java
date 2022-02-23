package hotel.booking.service;

import hotel.booking.domain.LoginUser;
import hotel.booking.domain.ResponseDataAPI;
import hotel.booking.domain.UserDomain;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.UserRepository;
import hotel.booking.security.CustomAuthenticationProvider;
import hotel.booking.security.TokenProvider;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationProvider authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("abc");
        return null;
    }

    @Override
    public String loginWithEmailAndPassword(LoginUser loginUser) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public UserDomain getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDomain userDomain = null;
        if (authentication.getPrincipal() instanceof UserDomain) {
           userDomain = (UserDomain) authentication.getPrincipal();
        }

        if(userDomain == null) {
            logger.error(StringUtils.buildLog(Error.BAD_CREDENTIALS, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.BAD_CREDENTIALS.getMessage(), Error.BAD_CREDENTIALS.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }

        return userDomain;
    }


}

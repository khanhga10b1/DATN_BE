package hotel.booking.service;

import hotel.booking.domain.LoginUser;
import hotel.booking.domain.ResponseDataAPI;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.UserRegister;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.domain.request.UserRequest;
import hotel.booking.entity.UserEntity;
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

    @Override
    public void editProfile(Long userId, UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        userEntity.setAddress(userRequest.getAddress());
        userEntity.setAvatar(userRequest.getAvatar());
        userEntity.setName(userRequest.getName());
        userEntity.setPhone(userRequest.getPhone());
        userRepository.save(userEntity);
    }

    @Override
    public void registerUser(UserRegister userRegister) {
        if(StringUtils.isEmpty(userRegister.getEmail()) || StringUtils.isEmpty(userRegister.getName()) || StringUtils.isEmpty(userRegister.getPassword())) {
            logger.error(StringUtils.buildLog(Error.REQUIRED_FIELD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw  new CustomException(Error.REQUIRED_FIELD.getMessage(), Error.REQUIRED_FIELD.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findByEmail(userRegister.getEmail().trim()).orElse(null);
        if(user != null) {
            logger.error(StringUtils.buildLog(Error.EXIST_EMAIL, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw  new CustomException(Error.EXIST_EMAIL.getMessage(), Error.EXIST_EMAIL.getCode(),
                    HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRegister.getName());
        userEntity.setEmail(userRegister.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public Boolean checkAccount(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        return userEntity != null;
    }

    @Override
    public void changePassword(Long id,PasswordRequest passwordRequest) {

        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });

        if(!passwordEncoder.matches(passwordRequest.getOldPass(), userEntity.getPassword())) {
            logger.error(StringUtils.buildLog(Error.OLD_PASSWORD_IS_WRONG, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw  new CustomException(Error.OLD_PASSWORD_IS_WRONG.getMessage(), Error.OLD_PASSWORD_IS_WRONG.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        userEntity.setPassword(passwordEncoder.encode(passwordRequest.getNewPass()));

    }


}

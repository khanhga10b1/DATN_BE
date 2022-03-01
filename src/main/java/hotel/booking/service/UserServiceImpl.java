package hotel.booking.service;

import hotel.booking.constant.Role;
import hotel.booking.domain.*;
import hotel.booking.domain.request.AdminRequest;
import hotel.booking.domain.request.ChangeStatusRequest;
import hotel.booking.domain.request.PasswordRequest;
import hotel.booking.domain.request.UserRequest;
import hotel.booking.entity.RoleEntity;
import hotel.booking.entity.UserEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.RoleRepository;
import hotel.booking.repository.UserRepository;
import hotel.booking.security.CustomAuthenticationProvider;
import hotel.booking.security.TokenProvider;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.modelmapper.ModelMapper;
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

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("abc");
        return null;
    }

    @Override
    public Map<String, Object> loginWithEmailAndPassword(LoginUser loginUser) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenProvider.generateToken(authentication));
        UserEntity userEntity = userRepository.findByEmail(loginUser.getEmail()).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.WRONG_EMAIL_PASSWORD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.WRONG_EMAIL_PASSWORD.getMessage(), Error.WRONG_EMAIL_PASSWORD.getCode(),
                    HttpStatus.UNAUTHORIZED);
        });
        if (!userEntity.getStatus()) {
            logger.error(StringUtils.buildLog(Error.USER_IS_LOCK, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.USER_IS_LOCK.getMessage(), Error.USER_IS_LOCK.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }
        List<String> roleAdmins = Arrays.asList(Role.ADMIN.getCode(), Role.SUPER_ADMIN.getCode());
        if (loginUser.getIsAdmin() && !roleAdmins.contains(userEntity.getRoleEntity().getCode())) {
            logger.error(StringUtils.buildLog(Error.WRONG_EMAIL_PASSWORD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.WRONG_EMAIL_PASSWORD.getMessage(), Error.WRONG_EMAIL_PASSWORD.getCode(),
                    HttpStatus.UNAUTHORIZED);
        } else if (!loginUser.getIsAdmin() && roleAdmins.contains(userEntity.getRoleEntity().getCode())) {
            logger.error(StringUtils.buildLog(Error.WRONG_EMAIL_PASSWORD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.WRONG_EMAIL_PASSWORD.getMessage(), Error.WRONG_EMAIL_PASSWORD.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }
        return result;
    }

    @Override
    public UserDomain getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDomain userDomain = null;
        if (authentication.getPrincipal() instanceof UserDomain) {
            userDomain = (UserDomain) authentication.getPrincipal();
        }

        if (userDomain == null) {
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
    public void updatePaypal(Long userId, UserRequest request) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        userEntity.setPaypalId(request.getPaypalId());
        userRepository.save(userEntity);
    }

    @Override
    public void registerUser(UserRegister userRegister) {
        if (StringUtils.isEmpty(userRegister.getEmail()) || StringUtils.isEmpty(userRegister.getName()) || StringUtils.isEmpty(userRegister.getPassword())) {
            logger.error(StringUtils.buildLog(Error.REQUIRED_FIELD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.REQUIRED_FIELD.getMessage(), Error.REQUIRED_FIELD.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findByEmail(userRegister.getEmail().trim()).orElse(null);
        if (user != null) {
            logger.error(StringUtils.buildLog(Error.EXIST_EMAIL, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.EXIST_EMAIL.getMessage(), Error.EXIST_EMAIL.getCode(),
                    HttpStatus.BAD_REQUEST);
        }
        RoleEntity roleEntity = roleRepository.findByCode(Role.USER.getCode());
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRegister.getName());
        userEntity.setEmail(userRegister.getEmail());
        userEntity.setLinked(userRegister.getLinked());
        userEntity.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        userEntity.setRoleEntity(roleEntity);
        userRepository.save(userEntity);
    }

    @Override
    public Boolean checkAccount(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        return userEntity != null;
    }

    @Override
    public void changePassword(Long id, PasswordRequest passwordRequest) {

        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });

        if (!passwordEncoder.matches(passwordRequest.getOldPass(), userEntity.getPassword())) {
            logger.error(StringUtils.buildLog(Error.OLD_PASSWORD_IS_WRONG, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.OLD_PASSWORD_IS_WRONG.getMessage(), Error.OLD_PASSWORD_IS_WRONG.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        userEntity.setPassword(passwordEncoder.encode(passwordRequest.getNewPass()));
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDomain> getListAdmins() {
        return userRepository.findAllByRole(Arrays.asList(Role.ADMIN.getCode(), Role.SUPER_ADMIN.getCode())).stream().map(userEntity -> {
            UserDomain userDomain = mapper.map(userEntity, UserDomain.class);
            userDomain.setRole(mapper.map(userEntity.getRoleEntity(), RoleDomain.class));
            return userDomain;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDomain> getListUsers() {
        return userRepository.findAllByRole(Collections.singletonList(Role.USER.getCode())).stream().map(userEntity -> {
            UserDomain userDomain = mapper.map(userEntity, UserDomain.class);
            userDomain.setRole(mapper.map(userEntity.getRoleEntity(), RoleDomain.class));
            return userDomain;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseByName<String, Object> getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDomain userDomain = null;
        if (authentication.getPrincipal() instanceof UserDomain) {
            userDomain = (UserDomain) authentication.getPrincipal();
        }

        if (userDomain == null) {
            logger.error(StringUtils.buildLog(Error.BAD_CREDENTIALS, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.BAD_CREDENTIALS.getMessage(), Error.BAD_CREDENTIALS.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }
        return ResponseByName.Builder("account", userDomain).build();
    }

    @Override
    public UserDomain getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        return mapper.map(userEntity, UserDomain.class);
    }

    @Override
    public UserDomain registerAdmin( AdminRequest request) {
        if (StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getName()) || StringUtils.isEmpty(request.getPassword())) {
            logger.error(StringUtils.buildLog(Error.REQUIRED_FIELD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.REQUIRED_FIELD.getMessage(), Error.REQUIRED_FIELD.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findByEmail(request.getEmail().trim()).orElse(null);
        if (user != null) {
            logger.error(StringUtils.buildLog(Error.EXIST_EMAIL, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.EXIST_EMAIL.getMessage(), Error.EXIST_EMAIL.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        if (!Arrays.stream(Role.values()).map(Role::getCode).collect(Collectors.toList()).contains(request.getRoleCode())) {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        }

        RoleEntity roleEntity = roleRepository.findByCode(request.getRoleCode());

        user = new UserEntity();
        user.setRoleEntity(roleEntity);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setAvatar(request.getAvatar());
        user = userRepository.save(user);
        UserDomain userDomain  = mapper.map(user, UserDomain.class);
        userDomain.setRole(mapper.map(roleEntity, RoleDomain.class));
        return userDomain;
    }

    @Override
    public UserDomain updateAdmin(Long id, AdminRequest request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        if(request.getChangeStatus()) {
            userEntity.setStatus(request.getStatus());
        } else {
            userEntity.setAvatar(request.getAvatar());
            userEntity.setName(request.getName());
            userEntity.setEmail(request.getEmail());
        }
        userEntity = userRepository.save(userEntity);
        UserDomain userDomain = mapper.map(userEntity, UserDomain.class);
        userDomain.setRole(mapper.map(userEntity.getRoleEntity(), RoleDomain.class));
        return userDomain;
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        userRepository.delete(userEntity);
    }

    @Override
    public UserDomain changeStatus(Long id, ChangeStatusRequest request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        userEntity.setStatus(request.getStatus());
        userRepository.save(userEntity);
        UserDomain userDomain = mapper.map(userEntity, UserDomain.class);
        userDomain.setRole(mapper.map(userEntity.getRoleEntity(), RoleDomain.class));
        return userDomain;
    }


}

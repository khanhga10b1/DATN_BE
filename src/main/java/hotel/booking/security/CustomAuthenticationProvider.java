package hotel.booking.security;

import hotel.booking.domain.UserDomain;
import hotel.booking.entity.UserEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.UserRepository;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * The type Custom authentication provider.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    
    @Autowired
    private PasswordEncoder bCryptEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        UserDomain userDomain = new UserDomain();
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity != null) {
            userDomain.setId(userEntity.getId());
            userDomain.setEmail(userEntity.getEmail());
            userDomain.setPassword(userEntity.getPassword());
            userDomain.setPhone(userEntity.getPhone());
            userDomain.setAvatar(userEntity.getAvatar());
            userDomain.setAddress(userEntity.getAddress());
//            userDomain.setRoles(userEntity.getUserRoleChannels().stream()
//                    .map(userRoleChannelEntity -> userRoleChannelEntity.getRole().convertDomain())
//                    .collect(Collectors.toSet()));
            
            // Check login id and password
            if (bCryptEncoder.matches(password, userDomain.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDomain, password);
            } else {
                logger.error(StringUtils.buildLog(Error.INVALID_USERNAME_OR_PASSWORD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
                throw new CustomException(Error.INVALID_USERNAME_OR_PASSWORD.getMessage(),
                        Error.INVALID_USERNAME_OR_PASSWORD.getCode(),
                        HttpStatus.UNAUTHORIZED);
            }

          
        } else {
            logger.error(StringUtils.buildLog(Error.INVALID_USERNAME_OR_PASSWORD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.INVALID_USERNAME_OR_PASSWORD.getMessage(),
                    Error.INVALID_USERNAME_OR_PASSWORD.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}

package hotel.booking.security;


import hotel.booking.constant.ConstantDefine;
import hotel.booking.domain.UserDomain;
import hotel.booking.entity.UserEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.UserRepository;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

/**
 * The type Token provider.
 */
@Component
public class TokenProvider implements Serializable {


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    @Value("${jwt-token.expiration}")
    private Long JWT_EXPIRATION;
    @Autowired
    private UserRepository userRepository;

    public String generateToken(Authentication authentication) {
        Date now = new Date();
        UserDomain userDomain = (UserDomain) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDomain.getEmail())
                .setId(userDomain.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(ConstantDefine.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(ConstantDefine.BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error(StringUtils.buildLog(Error.TOKEN_INVALID, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.TOKEN_INVALID.getMessage(), Error.TOKEN_INVALID.getCode(),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token) {
        String email = getUsernameFromToken(token);
        UserDomain userDomain = new UserDomain();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.TOKEN_INVALID, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.TOKEN_INVALID.getMessage(), Error.TOKEN_INVALID.getCode(),
                    HttpStatus.UNAUTHORIZED);
        });
        userDomain.setEmail(userEntity.getEmail());
        userDomain.setPassword(userEntity.getPassword());
        userDomain.setId(userEntity.getId());
        userDomain.setName(userEntity.getName());
        userDomain.setStatus(userEntity.getStatus());
        userDomain.setPhone(userEntity.getPhone());
        userDomain.setAvatar(userEntity.getAvatar());
        userDomain.setAddress(userEntity.getAddress());

        return new UsernamePasswordAuthenticationToken(userDomain, "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Long getIdFromToken(String token) {
        return Long.parseLong(getClaimFromToken(token, Claims::getId));
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}

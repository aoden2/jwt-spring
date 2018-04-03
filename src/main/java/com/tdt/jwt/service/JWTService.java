package com.tdt.jwt.service;

import com.tdt.jwt.model.UserAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    @Autowired
    Environment env;
    @Autowired
    UserAuthService userAuthService;

    public UserAuth parseUserFromToken(String token) throws AuthenticationException {

        String username = Jwts.parser()
                .setSigningKey(env.getProperty("jwt.secret"))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return userAuthService.findById(username);

    }

    public String createTokenForUser(Object user) {

        if (user instanceof User) {
            return buildToken(((User) user).getUsername());
        } else if (user instanceof String) {
            return buildToken((String) user);
        } else {
            return buildToken(((UserAuth) user).getUsername());
        }

    }

    private String buildToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS256, env.getProperty("jwt.secret"))
                .setExpiration(new DateTime().plusSeconds(Integer.parseInt(env.getProperty("jwt.duration"))).toDate())
                .compact();
    }
}
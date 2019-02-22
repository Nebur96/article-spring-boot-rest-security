package com.nebur.spring.article.security;

import com.nebur.spring.article.enums.Role;
import com.nebur.spring.article.exceptions.InvalidTokenException;
import com.nebur.spring.article.service.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.nebur.spring.article.constants.Constants.HEADER_STRING;
import static com.nebur.spring.article.constants.Constants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ClientService clientService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //get HTTP request header
        String header = req.getHeader(HEADER_STRING);

        String username = null;
        String authToken = null;

        //check if header exist and stats with the defined TOKEN PREFIX -> Bearer
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            //remove the word Bearer in order to isolate the JWT Token
            authToken = header.replace(TOKEN_PREFIX,"");
            try {
                //try to decrypt the JWT TOKEN with the key with which it was signed
                username = jwtTokenUtil.getUsernameFromToken(authToken);

                //if by any reason it fail will generate an exception and it will be throw an Exception
                //where will be possible to find JWT Tokens signed with different keys or tokens that already expired
            } catch (IllegalArgumentException e) {
                throw new InvalidTokenException();
            }
        }
        //check if it was possible to retrieve a username from the JWT Token and if there is not already a authentication set
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //clientService implements UserDetailsService which will allow us to retrieve user credentials from Database
            UserDetails userDetails = clientService.loadUserByUsername(username);

            //check if data retrieved from token matches with the one founded on Database
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                //authentication and persist of user with Spring Security to keep him authenticated
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getRole())));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}
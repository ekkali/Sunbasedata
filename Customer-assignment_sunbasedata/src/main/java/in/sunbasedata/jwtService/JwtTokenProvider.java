package in.sunbasedata.jwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import in.sunbasedata.service.impl.UserDetailServiceImpl;

@Component
public class JwtTokenProvider {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(String username, String password) throws Exception {

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (UsernameNotFoundException e) {
            throw new Exception("user not found");
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);
        return token;
    }
}

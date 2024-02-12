package in.sunbasedata.jwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import in.sunbasedata.service.impl.UserDetailServiceImpl;

import java.io.IOException;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    private static final String[] excluded_urls = {
            "/login",
            "/process-login",
            "signup",
            "/process-signup",
            "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js", "/*.jsp", "/**/*.jsp"
    };


    // Check if the request should not be filtered based on the URL.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String url = request.getRequestURI();
        return Stream.of(excluded_urls).anyMatch(x -> pathMatcher.match(x, url));
    }

    // Perform JWT authentication for incoming requests.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the UserName");
            } catch (ExpiredJwtException e) {
                logger.info("Given Jwt Token is Expired");
            } catch (MalformedJwtException e) {
                logger.info("Some changes are made in the token !! Invalid Token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Token Missing or Invalid ");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
            Boolean validateToken = jwtUtil.validateToken(token, userDetails);

            if (validateToken) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation Fail !!");
            }
        }
        filterChain.doFilter(request, response);
    }


}

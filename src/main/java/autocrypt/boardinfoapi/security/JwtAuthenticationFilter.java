package autocrypt.boardinfoapi.security;

import autocrypt.boardinfoapi.common.constants.SecurityConstants;
import autocrypt.boardinfoapi.common.exception.ErrorResult;
import autocrypt.boardinfoapi.common.exception.JwtAuthenticationException;
import autocrypt.boardinfoapi.security.JwtUtil;
import autocrypt.boardinfoapi.security.property.JwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtUtil jwtUtil;
    private final String authenticateUrlPrefix;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, String authenticateUrlPrefix){
        super(authenticateUrlPrefix);
        this.jwtUtil = jwtUtil;
        this.authenticateUrlPrefix = authenticateUrlPrefix;
    }

    @Autowired
    MappingJackson2HttpMessageConverter messageConverter;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(checkSkipUrl(httpServletRequest)){
            chain.doFilter(request, response);
            return;
        }

        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = getToken(request);
        if(token == null){
            throw new JwtAuthenticationException("Token Is Empty!");
        }

        JwtPrincipal jwtPrincipal = jwtUtil.parseToken(token);
        return new UsernamePasswordAuthenticationToken(jwtPrincipal, null);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        messageConverter.write(new ErrorResult("UNAUTHORIZED", failed.getMessage()),
                MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }

    private boolean checkSkipUrl(HttpServletRequest request){
        return SecurityConstants.JWT_FILTER_SKIP_URL.contains(request.getRequestURI());
    }

    private String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken==null ){
            bearerToken="";
        }
        if( bearerToken.startsWith(SecurityConstants.BEARER)){
            return bearerToken.replace(SecurityConstants.BEARER,"");
        }
        return null;
    }
}

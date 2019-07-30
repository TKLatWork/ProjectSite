package my.site.account.ctrl;

import my.site.account.service.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 对于/api开头的地址，对返回进行包装
 */
public class ApiResultFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public ApiResultFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String url = ((HttpServletRequest)req).getRequestURI();
        filterChain.doFilter(req, res);

    }
}

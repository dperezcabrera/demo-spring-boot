package com.example.demo.logging;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

    public static final String MDC_REQ_ID = "req.id";
    public static final String MDC_USERNAME = "username";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication != null) {
                MDC.put(MDC_USERNAME, authentication.getName());
            }
            if (MDC.get(MDC_REQ_ID) == null) {
                MDC.put(MDC_REQ_ID, UUID.randomUUID().toString());
            }
            chain.doFilter(req, resp);
            time = System.currentTimeMillis() - time;
            log.debug("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(), time);
        } catch (Exception e) {
            time = System.currentTimeMillis() - time;
            log.error("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(), time);
            throw e;
        } finally {
            if (authentication != null) {
                MDC.remove(MDC_USERNAME);
            }
            MDC.remove(MDC_REQ_ID);
        }
    }

    @Override
    public void destroy() {
        // empty
    }
}

package com.xpu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class UserFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getServletPath();
        System.out.println(path);
        if ("/image".equals(path) || "/index.html".equals(path)){
            HttpSession session = request.getSession(false);
            if (session == null){
                String scheme = request.getScheme();//应用层协议
                String serverName = request.getServerName();
                int serverPort = request.getServerPort();
                String contextPath = request.getContextPath();
                String basePath = scheme+"://"+serverName+":"+serverPort+"/"+contextPath;
                System.out.println("无session"+basePath);
                ((HttpServletResponse)resp).sendRedirect(basePath+"/login.html");
                return;
            }
            System.out.println("有session");
//            chain.doFilter(req,resp);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

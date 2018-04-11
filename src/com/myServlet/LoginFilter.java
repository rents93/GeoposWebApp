package com.myServlet;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/positions")
public class LoginFilter extends HttpFilter {

    public void init(FilterConfig fc){
    }

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if (req.getSession().getAttribute("username") == null)
            resp.sendRedirect("login.html");
        else
            chain.doFilter(req, resp);

        //ciao
    }

    public void destroy(){

    }
}
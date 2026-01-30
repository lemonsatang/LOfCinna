package com.mysite.lofcinna;

import com.mysite.lofcinna.mapper.MainMapper;
import com.mysite.lofcinna.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MainMapper mainMapper;

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        String username = "";

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }

        HttpSession session = request.getSession();
        Optional<User> opt = mainMapper.findByusername(username);
        session.setAttribute("nickname",opt.get().getName());

        setDefaultTargetUrl("/");
        // 1️⃣ 기존 시큐리티의 savedRequest 확인
        SavedRequest savedRequest = requestCache.getRequest(request,response);

        // 2️⃣ 직접 저장한 prevPage 가져오기
        String prevPage = (String) session.getAttribute("prevPage");

        System.out.println("============================"+prevPage);

        if (savedRequest != null) {
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        } else if (prevPage != null) {
            if(prevPage.equals("http://localhost:8080/main/signUp")){
                redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
            }else{
                redirectStrategy.sendRedirect(request, response, prevPage);
            }
            session.removeAttribute("prevPage"); // 재사용 방지

        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}

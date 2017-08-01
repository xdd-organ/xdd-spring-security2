package com.hp.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "code1";
    public static final String SESSION_GENERATED_CAPTCHA_KEY = "code2";

    private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;  

    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        String genCode = this.obtainGeneratedCaptcha(request);  
        String inputCode = this.obtainCaptcha(request);
        String rememberMe = request.getParameter("rememberMe");
        if(StringUtils.isEmpty(genCode)) {
            //i18n
            //throw new RuntimeException(this.messages.getMessage("LoginAuthentication.captchaInvalid"));
            //throw new RuntimeException("没有验证码");
        //if(!genCode.equalsIgnoreCase(inputCode)){
            //throw new RuntimeException(this.messages.getMessage("LoginAuthentication.captchaNotEquals"));
        }

        return super.attemptAuthentication(request, response);  
    }  

    protected String obtainCaptcha(HttpServletRequest request){  
        return request.getParameter(this.captchaParameter);  
    }  

    protected String obtainGeneratedCaptcha (HttpServletRequest request){  
        //return (String)request.getSession().getAttribute(SESSION_GENERATED_CAPTCHA_KEY);
        return request.getParameter(SESSION_GENERATED_CAPTCHA_KEY);
    }  

} 
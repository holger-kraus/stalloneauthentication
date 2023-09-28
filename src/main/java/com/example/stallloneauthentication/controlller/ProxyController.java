package com.example.stallloneauthentication.controlller;

import com.example.stallloneauthentication.services.LoginService;
import com.example.stallloneauthentication.services.OAuthService;
import com.example.stallloneauthentication.services.ProxyService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import responses.Tokens;

import java.net.URISyntaxException;
import java.util.UUID;

@Controller
public class ProxyController {
    @Autowired
    ProxyService service;

    @Autowired
    OAuthService oAuthService;
    @Autowired
    LoginService loginService;

    @RequestMapping("/**")
    public ResponseEntity<String> sendRequestToSPM(@RequestBody(required = false) String body,
                                                   HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {
        return service.processProxyRequest(body,method,request,response, UUID.randomUUID().toString());
    }

    @GetMapping("/callback")
    public String getResponse(@RequestParam("code") String code, HttpServletResponse response) throws URISyntaxException {
      ResponseEntity<Tokens> tokens = oAuthService.getAccessToken(code);
//        Cookie cookie = new Cookie("access_token", tokens.getBody().getAccessToken());
//        response.addCookie(cookie);
        return "holger";
    }

    @GetMapping("/login")
    public ResponseEntity<String> initiateLogin(HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        return  loginService.login(null, method, request, response);
    }



}

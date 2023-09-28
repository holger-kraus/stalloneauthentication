package com.example.stallloneauthentication.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LoginService {

    public ResponseEntity<String> login(String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        //log if required in this line
        URI uri;
        uri = new URI("http", null, "localhost", 8080, null, null, null);

        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("client_id", "myclient");
        params.add("redirect_uri", "http://localhost:8081/callback");
        params.add("state","aa2a27b3-9dac-401a-8a16-4071a213dbdf");
        params.add("response_mode", "query");
        params.add("response_type", "code");
        params.add("scope", "openid");
        params.add("nonce","b722e7eb-205e-4dc8-80aa-155ed212bfd2");

        // replacing context path form urI to match actual gateway URI
        uri = UriComponentsBuilder.fromUri(uri)
                .path("/auth/realms/myrealm/protocol/openid-connect/auth")
                .queryParams(params)
                .build(true).toUri();
        System.out.println(uri.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>(null, null);
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);

        ResponseEntity<String> serverResponse = restTemplate.exchange(uri, method, httpEntity, String.class);

        return serverResponse;
    }
}

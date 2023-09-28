package com.example.stallloneauthentication.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import responses.Tokens;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class OAuthService {
    private final static Logger logger = LogManager.getLogger(OAuthService.class);

    public ResponseEntity<Tokens> getAccessToken(String authorizationGrant) throws URISyntaxException {
        //log if required in this line
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);

        URI url = new URI("http", null, "localhost", 8080, null, null, null);

        url = UriComponentsBuilder.fromUri(url)
                .path("/auth/realms/myrealm/protocol/openid-connect/token")
                .build(true).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);



        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("client_id", "myclient");
        params.add("redirect_uri", "http://localhost:8081/callback");
        params.add("grant_type","authorization_code");
        params.add("client_secret", "");
        params.add("code", authorizationGrant);
        HttpMethod method = HttpMethod.POST;

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<Tokens> serverResponse = restTemplate.exchange(url, method, request, Tokens.class);
        Tokens tokens =  serverResponse.getBody();
        System.out.println(serverResponse);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//        map.add("email", "first.last@example.com");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        return serverResponse;
    }
}

package net.skhu.tastyinventory_be.oauth2.service;

import net.skhu.tastyinventory_be.exception.ErrorCode;
import net.skhu.tastyinventory_be.exception.model.OAuth2RequestFailedException;
import net.skhu.tastyinventory_be.oauth2.ClientRegistration;
import net.skhu.tastyinventory_be.oauth2.OAuth2Token;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class NaverOAuth2Service extends OAuth2Service {
    public NaverOAuth2Service(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public void unlink(ClientRegistration clientRegistration, OAuth2Token token) {
        token = refreshOAuth2Token(clientRegistration, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("client_id", clientRegistration.getClientId());
        params.add("client_secret", clientRegistration.getClientSecret());
        params.add("grant_type", "delete");
        params.add("service_provider", "NAVER");
        params.add("access_token", token.getToken());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.exchange(clientRegistration.getProviderDetails().getUnlinkUri(), HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            throw new OAuth2RequestFailedException(
                    ErrorCode.REQUEST_VALIDATION_EXCEPTION,
                    String.format(
                            "%s 연동 해제 실패 [응답코드 : %d].",
                            clientRegistration.getRegistrationId().toUpperCase(), statusCode
                    )
            );
        }
    }
}

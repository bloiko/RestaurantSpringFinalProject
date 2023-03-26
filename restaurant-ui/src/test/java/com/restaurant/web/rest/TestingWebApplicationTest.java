package com.restaurant.web.rest;

import com.restaurant.RestaurantApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestaurantApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingWebApplicationTest {

    public static final String VALID_BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjp7ImF1dGhvcml0eSI6IkFETUlOIn0sImlhdCI6MTY3OTgzMzQwNCwiZXhwIjoxNjc5ODUxNDA0fQ.MyXqiCzhuaASOcffSClkwTaSJ2bZYimI12Y9o--ndBE";

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnForbiddenStatus() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, singletonList("Bearer invalidToken"));
        URI url = new URI("http://localhost:" + port + "/menu1");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(403);
    }

    @Test
    public void shouldReturnBadRequestStatus() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, singletonList("not formatted token"));
        URI url = new URI("http://localhost:" + port + "/menu1");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    public void shouldReturnSuccessWhenDoReportOrders() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, singletonList(VALID_BEARER_TOKEN));
        URI url = new URI("http://localhost:" + port + "/report/orders/month");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void shouldReturnSuccessWhenDoReportUsers() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, singletonList(VALID_BEARER_TOKEN));
        URI url = new URI("http://localhost:" + port + "/report/users");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }
}
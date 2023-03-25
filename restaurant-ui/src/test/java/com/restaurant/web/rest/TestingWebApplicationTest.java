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

    @Value(value="${local.server.port}")
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
}
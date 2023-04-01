package com.restaurant.web.rest;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestaurantApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingWebApplicationTest {

    public static final String USER_NAME = "usernameTest";

    public static String validBearerToken = null;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() {
        Optional<User> optionalUser = userRepository.findByUserName(USER_NAME);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            validBearerToken = "Bearer " + jwtProvider.createToken(user.getUserName(), user.getRole());
        }else{
            throw new RuntimeException();
        }
    }

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
        headers.put(HttpHeaders.AUTHORIZATION, singletonList(validBearerToken));
        URI url = new URI("http://localhost:" + port + "/report/orders/month");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void shouldReturnSuccessWhenDoReportUsers() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, singletonList(validBearerToken));
        URI url = new URI("http://localhost:" + port + "/report/users");
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }
}
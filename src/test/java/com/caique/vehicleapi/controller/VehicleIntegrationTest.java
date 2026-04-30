package com.caique.vehicleapi.controller;

import com.caique.vehicleapi.dto.LoginRequest;
import com.caique.vehicleapi.dto.VehicleRequest;
import com.caique.vehicleapi.model.AppUser;
import com.caique.vehicleapi.repository.UserRepository;
import com.caique.vehicleapi.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        vehicleRepository.deleteAll();

        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    private void createUser(String username, String password, List<String> roles) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }

    private String loginAndGetToken(String username, String password) throws Exception {

        LoginRequest login = new LoginRequest(username, password);

        String responseBody = webTestClient.post()
                .uri("/auth/login")
                .bodyValue(login)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        return objectMapper.readTree(responseBody)
                .get("token")
                .asText();
    }

    @Test
    void shouldCreateVehicle_withAdminJWT() throws Exception {

        createUser("admin", "123", List.of("ROLE_ADMIN"));

        String token = loginAndGetToken("admin", "123");

        VehicleRequest vehicle = new VehicleRequest(
                "Fiat", "Stilo", 2008, "Gray", 25000.0, "ABC1D23"
        );

        webTestClient.post()
                .uri("/vehicles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.brand").isEqualTo("Fiat");
    }

    @Test
    void shouldFailWithoutToken() {

        VehicleRequest vehicle = new VehicleRequest(
                "Fiat", "Stilo", 2008, "Gray", 25000.0, "ABC1D23"
        );

        webTestClient.post()
                .uri("/vehicles")
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailCreateVehicle_withUserRole() throws Exception {

        createUser("user", "123", List.of("ROLE_USER"));

        String token = loginAndGetToken("user", "123");

        VehicleRequest vehicle = new VehicleRequest(
                "Fiat", "Uno", 2010, "Black", 15000.0, "ABC1D23"
        );

        webTestClient.post()
                .uri("/vehicles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldFailWithInvalidToken() {

        VehicleRequest vehicle = new VehicleRequest(
                "Fiat", "Uno", 2010, "Black", 15000.0, "ABC1D23"
        );

        webTestClient.post()
                .uri("/vehicles")
                .header("Authorization", "Bearer lixo.total")
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailLoginWithWrongPassword() {

        createUser("admin", "123", List.of("ROLE_ADMIN"));

        LoginRequest login = new LoginRequest("admin", "wrong");

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(login)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldAllowUserToGetVehicles() throws Exception {

        createUser("user", "123", List.of("ROLE_USER"));

        String token = loginAndGetToken("user", "123");

        webTestClient.get()
                .uri("/vehicles")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFailWhenPlateDuplicated() throws Exception {

        createUser("admin", "123", List.of("ROLE_ADMIN"));
        String token = loginAndGetToken("admin", "123");

        VehicleRequest vehicle = new VehicleRequest(
                "Fiat", "Uno", 2010, "Black", 20000.0, "ABC1D23"
        );

        // first create
        webTestClient.post()
                .uri("/vehicles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isCreated();

        // second create (duplicated)
        webTestClient.post()
                .uri("/vehicles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(vehicle)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void shouldReturnPagedVehicles() throws Exception {

        createUser("user", "123", List.of("ROLE_USER"));
        String token = loginAndGetToken("user", "123");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/vehicles")
                        .queryParam("page", 0)
                        .queryParam("size", 5)
                        .queryParam("sort", "price,desc")
                        .build()
                )
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").exists()
                .jsonPath("$.size").isEqualTo(5);
    }
}
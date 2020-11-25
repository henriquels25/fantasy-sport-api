package io.henriquels25.fantasysport.player.infra.client;

import io.henriquels25.fantasysport.player.TeamRepository;
import io.henriquels25.fantasysport.player.exception.TeamServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(classes = WebClientAutoConfiguration.class)
@ActiveProfiles("test")
@Import(TeamClient.class)
@AutoConfigureWireMock(port = 0)
class TeamClientTest {

    @Autowired
    private TeamRepository client;

    @Test
    void shouldReturnTrueWhenTeamExists() {
        stubFor(get(urlEqualTo("/teams/existsTeamId"))
                .willReturn(aResponse().withBodyFile("exists_team.json")
                        .withHeader("Content-Type", "application/json")));

        StepVerifier.create(client.exists("existsTeamId"))
                .expectNext(true)
                .verifyComplete();

        verify(getRequestedFor(urlEqualTo("/teams/existsTeamId")));
    }

    @Test
    void shouldReturnFalseWhenTeamNotExists() {
        stubFor(get(urlEqualTo("/teams/fakeTeamId"))
                .willReturn(aResponse().withStatus(404)));

        StepVerifier.create(client.exists("fakeTeamId"))
                .expectNext(false)
                .verifyComplete();

        verify(getRequestedFor(urlEqualTo("/teams/fakeTeamId")));
    }

    @Test
    void shouldReturnErrorWhenAPINotAvailable() {
        stubFor(get(urlEqualTo("/teams/errorTeamId"))
                .willReturn(aResponse().withStatus(500)));

        StepVerifier.create(client.exists("errorTeamId"))
                .expectError(TeamServiceUnavailableException.class)
                .verify();
    }

}

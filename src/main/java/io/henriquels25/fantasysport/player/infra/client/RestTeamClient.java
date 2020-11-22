package io.henriquels25.fantasysport.player.infra.client;

import io.henriquels25.fantasysport.player.exception.TeamServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
class RestTeamClient implements TeamClient {
    private final WebClient webClient;

    public RestTeamClient(WebClient.Builder webClientBuilder,
                          @Value("${services.teams.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Boolean> exists(String teamId) {
        return webClient.get().uri("teams/{teamId}", teamId)
                .exchange().map(this::convertResponse);
    }

    private boolean convertResponse(ClientResponse clientResponse) {
        switch (clientResponse.statusCode().value()) {
            case 200:
                return true;
            case 404:
                return false;
            default:
                throw new TeamServiceUnavailableException();
        }
    }
}

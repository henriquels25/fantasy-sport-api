package io.henriquels25.fantasysport.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.fernando;
import static io.henriquels25.fantasysport.player.factories.PlayerFactory.henrique;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerFacadeTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerFacade facade;

    @Test
    void shouldReturnTheListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(Flux.just(henrique(), fernando()));

        StepVerifier.create(facade.allPlayers())
                .expectNext(henrique())
                .expectNext(fernando())
                .expectComplete()
                .verify();
    }

}

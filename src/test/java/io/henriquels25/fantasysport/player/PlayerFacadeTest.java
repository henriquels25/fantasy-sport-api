package io.henriquels25.fantasysport.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static org.mockito.Mockito.verify;
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

    @Test
    void shouldSaveANewPlayer() {
        when(playerRepository.save(diego())).thenReturn(Mono.just("id1"));

        StepVerifier.create(facade.create(diego()))
                .expectNext("id1")
                .expectComplete()
                .verify();

        verify(playerRepository).save(diego());
    }

    @Test
    void shouldUpdateAPlayer() {
        when(playerRepository.update("id1", diego())).thenReturn(Mono.empty());

        StepVerifier.create(facade.update("id1", diego()))
                .expectComplete()
                .verify();

        verify(playerRepository).update("id1", diego());
    }


    @Test
    void shouldDeleteAPlayer() {
        when(playerRepository.delete("id1")).thenReturn(Mono.empty());

        StepVerifier.create(facade.delete("id1"))
                .expectComplete()
                .verify();

        verify(playerRepository).delete("id1");
    }

    @Test
    void shouldFindAPlayerById() {
        when(playerRepository.findById("id1")).thenReturn(Mono.just(henrique()));

        StepVerifier.create(facade.findById("id1"))
                .expectNext(henrique())
                .verifyComplete();

        verify(playerRepository).findById("id1");
    }
}

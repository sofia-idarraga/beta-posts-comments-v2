package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases;

import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.DomainViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BringPostByIdUseCaseTest {

    @Mock
    DomainViewRepository repository;

    @Mock
    BringPostByIdUseCase useCase;

    @BeforeEach
    void init() {
        useCase = new BringPostByIdUseCase(repository);
    }

    @Test
    @DisplayName("BringPostByIdTest")
    void bringPostByIdTest() {
        PostViewModel post = new PostViewModel("1", "Sofia", "Post1 Test", new ArrayList<>());

        Mono<PostViewModel> responseExpected = Mono.just(post);

        Mockito.when(repository.findByAggregateId(Mockito.anyString())).thenReturn(responseExpected);

        var useCaseExecute = useCase.apply(Mono.just("1"));

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(postViewModel -> postViewModel.getAggregateId().equals("1") &&
                        postViewModel instanceof PostViewModel)
                .expectComplete().verify();
        Mockito.verify(repository).findByAggregateId(Mockito.anyString());
    }
}
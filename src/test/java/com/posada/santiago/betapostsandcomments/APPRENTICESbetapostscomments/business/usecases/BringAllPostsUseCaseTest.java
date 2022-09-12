package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases;

import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.DomainViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BringAllPostsUseCaseTest {

    @Mock
    DomainViewRepository repository;

    @Mock
    BringAllPostsUseCase useCase;

    @BeforeEach
    void init() {
        useCase = new BringAllPostsUseCase(repository);
    }

    @Test
    @DisplayName("BringAllPostsTest")
    void bringAllPostsUseCaseTest() {

        ArrayList<PostViewModel> posts = new ArrayList<>();
        posts.add(new PostViewModel("1", "Sofia", "Post1 Test", new ArrayList<>()));
        posts.add(new PostViewModel("2","Sofia","Post2Test",new ArrayList<>()));
        Flux<PostViewModel> responseExpected = Flux.fromIterable(posts);

        Mockito.when(repository.findAllPosts()).thenReturn(responseExpected);

        var useCaseExecute = useCase.apply().collectList();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(list->
                        list.get(0).getAggregateId().equals("1") &&
                 list.get(1).getAggregateId().equals("2") &&
                        list.get(0) instanceof PostViewModel)
                .expectComplete().verify();
        Mockito.verify(repository).findAllPosts();
    }

}
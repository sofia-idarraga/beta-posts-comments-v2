package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases;

import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.DomainViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.EventBus;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.CommentViewModel;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.domain.events.CommentAdded;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.domain.events.PostCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UpdateViewUseCaseTest {

    @Mock
    DomainViewRepository repository;
    @Mock
    EventBus eventBus;
    @Mock
    ViewUpdater viewUpdater;
    @Mock
    UpdateViewUseCase useCase;

    @BeforeEach
    void init() {
        viewUpdater = new ViewUpdater(repository, eventBus);
        useCase = new UpdateViewUseCase(viewUpdater);
    }


    @Test
    @DisplayName("UpdateViewPostCreated")
    void updateViewPostCreated() {

        var postCreatedEvent = new PostCreated("Test", "Sofia");
        postCreatedEvent.setAggregateRootId("0");

        var post = new PostViewModel("0","Sofia","Test", new ArrayList<>());

        Mono<PostViewModel> responseExpected = Mono.just(post);

        Mockito.when(repository.saveNewPost(Mockito.any(PostViewModel.class)))
                .thenReturn(responseExpected);

        useCase.accept(postCreatedEvent);

        Mockito.verify(repository).saveNewPost(Mockito.any(PostViewModel.class));

        Mockito.verify(eventBus).publishPostCreated(Mockito.any(PostViewModel.class));
    }


    @Test
    @DisplayName("UpdateViewCommentAdded")
    void updateViewCommentAdded(){

        var commentAdded = new CommentAdded("1","Sofia","Test");

        commentAdded.setAggregateRootId("0");

        var post = new PostViewModel("0","Sofia","Test", new ArrayList<>());

        var comment = new CommentViewModel("1","0","Sofia","Test");

        post.setComments(List.of(comment));

        Mono<PostViewModel> responseExpected = Mono.just(post);

        Mockito.when(repository.addCommentToPost(Mockito.any(CommentViewModel.class)))
                .thenReturn(responseExpected);

        useCase.accept(commentAdded);

        Mockito.verify(repository).addCommentToPost(Mockito.any(CommentViewModel.class));
        Mockito.verify(eventBus).publishCommentAdded(Mockito.any(CommentViewModel.class));
    }

}
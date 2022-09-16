package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.application.handlers;


import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases.BringAllPostsUseCase;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases.BringPostByIdUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class QueryHandler {

    //Create a route that allows you to make a Get Http request that brings you all the posts and also a post by its id

    @Bean
    public RouterFunction<ServerResponse> getPostById(BringPostByIdUseCase useCase) {
        return route(
                GET("/post/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply(Mono.just(request.pathVariable("id")))
                        .flatMap(postViewModel -> {
                            log.info("Post found with id " + postViewModel.getAggregateId());
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(postViewModel);
                        })
                        .onErrorResume(error -> {
                            log.error(error.getLocalizedMessage());
                            return ServerResponse.badRequest().build();
                        })
        );

        // ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        // .body(BodyInserters.fromPublisher(useCase.apply(Mono.just(request.pathVariable("id"))), PostViewModel.class))
        // .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> getAllPosts(BringAllPostsUseCase useCase) {
        return route(
                GET("/posts").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply()
                        .collectList()
                        .flatMap(posts -> {
                            log.info("Posts found");
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                    .body(BodyInserters.fromPublisher(useCase.apply(), PostViewModel.class));
                        })
                        .onErrorResume(error -> {
                            log.error(error.getLocalizedMessage());
                            return ServerResponse.badRequest().build();
                        })

                // ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                // .body(BodyInserters.fromPublisher(useCase.apply(), PostViewModel.class))
        );
    }

}

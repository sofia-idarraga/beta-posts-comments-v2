package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases;


import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.application.adapters.repository.MongoViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.DomainViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BringAllPostsUseCase {
    //Finish the implementation of this class using the functional interfaces

    private final DomainViewRepository repository;

    public BringAllPostsUseCase(DomainViewRepository repository) {
        this.repository = repository;
    }

    public Flux<PostViewModel> apply(){

        return repository.findAllPosts()
                .switchIfEmpty(Flux.error(new Throwable("Anything found")));
    }
}

package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UpdateViewUseCase implements Consumer<DomainEvent>{

    private final ViewUpdater updater;

    public UpdateViewUseCase(ViewUpdater updater) {
        this.updater = updater;
    }

    // This use case implements Consumer, that's why we need to add the accept method
    // All consumers do "something" and return anything, that's why this method is a void.
    // Accept will publish the event (to Rabbit) and apply it to the view updater
    @Override
    public void accept(DomainEvent domainEvent){
        this.updater.applyEvent(domainEvent);
    }
}

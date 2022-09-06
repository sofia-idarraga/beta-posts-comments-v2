package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.application.handlers;



import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.application.adapters.bus.Notification;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.usecases.UpdateViewUseCase;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class QueueHandler implements Consumer<String> {
    private final Gson gson = new Gson();
    private final UpdateViewUseCase useCase;

    public QueueHandler(UpdateViewUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void accept(String received) {
        //Finish the implementation of this Method
        Notification notification = gson.fromJson(received, Notification.class);
        String type=  notification.getType().replace("sofka.alphapostcomments", "posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments");
        DomainEvent event = null;
        try {
            event = (DomainEvent) gson.fromJson(notification.getBody(), Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        useCase.accept(event);
    }
}

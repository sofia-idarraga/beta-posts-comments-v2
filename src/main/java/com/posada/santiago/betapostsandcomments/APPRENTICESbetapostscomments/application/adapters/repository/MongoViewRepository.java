package com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.application.adapters.repository;


import com.google.gson.Gson;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.DomainViewRepository;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.CommentViewModel;
import com.posada.santiago.betapostsandcomments.APPRENTICESbetapostscomments.business.gateways.model.PostViewModel;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Repository
public class MongoViewRepository implements DomainViewRepository {
    private final ReactiveMongoTemplate template;

    private final Gson gson = new Gson();

    public MongoViewRepository(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<PostViewModel> findByAggregateId(String aggregateId) {
        /**Make the implementation, using the template, to find a post by its aggregateId*/
        var query = new Query(Criteria.where("aggregateId").is(aggregateId));

        return template.findOne(query,PostViewModel.class);
    }

    @Override
    public Flux<PostViewModel> findAllPosts() {
        /**make the implementation, using the template, of the method find all posts that are stored in the db*/
        return template.findAll(PostViewModel.class);
    }

    @Override
    public Mono<PostViewModel> saveNewPost(PostViewModel post) {
        /** make the implementation, using the template, to save a post*/
        return template.save(post);
    }

    @Override
    public Mono<PostViewModel> addCommentToPost(CommentViewModel comment) {
        /** make the implementation, using the template, to find the post in the database that you want to add the comment to,
         * then add the comment to the list of comments and using the Update class update the existing post
         * with the new list of comments*/

        return this.findByAggregateId(comment.getPostId())
                .flatMap(postViewModel -> {
                    var list = postViewModel.getComments();
                    list.add(comment);
                    postViewModel.setComments(list);
                    return template.save(postViewModel);
                });
    }
}

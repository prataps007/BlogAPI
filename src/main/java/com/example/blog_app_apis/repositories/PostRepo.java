package com.example.blog_app_apis.repositories;

import com.example.blog_app_apis.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface PostRepo extends MongoRepository<Post,String> {

    //List<Post> findByUserId(User user);
    List<Post> findByUserId(String userId);

    // Custom query to find posts by the 'id' field inside the 'category' object
    @Query("{ 'category.$id': ObjectId(?0) }")   // we have declared 'id' inside category as 'categoryId' -> so, this line is necessary
    List<Post> findByCategoryId(String categoryId);

    List<Post> findByTitleContaining(String title);

    // for draft post publishing
    List<Post> findAllByIsDraftTrueAndScheduledPublishTimeBeforeAndAddedDateIsNull(Date now);
}

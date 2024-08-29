package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.CommentDto;
import com.example.blog_app_apis.payloads.UserDto;
import com.example.blog_app_apis.services.CommentService;
import com.example.blog_app_apis.services.ContentModerationService;
import com.example.blog_app_apis.services.NotificationService;
import com.example.blog_app_apis.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    private ContentModerationService contentModerationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostService postService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto, @PathVariable String postId){ //  @PathVariable String userId

        String commentText = commentDto.getContent();
        double toxicityScore = contentModerationService.analyzeText(commentText);

        if (toxicityScore > 0.5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Comment is inappropriate due to toxic content: " + toxicityScore);
        }

        CommentDto createComment = commentService.createComment(commentDto,postId); // userId

        // Get the post author's user (assuming postService can provide it)
        UserDto postAuthor = postService.getPostById(postId).getUser();

        // Create a notification
        notificationService.createNotification(postAuthor, "A new comment on your post.");

        return new ResponseEntity<>(createComment, HttpStatus.CREATED);

    }


    // ONLY ADMIN OR MODERATOR or  THE USER WHO CREATED THE COMMENT CAN DELETE THE SAME COMMENT
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or @commentServiceImpl.isCommentOwner(#commentId)")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully !!",true), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable String postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // get comment by id

}

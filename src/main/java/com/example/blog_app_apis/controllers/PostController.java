package com.example.blog_app_apis.controllers;

import com.example.blog_app_apis.config.AppConstants;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.payloads.ApiResponse;
import com.example.blog_app_apis.payloads.PostDto;
import com.example.blog_app_apis.payloads.PaginatedApiResponse;
import com.example.blog_app_apis.services.ContentModerationService;
import com.example.blog_app_apis.services.FileService;
import com.example.blog_app_apis.services.PostService;
import com.example.blog_app_apis.services.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private ContentModerationService contentModerationService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SecurityService securityService;

    @Value("${project.image}")
    private String path;

    // create post
    @PostMapping("/category/{categoryId}/posts")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto, @PathVariable String categoryId,
                                        @RequestParam(value = "isDraft", defaultValue = "false", required = false) boolean isDraft,
                                        @RequestParam(value = "scheduledPublishTime", required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    Date scheduledPublishTime){

        User user = securityService.getCurrentUser();

        String postText = postDto.getContent();

        double toxicityScore = contentModerationService.analyzeText(postText);

        if (toxicityScore > 0.5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Post is inappropriate due to toxic content: " + toxicityScore);
        }

        PostDto createdPost = postService.createPost(postDto, user, categoryId, isDraft, scheduledPublishTime);  // userId,categoryId

        return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
    }

    // get post by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable String userId){
        List<PostDto> posts = postService.getPostsByUser(userId);

        return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
    }

    // get post by category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable String categoryId){
        List<PostDto> posts = postService.getPostsByCategory(categoryId);

        return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
    }

    //  ***************   get all posts  *******************
    @GetMapping("/posts")
    public ResponseEntity<PaginatedApiResponse> getAllPosts(@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){
        PaginatedApiResponse paginatedApiResponse = postService.getAllPost(pageNumber,pageSize,sortBy,sortDir);

        return new ResponseEntity<PaginatedApiResponse>(paginatedApiResponse,HttpStatus.OK);
    }

    // get post details by id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String postId){
        PostDto postDto = postService.getPostById(postId);

        return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
    }

    // delete post
    // only owner can delete the post
    @PreAuthorize("@postServiceImpl.isPostOwner(#postId)")
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable String postId){
        postService.deletePost(postId);

        return new ApiResponse("post is successfully deleted !!",true);
    }

    // update post
    // // only the EDITOR or the user who created the post can edit the same post
    @PreAuthorize("hasRole('ROLE_EDITOR') or @postServiceImpl.isPostOwner(#postId)")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable String postId){
        PostDto updatePost = postService.updatePost(postDto,postId);

        return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
    }

    // search post
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
        List<PostDto> result = postService.searchPosts(keywords);

        return new ResponseEntity<List<PostDto>>(result,HttpStatus.OK);
    }


//    // post image upload
    @PreAuthorize("hasRole('ROLE_EDITOR') or @postServiceImpl.isPostOwner(#postId)")   // only the EDITOR or the user who created the post can upload post image
    @PostMapping("/posts/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable String postId) throws IOException {
        PostDto postDto = postService.getPostById(postId);

        String fileName = fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = postService.updatePost(postDto, postId);

        return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
    }

    // method to serve files
    @GetMapping(value="/posts/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName,
                              HttpServletResponse response) throws IOException{
        InputStream resource = fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}

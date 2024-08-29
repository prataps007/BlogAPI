package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.config.AppConstants;
import com.example.blog_app_apis.entities.Activity;
import com.example.blog_app_apis.entities.Category;
import com.example.blog_app_apis.entities.Post;
import com.example.blog_app_apis.entities.User;
import com.example.blog_app_apis.exceptions.ResourceNotFoundException;
import com.example.blog_app_apis.payloads.PostDto;
import com.example.blog_app_apis.payloads.PaginatedApiResponse;
import com.example.blog_app_apis.repositories.ActivityRepo;
import com.example.blog_app_apis.repositories.CategoryRepo;
import com.example.blog_app_apis.repositories.PostRepo;
import com.example.blog_app_apis.repositories.UserRepo;
import com.example.blog_app_apis.services.PostService;
import com.example.blog_app_apis.services.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SecurityService securityService; // Inject the SecurityService

    @Autowired
    private ActivityRepo activityRepo;


    @Autowired
    private CategoryRepo categoryRepo;


    @Override
    public PostDto createPost(PostDto postDto, User user, String categoryId,boolean isDraft, Date scheduledPublishTime) { //String userId, String categoryId

        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        Post post = modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");

        // Set the draft status and scheduled publish time
        post.setDraft(isDraft);
        post.setScheduledPublishTime(scheduledPublishTime);

        // If not a draft and no scheduled time is provided, publish immediately
        if (!isDraft && scheduledPublishTime == null) {
            post.setAddedDate(new Date());
        }

        // Set the User and Category objects in the Post entity
        post.setUser(user);
        post.setCategory(category);

        Post savedPost = postRepo.save(post);

        // Create an activity record
        Activity activity = new Activity();
        activity.setUserId(user.getId());
        activity.setAction("CREATE_POST");
        activity.setType("POST");
        activity.setTargetId(savedPost.getPostId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);

        PostDto savedPostDto = modelMapper.map(savedPost, PostDto.class);

        return savedPostDto;
    }

    @Override
    //@CachePut(value="posts", key="#postId")
    public PostDto updatePost(PostDto postDto, String postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id",postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post updatedPost = postRepo.save(post);

        // Create an activity record
        User currentUser = securityService.getCurrentUser();

        Activity activity = new Activity();
        activity.setUserId(currentUser.getId());
        activity.setAction("UPDATE_POST");
        activity.setType("POST");
        activity.setTargetId(updatedPost.getPostId());
        activity.setTimestamp(LocalDateTime.now());

        activityRepo.save(activity);

        return modelMapper.map(updatedPost,PostDto.class);
    }

    @Override
//    @Transactional
//    @CacheEvict(value = "posts", key = "#postId")
    public void deletePost(String postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","post id",postId));
        postRepo.delete(post);
    }

    @Override
    //@Cacheable(value = "posts")
    public PaginatedApiResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort=null;
        if(sortDir.equalsIgnoreCase(AppConstants.SORT_DIR)) {
           sort=Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePost = postRepo.findAll(pageable);
        List<Post> allPosts = pagePost.getContent();


        //List<Post> allPosts = this.postRepo.findAll();
        List<PostDto> postDtos = allPosts.stream().map((post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        PaginatedApiResponse paginatedApiResponse = new PaginatedApiResponse();
        paginatedApiResponse.setContent(postDtos);
        paginatedApiResponse.setPageNumber(pagePost.getNumber());
        paginatedApiResponse.setPageSize(pagePost.getSize());
        paginatedApiResponse.setTotalElements(pagePost.getTotalElements());
        paginatedApiResponse.setTotalPages(pagePost.getTotalPages());
        paginatedApiResponse.setLastPage(pagePost.isLast());

        return paginatedApiResponse;
    }

    @Override
    //@Cacheable(value = "posts", key = "#postId")
    public PostDto getPostById(String postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByCategory(String categoryId) {
        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","category id",categoryId));

        List<Post> posts = postRepo.findByCategoryId(cat.getCategoryId());


        List<PostDto> postDtos = posts.stream().map((post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        return postDtos;
    }

    @Override
    public List<PostDto> getPostsByUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        List<Post> posts = postRepo.findByUserId(user.getId());

        List<PostDto> postDtos = posts.stream().map((post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = postRepo.findByTitleContaining(keyword);
        List<PostDto> postDtos = posts.stream().map((post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        return postDtos;
    }


    //    This method will check if the authenticated user is the owner of the post.
    public boolean isPostOwner(String postId) {
        // Get the current authenticated user
        User currentUser = securityService.getCurrentUser();

        // Fetch the post from the database using the postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        // Check if the current user's ID matches the ID of the user who created the post
        return post.getUser().getId().equals(currentUser.getId());
    }

}

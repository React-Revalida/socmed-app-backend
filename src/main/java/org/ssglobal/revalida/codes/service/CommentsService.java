package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.CommentsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Comments;
import org.ssglobal.revalida.codes.model.Likes;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.CommentsRepository;
import org.ssglobal.revalida.codes.repos.PostsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CommentsService {

    private static final Logger LOG = LoggerFactory.getLogger(LikesService.class);
    private final AppUserRepository appUserRepository;
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    
	public CommentsService(AppUserRepository appUserRepository, PostsRepository postsRepository,
			CommentsRepository commentsRepository) {
		this.appUserRepository = appUserRepository;
		this.postsRepository = postsRepository;
		this.commentsRepository = commentsRepository;
	}
	
	
	public Set<CommentsDTO> getComments(Integer postId){
    	Set<Comments> ids = commentsRepository.findAllByPostId(postId);
    	Set<CommentsDTO> commentsDTO = new HashSet<>();
    	return mapToCommentsDTO(ids, commentsDTO);
    }
	
	private Set<CommentsDTO> mapToCommentsDTO(Set<Comments> commentsTbl, Set<CommentsDTO> commentsTblDTO) {
		for (Comments com: commentsTbl) {
			CommentsDTO commentsDTO = new CommentsDTO();
			commentsDTO.setMessage(com.getMessage());
			commentsDTO.setTimestamp(com.getTimestamp());
			commentsDTO.setPost(null);
			commentsDTO.setUser(mapToAppUserDTO(com.getUser()));
			commentsTblDTO.add(commentsDTO);
		}
        return commentsTblDTO;
    }
	
	
	
	private AppUserDTO mapToAppUserDTO(AppUser appUser) {
		if (appUser != null) {
			AppUserDTO appUserDTO = new AppUserDTO();
			appUserDTO.setUserId(appUser.getUserId());
			appUserDTO.setUsername(appUser.getUsername());
            return appUserDTO;
        }
        return null;
	}

//	@Transactional
	public Boolean createComment(@Valid CommentsDTO commentsDTO)
			throws IOException {
		final Comments comment = new Comments();
		Optional<Posts> post = postsRepository.findById(commentsDTO.getPost());
		Optional<AppUser> user = appUserRepository.findById(commentsDTO.getUser().getUserId());
		if (user.isPresent()) { 
			mapPostUserToCommentsTbl(comment,commentsDTO, post.get(), user.get());
			Comments newComment = commentsRepository.save(comment);
			if (newComment != null) {
				return true;
			}
		}

		return false;
	} 
    
	private Comments mapPostUserToCommentsTbl (Comments comment, CommentsDTO commentsDTO, Posts post, AppUser user) {
		comment.setId(commentsDTO.getId());
		comment.setMessage(commentsDTO.getMessage());
		comment.setTimestamp(commentsDTO.getTimestamp());
		comment.setPost(post);
		comment.setUser(user);
	
		return comment;
	}
	
	
}

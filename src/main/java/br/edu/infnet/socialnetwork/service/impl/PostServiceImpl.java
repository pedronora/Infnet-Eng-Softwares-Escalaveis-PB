package br.edu.infnet.socialnetwork.service.impl;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.model.Post;
import br.edu.infnet.socialnetwork.repository.PostRepository;
import br.edu.infnet.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> filterByTitleAndContent(String param) {
        return postRepository.findByContentContainingIgnoreCase(param);
    }

    public void create(Post post) {
        postRepository.save(post);
    }

    public Optional<Post> getById(Long id) {
        Optional<Post> postToRetrieve = postRepository.findById(id);
        if (postToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado Post com id " + id);
        }
        return postToRetrieve;
    }

    public void update(Long id, Post postToUpdate) {
        Optional<Post> existingPost = getById(id);

        if (existingPost.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }

        postToUpdate.setUpdatedDate(new Date());
        postRepository.save(postToUpdate);
    }

    public void delete(Long id) {
        Optional<Post> postToDelete = getById(id);

        if (postToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }
        postRepository.delete(postToDelete.get());
    }
}

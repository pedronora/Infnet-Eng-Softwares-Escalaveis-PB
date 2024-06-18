package br.edu.infnet.socialnetwork.service.impl;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.model.Post;
import br.edu.infnet.socialnetwork.repository.PostRepository;
import br.edu.infnet.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> filterByTitleAndContent(String param) {
        return postRepository.findByContentContainingIgnoreCase(param);
    }

    @Override
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> getById(Long id) {
        Optional<Post> postToRetrieve = postRepository.findById(id);
        if (postToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado Post com id " + id);
        }
        return postToRetrieve;
    }

    @Override
    public Post update(Long id, Post postToUpdate) {
        Optional<Post> existingPostOpt = getById(id);

        if (existingPostOpt.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }

        Post existingPost = existingPostOpt.get();
        existingPost.setTitle(postToUpdate.getTitle());
        existingPost.setContent(postToUpdate.getContent());

        return postRepository.save(existingPost);
    }

    @Override
    public void delete(Long id) {
        Optional<Post> postToDelete = getById(id);

        if (postToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }
        postRepository.delete(postToDelete.get());
    }

    @Override
    public void deleteAll() {
        postRepository.deleteAll();
    }
}

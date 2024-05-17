package br.edu.infnet.socialnetwork.services;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import models.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    List<Post> posts = initValues();

    private List<Post> initValues() {
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(1, "Primeiro post", "Primeiro post", new Date(), new Date()));
        posts.add(new Post(2, "Segundo Post", "Segundo Post", new Date(), new Date()));
        return posts;
    }

    public List<Post> getAll() {
        return posts;
    }

    public List<Post> filterByTitleAndContent(String param) {
        List<Post> posts = getAll();
        return posts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(param.toLowerCase()) ||
                        post.getContent().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void create(Post post) {
        posts.add(post);
    }

    public Optional<Post> getById(int id) {
        Optional<Post> postToRetrieve = getPost(id);
        if (postToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado Post com id " + id);
        }
        return postToRetrieve;
    }

    public void update(Integer id, Post postToUpdate) {
        Optional<Post> existingPost = getPost(id);

        if (existingPost.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }

        postToUpdate.setUpdatedDate(new Date());
        posts.set(id, postToUpdate);
    }

    public void delete(int id) {
        Optional<Post> postToDelete = getPost(id);

        if (postToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado post com id " + id);
        }
        posts.remove(postToDelete.get());
    }

    private Optional<Post> getPost(int id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst();
    }
}

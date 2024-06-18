package br.edu.infnet.socialnetwork.service;

import java.util.List;
import java.util.Optional;

import br.edu.infnet.socialnetwork.model.Post;

public interface PostService {

    public List<Post> getAll();

    public List<Post> filterByTitleAndContent(String param);

    public void create(Post post);

    public Optional<Post> getById(Long id);

    public void update(Long id, Post postToUpdate);

    public void delete(Long id);
}

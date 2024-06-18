package br.edu.infnet.socialnetwork.service;

import java.util.List;
import java.util.Optional;

import br.edu.infnet.socialnetwork.model.Comment;

public interface CommentService {

    public List<Comment> getAll();

    public List<Comment> filterByTitleAndContent(String param);

    public Comment create(Comment comment);

    public Optional<Comment> getById(Long id);

    public Comment update(Long id, Comment commentToUpdate);

    public void delete(Long id);

    public void deleteAll();
}

package br.edu.infnet.socialnetwork.services;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import models.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    List<Comment> comments = initValues();

    private List<Comment> initValues() {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1, "Comentário no post 1", 1, new Date(), new Date()));
        comments.add(new Comment(2, "Comentário no post 2", 2, new Date(), new Date()));
        return comments;
    }

    public List<Comment> getAll() {
        return comments;
    }

    public List<Comment> filterByTitleAndContent(String param) {
        List<Comment> posts = getAll();
        return comments.stream()
                .filter(comment -> comment.getContent().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void create(Comment post) {
        comments.add(post);
    }

    public Optional<Comment> getById(int id) {
        Optional<Comment> commentToRetrieve = getComment(id);
        if (commentToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }
        return commentToRetrieve;
    }

    public void update(Integer id, Comment commentToUpdate) {
        Optional<Comment> existingComment = getComment(id);

        if (existingComment.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }

        commentToUpdate.setUpdatedDate(new Date());
        comments.set(id, commentToUpdate);
    }

    public void delete(int id) {
        Optional<Comment> commentToDelete = getComment(id);

        if (commentToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentaŕio com id " + id);
        }
        comments.remove(commentToDelete.get());
    }

    private Optional<Comment> getComment(int id) {
        return comments.stream()
                .filter(post -> post.getId() == id)
                .findFirst();
    }
}

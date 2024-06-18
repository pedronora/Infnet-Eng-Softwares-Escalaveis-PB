package br.edu.infnet.socialnetwork.services;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.models.Comment;
import br.edu.infnet.socialnetwork.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public List<Comment> filterByTitleAndContent(String param) {
        return commentRepository.findByContentContainingIgnoreCase(param);
    }

    public void create(Comment comment) {
        commentRepository.save(comment);
    }

    public Optional<Comment> getById(Long id) {
        Optional<Comment> commentToRetrieve = commentRepository.findById(id);
        if (commentToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }
        return commentToRetrieve;
    }

    public void update(Long id, Comment commentToUpdate) {
        Optional<Comment> existingComment = getById(id);

        if (existingComment.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }

        commentToUpdate.setUpdatedDate(new Date());
        commentRepository.save(commentToUpdate);
    }

    public void delete(Long id) {
        Optional<Comment> commentToDelete = getById(id);

        if (commentToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentaŕio com id " + id);
        }
        commentRepository.delete(commentToDelete.get());
    }
}

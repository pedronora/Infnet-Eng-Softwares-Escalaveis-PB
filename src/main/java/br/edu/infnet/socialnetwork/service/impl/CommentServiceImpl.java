package br.edu.infnet.socialnetwork.service.impl;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.model.Comment;
import br.edu.infnet.socialnetwork.repository.CommentRepository;
import br.edu.infnet.socialnetwork.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> filterByTitleAndContent(String param) {
        return commentRepository.findByContentContainingIgnoreCase(param);
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getById(Long id) {
        Optional<Comment> commentToRetrieve = commentRepository.findById(id);
        if (commentToRetrieve.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }
        return commentToRetrieve;
    }

    @Override
    public Comment update(Long id, Comment commentToUpdate) {
        Optional<Comment> existingCommentOpt = getById(id);
    
        if (existingCommentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentário com id " + id);
        }
    
        Comment existingComment = existingCommentOpt.get();
        existingComment.setContent(commentToUpdate.getContent());
       
        return commentRepository.save(existingComment);
    }
    

    @Override
    public void delete(Long id) {
        Optional<Comment> commentToDelete = getById(id);

        if (commentToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado comentaŕio com id " + id);
        }
        commentRepository.delete(commentToDelete.get());
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();;
    }
}

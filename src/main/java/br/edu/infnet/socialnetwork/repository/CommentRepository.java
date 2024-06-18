package br.edu.infnet.socialnetwork.repository;

import java.util.List;

import br.edu.infnet.socialnetwork.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContentContainingIgnoreCase(String content);
}

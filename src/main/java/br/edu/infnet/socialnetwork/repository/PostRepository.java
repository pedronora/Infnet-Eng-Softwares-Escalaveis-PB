package br.edu.infnet.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.infnet.socialnetwork.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByContentContainingIgnoreCase(String content);
}

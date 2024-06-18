package br.edu.infnet.socialnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.infnet.socialnetwork.model.Comment;
import br.edu.infnet.socialnetwork.service.CommentService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CommentServiceTest {
    @Autowired
    CommentService commentService;

    @BeforeEach
    public void setUp() {
        commentService.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar a lista de comments do banco")
    public void getAllTest() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            Comment comment = new Comment();
            comment.setContent("Comment Content Test " + (i + 1));
            commentService.create(comment);
        }

        List<Comment> comments = commentService.getAll();

        assertEquals(n, comments.size());
    }

    @Test
    @DisplayName("Deve inserir um comment no banco")
    public void createTest() {
        List<Comment> all = commentService.getAll();
        int tamanhoInicial = all.size();
        log.info("Create Test - Tamanho Inicial: ", tamanhoInicial);

        Comment comment = new Comment();
        comment.setContent("Comment Content Test");
        commentService.create(comment);

        all = commentService.getAll();
        int tamanhoFinal = all.size();
        log.info("Create Test - Tamanho Final: ", tamanhoFinal);

        assertEquals(tamanhoInicial + 1, tamanhoFinal);
    }

    @Test
    @DisplayName("Deve retornar o comentário pelo id")
    public void getByIdTest() {
        Comment comment = new Comment();
        comment.setContent("Comment Content Test");
        Comment newComment = commentService.create(comment);

        Comment commentFounded = commentService.getById(newComment.getId()).get();
        log.info("Post Founded by Id: ", commentFounded);

        assertEquals(newComment.getId(), commentFounded.getId());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos")
    public void updateTest() {
        Comment comment = new Comment();
        comment.setContent("Comment Content Test");
        Comment createdComment = commentService.create(comment);

        String newContent = "Comment Content Test Updated";

        createdComment.setContent(newContent);
        Comment updatedComment = commentService.update(createdComment.getId(), createdComment);
        assertEquals(newContent, updatedComment.getContent());
        assertEquals(createdComment.getCreatedDate(), updatedComment.getCreatedDate());
        assertNotEquals(createdComment.getUpdatedDate(), updatedComment.getUpdatedDate());
    }

    @Test
    @DisplayName("Deve deletar um Post do banco")
    public void deleteTest() {
        Comment comment = new Comment();
        comment.setContent("Comment Content Test");
        Comment createdComment = commentService.create(comment);

        List<Comment> comments = commentService.getAll();
        int tamanhoInicial = comments.size();

        commentService.delete(createdComment.getId());

        comments = commentService.getAll();
        int tamanhoFinal = comments.size();

        assertEquals(tamanhoInicial - 1, tamanhoFinal);
    }
}

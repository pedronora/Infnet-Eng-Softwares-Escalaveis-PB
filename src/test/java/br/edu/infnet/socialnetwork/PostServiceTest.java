package br.edu.infnet.socialnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.infnet.socialnetwork.model.Post;
import br.edu.infnet.socialnetwork.service.PostService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class PostServiceTest {
    @Autowired
    PostService postService;

    @Test
    @DisplayName("Deve inserir um post no banco")
    public void createTest() {
        List<Post> all = postService.getAll();
        int tamanhoInicial = all.size();
        log.info("Create Test - Tamanho Inicial: ", tamanhoInicial);

        Post post = new Post();
        post.setTitle("Post Test");
        post.setContent("Post Content Test");
        postService.create(post);

        all = postService.getAll();
        int tamanhoFinal = all.size();
        log.info("Create Test - Tamanho Final: ", tamanhoFinal);

        assertEquals(tamanhoInicial + 1, tamanhoFinal);
    }

    @Test
    @DisplayName("Deve retornar a cerveja pelo nome")
    public void getByIdTest() {
        Post post = new Post();
        post.setTitle("Post Test");
        post.setContent("Post Content Test");
        postService.create(post);

        Post postFounded = postService.getById(post.getId()).get();
        log.info("Post Founded by Id: ", postFounded);

        assertEquals(post.getId(), postFounded.getId());
    }

    @Test
    @DisplayName("Deve deletar um Post do banco")
    public void deleteTest() {
        Post post = new Post();
        post.setTitle("Post Test");
        post.setContent("Post Content Test");
        postService.create(post);

        List<Post> all = postService.getAll();
        int tamanhoInicial = all.size();

        postService.delete(post.getId());

        all = postService.getAll();
        int tamanhoFinal = all.size();

        assertEquals(tamanhoInicial - 1, tamanhoFinal);
    }

}

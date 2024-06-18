package br.edu.infnet.socialnetwork.controller;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.model.Post;
import br.edu.infnet.socialnetwork.payload.DetailPayload;
import br.edu.infnet.socialnetwork.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    Logger logger = LoggerFactory.getLogger(PostController.class);

    final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Retorna uma lista de Posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Post.class))) }),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping()
    public ResponseEntity<List<Post>> getAll(@RequestParam(required = false) Optional<String> q) {
        if (q.isEmpty()) {
            logger.info("Retornada lista de todos os posts");
            return ResponseEntity.ok(postService.getAll());
        } else {
            List<Post> posts = postService.filterByTitleAndContent(q.get());
            if (posts.isEmpty()) {
                logger.info("Não há posts para serem apresentadas");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("Retornada lista de posts com este parâmetro: " + q);
                return ResponseEntity.ok(posts);
            }
        }
    }

    @Operation(summary = "Publica um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post publicado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) })
    })
    @PostMapping()
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post newPost = postService.create(post);
        logger.info("Criado post com id '" + newPost.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    @Operation(summary = "Retorna um post pelo seu ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @ApiResponse(responseCode = "404", description = "Post não encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) }) })
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<Post> postFounded = postService.getById(id);
            logger.info("Post de id '" + id + "' recuperado com sucesso!");
            return ResponseEntity.status(HttpStatus.OK).body(postFounded);
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado post com id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Atualiza um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Post atualizado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)) }),
            @ApiResponse(responseCode = "404", description = "Post não encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) }) })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Post post, @PathVariable Long id) {
        try {
            Post updatedPost = postService.update(id, post);
            logger.info("Post de id '" + id + "' atualizado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(updatedPost);
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado post com id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Deleta um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletado com sucesso!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) }),
            @ApiResponse(responseCode = "404", description = "Post não encontrado!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            postService.delete(id);
            logger.info("Post de id '" + id + "' deletado com sucesso!");
            return ResponseEntity.status(HttpStatus.OK).body(new DetailPayload("Deletado com sucesso"));
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado post com id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }
}

package br.edu.infnet.socialnetwork.controllers;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.payload.DetailPayload;
import br.edu.infnet.socialnetwork.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import models.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Retorna uma lista de Posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Post.class)))}),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping()
    public ResponseEntity<List<Post>> getAll(@RequestParam(required = false) Optional<String> param) {
        if (param.isEmpty()) {
            return ResponseEntity.ok(postService.getAll());
        } else {
            List<Post> posts = postService.filterByTitleAndContent(param.get());
            if (posts.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(posts);
            }
        }
    }

    @Operation(summary = "Publica um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post publicado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))})
    })
    @PostMapping("/create")
    public ResponseEntity<DetailPayload> create(@RequestBody Post post) {
        postService.create(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DetailPayload("Post publicado com sucesso!"));
    }

    @Operation(summary = "Retorna um post pelo seu ID")

    @GetMapping("/{id}/details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Post não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            Optional<Post> postFounded = postService.getById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(postFounded);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Atualiza um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Post atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Post não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    @PutMapping("/{id}/update")
    public ResponseEntity<DetailPayload> update(@RequestBody Post post, @PathVariable int id) {
        try {
            postService.update(id, post);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new DetailPayload("Post de id '" + id + "' atualizado com sucesso!"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Deleta um post")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Deletado com sucesso!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post não encontrado!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))})
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            postService.delete(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DetailPayload("Deletado com sucesso"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }
}

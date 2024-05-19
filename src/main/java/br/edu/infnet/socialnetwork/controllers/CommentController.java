package br.edu.infnet.socialnetwork.controllers;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.payload.DetailPayload;
import br.edu.infnet.socialnetwork.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import models.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Retorna uma lista de comentários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Comment.class)))}),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping()
    public ResponseEntity<List<Comment>> getAll(@RequestParam(required = false) Optional<String> param) {
        if (param.isEmpty()) {
            return ResponseEntity.ok(commentService.getAll());
        } else {
            List<Comment> comments = commentService.filterByTitleAndContent(param.get());
            if (comments.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(comments);
            }
        }
    }

    @Operation(summary = "Publica um comentário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentário publicado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))})
    })
    @PostMapping("/create")
    public ResponseEntity<DetailPayload> create(@RequestBody Comment comment) {
        commentService.create(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DetailPayload("Comentário publicado com sucesso!"));
    }

    @Operation(summary = "Retorna um comentário pelo seu ID")
    @GetMapping("/{id}/details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            Optional<Comment> commentFounded = commentService.getById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentFounded);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Atualiza um comentário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comentário atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    @PutMapping("/{id}/update")
    public ResponseEntity<DetailPayload> update(@RequestBody Comment comment, @PathVariable int id) {
        try {
            commentService.update(id, comment);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new DetailPayload("Comentário de id '" + id + "' atualizado com sucesso!"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Deleta um comentário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Deletado com sucesso!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comentário não encontrado!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))})
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            commentService.delete(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new DetailPayload("Deletado com sucesso"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }
}


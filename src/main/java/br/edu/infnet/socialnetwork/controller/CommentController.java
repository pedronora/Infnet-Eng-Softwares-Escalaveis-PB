package br.edu.infnet.socialnetwork.controller;

import br.edu.infnet.socialnetwork.exception.ResourceNotFoundException;
import br.edu.infnet.socialnetwork.model.Comment;
import br.edu.infnet.socialnetwork.payload.DetailPayload;
import br.edu.infnet.socialnetwork.service.CommentService;
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
@RequestMapping("/api/comments")
public class CommentController {

    Logger logger = LoggerFactory.getLogger(CommentController.class);

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
    public ResponseEntity<List<Comment>> getAll(@RequestParam(required = false) Optional<String> q) {
        if (q.isEmpty()) {
            logger.info("Retornada lista de todos os comentários");
            return ResponseEntity.ok(commentService.getAll());
        } else {
            List<Comment> comments = commentService.filterByTitleAndContent(q.get());
            if (comments.isEmpty()) {
                logger.info("Não há comentários para serem apresentadas");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("Retornada lista de comentários com este parâmetro: " + q);
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
    @PostMapping()
    public ResponseEntity<Comment> create(@RequestBody Comment comment) {
        Comment newComment = commentService.create(comment);
        logger.info("Criado comment com id '" + newComment.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @Operation(summary = "Retorna um comentário pelo seu ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<Comment> commentFounded = commentService.getById(id);
            logger.info("Comment de id '" + id + "' recuperado com sucesso!");
            return ResponseEntity.status(HttpStatus.OK).body(commentFounded);
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado comment com id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Atualiza um comentário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comentário atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}
            )})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Comment comment, @PathVariable Long id) {
        try {
            Comment updatedComment = commentService.update(id, comment);
            logger.info("Comment de id '" + id + "' atualizado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(updatedComment);
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado comment com id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @Operation(summary = "Deleta um comentário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deletado com sucesso!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comentário não encontrado!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailPayload.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            commentService.delete(id);
            logger.info("Comment de id '" + id + "' deletado com sucesso!");
            return ResponseEntity.status(HttpStatus.OK).body(new DetailPayload("Deletado com sucesso"));
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado comment com id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }
}


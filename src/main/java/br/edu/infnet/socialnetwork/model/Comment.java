package br.edu.infnet.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updatedAt;

        @PrePersist
    protected void onCreate() {
        Date now = new Date();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}

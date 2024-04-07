package com.blockix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "t_user_view")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anonymous_user")
    private boolean anonymousUser = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToMany(mappedBy = "views", fetch = FetchType.LAZY)
    private List<Article> viewedArticles = new LinkedList<>();
}

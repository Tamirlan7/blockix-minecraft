package com.blockix.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "t_news")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "views", fetch = FetchType.LAZY)
    private List<News> viewedNews;
}

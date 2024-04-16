package com.example.askme.domain;

import com.example.askme.domain.constant.ContentStatus;
import com.example.askme.domain.constant.SolveState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private SolveState state;

    @Enumerated(value = EnumType.STRING)
    private ContentStatus status;

    @Setter
    private String imageUrl;

    @ColumnDefault("0")
    private long viewCount = 0;

    @ColumnDefault("0")
    private long likeCount = 0;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final List<ArticleComment> articleComments = new ArrayList<>();

    private Article(UserAccount userAccount, String title, String content, SolveState state, ContentStatus status, String imageUrl, int viewCount, int likeCount) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.state = state;
        this.status = status;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static Article createArticle(UserAccount userAccount, String title, String content, SolveState state, ContentStatus status, String imageUrl, int viewCount, int likeCount) {
        return new Article(userAccount, title, content, state, status, imageUrl, viewCount, likeCount);
    }
}

package com.example.askme.dao.article;

import com.example.askme.dao.AuditingFields;
import com.example.askme.dao.comment.Comment;
import com.example.askme.common.constant.ContentStatus;
import com.example.askme.common.constant.SolveState;
import com.example.askme.dao.account.Account;
import jakarta.persistence.*;
import lombok.*;
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
    private Account account;

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

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {this.likeCount++; }

    public void decreaseLikeCount() {
        this.likeCount--;

        if (this.likeCount < 0) {
            this.likeCount = 0;
        }
    }

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final List<Comment> articleComments = new ArrayList<>();

    private Article(Account account, String title, String content, SolveState state, ContentStatus status, String imageUrl, long viewCount, long likeCount) {
        this.account = account;
        this.title = title;
        this.content = content;
        this.state = state;
        this.status = status;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static Article createArticle(Account account, String title, String content, SolveState state, ContentStatus status, String imageUrl, long viewCount, long likeCount) {
        return new Article(account, title, content, state, status, imageUrl, viewCount, likeCount);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

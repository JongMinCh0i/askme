package com.example.askme.domain;

import com.example.askme.domain.constant.ContentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAccount userAccount;

    @Enumerated(value = EnumType.STRING)
    private ContentStatus status;

    @Setter
    @Column(nullable = false, length = 1000)
    private String content;

    @Setter
    private String imageUrl;

    @ColumnDefault("0")
    private long likeCount = 0;

    public void increaseLikeCount() {
        this.likeCount++;
    }

    private ArticleComment(Article article, UserAccount userAccount, ContentStatus status, String content, String imageUrl, int likeCount) {
        this.article = article;
        this.userAccount = userAccount;
        this.status = status;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public static ArticleComment createArticleComment(Article article, UserAccount userAccount, String content, String imageUrl) {
        return new ArticleComment(article, userAccount, ContentStatus.PUBLISH, content, imageUrl, 0);
    }
}

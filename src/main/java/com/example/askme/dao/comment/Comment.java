package com.example.askme.dao.comment;

import com.example.askme.dao.AuditingFields;
import com.example.askme.dao.article.Article;
import com.example.askme.dao.account.Account;
import com.example.askme.common.constant.ContentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account account;

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

    private Comment(Article article, Account account, ContentStatus status, String content, String imageUrl, int likeCount) {
        this.article = article;
        this.account = account;
        this.status = status;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public static Comment createArticleComment(Article article, Account account, String content, String imageUrl) {
        return new Comment(article, account, ContentStatus.PUBLISH, content, imageUrl, 0);
    }
}

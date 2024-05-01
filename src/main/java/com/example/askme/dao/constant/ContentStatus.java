package com.example.askme.dao.constant;

public enum ContentStatus {
    PUBLISH,  // 게시글, 댓글이 최초로 공개된 상태
    EDITED,   // 게시글, 댓글이 수정된 상태
    DELETED;  // 게시글, 댓글이 삭제 되어 보이지 않는 상태

    public String getStatus() {
        return this.name();
    }
}

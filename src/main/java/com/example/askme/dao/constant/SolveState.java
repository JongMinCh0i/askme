package com.example.askme.dao.constant;

public enum SolveState {

    SOLVED, NOT_SOLVED_YET; // 게시글 해결, 미해결

    public String getStatus() {
        return this.name();
    }
}

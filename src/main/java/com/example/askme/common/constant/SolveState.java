package com.example.askme.common.constant;

public enum SolveState {

    SOLVED, NOT_SOLVED_YET; // 게시글 해결, 미해결

    public String getStatus() {
        return this.name();
    }
}

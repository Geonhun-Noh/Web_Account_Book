package com.hello.board.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hello.board.service.BoardService;

@Controller
public class BoardController {

    private BoardService boardService;

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping("/board")
    public String viewBoardListPage() {
        return "board/board";
    }

}
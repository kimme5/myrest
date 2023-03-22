package com.godcoder.myrest.controller;

import com.godcoder.myrest.model.Board;
import com.godcoder.myrest.repository.BoardRepository;
import com.godcoder.myrest.service.BoardService;
import com.godcoder.myrest.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardValidator boardValidator;
    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String list(Model model
                     , @PageableDefault(size = 10) Pageable pageable
                     , @RequestParam(required = false, defaultValue = "") String searchText) {
        /*
         * 페이지구분없이 전체 목록을 가져옴
         * List<Board> boards = boardRepository.findAll();
         * --> 페이징처리를 위해 아래와 같이 수정함
         */
        /*
         * findAll(PageRequest.of(pageNumber, size)) 메소드를 통해 대상 페이지와 개수를 return 받을 수 있음
         * Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));
         * --> 전체 게시물 목록에 대한 페이징 처리를 위해 아래와 같이 수정함
         */
        /*
         * pageable 파라미터를 전달하여 게시글 목록에 대한 paging 처리를 할 수 있음
         * Page<Board> boards = boardRepository.findAll(pageable);
         * --> 검색 키워드를 파라미터로 전달하기 위해 아래와 같이 수정함
         */
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);

        int countPage = 5;
        int totalPage = boards.getTotalPages();
        int nowPage = boards.getPageable().getPageNumber() + 1;
        int startPage = (((int)(Math.ceil((double)nowPage/countPage))) - 1) * countPage + 1;
        int endPage = Math.min((startPage + countPage - 1), totalPage);

        model.addAttribute("boards", boards);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/board/list";
    }
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        } else {
            Optional<Board> board = boardRepository.findById(id);
         // Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }

        return "/board/form";
    }

    @PostMapping("/form")
 // public String formSave(@ModelAttribute Board board) {
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {

        boardValidator.validate(board, bindingResult);
        // Board.title에 정의한 Size 조건에 대한 확인 처리
        if(bindingResult.hasErrors()) {
            return "/board/form";
        }
        /*
         * 사용자의 이름을 security로 얻어오기 위해 파라미터에서 Authentication을 받아서 사용하는 방법과
         * 메소드 내부에서 사용하는 방법 2가지 모두 가능함
         * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         * String userName = auth.getName();
         */
        String userName = authentication.getName();
        boardService.save(userName, board);
        return "redirect:/board/list";
    }

}

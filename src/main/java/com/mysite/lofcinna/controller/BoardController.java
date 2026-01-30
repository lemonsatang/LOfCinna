package com.mysite.lofcinna.controller;

import com.mysite.lofcinna.mapper.BoardMapper;
import com.mysite.lofcinna.model.Board;
import com.mysite.lofcinna.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/board")
@Controller
public class BoardController {

    @Autowired
    private BoardMapper bMapper;

    // 리스트
    @GetMapping("/list")
    public String list(){
        return "/board/list";
    }

    @ResponseBody
    @PostMapping("/getList")
    public List<Board> getList(Board board){
        List<Board> result = bMapper.getList(board);
        System.out.println(result.get(0).getTotalCount());
        return result;
    }

    // 글쓰기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String write(Board board){
        return "/board/write";
    }

    @PostMapping("/write")
    public String addBoard(Board board, Principal principal){
        // DB 저장하기
        MultipartFile imageFile = board.getImageFile();
        Date createDate = new Date();
        // 이미지 파일 이름이 중복되는 것을 방지하기 위해 현재시간_원래파일명 저장
        String storeFileName =  createDate.getTime() +"_" + imageFile.getOriginalFilename();
        // 이미지를 public/images 폴더에 저장한다
        if(!imageFile.isEmpty()){
            try{
                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);
                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }
                try(InputStream is = imageFile.getInputStream()){
                    // 이미지 파일을 인풋스트림 is로 만들어서 이미지폴더에 복사한다.
                    Files.copy(is, Paths.get(uploadDir+storeFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                board.setFileName(storeFileName);
            }catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }

        board.setCuser(principal.getName());
        bMapper.addBoard(board);
        return "redirect:/board/list";
    }

    // 상세보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model,Principal principal){
        Map<String, Object> result = bMapper.getDetail(id);
        if(principal != null){
            result.put("id", principal.getName());
        }else{
            result.put("id", "");
        }

        model.addAttribute("item", result);
        return  "/board/detail";
    }

    // 댓글 추가
    @ResponseBody
    @PostMapping("/addComment")
    public Map<String, Object> addComment(Comment comment, Principal principal){
        Map<String, Object> result = new HashMap<>();

        comment.setCuser(principal.getName());
        try {
            bMapper.addComment(comment);
            result.put("success", true);
            result.put("message", "댓글 등록이 완료되었습니다.");
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "댓글 등록 중 오류가 발생했습니다.");
        }

        return result;
    }

    // 댓글 가져오기
    @ResponseBody
    @PostMapping("/getComment/{bno}")
    public List<Comment> getComment(@PathVariable Long bno,Principal principal){
        List<Comment> result = bMapper.getComment(bno);
        boolean same = false;
        if(principal != null){
            for(Comment comment : result){
                if(principal.getName().equals(comment.getCuser())){
                    same = true;
                }
                comment.setSame(same);
                same = false;
            }
        }
        return result;
    }

    // 댓글 삭제
    @ResponseBody
    @PostMapping("/delComment/{cno}")
    public Map<String, Object> delComment(@PathVariable Long cno){
        Map<String, Object> result = new HashMap<>();
        try{
            bMapper.delComment(cno);
            result.put("success", true);
            result.put("message", "삭제 완료되었습니다.");
        }catch (Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    // 글 삭제
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping("/delBoard")
    public  Map<String, Object> delBoard(Board board){
        Map<String, Object> result = new HashMap<>();
        try{
            // 이미지 삭제
            if(board.getFileName() != null && !board.getFileName().equals("")){
                String uploadDir = "public/";
                Path oldImagePath = Paths.get(uploadDir + board.getFileName());
                Files.deleteIfExists(oldImagePath); // 먼저 이미지 삭제
//                if(!Files.deleteIfExists(oldImagePath)){
//                    result.put("success", false);
//                    return result;
//                }
            }

            // 댓글 삭제
            bMapper.delBoardComment(board.getBno());

            // 글 삭제
            bMapper.delBoard(board.getBno());

            result.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 수정 페이지
    @GetMapping("/edit/{bno}")
    public String edit(@PathVariable Long bno, Model model){
        Map<String, Object> board = bMapper.getDetail(bno);
        model.addAttribute("board", board);

        return "board/write";
    }

    // 수정하기
    @PostMapping("/edit/{bno}")
    public String edit(Board board){
        Map<String, Object> info = bMapper.getDetail((long) board.getBno());
        //수정할 이미지가 있으면 기존 이미지 삭제하고 수정 이미지 업로드
        if(!board.getImageFile().isEmpty()) {
            String uploadDir = "public/images/";
            Path oldImagePath = Paths.get(uploadDir + info.get("file_name"));
            try {
                Files.deleteIfExists(oldImagePath);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            // 새이미지 업로드
            MultipartFile imageFile = board.getImageFile();
            Date createDate = new Date();
            String storeFileName = createDate.getTime() + "_" + imageFile.getOriginalFilename();

            try (InputStream is = imageFile.getInputStream()) {
                Files.copy(is, Paths.get(uploadDir + storeFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            board.setFileName(storeFileName); //이미지 파일 이름을 업데이트
        }
        bMapper.editBoard(board);

        return "redirect:/board/list";
    }
}

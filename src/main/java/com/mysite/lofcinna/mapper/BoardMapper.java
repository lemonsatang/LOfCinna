package com.mysite.lofcinna.mapper;

import com.mysite.lofcinna.model.Board;
import com.mysite.lofcinna.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    void addBoard(Board board);

    List<Board> getList(Board board);

    Map<String, Object> getDetail(Long id);

    int addComment(Comment comment);

    List<Comment> getComment(Long bno);

    void delComment(Long cno);

    void delBoardComment(int bno);

    void delBoard(int bno);

    void editBoard(Board board);
}

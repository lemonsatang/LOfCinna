package com.mysite.lofcinna.mapper;

import com.mysite.lofcinna.model.PreRev;
import com.mysite.lofcinna.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MainMapper {
    int idChk(String id);

    void signUp(User user);

    Optional<User> findByusername(String username);

    String findId(String name, String email);

    int revChk(PreRev preRev);

    void addPreRev(PreRev preRev);
}

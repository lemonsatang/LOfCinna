package com.mysite.lofcinna.service;

import com.mysite.lofcinna.mapper.MainMapper;
import com.mysite.lofcinna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    private MainMapper mainMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void signUp(User user) {
        String pass = encoder.encode(user.getPassword());
        user.setPassword(pass);
        mainMapper.signUp(user);
    }
}

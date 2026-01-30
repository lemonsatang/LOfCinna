package com.mysite.lofcinna;

import com.mysite.lofcinna.mapper.MainMapper;
import com.mysite.lofcinna.model.PreRev;
import com.mysite.lofcinna.model.User;
import com.mysite.lofcinna.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private MainMapper mainMapper;

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    /* 로그인 */
    @GetMapping("/main/login")
    public String login(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/main/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }

        return "login";
    }

    @ResponseBody
    @PostMapping("/main/findId")
    public Map<String,Object> findId(@RequestParam("name") String name, @RequestParam("email") String email){
        Map<String,Object> map = new HashMap<>();
        String id = mainMapper.findId(name,email);
        if(id != null){
            map.put("state","exist");
            map.put("id",id);
        }else{
            map.put("state","noExist");
        }

        return map;
    }


    /* 회원 가입 */
    @GetMapping("/main/sign")
    public String sign(){
        return "sign";
    }

    @ResponseBody
    @PostMapping("/main/idChk/{id}")
    public Map<Object, String> idChk(@PathVariable("id") String id){
        Map<Object, String> map = new HashMap<>();
        int exist = mainMapper.idChk(id);

        if(exist==1){
            map.put("message","중복된 아이디입니다.");
            map.put("result","exist");
        }else{
            map.put("message","사용 가능한 아이디 입니다.");
            map.put("result","success");
        }

        return map;
    }

    @PostMapping("/main/signUp")
    public String signUp(User user){
        try{
            mainService.signUp(user);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "login";
    }

    @GetMapping("/main/info")
    public String info(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("nickname"));
        return "info";
    }

    @GetMapping("/main/classInfo")
    public String classInfo(){
        return "classInfo";
    }

    /* 사전 예약 */
    @GetMapping("/main/preRev")
    public String preRev(){
        return "preRev";
    }

    @ResponseBody
    @PostMapping("/main/addPreRev")
    public Map<String, Object> addPreRev(PreRev preRev){
        Map<String, Object> result = new HashMap<>();

        int exist = mainMapper.revChk(preRev);
        System.out.println(exist);
        if(exist==1){
            result.put("message","이미 사전예약 완료되었습니다.");
        }else{
            try{
                mainMapper.addPreRev(preRev);
                result.put("message",preRev.getName()+"님, 사전예약이 완료되었습니다!");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

}

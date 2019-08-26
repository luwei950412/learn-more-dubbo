package com.sxdubbo.learn.front.controller;



import com.sxdubbo.learn.front.utils.AppMD5Util;
import com.sxdubboapi.learn.domain.*;
import com.sxdubboapi.learn.service.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * created by  luwei
 * 2018-01-04 14:12.
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserService userService;

    @Autowired
    public CourseService courseService;

    @Autowired
    public CommentService commentService;
//    public User user;

    @Autowired
    public RedisService redisService;

    @Autowired
    public UserCourseService userCourseService;

    @Autowired
    public CreditsService creditsService;

    @Autowired
    public HeadFrameService headFrameService;
    @Autowired
    public UserHeadFrameService userHeadFrameService;


    //    @ResponseBody
//    @GetMapping(value = "/index")
//    public JSONObject show(@PathVariable("name") String username){
////        BeanUtils.copyProperties();
//        JSONObject jsonObject = new JSONObject();
//        User user = new User();
//
//        user = userService.findByUsername(username);
//        System.out.println(user.getUsername()+"++++++++++++"+user.getPassword());
//        jsonObject.put("str", user);
//        return jsonObject;
//    }


    @GetMapping(value = "/frontRegister")
    public String register() {

        return "/front/user/register";
    }
    @PostMapping("/isExistUsername")
    @ResponseBody
    public String isExistUsername(@RequestParam("username") String username){
        User user = userService.findByUsername(username);
        if(user == null){
            return "true";
        }else{
            return "false";
        }
    }
    @PostMapping("/frontRegister")
    @ResponseBody
    public String addUser(@Valid User user){
        System.out.println(user.getUsername()+user.getEmail());
        user.setUserType(1);
        user.setUserStatus(0);
        user.setPassword(AppMD5Util.getMD5(user.getPassword()));
        user.setCreateDate(new Date());
        user.setModifyDate(new Date());
        user.setHeadimg("default.png");
        User user1 = userService.addUser(user);
        Credits credits = new Credits();
        credits.setUser(user1);
        credits.setCreateDate(new Date());
        credits.setModifyDate(new Date());
        credits.setCredit(5);
        Credits credits1 = creditsService.addCredits(credits);

        UserHeadFrame userHeadFrame= new UserHeadFrame();
        userHeadFrame.setUser(user1);
        userHeadFrame.setHeadFrame(headFrameService.findById(1));
        userHeadFrame.setChoice(1);
        userHeadFrame.setCreateDate(new Date());
        UserHeadFrame userHeadFrame1 =userHeadFrameService.addUserHeadFrame(userHeadFrame);

        if(user1 != null && credits1 !=null && userHeadFrame1 !=null){
            return "success";
        }else{
            return "error";
        }

    }

    //    @GetMapping(value = "/test")
//    public String hello(){
//        System.out.println("hello this is test++++++++++++++");
//        return "/user/test";
//    }
//
//
//    @RequestMapping(value = "/add")
////    @RequiresPermissions("user:add")
//    public String add(@Valid User user) {
//        System.out.println(user.getUsername());
//        user.setUserType(0);
//        user.setUserStatus(0);
//        user.setCreateDate(new Date());
//        user.setModifyDate(new Date());
//        System.out.println("add success++++++++++++" + user.getUsername());
//        userService.addUser(user);
//        return "/user/login";
//    }


//    @GetMapping(value = "/getByName")
//    public String getByUsername(@PathVariable(value = "username") String username, Model model) {
//        User user = userService.findByUsername(username);
//        model.addAttribute("user", user);
//        return "user/show";
//    }



    @GetMapping(value = "/frontLogin")
    public String frontLogin(){
        return "/front/user/login";
    }

    @PostMapping("/frontLogin")
    @ResponseBody
    public String frontLogin(@Valid User user, HttpServletRequest request, HttpServletResponse response)throws IOException {

        User user1 = userService.findByUsername(user.getUsername());
        if(user1 != null) {
            if (user1.getPassword().equals(AppMD5Util.getMD5(user.getPassword()))) {

                if(user1.getUserType() ==0){
                    return "admin";
                }else{
                    if(user1.getUserStatus() == 2){
                        return "frozen";
                    }else{
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/html;charset=UTF-8");
                        //使用request对象的getSession()获取session，如果session不存在则创建一个
                        HttpSession session = request.getSession();
                        //将数据存储到session中
                        session.setAttribute("userFront", user1);
                        //头像框放入缓存
                        UserHeadFrame userHeadFrame = userHeadFrameService.findByUserIdAndChoice(user1.getId(), 1);
                        session.setAttribute("userHeadFrame", userHeadFrame);
                        //用户积分放入缓存
                        Credits credits = creditsService.findByUserId(user1.getId());
                        session.setAttribute("credits", credits);

                        redisService.setObj("userFront", user1);
                        return "success";
                    }
                }
            } else {
                return "error";
            }
        }else{
            return "no_user";
        }
    }


    @RequestMapping(value = "/frontLogout", method = RequestMethod.GET)
    public String frontLogout(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        try {
            System.out.println("front logout");
            redisService.delObj("userFront");
            HttpSession session1 = request.getSession();
            session1.setAttribute("userFront",null);
//            session1.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/front/index";
    }

    public List<User> getAll() {
        return userService.findAllUser();
    }







    @PostMapping("/updateFrontUser")
    public String updateFrontUser(@Valid User user , BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes){
        User user1 = userService.getUserById(user.getId());
        System.out.println("here is front modify");
        user.setCreateDate(user1.getCreateDate());
        user.setHeadimg(user1.getHeadimg());
        user.setModifyDate(new Date());
        user.setUserType(user1.getUserType());
        user.setUserStatus(user1.getUserStatus());
        user.setPassword(user1.getPassword());
        userService.addUser(user);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //使用request对象的getSession()获取session，如果session不存在则创建一个
        HttpSession session = request.getSession();
        //将数据存储到session中
        session.setAttribute("userFront", user);
        redisService.setObj("userFront", user);
        return "redirect:/mycenter/myprofile";
    }


    @GetMapping("/frontLecture")
    public String frontLecture(Model model){
        List<User> lectureList = userService.findByUserType(2);
        model.addAttribute("lectureList",lectureList);
        return "/front/user/front_lecture";
    }

    @PostMapping("/applyLecture")
    public String applyLecture(@Valid User user, Model model, RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response){
        User user1 = userService.getUserById(user.getId());
        System.out.println("here is applyLecture"+user1.getUsername());
        user1.setIntroduction(user.getIntroduction());
        user1.setUserStatus(1);
        user1.setModifyDate(new Date());
        User user2 =userService.addUser(user1);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
       session.setAttribute("userFront",user2);
        return "redirect:/front/lectureEnrollInfo";
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(@Valid User user, HttpServletRequest request, HttpServletResponse response){
        System.out.println(user.getId()+"here is password modify");
        User user1 = userService.getUserById(user.getId());
        user1.setPassword(AppMD5Util.getMD5(user.getPassword()));
//        user1.setPassword(user.getPassword());
        User user2 = userService.addUser(user1);
        if(user2 != null){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //使用request对象的getSession()获取session，如果session不存在则创建一个
            HttpSession session = request.getSession();
            //将数据存储到session中
            session.setAttribute("userFront", user2);
            return "success";
        }else{
            return "error";
        }

    }

    @PostMapping("/jdApply")
    public String jdApply_user(@Valid User user, RedirectAttributes attributes,
                               HttpServletRequest request, HttpServletResponse response){
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
//        //使用request对象的getSession()获取session，如果session不存在则创建一个
//        HttpSession session = request.getSession();
//
//
//        session.setAttribute("userFrong",);
        return "direct:/user/frontLogin";
    }

}

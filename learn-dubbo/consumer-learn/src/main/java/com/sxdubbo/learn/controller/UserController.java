package com.sxdubbo.learn.controller;


import com.sxdubbo.learn.utils.AppMD5Util;
import com.sxdubbo.learn.utils.FileUtils;
import com.sxdubbo.learn.utils.HdfsUtil;
import com.sxdubbo.learn.utils.IsOs;
import com.sxdubboapi.learn.domain.*;
import com.sxdubboapi.learn.service.*;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.net.URISyntaxException;
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


    @GetMapping(value = "/login")
    public String loginForm() {
        return "/admin/user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@Valid User user, HttpServletRequest request, HttpServletResponse response)throws IOException {
        User user1 = userService.findByUsername(user.getUsername());
        if(user1.getPassword().equals(AppMD5Util.getMD5(user.getPassword()))){
            if(user1.getUserType() == 1){
                return "no_right";
            }else{
                if(user1.getUserStatus() == 2){
                return "frozen";
                }else{
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html;charset=UTF-8");
                    HttpSession session = request.getSession();
                    session.setAttribute("userInfo", user1);
                    redisService.setObj("user", user1);
                    return "success";
                }
            }
        }else{
            return "error";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            redisService.delObj("user");
            HttpSession session1 = request.getSession();
            session1.setAttribute("userInfo",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "/user/login";
    }

    public List<User> getAll() {
        return userService.findAllUser();
    }

    @GetMapping(value = "/lecture")
    public String getAllLecture(Model model,@RequestParam(name = "s",required = false,defaultValue = "1")String s,@RequestParam(name = "p",required = true,defaultValue = "1")String p) {

        List<User> lectureList = getAll();
        Iterator<User> iterator=lectureList.iterator();
        while(iterator.hasNext()){
            User user=iterator.next();
            if(user.getUserType()==0||user.getUserType()==1){
                iterator.remove();
            }
        }
        int flag=4;
        int size = lectureList.size();
        int temp = size / flag + 1;
        boolean special = size % flag == 0;
        List<User> cutList = null;
        for (int i = 0; i < temp; i++) {
            if (i == temp - 1) {
                if (special) {
                    break;
                }
                cutList = lectureList.subList(flag * i, size);
            } else {
                cutList = lectureList.subList(flag * i, flag * (i + 1));
            }
            if(Integer.parseInt(p)==(i+1)){
                model.addAttribute("lectureList",cutList);
                break;
            }
        }
        System.out.println(cutList.size());
        model.addAttribute("current_Page",Integer.parseInt(p));
        System.out.println("lectureList.size()"+lectureList.size());
        model.addAttribute("lectureListSize",lectureList.size());
//        model.addAttribute("lectureList", lectureList);
        return "/admin/user/lecture";
    }


    @GetMapping(value = "/common")
    public String getCommon(Model model,@RequestParam(value = "courseId",required = false)Integer courseId,
                            HttpServletRequest request, HttpServletResponse response,@RequestParam(name = "s",required = false,defaultValue = "1")String s,@RequestParam(name = "p",required = true,defaultValue = "1")String p) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //使用request对象的getSession()获取session，如果session不存在则创建一个
        HttpSession session = request.getSession();
        //将数据存储到session中
        User user = (User)session.getAttribute("userInfo");
        List<Course> courseList = new ArrayList<>();
        if(user != null) {
            if (user.getUserType() == 0) {
                List<User> commonList = userService.findByUserType(1);
                courseList = courseService.findAllCourse();
                int flag=4;

                commonList = userService.findByUserType(1);
                int size = commonList.size();
                int temp = size / flag + 1;
                boolean special = size % flag == 0;
                List<User> cutList = null;
                for (int i = 0; i < temp; i++) {
                    if (i == temp - 1) {
                        if (special) {
                            break;
                        }
                        cutList = commonList.subList(flag * i, size);
                    } else {
                        cutList = commonList.subList(flag * i, flag * (i + 1));
                    }
                    if(Integer.parseInt(p)==(i+1)){
                        model.addAttribute("commonList",cutList);
                        System.out.println("commonList"+cutList);
                        break;
                    }
                }
                System.out.println(cutList.size());
                model.addAttribute("current_Page",Integer.parseInt(p));
                model.addAttribute("commonListSize",commonList.size());
                System.out.println("commonListSize"+commonList.size());
//                ==========================================================
                model.addAttribute("courseList", courseList);
//                model.addAttribute("commonList", commonList);
            } else {
                List<User> commonList = new ArrayList<>();
                courseList = courseService.findByUserId(user.getId());
                model.addAttribute("courseList", courseList);
                if (courseId != null) {
                    List<UserCourse> userCourseList = userCourseService.findByCourseId(courseId);
                    for (int i = 0; i < userCourseList.size(); i++) {
                        User user1 = userService.getUserById(userCourseList.get(i).getUser().getId());
                        commonList.add(user1);
                    }
                    model.addAttribute("commonList", commonList);
                } else {
                    int flag=4;

                    commonList = userService.findByUserType(1);
                    int size = commonList.size();
                    int temp = size / flag + 1;
                    boolean special = size % flag == 0;
                    List<User> cutList = null;
                    for (int i = 0; i < temp; i++) {
                        if (i == temp - 1) {
                            if (special) {
                                break;
                            }
                            cutList = commonList.subList(flag * i, size);
                        } else {
                            cutList = commonList.subList(flag * i, flag * (i + 1));
                        }
                        if(Integer.parseInt(p)==(i+1)){
                            model.addAttribute("commonList",cutList);
                            System.out.println("commonList"+cutList);
                            break;
                        }
                    }
                    System.out.println(cutList.size());
                    model.addAttribute("current_Page",Integer.parseInt(p));
                    model.addAttribute("commonListSize",commonList.size());
                    System.out.println("commonListSize"+commonList.size());
//                    model.addAttribute("commonList", commonList);
                }
            }
        }
        return "/admin/user/common";
    }

    @GetMapping(value = "/view")
    public String getUserById(@RequestParam(value = "id", required = false) Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("userView", user);
        return "/admin/user/user_view";
    }

    @GetMapping(value = "/editStatus")
    public String editUserStatus(@RequestParam(value = "id") Integer id,@RequestParam(value = "flag",required = false) Integer flag, @RequestParam(value = "status") Integer status, Model model) {
        User user = new User();
        User user1 = userService.getUserById(id);
        if(flag !=null && flag == 1){
            user1.setUserType(2);
            user1.setUserStatus(0);
            user = userService.addUser(user1);
        }else{
            user = userService.updateUserStatusById(status, id);
        }
//        System.out.println("here is update," + user.getUserType() + user.getUsername());
        if (user1.getUserType() == 1) {
            List<User> commonList = userService.findByUserType(1);
            model.addAttribute("commonList", commonList);
            model.addAttribute("commonListSize",commonList.size());
            model.addAttribute("commonListSize",commonList.size());
            model.addAttribute("current_Page",1);
            return "/admin/user/common";
        } else {
            List<User> lectureList = userService.findByUserType(2);
            model.addAttribute("lectureListSize",lectureList.size());
            model.addAttribute("lectureList", lectureList);
            model.addAttribute("current_Page",1);
            return "/admin/user/lecture";
        }

    }

    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id") Integer id, Model model) {

        User user = userService.getUserById(id);
        userService.deleteUser(id);
        System.out.println("here is delete," + user.getUserType() + user.getUsername());
        if (user.getUserType() == 2) {
            List<User> lectureList = getAll();
            model.addAttribute("lectureList", lectureList);
            return "/admin/user/lecture";
        } else {
            List<User> commonList = getAll();
            model.addAttribute("commonList", commonList);
            return "/admin/user/common";
        }

    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid User user, BindingResult bindingResult, HttpServletRequest request,
                             @RequestParam(value="headimg") MultipartFile file, RedirectAttributes attributes) throws Exception {

        User user1 = userService.getUserById(user.getId());
        user.setCreateDate(user1.getCreateDate());
        user.setModifyDate(new Date());
        user.setPassword(user1.getPassword());
        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            Boolean isWindows = IsOs.isOsWindows();
            String filePath1 = "";
            if(isWindows == true){
                filePath1 = "C:\\data\\userHead\\";
            }else{
                filePath1 = "/tmp/data/userHead";
            }
//            String filePath1 = ClassUtils.getDefaultClassLoader().getResource("static/admin/").getPath();
            try {
                filePath1 = URLDecoder.decode(filePath1, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String file_name = System.currentTimeMillis() + suffixName;
            try {
                FileUtils.uploadFile(file.getBytes(), filePath1, file_name);
            } catch (Exception e) {
                // TODO: handle exception
            }
//             conf
            Configuration conf = new Configuration();
            String localDir =filePath1;
            String hdfsDir = "/learn/"+file_name;
            filePath1 = filePath1.substring(1);

            FileSystem hdfs = HdfsUtil.getFileSystem();
            HdfsUtil.copyFileToHDFS(file.getInputStream(),hdfsDir);
            System.out.println("Upload to " + conf.get("fs.default.name"));
            user.setHeadimg(file_name);
        } else {
            user.setHeadimg(user1.getHeadimg());
        }
        userService.addUser(user);
        if (user.getUserType() == 2 || user.getUserType() == 0) {
            return "redirect:/user/lecture";
        } else {
            return "redirect:/user/common";
        }

    }

    @PostMapping("/updateAdminUser")
    public String updateAdminUser(@Valid User user, BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response,
                                  @RequestParam(value="headimg") MultipartFile file, RedirectAttributes attributes){
        User user1 = userService.getUserById(user.getId());
        user.setCreateDate(user1.getCreateDate());
        user.setModifyDate(new Date());
        user.setPassword(user1.getPassword());
        user.setUserStatus(user1.getUserStatus());
        user.setUserType(user1.getUserType());
        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            String filePath = ClassUtils.getDefaultClassLoader().getResource("static/admin/upload/").getPath();
            String filePath1 = filePath+"upload/";
            try {
                filePath1 = URLDecoder.decode(filePath1, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String file_name = System.currentTimeMillis() + suffixName;
            try {
                FileUtils.uploadFile(file.getBytes(), filePath1, file_name);
            } catch (Exception e) {
                // TODO: handle exception
            }
            user.setHeadimg(file_name);
        } else {
            user.setHeadimg(user1.getHeadimg());
        }

        userService.addUser(user);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //使用request对象的getSession()获取session，如果session不存在则创建一个
        HttpSession session = request.getSession();
        //将数据存储到session中
        session.setAttribute("userInfo", user);

        redisService.setObj("user", user);


        return "redirect:/admin/index";
    }

}

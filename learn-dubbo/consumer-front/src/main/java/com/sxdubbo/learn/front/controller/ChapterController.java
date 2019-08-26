package com.sxdubbo.learn.front.controller;

import com.sxdubboapi.learn.domain.*;
import com.sxdubboapi.learn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * created by  luwei
 * 2018-02-01 15:44.
 **/
@Controller
@RequestMapping("/chapter")
public class ChapterController {
    @Autowired
    public ChapterService chapterService;
    @Autowired
    public VideoService videoService;
    @Autowired
    public CourseService courseService;
    @Autowired
    public UserService userService;
    @Autowired
    public UserVideoService userVideoService;
    @Autowired
    public RedisService redisService;
    @Autowired
    public TestPaperService testPaperService;
    @Autowired
    public UserCourseService userCourseService;
    @Autowired
    public CourseScoreService courseScoreService;

    @GetMapping("/listChapter")
    public String listChapter(@RequestParam("id") Integer id, Model model, HttpServletRequest request, HttpServletResponse response){
        Course course = courseService.findById(id);
        List<Chapter> chapterList = chapterService.findByCourseId(id);
        List<Video> videoList = videoService.findByCourseId(id);
        System.out.println(course.getUserId()+course.getCourseName());
        User user = userService.getUserById(course.getUserId());
        List<UserCourse> userCourseList = userCourseService.findByCourseId(id);

        List<TestPaper> testPaper=testPaperService.findByCourse(course);

        double total_time = 0.0;
        for(int i=0;i< videoList.size();i++){
            total_time = total_time + videoList.get(i).getVideoDuration();
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //使用request对象的getSession()获取session，如果session不存在则创建一个
        HttpSession session = request.getSession();
        //将数据存储到session中
        User user1 = (User)session.getAttribute("userFront");
        if(user1 !=null){
            List<UserVideo> userVideoList = userVideoService.findByUserIdAndCourseId(user1.getId(),id);
            CourseScore courseScore=courseScoreService.findByCourseAndUser(course,user1);

            model.addAttribute("userVideoList",userVideoList);
            model.addAttribute("Score",courseScore);
            System.out.println(userVideoList.size()+"@@@@@@@@@@@@"+user1.getId()+"%%%%"+id);
        }



        model.addAttribute("testpaper",testPaper);
        model.addAttribute("course",course);
        model.addAttribute("chapterList",chapterList);
        model.addAttribute("videoList",videoList);
        model.addAttribute("user",user);
        model.addAttribute("userCourseNum",userCourseList.size());
        model.addAttribute("courseTime",total_time/3600);

        return "/front/course/chapter_list";
    }
    @GetMapping("/chapterManage")
    public String chapterMange(Model model, @RequestParam("id") Integer courseId){
//        User user_redis = new User();
//        user_redis = (User) redisService.getObj("user");

        List<Chapter> chapterList = chapterService.findByCourseId(courseId);
        model.addAttribute("chapterList", chapterList);
        Course course = courseService.findById(courseId);
        model.addAttribute("chapterNum",course.getChapterNum());
        model.addAttribute("courseId",courseId);
        return "/admin/chapter/chapter_manage";
    }

    @RequestMapping("/addChapter")
    public String addChapter(@Valid Chapter chapter, Model model, RedirectAttributes attributes){
        chapter.setCreateDate(new Date());
        chapter.setModifyDate(new Date());
        Chapter chapter1 = new Chapter();
        System.out.println("add success++++++++++++" + chapter.getChapterName());
        chapter1 = chapterService.addChapter(chapter);
        attributes.addAttribute("id",chapter.getCourseId());
        return "redirect:/chapter/chapterManage";
    }

    @GetMapping("/deleteChapter")
    public String deleteChapter(@RequestParam("id") Integer id, RedirectAttributes attributes){
        Chapter chapter = new Chapter();
        chapter = chapterService.findById(id);
        chapterService.deleteChapter(id);
        attributes.addAttribute("id",chapter.getCourseId());
        return "redirect:/chapter/chapterManage";
    }

    @PostMapping("/updateChapter")
    public String updateChapter(@Valid Chapter chapter, RedirectAttributes attributes){
        Chapter chapter_final = chapterService.findById(chapter.getId());
//        System.out.println(chapter.getId());
        chapter_final.setModifyDate(new Date());
        chapter_final.setChapterName(chapter.getChapterName());
        chapter_final.setVideoNum(chapter.getVideoNum());
//        Chapter chapter1 = new Chapter();
        System.out.println("update success++++++++++++" + chapter.getChapterName());
        Chapter chapter1 = chapterService.addChapter(chapter_final);
        attributes.addAttribute("id",chapter_final.getCourseId());
        return "redirect:/chapter/chapterManage";
    }


}

package com.mmking.course.controller;

import com.mmking.api.CourseApi;
import com.mmking.course.service.TeachPlanService;
import com.mmking.framework.domain.cms.course.Teachplan;
import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import com.mmking.framework.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course")
public class TeachPlanController implements CourseApi {

    @Autowired
    private TeachPlanService teachPlanService;



    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTreeNodeByCourseId(@PathVariable("courseId") String courseId) {
        return teachPlanService.findRootTree(courseId);

    }



    /**
     * //添加课程计划
     * @author ShangKun
     * @param teachplan :
     * @return com.mmking.framework.model.response.ResponseResult
     * @date 2020/6/11 20:05
     */
    @Override
    @PostMapping("teachplan/add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {

        return teachPlanService.addTeachPlan(teachplan);

    }
}

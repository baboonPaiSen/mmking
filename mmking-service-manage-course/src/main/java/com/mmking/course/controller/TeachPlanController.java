package com.mmking.course.controller;

import com.mmking.api.CourseApi;
import com.mmking.course.service.TeachPlanService;
import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class TeachPlanController implements CourseApi {

    @Autowired
    private TeachPlanService teachPlanService;

    @Override
    @GetMapping("/teachPlan/list/{courseId}")
    public TeachplanNode findTreeNodeByCourseId(@PathVariable("courseId") String courseId) {
        return teachPlanService.findRootTree(courseId);
    }
}

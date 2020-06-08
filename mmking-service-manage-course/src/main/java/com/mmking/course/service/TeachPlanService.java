package com.mmking.course.service;

import com.mmking.course.mapper.TeachPlanMapper;
import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachPlanService {

    @Autowired
    private TeachPlanMapper teachPlanMapper;

    public TeachplanNode findRootTree (String courseId){
        TeachplanNode teachplanNode = teachPlanMapper.selectListByCourseId(courseId);
        if (teachplanNode!=null){

            return  teachplanNode;
        }

        return  null;

    }



}

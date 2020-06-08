package com.mmking.course.service;

import com.mmking.course.mapper.TeachPlanMapper;
import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TeachPlanServiceTest {

    @Autowired
    private TeachPlanMapper teachPlanMapper;
    @Test
    public  void testTree(){
        TeachplanNode teachplanNode = teachPlanMapper.selectListByCourseId("4028e581617f945f01617f9dabc40000");
    }
}
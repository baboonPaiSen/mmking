package com.mmking.course.service;

import com.mmking.course.mapper.CourseBaseRepository;
import com.mmking.course.mapper.TeachPlanMapper;
import com.mmking.course.mapper.TeachPlanNodeMapper;
import com.mmking.framework.domain.cms.course.CourseBase;
import com.mmking.framework.domain.cms.course.Teachplan;
import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.framework.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TeachPlanService {

    @Autowired
    private TeachPlanNodeMapper teachPlanNodeMapper;
    @Autowired
    private TeachPlanMapper teachPlanMapper;

    @Autowired
    private CourseBaseRepository courseBaseRepository;


    /**
     * //TODO找到节点树
     * @author ShangKun
     * @param courseId :
     * @return com.mmking.framework.domain.cms.course.ext.TeachplanNode
     * @date 2020/6/13 16:30
     */
    public TeachplanNode findRootTree (String courseId){
        return teachPlanNodeMapper.selectListByCourseId(courseId);

    }



    /**
     * //TODO添加课程计划
     * @author ShangKun
     * @param teachplan :
     * @return com.mmking.framework.model.response.ResponseResult
     * @date 2020/6/11 20:05
     */
    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {

        String courseid = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        if(StringUtils.isEmpty(parentId)){
            parentId = this.findParentId(courseid);
        }
        Teachplan newTeachplan = new Teachplan();
        BeanUtils.copyProperties(teachplan,newTeachplan);
        newTeachplan.setParentid(parentId);
        newTeachplan.setCourseid(courseid);
        Optional<Teachplan> parent = teachPlanMapper.findById(parentId);
        String grade = parent.get().getGrade();
        newTeachplan.setGrade(String.valueOf(Integer.valueOf(grade)+1));
        Teachplan save = teachPlanMapper.save(newTeachplan);
        return  new ResponseResult(CommonCode.SUCCESS);

    }

    /**
     * //TODO查询父节点id
     * @author ShangKun
     * @param courseId :
     * @return void
     * @date 2020/6/15 11:29
     */
    @Transactional
    public String findParentId(String courseId) {

            List<Teachplan> byCourseidAndParentid = teachPlanMapper.findByCourseidAndParentid(courseId, String.valueOf(0));
            //查询不到自动添加根节点

            Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
            if (!byId.isPresent()){
                return  null;
            }
            CourseBase courseBase = byId.get();
            if (CollectionUtils.isEmpty(byCourseidAndParentid)){
                Teachplan root = new Teachplan();
                root.setParentid("0");
                root.setGrade("1");
                root.setPname(courseBase.getName());
                root.setCourseid(courseId);
                root.setStatus("0");
                Teachplan save = teachPlanMapper.save(root);
                return save.getId();
            }

        return  byCourseidAndParentid.get(0).getId();
        }
    }


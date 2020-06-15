package com.mmking.course.mapper;

import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TeachPlanNodeMapper  {

    TeachplanNode selectListByCourseId(String courseId);

//    List<Teachplan> findbyCourseIdAndParentid(String courseId ,String  parentId);


}

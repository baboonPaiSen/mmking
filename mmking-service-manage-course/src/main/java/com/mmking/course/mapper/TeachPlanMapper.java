package com.mmking.course.mapper;

import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface TeachPlanMapper {

    TeachplanNode selectListByCourseId(String courseId);
}

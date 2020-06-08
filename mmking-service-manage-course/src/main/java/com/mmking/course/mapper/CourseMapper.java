package com.mmking.course.mapper;

import com.mmking.framework.domain.cms.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {


   CourseBase findCourseBaseById(String id);
}

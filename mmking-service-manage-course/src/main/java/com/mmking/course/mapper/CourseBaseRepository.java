package com.mmking.course.mapper;

import com.mmking.framework.domain.cms.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseBaseRepository  extends JpaRepository<CourseBase,String> {
}
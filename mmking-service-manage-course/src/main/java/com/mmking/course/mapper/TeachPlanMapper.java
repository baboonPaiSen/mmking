package com.mmking.course.mapper;

import com.mmking.framework.domain.cms.course.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description: TODO
 * @date: 2020/6/13 16:17
 * @author: ShangKun
 * @version: 2.0.0
 */
@Mapper
public interface TeachPlanMapper extends JpaRepository<Teachplan,String> {

     List<Teachplan> findByCourseidAndParentid(String courseid,String parentId);

}

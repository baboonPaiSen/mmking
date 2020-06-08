package com.mmking.api;


import com.mmking.framework.domain.cms.course.ext.TeachplanNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程管理接口",description = "课程接口，提供课程的增、删、改、查")
public interface CourseApi {
    @ApiOperation("根据课程id查树形关系")
    TeachplanNode findTreeNodeByCourseId(String courseId);
}

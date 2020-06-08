package com.mmking.api;


import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.domain.cms.request.QueryPageRequest;
import com.mmking.framework.domain.cms.response.CmsPageResult;
import com.mmking.framework.model.response.QueryResponseResult;
import com.mmking.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")
            @ApiImplicitParams({@ApiImplicitParam(name="page",value="页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value="每页记录数",required=true,paramType="path",dataType="int")})
    QueryResponseResult findList(Integer page, Integer size, QueryPageRequest queryPageRequest);

    @ApiOperation("新增页面")
    CmsPageResult insertPage(CmsPage cmsPage);

    @ApiOperation("根据主键(pageId)检索")
    CmsPageResult selectPageById(String pageId);
    @ApiOperation("根据主键(pageId)修改页面")
    CmsPageResult updatePageById(String pageId,CmsPage cmsPage);

    @ApiOperation("根据主键(pageId)删除页面")
    ResponseResult deletePageById(String pageId);


}

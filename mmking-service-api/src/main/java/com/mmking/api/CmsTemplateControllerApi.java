package com.mmking.api;

import com.mmking.framework.domain.cms.CmsTemplate;
import com.mmking.framework.model.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="cms模板管理接口",description = "cms模板管理接口，提供模板的增、删、改、查")
public interface CmsTemplateControllerApi {
    @ApiOperation("根据站点id查询模板")
    List<CmsTemplate> queryTemplateBySiteId(String siteId);

    @ApiOperation("生成静态页预览图")

    void generatePageByPageId(String id, HttpServletResponse response);


    @ApiOperation("正式生成静态页并改写文件数据")
    void insertHtmlFileId(String pageId);

}

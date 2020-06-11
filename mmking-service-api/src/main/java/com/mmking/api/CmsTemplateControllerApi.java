package com.mmking.api;

import com.mmking.framework.domain.cms.CmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="cms模板管理接口")
public interface CmsTemplateControllerApi {
    @ApiOperation("根据站点id查询模板")
    List<CmsTemplate> queryTemplateBySiteId(String siteId);

    @ApiOperation("生成静态页预览图")

    void generatePageByPageId(String id, HttpServletResponse response);


    @ApiOperation("正式生成静态页并改写文件数据")
    ResponseEntity insertHtmlFileId(String pageId);

}

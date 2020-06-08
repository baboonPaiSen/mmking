package com.mmking.manage_cms.controller;

import com.mmking.api.CmsTemplateControllerApi;
import com.mmking.framework.domain.cms.CmsTemplate;
import com.mmking.framework.exception.MMkingException;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.manage_cms.service.CmsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {


    @Autowired
    private CmsTemplateService cmsTemplateService;
    /**
     * 根据站点id查询template
     * @param siteId
     * @return
     */
    @Override
    @GetMapping("{siteId}")
    public List<CmsTemplate> queryTemplateBySiteId(@PathVariable("siteId") String siteId) {
         return  cmsTemplateService.queryTemplateBySiteId(siteId);
    }


    /**
     * 根据pageId生成静态页预览
     * @param pageId
     * @return
     */
    @Override
    @GetMapping("preview/{pageId}")
    public void generatePageByPageId(@PathVariable  String pageId, HttpServletResponse response) {
        String page = cmsTemplateService.generatePage(pageId);
        try {
            response.getWriter().write(page);
        } catch (IOException e) {
            log.error("【写入页面异常】pageId:{}",pageId);
            throw  new MMkingException(CommonCode.IO_ERROR);
        }
    }

    /**
     * 根据pageId生成静态页并改写数据库
     * @param pageId
     */
    @Override
    @PostMapping("{pageId}")
    public void insertHtmlFileId(@PathVariable("pageId") String pageId) {
         cmsTemplateService.insertHtmlFileId(pageId);
    }
}

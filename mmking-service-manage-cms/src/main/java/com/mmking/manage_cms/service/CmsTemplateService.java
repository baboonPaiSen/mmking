package com.mmking.manage_cms.service;

import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.domain.cms.CmsTemplate;
import com.mmking.framework.domain.cms.response.CmsCode;
import com.mmking.framework.domain.cms.response.CmsPageResult;
import com.mmking.framework.exception.MMkingException;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.manage_cms.mapper.CmsTemplateRepository;
import com.mmking.utils.JsonUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CmsTemplateService {
    @Autowired
    private CmsPageService cmsPageService;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据站点id查询template
     * @param siteId
     * @return
     */
    public List<CmsTemplate> queryTemplateBySiteId(String siteId) {

        List<CmsTemplate> list = cmsTemplateRepository.findBySiteId(siteId);

        return  list;
    }

    /**
     * 根据pageId生成静态页
     * @param pageId
     * @return
     */
    public String generatePage(String pageId){
        /*获取数据模型*/
        Map model = this.getTemplateModel(pageId);
        /*获取模板*/
        String template = this.getTemplate(pageId);
        if (StringUtils.isBlank(template)){
            throw new MMkingException(CmsCode.CMS_NOTFOUNT);
        }


        //合成静态页
        Configuration configuration = new Configuration(Configuration.getVersion());
        /*创建模板加载器*/
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

        stringTemplateLoader.putTemplate(pageId,template);

        /*添加模板加载器*/
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            Template generatePageTemplate = configuration.getTemplate(pageId);

            /*注意model是一个list 里面存放的map*/

            return FreeMarkerTemplateUtils.processTemplateIntoString(generatePageTemplate, model);
        } catch (Exception e) {
            throw new MMkingException(CommonCode.IO_ERROR);
        }



    }

    /**
     * 根据pageId查询数据模型
     * @param pageId
     * @return
     */
    private Map getTemplateModel(@NotNull String pageId){


        CmsPageResult cmsPageResult = cmsPageService.selectPageById(pageId);
        if (cmsPageResult.getCode()!=404){
            String dataUrl = cmsPageResult.getCmsPage().getDataUrl();
            /*获取远程调用*/
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            /*获取查询到的内容*/
            Map body = forEntity.getBody();
            if (body==null){
                throw new MMkingException(CmsCode.CMSCONFIG_NOTFOUND);
            }


            return  body;
        }else {
            throw new MMkingException(CmsCode.CMS_NOTFOUNT);
        }



    }


    /**
     * 根据pageId返回模板内容
     * @param pageId
     * @return
     */
    private String getTemplate(String pageId){

        /*定义*/
        CmsTemplate cmsTemplate = null;

        CmsPageResult cmsPageResult = cmsPageService.selectPageById(pageId);
        if (cmsPageResult.getCode()!=404){
            String templateId = cmsPageResult.getCmsPage().getTemplateId();
            Optional<CmsTemplate> template = cmsTemplateRepository.findById(templateId);
            if (template.isPresent()){
                 cmsTemplate = template.get();
            }

            /*去mongodb 下载*/
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(cmsTemplate.getTemplateFileId())));
            if (gridFSFile==null){
                throw new MMkingException(CommonCode.IO_ERROR);
            }
            /*获取下载流*/
            GridFSDownloadStream stream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            /*开始下载*/

            GridFsResource resource = new GridFsResource(gridFSFile, stream);

            try {
                InputStream inputStream = resource.getInputStream();

                return IOUtils.toString(inputStream, "utf-8");
            } catch (Exception e) {
                log.error("[从服务器下载数据异常]{}",templateId);
                e.printStackTrace();
                throw new MMkingException(CommonCode.IO_ERROR);

            }
        }

        throw new MMkingException(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
    }

    /**
     * 根据pageId向GridFs中添加HTML文件返回文件名
     * @param pageId
     */
    public  String  createPageFileToGridFS(String pageId){

        /*生成静态页*/
        String html = this.generatePage(pageId);
        /*获取页面名称*/
        CmsPageResult cmsPageResult = cmsPageService.selectPageById(pageId);
        if (cmsPageResult.getCode()==404){
            throw  new MMkingException(CmsCode.CMS_NOTFOUNT);
        }
          String pageName = cmsPageResult.getCmsPage().getPageName();
        /*向GridFS中写入数据*/
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toInputStream(html, "utf-8");
        } catch (IOException e) {
            log.error("【文件写入异常】 页面名：{}",pageName);
            throw  new MMkingException(CommonCode.IO_ERROR);
        }

        ObjectId objectId = gridFsTemplate.store(inputStream, pageName);

        return  objectId.toString();



    }

    /**
     * 根据pageId生成静态化页面 ，重写HtmlFileId，发送Mq
     * @param pageId
     */
    public void insertHtmlFileId(String pageId){

        String htmlFileId = createPageFileToGridFS(pageId);

        CmsPageResult cmsPageResult = cmsPageService.selectPageById(pageId);
        if (cmsPageResult.getCode()==404){
            throw  new MMkingException(CmsCode.CMS_NOTFOUNT);
        }


        CmsPage cmsPage = cmsPageResult.getCmsPage();
        cmsPage.setHtmlFileId(htmlFileId);
        cmsPageService.updatePageById(pageId,cmsPage);

        Map<String, String> map = new HashMap<>();
        map.put("pageId",pageId);

        amqpTemplate.convertAndSend("mmking.page","html.create", JsonUtils.serialize(map));
    }

}

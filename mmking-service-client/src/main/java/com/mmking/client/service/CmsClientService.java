package com.mmking.client.service;

import com.mmking.client.mapper.CmsPageRepository;
import com.mmking.client.mapper.CmsSiteRepository;
import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.domain.cms.CmsSite;
import com.mmking.framework.domain.cms.response.CmsCode;
import com.mmking.framework.exception.MMkingException;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CmsClientService {

    private final CmsPageRepository pageRepository;
    private final CmsSiteRepository siteRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    public CmsClientService(CmsPageRepository pageRepository, CmsSiteRepository siteRepository, GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket) {
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
    }

    /**
     * 根据pageId获取WEB路径
     * @param pageId
     * @return
     */
    public String  findHtmlPath(String pageId){

        /*查询CmsPage获得域名后缀和网址名称*/
        CmsPage cmsPage = getCmsPage(pageId);


        String pageWebPath = cmsPage.getPageWebPath();
        String pageHtml = cmsPage.getPageHtml();

        /*查询CmsSite并获得域名前缀*/

        Optional<CmsSite> siteOptional = siteRepository.findById(cmsPage.getSiteId());
        if (!siteOptional.isPresent()){
            throw new MMkingException(CmsCode.CMSSITE_NOTFOUND);
        }
        CmsSite cmsSite = siteOptional.get();
        String siteWebPath = cmsSite.getSiteWebPath();

        /*拼合地址获得网址路径*/

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer append = stringBuffer.append(Arrays.asList(siteWebPath, pageWebPath, pageHtml));


        return append.toString();

    }

    /**
     * 根据pageId获取物理路径
     * @return
     */
    public String  findHtmlPhysicalPath(String pageId){
        CmsPage cmsPage = getCmsPage(pageId);
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        if (StringUtils.isNotBlank(pagePhysicalPath)){
            return  pagePhysicalPath+cmsPage.getPageName();
        }

        throw new MMkingException(CmsCode.CMS_PHYSICALPATH_NOTFOUNT);
    }

    /**
     * 根据pageId查询CmsPage
     * @param pageId
     * @return
     */
    public CmsPage getCmsPage(String pageId) {
        Optional<CmsPage> pageOptional = pageRepository.findById(pageId);
        if (!pageOptional.isPresent()){
            throw new MMkingException(CmsCode.CMS_NOTFOUNT);
        }
        return pageOptional.get();
    }

    /**
     * 根据pageId下载文件并存储到路径中
     * @param pageId
     */
    public  void  saveHtmlToPath(String pageId){

        CmsPage cmsPage = this.getCmsPage(pageId);
        /*绑定操作对象*/
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(cmsPage.getHtmlFileId())));
        /*打开下载流*/
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        /*通过GridFsResource获取输入流*/
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

        InputStream inputStream = null;
        FileOutputStream outputStream = null;


        try {
            /*获得输入流*/
             inputStream = gridFsResource.getInputStream();
            /*根据路径输入到磁盘*/
            outputStream =new FileOutputStream(new File(this.findHtmlPhysicalPath(pageId)));
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
                if (outputStream!=null)

                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



}

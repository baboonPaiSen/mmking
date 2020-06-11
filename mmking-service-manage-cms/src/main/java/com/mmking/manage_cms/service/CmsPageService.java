package com.mmking.manage_cms.service;


import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.domain.cms.request.QueryPageRequest;
import com.mmking.framework.domain.cms.response.CmsPageResult;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.framework.model.response.QueryResponseResult;
import com.mmking.framework.model.response.QueryResult;
import com.mmking.framework.model.response.ResponseResult;
import com.mmking.manage_cms.mapper.CmsPageRepository;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private  CmsTemplateService cmsTemplateService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private AmqpTemplate  amqpTemplate;

    /**
     * //TODO分页查询
     * @author ShangKun
     * @param cachePage :
     * @param size :
     * @param queryPageRequest :
     * @return com.mmking.framework.model.response.QueryResponseResult
     * @date 2020/6/9 11:24
     */
    public QueryResponseResult findList(Integer cachePage, Integer size, QueryPageRequest queryPageRequest) {
        /*处理页码  默认0页为第一页*/
        int page = cachePage-1;
        Pageable pageable = PageRequest.of(page, size);

        /*设置模糊查询*/

        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        /*设置查询条件*/
        CmsPage cmsPage = new CmsPage();
        BeanUtils.copyProperties(queryPageRequest,cmsPage);
        /*获得查询条件*/
        Example<CmsPage> example = Example.of(cmsPage, matcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        /*封装queryResult对象*/
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        //设置总页数
        queryResult.setTotal(all.getTotalPages());
        queryResult.setList(all.getContent());

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    /**
     * //TODO新增页面
     * @author ShangKun
     * @param cmsPage :
     * @return com.mmking.framework.domain.cms.response.CmsPageResult
     * @date 2020/6/10 14:41
     */
    public CmsPageResult insertPage(CmsPage cmsPage) {
        /*判断页面是否存在，存在则保存，不存在则报错*/
        CmsPage cachePage = new CmsPage();
        /*索引匹配*/
        cachePage.setSiteId(cmsPage.getSiteId());
        cachePage.setPageName(cmsPage.getPageName());
        cachePage.setPageWebPath(cmsPage.getPageWebPath());

        Example<CmsPage> example = Example.of(cachePage);
        Optional<CmsPage> one = cmsPageRepository.findOne(example);
        //判断是否存在
        if (one.isPresent()){
            //存在，抛出异常
            return  new CmsPageResult(CommonCode.FAIL,null);
        }
        else {
            cmsPageRepository.save(cmsPage);

            return  new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }

    }
    /**
     * 主键(pageId)检索页面
     * @param pageId
     * @return
     */
    public CmsPageResult selectPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }else {
            return new CmsPageResult(CommonCode.NOT_FOUNT,null);
        }
    }
    /**
     * 根据主键(pageId)修改cmsPage
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult updatePageById(String pageId, CmsPage cmsPage) {

        CmsPageResult cmsPageResult = this.selectPageById(pageId);
        /*取出其中的数值*/
        if (cmsPageResult.getCode()!=404){
            CmsPage findPage = cmsPageResult.getCmsPage();
            /*把查询到的结果 去除id封装到查询结果*/
            BeanUtils.copyProperties(cmsPage,findPage);
            CmsPage save = cmsPageRepository.save(findPage);
            return  new CmsPageResult(CommonCode.SUCCESS,save);
        }else{
            return  new CmsPageResult(CommonCode.FAIL,null);
        }
    }
    /**
     * 根据主键(pageId)删除cmsPage
     * @param pageId
     * @return
     */
    public ResponseResult deletePageById(String pageId) {
        CmsPageResult cmsPageResult = this.selectPageById(pageId);
        /*取出其中的数值*/
        if (cmsPageResult.getCode()!=404){
            cmsPageRepository.deleteById(pageId);
            return new  ResponseResult(CommonCode.SUCCESS);
        }else {
            return  new ResponseResult(CommonCode.FAIL);
        }
    }

    /**
     * //TODO生成静态页到磁盘
     * @author ShangKun
     * @param pageId :
     * @return com.mmking.framework.model.response.ResponseResult
     * @date 2020/6/11 16:07
     */
    public ResponseResult createPage(String pageId){
        String htmlString = cmsTemplateService.generatePage(pageId);

        CmsPage cmsPage = this.selectPageById(pageId).getCmsPage();
        try {
            InputStream inputStream = IOUtils.toInputStream(htmlString, "uft_8");
            ObjectId store = gridFsTemplate.store(inputStream, cmsPage.getPageName());
            cmsPage.setHtmlFileId(store.toString());
            this.updatePageById(pageId,cmsPage);
            /*发送消息到MQ*/


        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ResponseResult.SUCCESS();
    }


}

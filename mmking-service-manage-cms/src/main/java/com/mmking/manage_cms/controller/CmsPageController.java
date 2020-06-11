package com.mmking.manage_cms.controller;

import com.mmking.api.CmsPageControllerApi;
import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.domain.cms.request.QueryPageRequest;
import com.mmking.framework.domain.cms.response.CmsPageResult;
import com.mmking.framework.model.response.QueryResponseResult;
import com.mmking.framework.model.response.ResponseResult;
import com.mmking.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 分页查询
     * @param cachePage
     * @param size
     * @param queryPageRequest
     * @return
     */
    @Override
    @RequestMapping(path = "/list/{page}/{size}",method = RequestMethod.GET)
    public QueryResponseResult findList(@PathVariable("page") Integer cachePage,@PathVariable("size") Integer size, QueryPageRequest queryPageRequest) {

        return cmsPageService.findList(cachePage,size,queryPageRequest);
    }


    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping
    public CmsPageResult insertPage(@RequestBody @Valid CmsPage cmsPage) {


        return cmsPageService.insertPage(cmsPage);
    }


    /**
     * 主键(pageId)检索页面
     * @param pageId
     * @return
     */
    @GetMapping("{id}")
    @Override
    public CmsPageResult selectPageById(@PathVariable("id")  String pageId) {

        return cmsPageService.selectPageById(pageId);
    }

    /**
     * 根据主键(pageId)修改cmsPage
     * @param pageId
     * @param cmsPage
     * @return
     */
    @PutMapping("{id}")
    @Override
    public CmsPageResult updatePageById(@PathVariable("id") String pageId, @RequestBody CmsPage cmsPage) {
        return  cmsPageService.updatePageById(pageId,cmsPage);
    }

    /**
     * 根据主键(pageId)删除cmsPage
     * @param pageId
     * @return
     */
    @DeleteMapping("{id}")
    @Override
    public ResponseResult deletePageById(@PathVariable("id") String pageId) {

        return cmsPageService.deletePageById(pageId);
    }

}

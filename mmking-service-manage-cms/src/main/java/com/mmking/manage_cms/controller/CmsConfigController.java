package com.mmking.manage_cms.controller;

import com.mmking.api.CmsConfigApi;
import com.mmking.framework.domain.cms.CmsConfig;
import com.mmking.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigApi {
    @Autowired
    private CmsConfigService service;

    /**
     * 根据主键查询cmsConfigC:\Users\baboon\Downloads
     * @param id
     * @return
     */
    @Override
    @GetMapping("{id}")
    public CmsConfig selectCmsConfigByid( @PathVariable("id") String id) {
        return  service.selectCmsConfigByid(id);
    }
}

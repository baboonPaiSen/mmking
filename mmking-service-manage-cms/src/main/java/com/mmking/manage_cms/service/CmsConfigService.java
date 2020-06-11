package com.mmking.manage_cms.service;

import com.mmking.framework.domain.cms.CmsConfig;
import com.mmking.framework.domain.cms.response.CmsCode;
import com.mmking.framework.exception.MMkingException;
import com.mmking.manage_cms.mapper.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsConfigService {

    @Autowired
    private CmsConfigRepository repository;



    /**
     * 根据主键查询CmsConfig
     * @param id
     * @return
     */
    public CmsConfig selectCmsConfigByid(String id) {

        Optional<CmsConfig> optional = repository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }


        throw new MMkingException(CmsCode.CMSCONFIG_NOTFOUND);
    }
}

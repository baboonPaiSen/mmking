package com.mmking.api;

import com.mmking.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面配置接口" )
public interface CmsConfigApi {
    @ApiOperation(value = "根据主键查询cmsCnfig")
    CmsConfig selectCmsConfigByid(String id);
}

package com.mmking.framework.domain.cms.response;

import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.model.response.ResponseResult;
import com.mmking.framework.model.response.ResultCode;
import lombok.Data;

/**
 * Created by mrt on 2018/3/31.
 */
@Data
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;
    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}

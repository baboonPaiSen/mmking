package com.mmking.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryPageRequest {
    //站点ID
    @ApiModelProperty(value = "站点ID")
    @NotNull(message = "站点id不能为空")
    private String siteId;
    //页面ID
    @NotNull(message = "页面id不能为空")
    private String pageId;
    //页面名称
    private String pageName;
    // 别名
    private String pageAliase;
    // 模版id
    private String templateId;
}

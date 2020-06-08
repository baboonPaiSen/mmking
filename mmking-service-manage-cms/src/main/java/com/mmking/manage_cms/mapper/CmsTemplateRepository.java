package com.mmking.manage_cms.mapper;

import com.mmking.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

    List<CmsTemplate> findBySiteId(String siteId);
}

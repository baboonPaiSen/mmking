package com.mmking.manage_cms.mapper;

import com.mmking.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}

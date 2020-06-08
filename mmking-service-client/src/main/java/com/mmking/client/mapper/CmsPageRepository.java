package com.mmking.client.mapper;

import com.mmking.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository  extends MongoRepository<CmsPage,String> {
}

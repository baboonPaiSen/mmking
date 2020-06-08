package com.mmking.manage_cms;


import com.mmking.framework.domain.cms.CmsPage;


import com.mmking.framework.exception.MMkingException;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.manage_cms.mapper.CmsPageRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(value = false)
public class manage_cms {
    @Autowired
    private CmsPageRepository repository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 查全部
     */
    @Test
    public void testConnection(){
        List<CmsPage> all = repository.findAll();
        System.out.println(all);

    }


    @Test
    public  void testFindByConditon(){
        
        CmsPage cmsPage = new CmsPage();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        List<CmsPage> all = repository.findAll(example);
        System.out.println(all);

    }

    @Test
    public  void testAdvice(){
        throw  new MMkingException(CommonCode.SUCCESS);
    }



    @Test
    public void insertFile() throws  Exception{
        File file = new File("L:\\Writing\\mmking\\mmking-service-manage-cms\\src\\main\\resources\\templates\\index_banner.ftl");

        FileInputStream inputStram=new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(inputStram, "index_banner.html");

        System.out.println(objectId.toString());

    }

    @Test
    public  void findFile()throws  Exception{
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e96e4a2639bf52a44c9b6b8")));
        /*获取下载流*/
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        /*开始下载*/

        GridFsResource resource = new GridFsResource(gridFSFile, stream);

        InputStream inputStream = resource.getInputStream();



    }

}
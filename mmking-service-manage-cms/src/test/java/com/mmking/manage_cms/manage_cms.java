package com.mmking.manage_cms;


import com.mmking.framework.domain.cms.CmsPage;
import com.mmking.framework.exception.MMkingException;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.manage_cms.mapper.CmsPageRepository;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

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

        Sort sort;
        Pageable pageable = PageRequest.of(1,1);
        Page<CmsPage> all = repository.findAll(pageable);


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
        File file = new File("H:\\项目\\mmking\\mmking-service-manage-cms\\src\\main\\resources\\templates\\index_banner.ftl");

        FileInputStream inputStram=new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(inputStram, "index_banner.html");

        System.out.println(objectId.toString());

    }

    @Test
    public  void findFile()throws  Exception{
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5a7719d76abb5042987eec3a")));
        /*获取下载流*/
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(Objects.requireNonNull(gridFSFile).getObjectId());
        /*开始下载*/

        GridFsResource resource = new GridFsResource(gridFSFile, stream);

        String s = IOUtils.toString(resource.getInputStream(), "utf-8");

        System.out.println(s);


    }

    @Test
    public  void createFileToLocal() throws  Exception {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5a7719d76abb5042987eec3a")));
        /*打开下载流*/
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        /*通过GridFsResource获取输入流*/
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

        InputStream inputStream = null;
        FileOutputStream outputStream = null;



        /*获得输入流*/
        inputStream = gridFsResource.getInputStream();
        /*根据路径输入到磁盘*/
        outputStream = new FileOutputStream(new File("G:\\gridFs\\index.html"));
        IOUtils.copy(inputStream, outputStream);
    }


}
package com.mmking.manage_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsTemplateServiceTest {
    @Autowired
    CmsTemplateService cmsTemplateService;


    @Test
    public void generatePage() {
        String s = cmsTemplateService.generatePage("5a795ac7dd573c04508f3a56");
        System.out.println(s);
    }
}
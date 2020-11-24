package com.highershine.portal.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.entity.po.Category;
import com.highershine.portal.common.mapper.CategoryMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
@Rollback
@Transactional
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    // 声明Jackson工具类
    public final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private CategoryMapper categoryMapper;
    private Long ID = 0L;
    private String JSESSIONID = "f0a81419f0d2dbaa41a0ed08c507e21fb22ea7d6d9c0858c0d1ecf22baee6935";

    @Test
    public void getCategoryList() throws Exception {
        Map param = new HashMap<>();
        param.put("pageSize",10);
        param.put("pageNum",1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/category/getCategoryList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MAPPER.writeValueAsString(param))
                .cookie(new Cookie("jsessionid",JSESSIONID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = MAPPER.readValue(contentAsString, Map.class);
        Object code = map.get("code");
        Assert.assertEquals((int)code,1);
    }

    @Test
    public void addCategory() throws Exception {

        Map param = new HashMap<>();
        param.put("name","通知");
        param.put("alias","TZ");
        param.put("sort",5);
        param.put("status",true);
        param.put("extId","11111111");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/category/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MAPPER.writeValueAsString(param))
                .cookie(new Cookie("jsessionid",JSESSIONID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = MAPPER.readValue(contentAsString, Map.class);
        Object code = map.get("code");
        Assert.assertEquals((int)code,1);
        Map data = (Map) map.get("data");
        ID = Long.parseLong(data.get("id").toString());
        Assert.assertEquals(data.get("alias"),"TZ");

    }

    @Test
    public void delTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/category/del/2")
                .cookie(new Cookie("jsessionid",JSESSIONID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = MAPPER.readValue(contentAsString, Map.class);
        Object code = map.get("code");
        Assert.assertEquals((int)code,1);
    }

    @Test
    public void updateTest() throws Exception {
        Category category = categoryMapper.selectByPrimaryKey(3L);
        category.setAlias("TXTX");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/category/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MAPPER.writeValueAsString(category))
                .cookie(new Cookie("jsessionid",JSESSIONID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = MAPPER.readValue(contentAsString, Map.class);
        Object code = map.get("code");
        Assert.assertEquals((int)code,1);
        Map data = (Map) map.get("data");
        Assert.assertEquals(data.get("alias"),"TXTX");
    }

}
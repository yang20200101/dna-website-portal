package com.highershine.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.PageConstant;
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

@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String JSESSIONID = "8560afcb570134952381b95e06fb3901fe560ae478f3ccccb649b52e0adade55";

    // 声明Jackson工具类
    public final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void getArticleList() throws Exception{
        Map map = new HashMap<>();
        map.put("pageNum", PageConstant.PAGE_CURRENT);
        map.put("pageSize",PageConstant.PAGE_SIZE);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/article/getList")
                .cookie(new Cookie("jsessionid", JSESSIONID))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MAPPER.writeValueAsString(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
    }

    @Test
    public void findArticleById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/article/find/1")
                .cookie(new Cookie("jsessionid", JSESSIONID))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
    }
}
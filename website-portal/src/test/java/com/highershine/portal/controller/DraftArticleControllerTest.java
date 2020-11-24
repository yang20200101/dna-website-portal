package com.highershine.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DraftArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String JSESSIONID = "0dc44e3e5be1cf381b80bd7aed1d8cccec3822bb316e9f4172e48b59f8c70b22";

    // 声明Jackson工具类
    public final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void getList() {
    }

    @Test
    public void findDraftArticleById() {
    }

    @Test
    public void addDraftArticle() {
    }

    @Test
    public void batchPublish() throws Exception {
        Map map = new HashMap<>();
        String[] idList = {"1", "2"};
        map.put("idList",idList);
        System.out.println(MAPPER.writeValueAsString(map));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/draft/publish")
                .content(MAPPER.writeValueAsString(map))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie("jsessionid", JSESSIONID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status, HttpStatus.OK.value());
    }
}
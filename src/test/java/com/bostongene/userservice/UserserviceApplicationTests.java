package com.bostongene.userservice;

import com.bostongene.userservice.entity.CustomUser;
import com.bostongene.userservice.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserserviceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private UsersService usersService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void createAndFindByEmail() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/create")
                .param("firstName", "Mary")
                .param("lastName", "Zubrikhina")
                .param("email", "m.zubrikhina2014@yandex.ru")
                .param("password", "1111")
                .param("date", "1993-08-14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("m.zubrikhina2014@yandex.ru")))
                .andExpect(jsonPath("$.firstName", is("Mary")))
                .andExpect(jsonPath("$.lastName", is("Zubrikhina")))
                .andExpect(jsonPath("$.date", is("1993-08-14")))
                .andExpect(jsonPath("$.password", is(DigestUtils.sha256Hex("1111")))).andReturn();

        String response = result.getResponse().getContentAsString();
        CustomUser user = mapper.readValue(response, CustomUser.class);
        mockMvc.perform(get("/users/find")
                .param("email", "m.zubrikhina2014@yandex.ru"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("m.zubrikhina2014@yandex.ru")))
                .andExpect(jsonPath("$.firstName", is("Mary")))
                .andExpect(jsonPath("$.lastName", is("Zubrikhina")))
                .andExpect(jsonPath("$.date", is("1993-08-14")))
                .andExpect(jsonPath("$.password", is(DigestUtils.sha256Hex("1111"))))
        ;


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete")
                .param("id", String.valueOf(user.getId()))).andExpect(status().isOk());

    }


    @Test
    public void createAndUpdate() throws Exception {

        MvcResult result = mockMvc.perform(post("/users/create")
                .param("firstName", "Mary")
                .param("lastName", "Zubrikhina")
                .param("email", "m.zubrikhina2014@yandex.ru")
                .param("password", "1111")
                .param("date", "1993-08-14")).andReturn();

        String response = result.getResponse().getContentAsString();
        CustomUser user = mapper.readValue(response, CustomUser.class);
        mockMvc.perform(post("/users/update")
                .param("id", String.valueOf(user.getId()))
                .param("firstName", "Mariya")
                .param("date", "1993-09-14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("m.zubrikhina2014@yandex.ru")))
                .andExpect(jsonPath("$.firstName", is("Mariya")))
                .andExpect(jsonPath("$.lastName", is("Zubrikhina")))
                .andExpect(jsonPath("$.date", is("1993-09-14")))
                .andExpect(jsonPath("$.password", is(DigestUtils.sha256Hex("1111"))));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete")
                .param("id", String.valueOf(user.getId()))).andExpect(status().isOk());
    }


    @Test
    public void createAndDelete() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/create")
                .param("firstName", "Mary")
                .param("lastName", "Zubrikhina")
                .param("email", "m.zubrikhina2014@yandex.ru")
                .param("password", "1111")
                .param("date", "1993-08-14"))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        CustomUser user = mapper.readValue(response, CustomUser.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete")
                .param("id", String.valueOf(user.getId()))).andExpect(status().isOk());

        mockMvc.perform(get("/users/find")
                .param("email", "m.zubrikhina2014@yandex.ru"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test(expected = NoSuchElementException.class)
    public void updateNotCreatedUser() throws Throwable {
        try {
            mockMvc.perform(post("/users/update")
                    .param("id", "1")
                    .param("firstName", "Mariya")
                    .param("date", "1993-09-14"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is("m.zubrikhina2014@yandex.ru")))
                    .andExpect(jsonPath("$.firstName", is("Mariya")))
                    .andExpect(jsonPath("$.lastName", is("Zubrikhina")))
                    .andExpect(jsonPath("$.date", is("1993-09-14")))
                    .andExpect(jsonPath("$.password", is(DigestUtils.sha256Hex("1111"))));

        } catch (NestedServletException e) {
            throw e.getCause();
        }
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void createTwoUsersWithSameEmail() throws Throwable {
        try {
            mockMvc.perform(post("/users/create")
                    .param("firstName", "Mary")
                    .param("lastName", "Zubrikhina")
                    .param("email", "m.zubrikhina2018@yandex.ru")
                    .param("password", "1111")
                    .param("date", "1993-08-14"));
            mockMvc.perform(post("/users/create")
                    .param("firstName", "Mary")
                    .param("lastName", "Zubrikhina")
                    .param("email", "m.zubrikhina2018@yandex.ru")
                    .param("password", "1111")
                    .param("date", "1993-08-14"));

        } catch (NestedServletException e) {
            throw e.getCause();
        }
    }
}

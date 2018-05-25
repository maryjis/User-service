package com.bostongene.userservice.rest;

import com.bostongene.userservice.entity.CustomUser;
import com.bostongene.userservice.service.UsersService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CustomUser create(@RequestParam(value = "firstName", required = true) String firstName,
                             @RequestParam(value = "lastName", required = true) String lastName, @RequestParam(value = "email", required = true) String email,
                             @RequestParam(value = "password", required = true) String password, @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        CustomUser user = new CustomUser(firstName, lastName, DigestUtils.sha256Hex(password), email, date);
        return usersService.create(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CustomUser update(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "firstName", required = false) String firstName,
                             @RequestParam(value = "lastName", required = false) String lastName, @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "password", required = false) String password, @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        CustomUser user = usersService.find(id);
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (password != null) {
            user.setPassword(DigestUtils.sha256Hex(password));

        }
        if (date != null) {
            user.setDate(date);
        }
        return usersService.create(user);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public CustomUser find(@RequestParam(value = "email", required = true) String email) {
        return usersService.findByEmail(email);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestParam(value = "id", required = true) Long id) {
        usersService.delete(id);
    }


}

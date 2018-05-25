package com.bostongene.userservice.service;


import com.bostongene.userservice.dao.UsersRepository;
import com.bostongene.userservice.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Iterable<CustomUser> findAll() {
        return usersRepository.findAll();
    }

    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    public CustomUser create(CustomUser user) {
        return usersRepository.save(user);
    }

    public CustomUser findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public CustomUser find(Long id) {
        return usersRepository.findById(id).get();
    }
}

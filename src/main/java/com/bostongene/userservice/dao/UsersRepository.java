package com.bostongene.userservice.dao;

import com.bostongene.userservice.entity.CustomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends CrudRepository<CustomUser, Long> {

    public CustomUser findByEmail(String email);
}

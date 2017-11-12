package com.dgcdevelopment.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(String username);
    
    List<User> findByUsernameAndPassword(String username, String password);
    
    User findOneByUsername(String username);
    
    void deleteByUsername(String username);
}
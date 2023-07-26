package com.example.webShop.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

public interface UserDAO extends CrudRepository<User, Integer> {
    List<User> findAllByEmail(String email);

}

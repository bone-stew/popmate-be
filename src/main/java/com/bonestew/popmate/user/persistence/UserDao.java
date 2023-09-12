package com.bonestew.popmate.user.persistence;

import com.bonestew.popmate.user.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    Optional<User> findByEmail(String email);

    void register(User user);
}

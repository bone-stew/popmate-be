package com.bonestew.popmate.security.persistence;

import com.bonestew.popmate.security.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao {

    // 이메일로 있는지 확인한다.
    Optional<User> findByEmail(String email);

    void register(User user);
}

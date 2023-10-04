package com.bonestew.popmate.user.persistence;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.user.domain.Role;
import com.bonestew.popmate.user.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    Optional<User> findByEmail(String email);

    void register(User user);

    Optional<User> findById(Long userId);

    Role getRole(Long userId);

    Optional<User> findBackOfficeUser(@Param("email") String email,
                                      @Param("password") String password);
}

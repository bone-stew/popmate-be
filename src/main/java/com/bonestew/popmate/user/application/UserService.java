package com.bonestew.popmate.user.application;

import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.user.exception.UserNotFoundException;
import com.bonestew.popmate.user.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User getUserById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}

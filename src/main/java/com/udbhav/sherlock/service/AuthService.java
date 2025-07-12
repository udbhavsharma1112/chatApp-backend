package com.udbhav.sherlock.service;

import com.udbhav.sherlock.dao.AuthUserDao;
import com.udbhav.sherlock.dao.UserDao;
import com.udbhav.sherlock.model.AuthUser;
import com.udbhav.sherlock.model.User;
import com.udbhav.sherlock.utils.AuthUtil;
import com.udbhav.sherlock.utils.Logger;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final AuthUserDao userDao;

    public AuthService(AuthUserDao userDao) {
        this.userDao = userDao;
    }

    public String register(AuthUser user) {
        String userId = UUID.randomUUID().toString();
        Logger.info("Registering user: " + user.getEmailId() + " with userId: " + userId);
        userDao.insertUser(userId, user.getUserName(), user.getEmailId(), user.getPassword());
        return AuthUtil.generateToken(userId);
    }

    public Optional<String> login(String emailId, String password) {
        if (emailId == null || password == null) {
            return Optional.empty();
        }
        Logger.info("Attempting to authorise: " + emailId + " with password: " + password);
        Optional<String> token = userDao.findByEmailAndPassword(emailId, password).map(user -> AuthUtil.generateToken(user.getUserId()));
        if (token.isEmpty()) {
            Logger.error("Login failed for user: " + emailId);
        } else {
            Logger.info("Login successful for user: " + emailId + " with token: " + token.get());
        }
        return token;
    }
}

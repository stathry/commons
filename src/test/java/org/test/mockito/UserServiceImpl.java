package org.test.mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 * Created by dongdaiming on 2018-12-28 18:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Override
    public boolean login(String name, String pwd) {
        System.out.println("userService.login, name " + name + ", pwd " + pwd);
        return userDAO.login(name, pwd);
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}

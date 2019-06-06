package org.teststandard;

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
        System.out.println("userService.login, name " + name + ", pwd " + pwd + ", id " + IdUtils.id());
        return userDAO.login(name, pwd);
    }

    @Override
    public String reg(String name, String pwd) {
        String id = IdUtils.id();
        System.out.println("userService.reg, name " + name + ", pwd " + pwd + ", id " + id);
        return id;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}

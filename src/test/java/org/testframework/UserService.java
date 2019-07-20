package org.testframework;

/**
 * UserService
 * Created by dongdaiming on 2018-12-28 17:45
 */
public interface UserService {

    boolean login(String name, String pwd);

    String reg(String name, String pwd);
}

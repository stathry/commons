package org.test.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.test.UserDAO;
import org.test.UserService;
import org.test.UserServiceImpl;

/**
 * MockServiceTest
 * Created by dongdaiming on 2018-12-28 11:48
 */
@RunWith(MockitoJUnitRunner.class)
public class MockServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService injectUserService = new UserServiceImpl();

    @InjectMocks
    private UserServiceImpl injectUserService2;

    @Test
    public void testMock() {
        Assert.assertNotNull(userService);
        userService.login("u1", "p1");
        Mockito.verify(userService).login("u1", "p1");
    }

    @Test
    public void testStub() {
        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Mockito.when(userService.login("admin", "admin")).thenReturn(true);
        Mockito.when(userService.login("root", "root")).thenReturn(true);

        Assert.assertTrue(userService.login("admin", "admin"));
        Assert.assertTrue(userService.login("root", "root"));
        Assert.assertFalse(userService.login("admin", "root"));
    }

    @Test
    public void testInjectService() {
        Assert.assertNotNull(injectUserService);

        Mockito.when(userDAO.login(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Mockito.when(userDAO.login("admin", "admin")).thenReturn(true);
        Mockito.when(userDAO.login("root", "root")).thenReturn(true);

        Assert.assertTrue(injectUserService.login("admin", "admin"));
        Assert.assertTrue(injectUserService.login("root", "root"));
        Assert.assertFalse(injectUserService.login("admin", "root"));
        Assert.assertFalse(injectUserService.login("hello", "2019"));
    }

    @Test
    public void testInjectService2() {
        Assert.assertNotNull(injectUserService2);
        MockitoAnnotations.initMocks(this);

        Mockito.when(userDAO.login(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Mockito.when(userDAO.login("admin", "admin")).thenReturn(true);
        Mockito.when(userDAO.login("root", "root")).thenReturn(true);

        Assert.assertTrue(injectUserService2.login("admin", "admin"));
        Assert.assertTrue(injectUserService2.login("root", "root"));
        Assert.assertFalse(injectUserService2.login("admin", "root"));
        Assert.assertFalse(injectUserService2.login("hello", "2019"));
    }

}

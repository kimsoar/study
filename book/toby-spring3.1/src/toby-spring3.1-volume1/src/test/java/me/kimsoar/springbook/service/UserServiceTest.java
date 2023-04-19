package me.kimsoar.springbook.service;

import me.kimsoar.springbook.dao.UserDao;
import me.kimsoar.springbook.model.Level;
import me.kimsoar.springbook.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, UserLevelUpgradePolicyImpl.MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserLevelUpgradePolicyImpl.MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() throws SQLException {
        userDao.deleteAll();

        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    private void checkLevelUpgrade(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void upgradeAllOrNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
        testUserService.setDataSource(this.dataSource);
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        } catch (SQLException e) {
        }

        checkLevelUpgrade(users.get(1), false);
    }
}

package me.kimsoar.springbook.dao;


import me.kimsoar.springbook.dao.DataFactory;
import me.kimsoar.springbook.dao.UserDao;
import me.kimsoar.springbook.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

// @DirtiesContext
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")*/
public class UserDaoTest {

    //@Autowired
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        this.dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource("com.mysql.cj.jdbc.Driver","jdbc:mysql://localhost/testdb","root","root123",true);
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource);
        this.dao.setDataSource(dataSource);

        this.user1 = new User("agyumee", "박성철", "springno1");
        this.user2 = new User("bleegw700", "이길원", "springno2");
        this.user3 = new User("cpark100", "홍길동", "springno3 ");
    }

    @Test
    public void addAndGet() throws SQLException {
        this.dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        this.dao.add(this.user1);
        this.dao.add(this.user2);
        assertThat(this.dao.getCount(), is(2));

        User userget1 = this.dao.get(this.user1.getId());
        assertThat(this.user1.getName(), is(userget1.getName()));
        assertThat(this.user1.getPassword(), is(userget1.getPassword()));

        User userget2 = this.dao.get(this.user2.getId());
        assertThat(this.user2.getName(), is(userget2.getName()));
        assertThat(this.user2.getPassword(), is(userget2.getPassword()));


    }

    @Test
    public void count() throws SQLException {

        this.dao.deleteAll();
        assertThat(this.dao.getCount(), is(0));

        this.dao.add(user1);
        assertThat(this.dao.getCount(), is(1));

        this.dao.add(user2);
        assertThat(this.dao.getCount(), is(2));

        this.dao.add(user3);
        assertThat(this.dao.getCount(), is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        this.dao.deleteAll();
        assertThat(this.dao.getCount(), is(0));

        this.dao.get("unkown_id");
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}

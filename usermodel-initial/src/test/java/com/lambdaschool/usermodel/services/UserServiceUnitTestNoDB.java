package com.lambdaschool.usermodel.services;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class, properties = {"command.line.runner.enabled=false"})
public class UserServiceUnitTestNoDB {

    @Autowired
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private HelperFunctions helperFunctions;

    @MockBean
    private UserRepository userRepository;

    private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(11);

        userList.add(u1);
//        userService.save(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(21);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(22);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(23);

        userList.add(u2);
//        userService.save(u2);


        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(30);
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(31);

        userList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(40);
        u4.getRoles()
                .add(new UserRoles(u4,
                        r2));
        userList.add(u4);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
        Mockito.when(userRepository.findAll())
                .thenReturn(userList);
        System.out.println(userService.findAll());
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("admin"))
                .thenReturn(userList);
        assertEquals(4, userService.findByNameContaining("admin").size());
    }

    @Test
    public void findUserById() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        assertEquals("admin", userService.findUserById(1L).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound(){
        Mockito.when(userRepository.findById(9999L))
                .thenThrow(ResourceNotFoundException.class);

        assertEquals("admin", userService.findUserById(9999).getUsername());
    }

    @Test
    public void findByName() {
        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(userList.get(0));
        assertEquals("admin", userService.findByName("admin").getUsername());
    }

    @Test
    public void delete() {
        Mockito.when(userRepository.findById(10L))
                .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing().when(userRepository).deleteById(10L);

        userService.delete(10L);
        assertEquals(4, userList.size());
    }

    @Test
    public void save() {
        String newUsername = "chase";
        User newUser = new User(newUsername, "password", "chase@bianchi.com");
//        newUser.setUserid(99);

        Role newRole = new Role("Role");

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(newUser);
        Mockito.when(roleService.findRoleById(1L))
                .thenReturn(newRole);

        User addUser = userService.save(newUser);
        assertNotNull(addUser);
        assertEquals(newUsername, addUser.getUsername());
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
        Mockito.when(userRepository.findAll())
                .thenReturn(userList);
        Mockito.doNothing().when(userRepository).deleteAll();

        userService.deleteAll();
        assertEquals(4, userList.size());
    }
}
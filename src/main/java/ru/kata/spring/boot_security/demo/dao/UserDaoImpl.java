package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addUser(User user) {

        entityManager.persist(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {

        return entityManager.createQuery(" FROM User").getResultList();

    }

    @Override
    public User getUser(Long id) {

        return entityManager.find(User.class, id);
    }

    @Override
    public void editUser(Long id, User user) {

        User edit = entityManager.find(User.class, id);
        edit.setUsername(user.getUsername());
        edit.setFirstname(user.getFirstname());
        edit.setSecondName(user.getSecondName());
        edit.setAge(user.getAge());
        edit.setEmail(user.getEmail());
        edit.setPassword(user.getPassword());
    }

    @Override
    public void deleteUser(Long id) {

        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findByUsername(String username) {

        return entityManager.createQuery("select u from User u join fetch u.roles where u.username =:username").setParameter("username", username).getResultList();
    }
}


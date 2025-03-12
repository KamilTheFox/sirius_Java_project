package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.hpclab.hl.module1.controller.exeption.UserException;
import ru.hpclab.hl.module1.model.User;

import java.util.*;

import static java.lang.String.format;

@Repository
public class UserRepository {

    public static final String USER_NOT_FOUND_MSG = "User with ID %s not found";
    public static final String USER_EXISTS_MSG = "User with ID %s is already exists";

    private final Map<UUID, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(UUID id) {
        final var user = users.get(id);
        if (user == null) {
            throw new UserException(format(USER_NOT_FOUND_MSG, id));
        }
        return user;
    }

    public void delete(UUID id) {
        final var removed = users.remove(id);
        if (removed == null) {
            throw new UserException(format(USER_NOT_FOUND_MSG, id));
        }
    }

    public User save(User user) {
        if (ObjectUtils.isEmpty(user.getIdentifier())) {
            user.setIdentifier(UUID.randomUUID());
        }

        final var userData = users.get(user.getIdentifier());
        if (userData != null) {
            throw new UserException(format(USER_EXISTS_MSG, user.getIdentifier()));
        }

        users.put(user.getIdentifier(), user);

        return user;
    }

    public User put(User user) {
        final var userData = users.get(user.getIdentifier());
        if (userData == null) {
            throw new UserException(format(USER_NOT_FOUND_MSG, user.getIdentifier()));
        }

        final var removed = users.remove(user.getIdentifier());
        if (removed != null) {
            users.put(user.getIdentifier(), user);
        } else {
            throw new UserException(format(USER_NOT_FOUND_MSG, user.getIdentifier()));
        }

        return user;
    }

    public void clear(){
        users.clear();
    }

}

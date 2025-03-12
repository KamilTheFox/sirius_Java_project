package ru.hpclab.hl.module1.service;

import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(UUID.fromString(id));
    }

    public User updateUser(String id, User user) {
        user.setIdentifier(UUID.fromString(id));
        return userRepository.put(user);
    }
}

package ru.example.authservice.exception.user;


import static ru.example.authservice.utils.Constants.MESSAGE_USER_EXIST;

public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String username) {
        super(MESSAGE_USER_EXIST.formatted(username));
    }
}
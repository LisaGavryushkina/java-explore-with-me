package ru.practicum.ewm.user;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto addUser(UserDto user);

    User deleteUser(int userId);
}

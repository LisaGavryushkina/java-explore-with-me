package ru.practicum.ewm.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDtoForAdmin(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User toUser(UserDto userDto) {
        return new User(0,
                userDto.getName(),
                userDto.getEmail());
    }
}

package ru.practicum.ewm.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDtoForAdmin(User user, float rating) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                rating);
    }

    public UserDto toUserDtoForAdminWithoutRating(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                0.0f);
    }

    public UserDto toUserDto(User user, float rating) {
        return new UserDto(user.getId(),
                user.getName(),
                null,
                rating);
    }

    public User toUser(UserDto userDto) {
        return new User(0,
                userDto.getName(),
                userDto.getEmail());
    }
}

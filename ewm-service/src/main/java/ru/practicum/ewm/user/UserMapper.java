package ru.practicum.ewm.user;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.user.UserRepository.LikesAndTotal;

@Component
public class UserMapper {

    private float getRating(LikesAndTotal likesAndTotal) {
        int likes = likesAndTotal.getLikes();
        int total = likesAndTotal.getTotal();
        if (likes == 0) {
            return 0.0F;
        }
        return (float) likes / total;
    }

    public UserDto toUserDtoForAdmin(User user, LikesAndTotal likesAndTotal) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                getRating(likesAndTotal));
    }

    public UserDto toUserDtoForAdmin(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                0.0F);
    }

    public UserDto toUserDto(User user, LikesAndTotal likesAndTotal) {
        return new UserDto(user.getId(),
                user.getName(),
                null,
                getRating(likesAndTotal));
    }

    public User toUser(UserDto userDto) {
        return new User(0,
                userDto.getName(),
                userDto.getEmail());
    }
}

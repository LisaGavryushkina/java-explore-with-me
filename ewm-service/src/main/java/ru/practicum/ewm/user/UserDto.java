package ru.practicum.ewm.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private final int id;
    @NotBlank
    @Size(min = 2, max = 250)
    private final String name;
    @NotBlank
    @Size(min = 6, max = 254)
    @Pattern(regexp = "^[\\p{L}0-9!#$%&'*+/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{0,63}@[\\p{L}0-9-]{0,63}+(?:\\" +
            ".[\\p{L}0-9-]{2,63})*$", message = "Введенный email не соответсвует формату")
    private final String email;
    private final float rating;
}

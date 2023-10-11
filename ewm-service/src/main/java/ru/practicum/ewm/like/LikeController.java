package ru.practicum.ewm.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/users/{userId}/events/{eventId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto addLikeToEvent(@PathVariable int userId, @PathVariable int eventId, @RequestParam boolean isLike) {
        return likeService.addLike(userId, eventId, isLike);
    }

    @DeleteMapping("users/likes/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable int likeId) {
        likeService.deleteLike(likeId);
    }

    @GetMapping("/admin/likes/{likeId}")
    public LikeDto getLikeById(@PathVariable int likeId) {
        return likeService.getLikeById(likeId);
    }
}

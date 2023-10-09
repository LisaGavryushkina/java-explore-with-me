package ru.practicum.ewm.compilation;

import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationForResponseDto addCompilation(@RequestBody @Valid CompilationForRequestDto compilationForRequestDto) {
        return compilationService.addCompilation(compilationForRequestDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable int compId) {
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationForResponseDto updateCompilation(@PathVariable int compId,
                                                       @RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        return compilationService.updateCompilation(compId, updateCompilationDto);
    }

    @GetMapping("/compilations")
    public List<CompilationForResponseDto> getCompilations(@RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "false") boolean pinned) {
        return compilationService.getCompilations(from, size, pinned);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationForResponseDto getCompilationById(@PathVariable int compId) {
        return compilationService.getCompilationById(compId);
    }

}


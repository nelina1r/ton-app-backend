package ru.dedov.tonappbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dedov.tonappbackend.dto.LLaMaRequestDto;
import ru.dedov.tonappbackend.service.LLaMaService;

/**
 * Контроллер для обработки запросов к модели LLaMa
 *
 * @author Alexander Dedov
 * @since 28.05.2024
 */
@Log
@RestController
@RequestMapping("api/v1")
public class GenerationController {

	private final LLaMaService lLaMaService;

	@Autowired
	public GenerationController(LLaMaService lLaMaService) {
		this.lLaMaService = lLaMaService;
	}

	@Operation(summary = "генерация ответа от модели")
	@PostMapping("/generate")
	public ResponseEntity<?> generateAnswer(@Valid @RequestBody LLaMaRequestDto lLaMaRequestDto) {
		log.info("accepted POST request method generate answer by model with request: "
			+ lLaMaRequestDto.toString());
		return lLaMaService.generateAnswerByLLaMa(lLaMaRequestDto);
	}
}

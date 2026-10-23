package com.ipareventos.controladores.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ipareventos.excepciones.EmailDuplicadoException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "com.ipareventos.controladores.rest")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, List<String>> errores = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errores.computeIfAbsent(error.getField(), clave -> new ArrayList<>()).add(error.getDefaultMessage());
		});

		ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Revisa los datos enviados");

		problema.setTitle("Error de validación");
		problema.setProperty("errores", errores);

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail manejarValidacionServicio(ConstraintViolationException ex) {

		Map<String, List<String>> errores = new LinkedHashMap<>();

		ex.getConstraintViolations().forEach(error -> {
			String campo = error.getPropertyPath().toString();

			errores.computeIfAbsent(campo, clave -> new ArrayList<>()).add(error.getMessage());
		});

		ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				"Los parámetros enviados no son válidos");

		problema.setTitle("Error de validación");
		problema.setProperty("errores", errores);

		return problema;
	}

	@ExceptionHandler(EmailDuplicadoException.class)
	public ProblemDetail manejarEmailDuplicado(EmailDuplicadoException ex) {

		ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());

		problema.setTitle("Correo ya registrado");

		return problema;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail manejarConflictoDeDatos(DataIntegrityViolationException ex) {

		ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
				"Los datos entran en conflicto con un registro existente "
						+ "o con una restricción de la base de datos");

		problema.setTitle("Conflicto de datos");

		return problema;
	}
}
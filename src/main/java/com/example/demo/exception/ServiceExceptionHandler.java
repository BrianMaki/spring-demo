package com.example.demo.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(Exception ex, WebRequest request) {
		log.error("Internal server error occurred", ex);
		var apiError = ApiError.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.build();
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex) {
		List<ValidationError> validationErrors = ex.getConstraintViolations() != null
				? ex.getConstraintViolations().stream()
						.map(constraintViolation -> ValidationError.builder()
								.rejectedValue(getConstraintRejectedValue(constraintViolation))
								.field(constraintViolation.getPropertyPath().toString())
								.message(constraintViolation.getMessage())
								.code(constraintViolation.getConstraintDescriptor()
										.getAnnotation().annotationType().getSimpleName())
								.build())
						.collect(Collectors.toList())
				: null;
		var apiError = ApiError.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage())
				.validationErrors(validationErrors)
				.build();

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEnityNotFoundException(EntityNotFoundException ex) {
		var apiError = ApiError.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OptimisticLockException.class)
	public ResponseEntity<Object> handleOptimisticLockException(OptimisticLockException ex) {
		var apiError = ApiError.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UniqueConstraintException.class)
	public ResponseEntity<Object> handleUniqueConstraintException(UniqueConstraintException ex) {
		var apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).build();

		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@Override
	@NonNull
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			@NonNull HttpHeaders headers, HttpStatus status, @NonNull WebRequest request) {

		var apiError = ApiError.builder()
				.status(status.value())
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<>(apiError, headers, status);
	}

	@Override
	@NonNull
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			@NonNull HttpHeaders headers, HttpStatus status, @NonNull WebRequest request) {

		List<ValidationError> validationErrors = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			ValidationError.ValidationErrorBuilder builder = ValidationError.builder()
					.field(fieldError.getField())
					.message(fieldError.getDefaultMessage())
					.code(fieldError.getCode());
			
			Optional.ofNullable(fieldError.getRejectedValue())
					.ifPresent(rejectedValue -> builder.rejectedValue(rejectedValue.toString()));
			
			validationErrors.add(builder.build());
		});

		var apiError = ApiError.builder()
				.status(status.value())
				.message(status.getReasonPhrase())
				.validationErrors(validationErrors)
				.build();

		return new ResponseEntity<>(apiError, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(org.springframework.beans.TypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {

			String parameterName = ((MethodArgumentTypeMismatchException) ex).getName();
			var message = String.format("Method parameter: '%s' contains an invalid value: '%s'", parameterName,
					ex.getValue());
			log.error(message, ex);

			var apiError = ApiError.builder()
					.status(HttpStatus.BAD_REQUEST.value())
					.message(message)
					.build();

			return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private String getConstraintRejectedValue(ConstraintViolation<?> constraintViolation) {
		return constraintViolation.getInvalidValue() == null ? null : constraintViolation.getInvalidValue().toString();
	}
}

package com.moudjames23.eventskumojin.exceptions;

import com.moudjames23.eventskumojin.model.responses.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> resourceNotFoundException(ResourceNotFoundException exception)
    {
        HttpResponse response = HttpResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {

        Map<String, String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        HttpResponse response = HttpResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("NOK")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponse> handleInvalidInput(HttpMessageNotReadableException exception) {
        HttpResponse response = HttpResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HttpResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        String type = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
        Object value = exception.getValue();
        String message = String.format("'%s' doit être de type '%s' et '%s' n'en est pas un",
                name, type, value);

        HttpResponse response = HttpResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}

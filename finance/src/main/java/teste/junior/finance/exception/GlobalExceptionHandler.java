package teste.junior.finance.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public record ErrorResponse(Instant timestamp, int status, String error, String message) {}
    @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException ex) { return response(HttpStatus.NOT_FOUND, ex.getMessage()); }
    @ExceptionHandler(BusinessException.class) ResponseEntity<ErrorResponse> badRequest(BusinessException ex) { return response(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    @ExceptionHandler(ConflictException.class) ResponseEntity<ErrorResponse> conflict(ConflictException ex) { return response(HttpStatus.CONFLICT, ex.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(", ")));
    }
    @ExceptionHandler(Exception.class) ResponseEntity<ErrorResponse> general(Exception ex) {
        log.error("Unhandled exception correlationId={}", MDC.get("correlationId"), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }
    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) { return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message)); }
}
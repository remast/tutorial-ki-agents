package remast.winkelshop.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import remast.winkelshop.control.ConflictException;
import remast.winkelshop.control.NotFoundException;
import remast.winkelshop.control.UnauthorizedException;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<ErrorResponse.FieldErrorItem> fields = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();

        String message = "validation failed";
        if (!fields.isEmpty()) {
            ErrorResponse.FieldErrorItem first = fields.getFirst();
            if ("quantity".equals(first.getName()) && "must be >= 1".equals(first.getMessage())) {
                message = "quantity must be greater than 0";
            } else {
                message = first.getName() + " " + first.getMessage();
            }
        }

        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", message, fields));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("UNAUTHORIZED", exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("CONFLICT", exception.getMessage()));
    }

    private ErrorResponse.FieldErrorItem toFieldError(FieldError fieldError) {
        return new ErrorResponse.FieldErrorItem(fieldError.getField(), fieldError.getDefaultMessage());
    }
}

package com.voterapp.voter.vapp.exception;

import com.voterapp.voter.vapp.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VAppException.EntityNotFoundException.class)
    public final ResponseEntity<?> handleNotFoundExceptions(Exception ex, WebRequest request){
        Response<?> response = Response.notFound();
        response.addErrorMessageToResponse(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VAppException.DuplicateEntityException.class)
    public final ResponseEntity<?> handleDuplicateEntityExceptions(Exception ex, WebRequest request){
        Response<?> response = Response.duplicateEntity();
        response.addErrorMessageToResponse(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({VAppException.PollTimeExpiredException.class})
    public final  ResponseEntity<?> handlePollTimeExpiredException(Exception ex, WebRequest request){
        Response<?> response = Response.badRequest();
        response.addErrorMessageToResponse(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

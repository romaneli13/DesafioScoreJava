package com.serasa.desafio.models.exception;

import com.serasa.desafio.models.dto.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiError> tratamentoCustomException(CustomException ex) {
        ApiError apiError = new ApiError();
        apiError.setErroMensagem(ex.getMessage());
        apiError.setHttpStatus(ex.getHttpStatus());
        apiError.setErroDetalhes("ClassError: " + ex.getStackTrace()[0].getClassName() + " LineNumber: " + ex.getStackTrace()[0].getLineNumber());
        log.error("Error: {}", apiError);
        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> tratamentoExcecoesGenericas(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setErroMensagem(ex.getCause().getMessage());
        apiError.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setErroDetalhes("ClassError: " + ex.getStackTrace()[0].getClassName() + " LineNumber: " + ex.getStackTrace()[0].getLineNumber());
        log.error("Error: {}", apiError);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);

    }

}

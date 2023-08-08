package com.example.demo.global.exception;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.dto.ErrorResponse;
import com.example.demo.global.exception.error.DuplicatedMemberException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.validation.BindException;


/**
 * RestControllerAdvice : 컨트롤러가 던진 예외를 전역적으로 처리하기 위해 사용되는 어노테이션이다.
 * RESTful api를 구축하는데 용이하다.
 * 각 비즈니스로직 예외처리 정의 시 ErrorCode 에 기입해서 사용
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<CommonResponse> createErrorResponse(ErrorCode errorCode) {

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponse> handleRequestParamBindFailedException(BindException ex) {
        return createErrorResponse(ErrorCode.REQUEST_PARAM_BIND_FAILED);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    protected ResponseEntity<CommonResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        return createErrorResponse(ErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    protected ResponseEntity<CommonResponse> handleForbiddenException(HttpClientErrorException.Forbidden ex) {
        return createErrorResponse(ErrorCode.FORBIDDEN);
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    protected ResponseEntity<CommonResponse> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return createErrorResponse(ErrorCode.NOT_FOUND_ELEMENT);
    }
    @ExceptionHandler(MethodNotAllowedException.class)
    protected ResponseEntity<CommonResponse> handleMethodNotAllowedException(MethodNotAllowedException ex) {
        return createErrorResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DuplicatedMemberException.class)
    protected  ResponseEntity<CommonResponse> handleDuplicatedMemberException(DuplicatedMemberException ex){
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_EXCEPTION);
    }
}

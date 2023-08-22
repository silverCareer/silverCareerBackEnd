package com.example.demo.global.exception;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.dto.ErrorResponse;
import com.example.demo.global.exception.error.bid.NotFoundBidException;
import com.example.demo.global.exception.error.bid.NotFoundBidsException;
import com.example.demo.global.exception.error.payment.NotFoundPaymentHistoryException;
import com.example.demo.global.exception.error.payment.WrongPaymentInputException;
import com.example.demo.global.exception.error.account.NotEnoughBalanceException;
import com.example.demo.global.exception.error.account.NotFoundAccountException;
import com.example.demo.global.exception.error.charge.InvalidAmountException;
import com.example.demo.global.exception.error.likes.ExistLikesException;
import com.example.demo.global.exception.error.likes.NotFoundLikesException;
import com.example.demo.global.exception.error.member.*;
import com.example.demo.global.exception.error.product.InvalidProductInfoException;
import com.example.demo.global.exception.error.product.NotFoundProductException;
import com.example.demo.global.exception.error.product.NotFoundProductListException;
import com.example.demo.global.exception.error.search.NotFoundSearchException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionsException;
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
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Helper Function
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

    // Common
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
    @ExceptionHandler(InvalidAmountException.class)
    protected  ResponseEntity<CommonResponse> handleInvalidAmountInputException(InvalidAmountException ex){
        return createErrorResponse(ErrorCode.INVALID_AMOUNT_INPUT);
    }

    // Member
    @ExceptionHandler(DuplicateMemberException.class)
    protected ResponseEntity<CommonResponse> handleDuplicateMemberException(DuplicateMemberException ex) {
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_EXCEPTION);
    }

    @ExceptionHandler(DuplicateMemberPasswordException.class)
    protected ResponseEntity<CommonResponse> handleDuplicateMemberPasswordException(DuplicateMemberPasswordException ex) {
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_PASSWORD);
    }

    @ExceptionHandler(DuplicateMemberPhoneNumException.class)
    protected ResponseEntity<CommonResponse> handleDuplicateMemberPhoneNumException(DuplicateMemberPhoneNumException ex) {
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_PHONE_NUM);
    }

    @ExceptionHandler(WrongEmailInputException.class)
    protected ResponseEntity<CommonResponse> handleWrongEmailInputException(WrongEmailInputException ex) {
        return createErrorResponse(ErrorCode.WRONG_EMAIL_INPUT);
    }

    @ExceptionHandler(WrongPasswordInputException.class)
    protected ResponseEntity<CommonResponse> handleWrongPasswordInputException(WrongPasswordInputException ex) {
        return createErrorResponse(ErrorCode.WRONG_PASSWORD_INPUT);
    }

    @ExceptionHandler(WrongPhoneNumInputException.class)
    protected ResponseEntity<CommonResponse> handleWrongPhoneNumInputException(WrongPhoneNumInputException ex) {
        return createErrorResponse(ErrorCode.WRONG_PHONE_NUM_INPUT);
    }

    @ExceptionHandler(NotFoundMemberException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundMemberException(NotFoundMemberException ex) {
        return createErrorResponse(ErrorCode.NOT_EXISTED_MEMBER_EXCEPTION);
    }

    @ExceptionHandler(DuplicateMemberNameException.class)
    protected ResponseEntity<CommonResponse> handleDuplicatedMemberNameException(DuplicateMemberNameException ex) {
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_NAME);
    }

    @ExceptionHandler(DuplicateMemberEmailException.class)
    protected ResponseEntity<CommonResponse> handleDuplicatedMemberEmailException(DuplicateMemberEmailException ex) {
        return createErrorResponse(ErrorCode.DUPLICATE_MEMBER_EMAIL);
    }

    // Payment
    @ExceptionHandler(NotFoundPaymentHistoryException.class)
    protected ResponseEntity<CommonResponse> handleWrongPaymentInputException(NotFoundPaymentHistoryException ex) {
        return createErrorResponse(ErrorCode.NOT_FOUND_PAYMENT_HISTORY);
    }

    @ExceptionHandler(WrongPaymentInputException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundPaymentHistoryException(WrongPaymentInputException ex) {
        return createErrorResponse(ErrorCode.NOT_ENOUGH_MEMBER_BALANCE);
    }

    // Product
    @ExceptionHandler(NotFoundProductListException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundProductListException(NotFoundProductListException ex) {
        return createErrorResponse(ErrorCode.NOT_FOUND_PRODUCT_LIST);
    }

    @ExceptionHandler(NotFoundProductException.class)

    protected ResponseEntity<CommonResponse> handleNotFoundProductDetailException(NotFoundProductException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
    }

    @ExceptionHandler(InvalidProductInfoException.class)
    protected ResponseEntity<CommonResponse> handleInvalidProductInfoException(InvalidProductInfoException ex) {
        return createErrorResponse(ErrorCode.INVALID_PRODUCT_INFO);
    }

    // Bid
    @ExceptionHandler(NotFoundBidException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundBidException(NotFoundBidException ex) {
        return createErrorResponse(ErrorCode.NOT_FOUND_BID);
    }

    @ExceptionHandler(NotFoundBidsException.class)
    protected  ResponseEntity<CommonResponse> handleNotFoundBidsException(NotFoundBidsException ex) {
        return createErrorResponse(ErrorCode.NOT_FOUND_BIDS);
    }

    // Account
    @ExceptionHandler(NotFoundAccountException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundAccountException(NotFoundAccountException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_ACCOUNT);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    protected ResponseEntity<CommonResponse> handleNotEnoughBalanceException(NotEnoughBalanceException ex) {
        return createErrorResponse(ErrorCode.NOT_ENOUGH_ACCOUNT_BALANCE);
    }

    //likes
    @ExceptionHandler(ExistLikesException.class)
    protected ResponseEntity<CommonResponse> handleExistLikesException(ExistLikesException ex){
        return createErrorResponse(ErrorCode.EXIST_LIKES);
    }
      
    @ExceptionHandler(NotFoundLikesException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundLikesException(NotFoundLikesException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_LIKES);
    }

    // suggestion
     @ExceptionHandler(NotFoundSuggestionsException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundSuggestionsException(NotFoundSuggestionsException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_SUGGESTIONS);
     }

     @ExceptionHandler(NotFoundSuggestionException.class)
    protected  ResponseEntity<CommonResponse> handleNotFoundSuggestionException(NotFoundSuggestionException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_SUGGESTION);
     }

     // search
    @ExceptionHandler(NotFoundSearchException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundSearchException(NotFoundSearchException ex){
        return createErrorResponse(ErrorCode.NOT_FOUND_SEARCH_LIST);
    }
}

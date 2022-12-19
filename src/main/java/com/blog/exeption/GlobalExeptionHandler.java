package com.blog.exeption;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.payloads.ApiResponse;

import jakarta.validation.UnexpectedTypeException;

@RestControllerAdvice
public class GlobalExeptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExeptionHandler(ResourceNotFoundException ex){
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotValidExeption(MethodArgumentNotValidException ex){
		Map<String, String> resp = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) ->{
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			resp.put(fieldName, message);
		});
		return new ResponseEntity<Map<String,String>>(resp, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnexpectedTypeException.class)
	public ResponseEntity<ApiResponse> handleUnexpectedTypeExeption(UnexpectedTypeException ex){
//		String message = ex.toString();
		String message = "user already exist";
		ApiResponse apiResponse = new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidInput.class)
	public ResponseEntity<ApiResponse> handleInvalidInputExeption(InvalidInput ex){
//		Map<String, String> resp = new HashMap<>();
//		System.out.println(ex);
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message,false);
		System.out.println(apiResponse);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserExeption.class)
	public ResponseEntity<ApiResponse> handleUserExeption(UserExeption ex){
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message,false);
		System.out.println(apiResponse);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
	}
	
//	@ExceptionHandler(InsufficientAuthenticationException.class)
//	public ResponseEntity<ApiResponse> handleAuthenticationExeption(InsufficientAuthenticationException ex){
//		String message = ex.getMessage();
//		ApiResponse apiResponse = new ApiResponse(message,false);
//		System.out.println(apiResponse);
//		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
//	}
}

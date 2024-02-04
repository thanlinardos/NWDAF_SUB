package io.nwdaf.eventsubscription.controller.http;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.controller.SubscriptionsController;
import io.nwdaf.eventsubscription.model.InvalidParam;
import io.nwdaf.eventsubscription.model.ProblemDetails;
import io.nwdaf.eventsubscription.utilities.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleException(Exception ex) throws Exception {
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[" + ex.getClass() + "] An unexpected internal server error occurred while processing the " + method + " request.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NwdafFailureException.class)
    public ResponseEntity<ProblemDetails> handleException(NwdafFailureException ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[" + ex.getClass() + "] An unexpected internal server error occurred while processing the " + method + " request.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDetail(ex.getMessage());
        if (ex.getCauseString() != null && !ex.getCauseString().isEmpty()) {
            error.setCause(ex.getCauseString());
        }
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<ProblemDetails> handleException(MethodValidationException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[MethodValidationException] Validation failed for " + requestBodyType + " while processing the " + method + " request.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        ex.getAllValidationResults().forEach(validationResult -> {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(Objects.requireNonNull(validationResult.getMethodParameter().getParameterName()));
            invalidParam.setReason(validationResult.getResolvableErrors().toString());
            error.addInvalidParamsItem(invalidParam);
        });

        ex.getBeanResults().forEach(validationResult -> {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(Objects.requireNonNull(validationResult.getMethodParameter().getParameterName()));
            invalidParam.setReason(validationResult.getResolvableErrors().toString());
            error.addInvalidParamsItem(invalidParam);
        });

        ex.getValueResults().forEach(validationResult -> {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(Objects.requireNonNull(validationResult.getMethodParameter().getParameterName()));
            invalidParam.setReason(validationResult.getResolvableErrors().toString());
            error.addInvalidParamsItem(invalidParam);
        });

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ProblemDetails> handleException(HttpMessageNotWritableException ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[HttpMessageNotWritableException] An internal server error occurred while parsing the response body of the " + method + " request.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<ProblemDetails> handleException(ConversionNotSupportedException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        String propertyName = ex.getPropertyChangeEvent() != null ? ex.getPropertyChangeEvent().getPropertyName() : "";
        error.setTitle("ConversionNotSupportedException for the property '" + propertyName + "' of  expected type " + ex.getRequiredType() + " while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public ResponseEntity<ProblemDetails> handleException(HttpServerErrorException.BadGateway ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[BadGateway] Bad Gateway error for the " + method + " request.");
        error.setStatus(HttpStatus.BAD_GATEWAY.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ProblemDetails> handleException(AsyncRequestTimeoutException ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[AsyncRequestTimeoutException] An internal server error occurred while processing an async request for the " + method + " request.");
        error.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ProblemDetails> handleException(TimeoutException ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[TimeoutException] A timeout error occurred while processing an async request for the " + method + " request.");
        error.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public ResponseEntity<ProblemDetails> handleException(HttpServerErrorException.GatewayTimeout ex) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[GatewayTimeout] Gateway Timeout error for the " + method + " request.");
        error.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetails> handleException(HttpMessageNotReadableException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

//        HttpInputMessage inputMessage = ex.getHttpInputMessage();
//        String body = null;
//        try {
//            body = inputMessage.getBody().toString();
//        } catch (Exception e) {
//            // Ignore
//        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[HttpMessageNotReadableException] Invalid request body for " + requestBodyType + " while processing the " + method + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        if (ex.getCause() instanceof UnrecognizedPropertyException unrecognizedPropertyException) {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(unrecognizedPropertyException.getPropertyName());
            invalidParam.setReason(unrecognizedPropertyException.getMessage());
            error.addInvalidParamsItem(invalidParam);
        }

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleException(MethodArgumentNotValidException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[MethodArgumentNotValidException] Invalid request body for " + requestBodyType + " while processing the " + method + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(fieldError.getField());
            invalidParam.setReason(fieldError.getDefaultMessage());
            error.addInvalidParamsItem(invalidParam);
        });

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetails> handleException(HandlerMethodValidationException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[HandlerMethodValidationException] Validation failed for " + requestBodyType + " while processing the " + method + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        ex.getAllValidationResults().forEach(validationResult -> {
            InvalidParam invalidParam = new InvalidParam();
            invalidParam.setParam(Objects.requireNonNull(validationResult.getMethodParameter().getParameterName()));
            invalidParam.setReason(validationResult.getResolvableErrors().toString());
            error.addInvalidParamsItem(invalidParam);
        });

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ProblemDetails> handleException(TypeMismatchException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("TypeMismatchException for a parameter while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetails> handleException(MissingServletRequestParameterException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("MissingServletRequestParameterException for the parameter '" + ex.getParameterName() + "' of type " + ex.getParameterType() + " while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ProblemDetails> handleException(MissingServletRequestPartException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("MissingServletRequestPartException for the request part '" + ex.getRequestPartName() + "' while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ProblemDetails> handleException(ServletRequestBindingException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("ServletRequestBindingException while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleException(AuthenticationCredentialsNotFoundException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("AuthenticationCredentialsNotFoundException while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ProblemDetails> handleException(InsufficientAuthenticationException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("InsufficientAuthenticationException while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleException(AccessDeniedException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("AccessDeniedException while processing the " + method + " " + requestBodyType + " request.");
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetails> handleException(NoResourceFoundException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("NoResourceFoundException while processing the " + method + " " + requestBodyType + " request with path: " + ex.getResourcePath() + " .");
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetails> handleException(NoHandlerFoundException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("NoHandlerFoundException while processing the " + method + " " + requestBodyType + " request with path: " + ex.getRequestURL() + " .");
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetails> handleException(HttpMediaTypeNotSupportedException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[HttpMediaTypeNotSupportedException] Unsupported media type for body of type " + requestBodyType + " while processing the " + method + " request.");
        error.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleException(HttpMediaTypeNotAcceptableException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null, responseMediaType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
            responseMediaType = MediaType.APPLICATION_JSON_VALUE;
        }

        logger.warn(ex.getMessage());
        return "[HttpMediaTypeNotAcceptableException] Unaccepted media type for response media of type " + responseMediaType + " while processing the " + method +
                " request with body of type " + requestBodyType + ".";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleException(HttpRequestMethodNotSupportedException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        List<HttpMethod> supportedMethods = new ArrayList<>();
        if (uri != null && uri.endsWith("/nwdaf-eventsubscription/v1/subscriptions")) {
            supportedMethods.add(HttpMethod.POST);
        } else if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions/")) {
            supportedMethods.add(HttpMethod.PUT);
            supportedMethods.add(HttpMethod.DELETE);
        }

        logger.warn(ex.getMessage());
        return "[HttpRequestMethodNotSupportedException] Unsupported request method " + method + ". Supported methods are " + supportedMethods + ".";
    }

    @ExceptionHandler(PayloadTooLargeException.class)
    public ResponseEntity<ProblemDetails> handleException(PayloadTooLargeException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle(ex.getMessage());
        error.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
        error.setDetail("[PayloadTooLargeException] Payload too large for body of type " + requestBodyType + " while processing the "
                + method + " request of size " + ex.getSize() / 1_024 + "KBs. Maximum allowed size is " + Constants.max_bytes_per_subscription / 1_024 + "KBs.");
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(MissingContentLengthException.class)
    public ResponseEntity<ProblemDetails> handleException(MissingContentLengthException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        String requestBodyType = null;
        if (uri != null && uri.contains("/nwdaf-eventsubscription/v1/subscriptions")) {
            requestBodyType = "subscription";
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle(ex.getMessage());
        error.setStatus(HttpStatus.LENGTH_REQUIRED.value());
        error.setDetail("[MissingContentLengthException] Missing Content-Length header for body of type " + requestBodyType + " while processing the "
                + method + " request. Maximum allowed content length is " + Constants.max_bytes_per_subscription / 1_024 + "KBs.");
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.LENGTH_REQUIRED);
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ProblemDetails> handleException(HttpClientErrorException.TooManyRequests ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle(ex.getMessage());
        error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        error.setDetail("[TooManyRequests] Too many incoming requests while processing the "
                + method + " request.");
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ProblemDetails> handleException(UnsupportedOperationException ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = null, uri = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
        }

        ProblemDetails error = new ProblemDetails();
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getMessage());
        }
        error.setTitle("[UnsupportedOperationException] This endpoint has not been implemented yet for the received "
                + method + " request.");
        error.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        error.setDetail(ex.getMessage());
        error.setType(uri);
        error.setInstance(NwdafSubApplication.NWDAF_INSTANCE_ID.toString());
        error.setSupportedFeatures(Constants.supportedFeatures);

        logger.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_IMPLEMENTED);
    }
}

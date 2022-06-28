package autocrypt.boardinfoapi.common.exception.advice;

import autocrypt.boardinfoapi.common.exception.BaseException;
import autocrypt.boardinfoapi.common.exception.ErrorResult;
import autocrypt.boardinfoapi.common.exception.ParameterErrorResult;
import autocrypt.boardinfoapi.common.exception.ParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ParameterErrorResult> notValidRequestData(HttpMessageNotReadableException ex){
        ParameterErrorResult errorResult = new ParameterErrorResult("DATA_EX");
        errorResult.addDataErrors(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResult> baseException(BaseException ex){
        ErrorResult errorResult = new ErrorResult("BAD_EX", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<ParameterErrorResult> parameterException(ParameterException ex){
        ParameterErrorResult errorResult = new ParameterErrorResult("VALID_EX");
        errorResult.addParamErrors(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }
}

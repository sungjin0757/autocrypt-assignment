package autocrypt.boardinfoapi.common.exception;

import lombok.Data;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@Data
public class ParameterErrorResult {
    private String errorCode;
    private Map<String, String> errors = new HashMap<>();

    public ParameterErrorResult(String errorCode){
        this.errorCode = errorCode;
    }

    public void addDataErrors(HttpMessageNotReadableException ex){
        errors.put("Data Exception", ex.getMessage());
    }

    public void addParamErrors(ParameterException ex){
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(e -> errors.put(e.getCode(), e.getDefaultMessage()));
    }
}

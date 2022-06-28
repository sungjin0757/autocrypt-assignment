package autocrypt.boardinfoapi.controller.util;

import autocrypt.boardinfoapi.common.exception.ParameterException;
import org.springframework.validation.BindingResult;

public abstract class ValidateUtil {
    public static void checkValidate(BindingResult result){
        if(result.hasErrors()){
            throw new ParameterException(result);
        }
    }
}

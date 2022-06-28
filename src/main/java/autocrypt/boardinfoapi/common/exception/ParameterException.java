package autocrypt.boardinfoapi.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@Getter
public class ParameterException extends RuntimeException{
    private final BindingResult bindingResult;
}

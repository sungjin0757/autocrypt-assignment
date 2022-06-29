package autocrypt.boardinfoapi.vo;

import autocrypt.boardinfoapi.domain.enumerations.Locking;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestLock {
    @NotNull(message = "Lock Cannot Be Null")
    private Locking locking;
}

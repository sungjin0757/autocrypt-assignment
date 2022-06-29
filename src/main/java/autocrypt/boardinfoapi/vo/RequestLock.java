package autocrypt.boardinfoapi.vo;

import autocrypt.boardinfoapi.domain.enumerations.Locking;
import lombok.Data;

@Data
public class RequestLock {
    private Locking locking;
}

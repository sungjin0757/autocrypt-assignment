package autocrypt.boardinfoapi.vo;

import autocrypt.boardinfoapi.domain.enumerations.Locking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestLock {
    @NotNull(message = "Lock Cannot Be Null")
    @Schema(example = "Press ENABLE OR DISABLED")
    private Locking locking;
}

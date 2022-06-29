package autocrypt.boardinfoapi.controller;

import autocrypt.boardinfoapi.common.constants.SecurityConstants;
import autocrypt.boardinfoapi.common.constants.UrlConstants;
import autocrypt.boardinfoapi.common.exception.ParameterException;
import autocrypt.boardinfoapi.controller.util.ValidateUtil;
import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.security.JwtUtil;
import autocrypt.boardinfoapi.security.property.JwtPrincipal;
import autocrypt.boardinfoapi.service.UserService;
import autocrypt.boardinfoapi.vo.JwtResponse;
import autocrypt.boardinfoapi.vo.RequestLogin;
import autocrypt.boardinfoapi.vo.RequestUser;
import autocrypt.boardinfoapi.vo.ResponseUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(UrlConstants.USER_BASE)
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConstants.SWAGGER_KEY)
@Slf4j
public class UserApiController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입")
    @PostMapping(UrlConstants.SAVE)
    public ResponseEntity<ResponseUser> save(@Valid @RequestBody RequestUser requestUser,
                                             BindingResult result){
        ValidateUtil.checkValidate(result);

        String email = requestUser.getEmail();
        String password = requestUser.getPassword();
        String name = requestUser.getName();

        UserDto userDto = UserDto.newInstance(email, password, name);
        Long userId = userService.join(userDto);

        ResponseUser responseUser = ResponseUser.newInstance(userId, email, name);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @Operation(summary = "로그인")
    @PostMapping(UrlConstants.LOGIN)
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody RequestLogin requestLogin,
                                             BindingResult result){
        ValidateUtil.checkValidate(result);

        String email = requestLogin.getEmail();
        String password = requestLogin.getPassword();

        UserDto loginUser = userService.login(email, password);
        String token = jwtUtil.generateToken(JwtPrincipal.fromDto(loginUser));

        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));
    }

}

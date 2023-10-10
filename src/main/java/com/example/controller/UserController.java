package com.example.controller;


import com.example.dto.ResponseDTO;
import com.example.dto.UserDTO;
import com.example.entity.UserEntity;
import com.example.provider.TokenProvider;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try {
            if (userDTO == null || userDTO.getPassword() == null){
                throw new RuntimeException("Invalid Password Value");
            }
            //유저 객체를 저장할것을 만듬
            UserEntity user= UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .build();

            //서비스를 이용해 Repository 에저장
            UserEntity registerUser = userService.create(user);

            //DTO 에 값을 태워서 리턴해줌
            UserDTO responseUserDTO = UserDTO.builder().
                    id(registerUser.getId()).
                    username(registerUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);

        }catch (Exception e){

            //예외 처리된 내용을 ResponseDTO 에 저장
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            //저장된 값을 ResponseDTO 에 값을 저장후 전송
            return ResponseEntity.badRequest().body(responseDTO);
            
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        UserEntity user = userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword());
        //자격증명 확인하기

        if (user != null){
            //토큰 만들기
            final String token = tokenProvider.create(user);

            //증명 권한이 없는 경우 처리
            final UserDTO responseDTO = UserDTO.builder().
                    username(user.getUsername())
                    .id(user.getId())
                    .token(token)//토큰 추가
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }
        else{
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}

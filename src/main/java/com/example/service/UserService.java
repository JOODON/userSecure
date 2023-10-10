package com.example.service;


import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입을 처리하는 메서드
    public UserEntity create(final UserEntity userEntity){
        // 입력 값이 유효한지 확인
        if (userEntity == null || userEntity.getUsername() == null){
            throw new RuntimeException("Invalid arguments");
            // 올바르지 않은 인수 또는 매개 변수를 사용하여 예외를 발생시킴
        }
        final String username = userEntity.getUsername();

        // 중복된 사용자명이 있는지 확인
        if (userRepository.existsAllByUsername(username)){
            log.warn("Username already exits {}",username);
            throw new RuntimeException("Username already exits");
        }

        // 사용자 엔티티를 저장하고 반환
        return userRepository.save(userEntity);
    }

    // 자격 증명을 사용하여 사용자를 검색하는 메서드
    public UserEntity getByCredentials(final String username,final String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }


}

package com.example.repository;


import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity,String> {

    UserEntity findByUsername(String username);

    Boolean existsAllByUsername(String username);
    //username 과 일치하는 레코드가 있는지 확인하는 메소드
    UserEntity findByUsernameAndPassword(String username,String password);

}

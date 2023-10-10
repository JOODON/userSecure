package com.example.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    private String id; //유저에게 고유하게 부여되는 ID

    @Column(nullable = false)
    private String username; //유저 아이디

    private String password;//패스워드

    private String role;// 어드민 일반 사용자 구분 ROLE

    private String authProvider; //OAuth 구분자
}

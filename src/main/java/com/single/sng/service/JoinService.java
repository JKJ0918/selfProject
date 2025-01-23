package com.single.sng.service;

import com.single.sng.dto.JoinDTO;
import com.single.sng.entity.UserEntity;
import com.single.sng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public void joinProcess(JoinDTO joinDTO){

        //db에 이미 동일한 username을 가진 회원이 존재하는지?
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser){
            return;
        }


        // 받은 데이터를 엔티티로 변경시키는 작업
        UserEntity userInputData = new UserEntity();

        userInputData.setUsername(joinDTO.getUsername());
        userInputData.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); // 비밀번호 BCrypt 함호화
        userInputData.setRole("ROLE_ADMIN"); // 회원 자신이 ROLE 값을 넣을 수없어 앞단에 'ROLE_' 입력

        userRepository.save(userInputData);
    }

}

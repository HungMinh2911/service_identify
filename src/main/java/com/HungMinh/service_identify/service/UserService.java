package com.HungMinh.service_identify.service;
import java.util.HashSet;
import java.util.List;

import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.dto.response.MapperRespone;
import com.HungMinh.service_identify.entity.User;
import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.enums.Role;
import com.HungMinh.service_identify.mapper.UserMapper;
import com.HungMinh.service_identify.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// Chứa business logic (xử lý chính của ứng dụng).
@Service
@RequiredArgsConstructor // Tự động sinh constructor với tất cả các field final
@FieldDefaults (level = AccessLevel.PRIVATE , makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    public MapperRespone createUser(UserCreationRequest request){


        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("USER_EXISTED");
        User user =  userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> rolse = new HashSet<>();
        rolse.add(Role.USER.name()); // Role.USER.name() sẽ trả về chuỗi "USER"

        user.setRoles(rolse);
        return userMapper.toMapperRespone(userRepository.save(user));
    }
    public MapperRespone updateUser (String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(user,request);
        return userMapper.toMapperRespone(userRepository.save(user));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public List<MapperRespone> getUsers(){
        List<User> users = userRepository.findAll();
        return userMapper.toListMapper(users);

    }
    public MapperRespone getUser(String id){
        return userMapper.toMapperRespone(userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found")));
    }
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}


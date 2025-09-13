package com.HungMinh.service_identify.service;
import java.util.HashSet;
import java.util.List;

import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.dto.response.MapperRespone;
import com.HungMinh.service_identify.entity.User;
import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.enums.Role;
import com.HungMinh.service_identify.excepytion.AppException;
import com.HungMinh.service_identify.excepytion.ErorrCode;
import com.HungMinh.service_identify.mapper.UserMapper;
import com.HungMinh.service_identify.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
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

        //user.setRoles(rolse);
        return userMapper.toMapperRespone(userRepository.save(user));
    }
    public MapperRespone updateUser (String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(user,request);
        return userMapper.toMapperRespone(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')") //kiểm tra quyền trước khi chạy method”.
    public List<MapperRespone> getUsers(){
        log.info ("In method get users");
        List<User> users = userRepository.findAll();
        return userMapper.toListMapper(users);

    }
    @PostAuthorize("returnObject.username == authentication.name") //kiểm tra quyền sau khi method chạy, dựa trên kết quả trả về.
    public MapperRespone getUser(String id){
        return userMapper.toMapperRespone(userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found")));
    }
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public MapperRespone Myinfo(){
        var context = SecurityContextHolder.getContext();
        String user = context.getAuthentication().getName(); // chua ten dang nhap nguoi dung

        User user1 = userRepository.findByUsername(user).
                orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXISTED));

        return userMapper.toMapperRespone(user1);

    }
}


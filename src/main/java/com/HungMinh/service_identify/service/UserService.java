package com.HungMinh.service_identify.service;
import java.util.List;

import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.entity.User;
import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.excepytion.AppException;
import com.HungMinh.service_identify.excepytion.ErorrCode;
import com.HungMinh.service_identify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// Chứa business logic (xử lý chính của ứng dụng).
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request){
        User user = new User();

        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("USER_EXISTED");

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }
    public User updateUser (String userId, UserUpdateRequest request){
        User user = getUser(userId);

        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User getUser(String id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}


package com.HungMinh.service_identify.controller;

import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.dto.response.MapperRespone;
import com.HungMinh.service_identify.entity.User;
import com.HungMinh.service_identify.mapper.UserMapper;
import com.HungMinh.service_identify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Đây là nơi nhận request từ client (API endpoint).
@RestController
// @RestController = @Controller + @ResponseBody
//dùng khi muốn backend tra ve Json/Xml thay vì html
// giúp viết APi dễ dàng ko cần  @ResponseBody mỗi lần.
@RequestMapping("/users")
// nó như 1 cánh cổng chính được đặt tên và có thể đi vào các phòng
// nếu ko đặt các phòng con pk tư đặt tên
public class UserController {
    @Autowired
    // nếu ko viêt thì phải viết  UserService userService = new UserService();
    private UserService userService;
    private UserMapper userMapper;
    @PostMapping
    // @RequestBody  dùng để lấy dữ liệu từ phần thân (body) của HTTP request và chuyển đổi nó thành một đối tượng Java.
    // nếu ko có spring ko bt lấy dữ liệu từ body request
    APIResponse <User> createUser(@RequestBody @Valid  UserCreationRequest request) {
        APIResponse<User> T = new APIResponse<>();
        T.setResult(userService.createUser(request));
        return T;
    }

    @GetMapping
    List<User> getUsers(){
        return  userService.getUsers();
    }
    @GetMapping("/{userId}")
    MapperRespone getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    // @PathVariable
    //  lấy dữ liệu từ đường dẫn URL (path) mà client gửi lên.
    MapperRespone updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);

    }

    @DeleteMapping("/{userId}")
    String deleteUser9(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }

}

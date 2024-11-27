package com.zzc.init.admin.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.user.model.dto.*;
import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.admin.user.model.vo.UserVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 创建用户
     *
     * @param createUserDto
     */
    public long create(CreateUserDto createUserDto);


    long register(RegisterUserDto registerUserDto);

    //登录
    UserVO login(LoginRequest loginRequest);

    UserVO getLoginUser(HttpServletRequest request);

    boolean updateByUser(@RequestBody UpdateByUserRequest updateByUserRequest);

    boolean updateByAdmin(@RequestBody UpdateByAdminRequest updateByAdminRequest);

    boolean updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    UserVO getUserByUserId(long user_id);

    List<User> getUsersByCategory(String category, boolean ascending);

    List<User> getUsersBySearchText(String searchText);

    UserVO getUserVO(Long user_id);

    boolean deleteUsers(List<Long> user_ids);

    boolean updateMobile(@RequestBody String mobile, long user_id);

    boolean updateEmail(@RequestBody String email, long user_id);

    UserVO getUserInfo(String token);
}


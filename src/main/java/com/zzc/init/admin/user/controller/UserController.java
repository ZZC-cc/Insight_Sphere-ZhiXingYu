package com.zzc.init.admin.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzc.init.admin.user.model.dto.*;
import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.annotation.AuthCheck;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.constant.UserConstant;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.exception.ThrowUtils;
import com.zzc.init.utils.GeeTest.sdk.GeetestLib;
import com.zzc.init.utils.GeeTest.sdk.entity.GeetestLibResult;
import com.zzc.init.utils.GeeTest.sdk.enums.DigestmodEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Resource
    private UserService userService;

    @Value("${geetest.id}")
    private String GEETEST_ID;

    @Value("${geetest.key}")
    private String GEETEST_KEY;

    /**
     * 获取极验初始化信息
     *
     * @param request
     * @return
     */
    @Operation(summary = "获取极验初始化信息")
    @GetMapping("/geetest/register")
    public BaseResponse<Object> initGeetest(HttpServletRequest request) {
        GeetestLib gtSdk = new GeetestLib(GEETEST_ID, GEETEST_KEY);
        Map<String, String> params = new HashMap<>();
        params.put("user_id", "test"); // 可根据具体业务设置 user_id

        GeetestLibResult result = gtSdk.register(DigestmodEnum.MD5, params);

        if (result.getStatus() != 1) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "极验初始化失败");
        }
        return ResultUtils.success(result.getData());
    }


    /**
     * 登录
     *
     * @param loginRequest
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserVO userVO = userService.login(loginRequest);
        return ResultUtils.success(userVO);
    }
//
//    /**
//     * 用户注销
//     *
//     * @param request
//     * @return
//     */
//    @Operation(summary = "用户注销接口")
//    @GetMapping("/logout")
//    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
//        boolean result = userService.userLogout(request);
//        return ResultUtils.success(result);
//    }

    /**
     * 获取当前登录用户信息
     *
     * @param token Authorization token from request header
     * @return BaseResponse<UserVO>
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public BaseResponse<UserVO> getUserInfo(@RequestHeader("Authorization") String token) {
        UserVO userVO = userService.getUserInfo(token);
        return ResultUtils.success(userVO);
    }

    /**
     * 创建用户
     *
     * @param createUserDto 创建请求
     * @return 注册返回
     */
    @Operation(summary = "创建用户")
    @PostMapping("/create")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> userCreate(@RequestBody CreateUserDto createUserDto) {
        if (createUserDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        long result = userService.create(createUserDto);
        return ResultUtils.success("创建用户成功！用户id：" + result);
    }


    /**
     * 注册用户
     *
     * @param registerUserDto 创建请求
     * @return 注册返回
     */
    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody RegisterUserDto registerUserDto) {
        if (registerUserDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        long result = userService.register(registerUserDto);
        return ResultUtils.success("注册用户成功！用户id：" + result);
    }

//    /**
//     * 获取当前登录用户
//     *
//     * @param request
//     * @return
//     */
//    @Operation(summary = "获取当前登录用户接口")
//    @GetMapping("/get/login")
//    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
//        UserVO userVO = userService.getLoginUser(request);
//        return ResultUtils.success(userVO);
//    }


    /**
     * 用户更新资料
     *
     * @param updateByUserRequest
     * @param request
     * @return
     */
    @Operation(summary = "用户更新个人资料接口")
    @PutMapping("/update/byUser")
    public BaseResponse<String> updateUser_user(@RequestBody UpdateByUserRequest updateByUserRequest, HttpServletRequest request) {
        userService.updateByUser(updateByUserRequest);
        return ResultUtils.success("更新资料成功！");
    }

    /**
     * 管理员更新资料
     *
     * @param updateByAdminRequest 更新用户请求
     * @return 更新用户返回
     */
    @Operation(summary = "管理员更新用户资料接口")
    @PutMapping("/update/byAdmin")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> updateUserByAdmin(@RequestBody UpdateByAdminRequest updateByAdminRequest) {
        userService.updateByAdmin(updateByAdminRequest);
        return ResultUtils.success("更新资料成功！");
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码接口")
    @PostMapping("/update/password")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        userService.updatePassword(updatePasswordRequest, request);
        return ResultUtils.success("修改密码成功！请重新登录");
    }


    /**
     * 删除用户（管理员）
     *
     * @return
     */
    @Operation(summary = "删除用户接口")
    @DeleteMapping("/delete/")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> deleteUser(@RequestParam Long user_id) {
        if (user_id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        if (userService.getOne(queryWrapper) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有此用户");
        }
        boolean b = userService.removeById(user_id);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success("删除用户成功！");
    }


    /**
     * 根据 id 获取用户
     */
    @GetMapping("/get/byId")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "根据 id 获取用户")
    public BaseResponse<User> getUserByUserId(long user_id, HttpServletRequest request) {
        if (user_id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(user_id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    /**
     * 根据用户分类获取数据并排序
     *
     * @param category  分类名称（例如：user_name、user_phone、department_name等）
     * @param ascending 是否升序排序（true：升序，false：降序）
     * @return 分类排序后的用户列表
     */
    @Operation(summary = "根据分类类型获取用户接口")
    @GetMapping("/get/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<User>> getUsersByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<User> userVOList = userService.getUsersByCategory(category, ascending);
        return ResultUtils.success(userVOList);
    }

    /**
     * 多类型搜索
     *
     * @param searchText 搜索文本
     */
    @Operation(summary = "多类型搜索接口")
    @GetMapping("/get/search")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<User>> getUsersBySearchText(@RequestParam(required = false) String searchText) {
        List<User> userVOList = userService.getUsersBySearchText(searchText);
        return ResultUtils.success(userVOList);
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户接口")
    @DeleteMapping("/delete/users")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> deleteUsers(@RequestBody List<Long> user_ids) {
        boolean res = userService.deleteUsers(user_ids);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量删除用户失败");
        }
        return ResultUtils.success("批量删除用户成功！");
    }
}

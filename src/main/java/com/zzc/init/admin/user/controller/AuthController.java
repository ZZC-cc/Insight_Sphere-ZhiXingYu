package com.zzc.init.admin.user.controller;

import com.zzc.init.admin.user.model.dto.ResetPasswordRequest;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.config.EmailService;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.utils.GeeTest.sdk.GeetestLib;
import com.zzc.init.utils.GeeTest.sdk.entity.GeetestLibResult;
import com.zzc.init.utils.GeeTest.sdk.enums.DigestmodEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${geetest.id}")
    private String GEETEST_ID;

    @Value("${geetest.key}")
    private String GEETEST_KEY;


    private static final String RESET_PASSWORD_PREFIX = "RESET_PASSWORD_";
    private static final long CODE_EXPIRE_MINUTES = 10; // 验证码过期时间（分钟）

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
     * 发送验证码到邮箱
     */
    @PostMapping("/sendResetCode")
    public BaseResponse<String> sendResetCode(@RequestParam String email, HttpServletRequest request) {
        // 检查邮箱是否存在
        UserVO user = userService.getLoginUser(request);
        log.warn("用户邮箱：" + user.getEmail());
        log.warn("用户邮箱：" + email);
        if (!email.equals(user.getEmail())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱与用户不匹配");
        }

        // 生成随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000)); // 6位随机数

        // 保存到 Redis，设置过期时间
        String redisKey = RESET_PASSWORD_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 发送邮件
        emailService.sendEmail(email, "重置密码验证码", "您的验证码是：" + code + "，有效期为 " + CODE_EXPIRE_MINUTES + " 分钟。");

        return ResultUtils.success("验证码已发送，请检查您的邮箱");
    }

    /**
     * 校验验证码并重置密码
     */
    @PostMapping("/resetPassword")
    public BaseResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email = request.getEmail();
        String code = request.getCode();
        String newPassword = request.getNewPassword();

        // 验证 Redis 中是否有对应的验证码
        String redisKey = RESET_PASSWORD_PREFIX + email;
        String redisCode = redisTemplate.opsForValue().get(redisKey);

        if (redisCode == null || !redisCode.equals(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
        }

        // 验证通过，更新密码
        boolean isUpdated = userService.updatePasswordByEmail(email, newPassword);
        if (!isUpdated) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "密码重置失败");
        }

        // 删除 Redis 中的验证码
        redisTemplate.delete(redisKey);

        return ResultUtils.success("密码重置成功");
    }

    /**
     * 注册发送验证码到邮箱
     */
    @PostMapping("/register/sendResetCode")
    public BaseResponse<String> sendResetCodeForRegister(@RequestParam String email) {

        // 生成随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000)); // 6位随机数

        // 保存到 Redis，设置过期时间
        String redisKey = RESET_PASSWORD_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 发送邮件
        emailService.sendEmail(email, "注册验证码", "您的验证码是：" + code + "，有效期为 " + CODE_EXPIRE_MINUTES + " 分钟。");

        return ResultUtils.success("验证码已发送，请检查您的邮箱");
    }

    /**
     * 登录发送验证码到邮箱
     */
    @PostMapping("/login/sendResetCode")
    public BaseResponse<String> sendResetCodeForLogin(@RequestParam String email) {

        // 生成随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000)); // 6位随机数


        // 保存到 Redis，设置过期时间
        String redisKey = "EMAIL_CODE_" + email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 发送邮件
        emailService.sendEmail(email, "登录验证码", "您的验证码是：" + code + "，有效期为 " + CODE_EXPIRE_MINUTES + " 分钟。");

        return ResultUtils.success("验证码已发送，请检查您的邮箱");
    }

}

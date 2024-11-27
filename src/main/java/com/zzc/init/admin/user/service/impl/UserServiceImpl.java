package com.zzc.init.admin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.user.model.dto.*;
import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.admin.user.model.enums.UserRoleEnum;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.constant.UserConstant;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.mapper.UserMapper;
import com.zzc.init.utils.GeeTest.sdk.GeetestLib;
import com.zzc.init.utils.GeeTest.sdk.entity.GeetestLibResult;
import com.zzc.init.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${geetest.id}")
    private String GEETEST_ID;

    @Value("${geetest.key}")
    private String GEETEST_KEY;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "zzc";

    private static final String USER_SESSION_PREFIX = "USER_SESSION_";

    private static final int TOKEN_EXPIRE_HOURS = 2; // Token有效期2小时

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户登录
     *
     * @param loginRequest
     * @return
     */
    @Override
    public UserVO login(LoginRequest loginRequest) {

        //1. 验证验证码
        GeetestLib gtSdk = new GeetestLib(GEETEST_ID, GEETEST_KEY);
        GeetestLibResult result = gtSdk.successValidate(
                loginRequest.getGeetestChallenge(),
                loginRequest.getGeetestValidate(),
                loginRequest.getGeetestSeccode(), new HashMap<>()
        );

        if (result.getStatus() != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "极验校验失败");
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 2. 校验账号密码
        if (username.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号应不少于3位");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度应不少于6位");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("用户不存在");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        } else if (!user.getPassword().equals(encryptPassword)) {
            log.info("密码错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        } else if (user.getStatus() != 1) {
            log.info("用户已被禁用");
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "用户已被禁用");
        }

        // 3. 将用户信息和 token 存入 Redis 中
        String token = JwtUtil.generateToken(user);
        String userKey = USER_SESSION_PREFIX + user.getUser_id();
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(userKey + ":token", token, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        opsForValue.set(userKey, String.valueOf(user), TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);


        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);
        return userVO;
    }

    /**
     * 获取当前登录用户信息
     *
     * @param token
     * @return
     */
    @Override
    public UserVO getUserInfo(String token) {
        try {
            // 移除 Bearer 前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            // 从 Redis 中获取用户信息
            String userKey = USER_SESSION_PREFIX + JwtUtil.getUserFromToken(token).getUser_id();
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            User user = JwtUtil.getUserFromToken(token);
            if (user == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取用户信息失败");
            }
            if (!redisTemplate.hasKey(userKey)) {
                throw new BusinessException(ErrorCode.LOGIN_EXPIRED, "登录已过期，请重新登录");
            }

            // 将用户信息转换为 VO 对象返回
            UserVO userVO = UserVO.convertToUserVO(user);
            return userVO;
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取用户信息失败");
        }
    }


    /**
     * 创建用户
     *
     * @param createUserDto
     */
    @Override
    public long create(CreateUserDto createUserDto) {
        Long userId = null;
        try {
            //1.参数校验
            validateCreateUserDto(createUserDto);

            //2.校验账号
            synchronized (createUserDto.getUsername().intern()) {
                // 账户不能重复
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", createUserDto.getUsername());
                long count = this.baseMapper.selectCount(queryWrapper);
                if (count > 0) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
                }
            }

            // 3. 密码加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + createUserDto.getPassword()).getBytes());

            // 4. 插入数据
            User user = new User();
            BeanUtils.copyProperties(createUserDto, user);
            user.setPassword(encryptPassword);
            user.setStatus(1);
            user.setCreateTime(LocalDateTime.now());

            // 5. 返回结果
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            userId = user.getUser_id();
        } catch (Exception e) {
            log.error("创建用户失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建用户失败:" + e.getMessage());
        }
        return userId;
    }

    /**
     * 校验创建用户参数
     *
     * @param createUserDto
     */
    private static void validateCreateUserDto(CreateUserDto createUserDto) {
        String username = createUserDto.getUsername();
        String password = createUserDto.getPassword();
        Integer status = createUserDto.getStatus();
        String role = createUserDto.getRole();
        String avatar = createUserDto.getAvatar();
        String name = createUserDto.getName();
        String sex = createUserDto.getSex();
        String email = createUserDto.getEmail();
        String mobile = createUserDto.getMobile();
        if (StringUtils.isAnyBlank(username, password, role, avatar, name, sex.toString(), email, mobile)) {
            if (StringUtils.isBlank(username)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能为空");
            }
            if (StringUtils.isBlank(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能为空");
            }
            if (status < 0 || status > 1) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户状态错误");
            }
            if (StringUtils.isBlank(role)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户角色不能为空");
            }
            if (StringUtils.isBlank(avatar)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户头像不能为空");
            }
            if (StringUtils.isBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称不能为空");
            }
            if (StringUtils.isBlank(sex)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户性别不能为空");
            }
            if (StringUtils.isBlank(email)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户邮箱不能为空");
            }
            if (StringUtils.isBlank(mobile)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户手机号码不能为空");
            }
        }
        if (username.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号应不少于3位");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度应不少于6位");
        }
        if (!mobile.matches("^1[3456789]\\d{9}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "这个好像不是手机号码");
        }
        if (!sex.equals("男") && !sex.equals("女") && !sex.equals("保密")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别格式为[保密 or 男 or 女]");
        }
        if (!email.matches("^\\w+@\\w+\\.\\w+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        if (!role.equals(UserRoleEnum.ADMIN.getValue()) && !role.equals(UserRoleEnum.USER.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户角色错误[user普通用户，admin管理员]");
        }
    }

    /**
     * 校验注册用户参数
     *
     * @param registerUserDto
     */
    private static void validateRegisterUserDto(RegisterUserDto registerUserDto) {
        String username = registerUserDto.getUsername();
        String password = registerUserDto.getPassword();
        String avatar = registerUserDto.getAvatar();
        String name = registerUserDto.getName();
        String sex = registerUserDto.getSex();
        String email = registerUserDto.getEmail();
        String mobile = registerUserDto.getMobile();
        if (StringUtils.isAnyBlank(username, password, avatar, name, sex.toString(), email, mobile)) {
            if (StringUtils.isBlank(username)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能为空");
            }
            if (StringUtils.isBlank(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能为空");
            }
            if (StringUtils.isBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称不能为空");
            }
            if (StringUtils.isBlank(sex)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户性别不能为空");
            }
            if (StringUtils.isBlank(email)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户邮箱不能为空");
            }
            if (StringUtils.isBlank(mobile)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户手机号码不能为空");
            }
        }
        if (username.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号应不少于3位");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度应不少于6位");
        }
        if (!mobile.matches("^1[3456789]\\d{9}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "这个好像不是手机号码");
        }
        if (!email.matches("^\\w+@\\w+\\.\\w+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
    }


    /**
     * 注册用户
     *
     * @param registerUserDto
     */
    @Override
    public long register(RegisterUserDto registerUserDto) {
        Long userId = null;
        try {
            //1.参数校验
            validateRegisterUserDto(registerUserDto);

            //2.校验账号
            synchronized (registerUserDto.getUsername().intern()) {
                // 账户不能重复
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", registerUserDto.getUsername());
                long count = this.baseMapper.selectCount(queryWrapper);
                if (count > 0) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
                }
            }

            // 3. 密码加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + registerUserDto.getPassword()).getBytes());

            // 4. 插入数据
            User user = new User();
            BeanUtils.copyProperties(registerUserDto, user);
            user.setPassword(encryptPassword);
            user.setStatus(1);
            user.setRole(UserRoleEnum.USER.getValue());
            user.setCreateTime(LocalDateTime.now());
            if (registerUserDto.getAvatar().isEmpty()) {
                user.setAvatar("https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/avatar/1754526766348963841/QSBVqYU7-avtar.png");
            }

            // 5. 返回结果
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            userId = user.getUser_id();
        } catch (Exception e) {
            log.error("创建用户失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建用户失败:" + e.getMessage());
        }
        return userId;
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return 用户
     */
    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        // 从请求头中获取 Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.LOGIN_EXPIRED, "用户未登录");
        }

        token = token.substring(7);
        User user = JwtUtil.getUserFromToken(token);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 更新用户信息(用户自己修改自己的信息)
     */
    @Override
    public boolean updateByUser(@RequestBody UpdateByUserRequest updateByUserRequest) {

        // 1. 校验
        validateUpdateParams(updateByUserRequest);

        // 2. 查询用户
        User user = this.getById(updateByUserRequest.getUser_id());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        if (!user.getUsername().equals(updateByUserRequest.getUsername())) {
            // 校验用户账号是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", updateByUserRequest.getUsername());
            User user1 = this.getOne(queryWrapper);
            if (user1 != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号已存在");
            }
        }

        // 3. 更新
        BeanUtils.copyProperties(updateByUserRequest, user);
        user.setUpdateTime(LocalDateTime.now());

        // 4. 保存更新
        return this.updateById(user);
    }

    /**
     * 校验更新用户信息参数
     */
    private static void validateUpdateParams(UpdateByUserRequest updateByUserRequest) {
        Long user_id = updateByUserRequest.getUser_id();
        String username = updateByUserRequest.getUsername();
        String avatar = updateByUserRequest.getAvatar();
        String name = updateByUserRequest.getName();
        String sex = updateByUserRequest.getSex();
        String email = updateByUserRequest.getEmail();
        String mobile = updateByUserRequest.getMobile();
        if (user_id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能为空");
        }
        if (StringUtils.isBlank(avatar)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户头像不能为空");
        }
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称不能为空");
        }
        if (StringUtils.isBlank(sex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户性别不能为空");
        }
        if (!email.matches("^\\w+@\\w+\\.\\w+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        if (!mobile.matches("^1[3456789]\\d{9}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "这个好像不是手机号码");
        }
    }


    /**
     * 更新用户信息(管理员修改用户信息)
     */
    @Override
    public boolean updateByAdmin(@RequestBody UpdateByAdminRequest updateByAdminRequest) {

        //1.校验
        validateUpdateParams(updateByAdminRequest);

        //2.更新用户信息
        User oldUser = this.getById(updateByAdminRequest.getUser_id());
        User user = new User();
        BeanUtils.copyProperties(updateByAdminRequest, user);
        //如果密码发生改变
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + updateByAdminRequest.getPassword()).getBytes());
        if (!Objects.equals(oldUser.getPassword(), updateByAdminRequest.getPassword())) {
            user.setPassword(encryptPassword);
        }
        user.setUpdateTime(LocalDateTime.now());

        //3.保存更新
        return this.updateById(user);
    }

    /**
     * 修改密码
     */
    @Override
    public boolean updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        //1.校验
        if (updatePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空");
        }

        Long user_id = updatePasswordRequest.getUser_id();
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();
        String confirmPassword = updatePasswordRequest.getConfirmPassword();

        if (user_id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        if (StringUtils.isBlank(oldPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "旧密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码不能为空");
        }
        if (StringUtils.isBlank(confirmPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "确认密码不能为空");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码和确认密码不一致");
        }
        //2.查询用户
        User user = this.getById(user_id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        //3.校验旧密码是否正确
        String oldEncryptPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        if (!user.getPassword().equals(oldEncryptPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "旧密码不正确");
        }
        //4.修改密码
        String newEncryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setPassword(newEncryptPassword);
        request.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return this.updateById(user);
    }

    /**
     * 校验更新用户信息参数
     */
    private static void validateUpdateParams(UpdateByAdminRequest updateByAdminRequest) {
        if (updateByAdminRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空");
        }
        Long user_id = updateByAdminRequest.getUser_id();
        String username = updateByAdminRequest.getUsername();
        String password = updateByAdminRequest.getPassword();
        String role = updateByAdminRequest.getRole();
        String avatar = updateByAdminRequest.getAvatar();
        String name = updateByAdminRequest.getName();
        String sex = updateByAdminRequest.getSex();
        String email = updateByAdminRequest.getEmail();
        String mobile = updateByAdminRequest.getMobile();
        Integer status = updateByAdminRequest.getStatus();
        if (user_id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能为空");
        }
        if (StringUtils.isBlank(role)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户角色不能为空");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度应不少于6位");
        }
        if (StringUtils.isBlank(avatar)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户头像不能为空");
        }
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称不能为空");
        }
        if (StringUtils.isBlank(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户邮箱不能为空");
        }
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户手机号码不能为空");
        }
        if (status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户状态不能为空");
        }
        if (status < 1 || status > 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户状态不正确");
        }
        if (sex == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户性别不能为空");
        }
        if (!email.matches("^\\w+@\\w+\\.\\w+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        if (!mobile.matches("^1[3456789]\\d{9}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "这个好像不是手机号码");
        }
    }


    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }


    /**
     * 根据用户id获取用户信息
     *
     * @param user_id
     * @return
     */
    @Override
    public UserVO getUserByUserId(long user_id) {
        if (user_id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        User user = this.getOne(queryWrapper);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取所有用户列表（支持排序）
     *
     * @param category  类别名称（例如：user_name、user_phone、department_name等）
     * @param ascending 是否升序排序
     * @return 排序后的用户列表
     */
    @Override
    public List<User> getUsersByCategory(String category, boolean ascending) {
        if (StringUtils.isBlank(category)) {
            //返回所有用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            return this.list(queryWrapper);
        }
        // 1. 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 2. 设置排序方式
        if (ascending) {
            queryWrapper.orderByAsc(category);
        } else {
            queryWrapper.orderByDesc(category);
        }

        // 3. 查询
        return this.list(queryWrapper);
    }

    /**
     * 多类型搜索
     *
     * @param searchText 搜索文本
     */
    @Override
    public List<User> getUsersBySearchText(String searchText) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", searchText)
                .or()
                .like("name", searchText)
                .or()
                .like("email", searchText)
                .or()
                .like("mobile", searchText)
                .or()
                .like("role", searchText)
                .or()
                .like("user_id", searchText)
                .or()
                .like("sex", searchText)
                .or()
                .like("username", searchText)
                .or()
                .like("status", searchText);
        return this.list(queryWrapper);
    }

    /**
     * 根据id获取用户VO
     */
    @Override
    public UserVO getUserVO(Long user_id) {
        UserVO userVO = new UserVO();
        User user = this.getById(user_id);
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 批量删除用户
     */
    @Override
    public boolean deleteUsers(List<Long> user_ids) {
        if (user_ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        // 1. 删除用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", user_ids);
        return this.remove(queryWrapper);

    }

    /**
     * 修改手机号
     */
    @Override
    public boolean updateMobile(@RequestBody String mobile, long user_id) {
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户手机号不能为空");
        }
        if (user_id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        user.setMobile(mobile);
        return this.updateById(user);
    }

    /**
     * 修改邮箱
     */
    @Override
    public boolean updateEmail(@RequestBody String email, long user_id) {
        if (StringUtils.isBlank(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        }
        if (user_id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        user.setEmail(email);
        return this.updateById(user);
    }

}

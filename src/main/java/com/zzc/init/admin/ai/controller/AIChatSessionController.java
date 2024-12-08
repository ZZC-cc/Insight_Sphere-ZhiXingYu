package com.zzc.init.admin.ai.controller;

import com.zzc.init.admin.ai.model.dto.CreateSessionRequest;
import com.zzc.init.admin.ai.model.vo.AIChatSessionVO;
import com.zzc.init.admin.ai.service.AIChatSessionService;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/ai/chat/session")
public class AIChatSessionController {
    @Autowired
    private AIChatSessionService sessionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public BaseResponse<Long> createSession(@RequestBody CreateSessionRequest request) {
        return ResultUtils.success(sessionService.createSession(request.getUserId(), request.getModel()));
    }

    @PostMapping("/list")
    public BaseResponse<List<AIChatSessionVO>> getUserSessions(HttpServletRequest request) {
        UserVO userVO = userService.getLoginUser(request);
        return ResultUtils.success(sessionService.getUserSessions(userVO.getUser_id()));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Integer> deleteSession(@PathVariable String id) {
        return ResultUtils.success(sessionService.deleteSession(Long.valueOf(id)));
    }

    @PostMapping("/update/name")
    public BaseResponse<Boolean> updateSessionName(@RequestBody AIChatSessionVO sessionVO) {
        return ResultUtils.success(sessionService.updateSessionName(sessionVO.getId(), sessionVO.getSession_name()));
    }

    //更新聊天会话
    @PutMapping("/update")
    public BaseResponse<Boolean> updateSession(@RequestBody AIChatSessionVO sessionVO) {
        return ResultUtils.success(sessionService.updateSession(sessionVO));
    }
}

package com.zzc.init.admin.notice.controller;

import com.zzc.init.admin.notice.model.dto.CreateNoticeDto;
import com.zzc.init.admin.notice.model.dto.DeleteNoticeDto;
import com.zzc.init.admin.notice.model.dto.UpdateNoticeDto;
import com.zzc.init.admin.notice.model.entity.Notice;
import com.zzc.init.admin.notice.service.NoticeService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知接口
 */
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Resource
    private NoticeService noticeService;


    /**
     * 创建通知
     */
    @Operation(summary = "创建通知接口")
    @PostMapping(value = "/create")
    public BaseResponse<String> create(@Validated @RequestBody CreateNoticeDto dto) {
        boolean result = noticeService.create(dto);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success("创建通知成功");
    }


    /**
     * 更新通知
     */
    @Operation(summary = "更新通知接口")
    @PostMapping(value = "/update")
    public BaseResponse<String> update(@RequestBody UpdateNoticeDto dto) {
        boolean result = noticeService.update(dto);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新通知失败");
        }
        return ResultUtils.success("更新通知成功");
    }

    /**
     * 删除通知
     */
    @Operation(summary = "删除通知接口")
    @PostMapping(value = "/delete")
    public BaseResponse<String> delete(@RequestBody DeleteNoticeDto dto) {
        long notice_id = dto.getNotice_id();
        boolean result = noticeService.deleteNotice(notice_id);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除通知失败");
        }
        return ResultUtils.success("删除通知成功");
    }

    /**
     * 获取所有通知
     */
    @Operation(summary = "获取通所有知接口")
    @GetMapping(value = "/getAll")
    public BaseResponse<List<Notice>> getAllNotice() {
        List<Notice> notices = noticeService.all();
        return ResultUtils.success(notices);
    }

    /**
     * 获取最新通知
     */
    @Operation(summary = "获取最新通知接口")
    @GetMapping(value = "/get/news")
    public BaseResponse<Notice> getNewsNotice() {
        Notice notice = noticeService.news();
        return ResultUtils.success(notice);
    }

    /**
     * 根据id切换状态
     */
    @Operation(summary = "根据id切换状态接口")
    @PostMapping(value = "/switchStatus")
    public BaseResponse<String> switchStatus(@RequestBody Long notice_id) {
        boolean result = noticeService.changeStatus(notice_id);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "切换状态失败");
        }
        return ResultUtils.success("切换状态成功");
    }

    /**
     * 搜索通知
     */
    @Operation(summary = "搜索通知接口")
    @PostMapping(value = "/search")
    public BaseResponse<List<Notice>> search(@RequestParam(required = false) String searchText) {
        List<Notice> notices = noticeService.getUsersBySearchText(searchText);
        return ResultUtils.success(notices);
    }

}

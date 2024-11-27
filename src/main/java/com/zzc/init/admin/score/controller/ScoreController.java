package com.zzc.init.admin.score.controller;

import com.zzc.init.admin.score.model.dto.ScoreCreateRequest;
import com.zzc.init.admin.score.model.dto.ScoreUpdateRequest;
import com.zzc.init.admin.score.model.vo.ScoreVO;
import com.zzc.init.admin.score.service.ScoreService;
import com.zzc.init.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 成绩接口
 */
@RestController
@RequestMapping("/score")
@Slf4j
public class ScoreController {

    @Resource
    private ScoreService scoreService;

    /**
     * 创建成绩
     */
    @PostMapping("/create")
    public BaseResponse<String> createScore(@RequestBody ScoreCreateRequest request) {
        boolean score = scoreService.createScore(request.getLinkId());
        if (!score) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "创建失败");
        }
        return ResultUtils.success("创建成绩成功");
    }

    /**
     * 更新成绩
     */
    @PostMapping("/update")
    public BaseResponse<String> updateScore(@RequestBody ScoreUpdateRequest request) {
        boolean score = scoreService.updateScore(request);
        if (!score) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success("更新成绩成功");
    }

    /**
     * 删除成绩
     */
    @PostMapping("/delete")
    public BaseResponse<String> deleteScore(@RequestBody DeleteRequest request) {
        boolean score = scoreService.deleteScore(request.getId());
        if (!score) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success("删除成绩成功");
    }

    /**
     * 获取所有成绩VO
     */
    @PostMapping("/get/all/vo")
    public BaseResponse<List<ScoreVO>> getAllScoreVO() {
        List<ScoreVO> scoreVOList = scoreService.getAllScoreVoList();
        return ResultUtils.success(scoreVOList);
    }

    /**
     * 获取用户个人成绩VO
     */
    @PostMapping("/get/my/all/vo")
    public BaseResponse<List<ScoreVO>> getMyScoreVO(@RequestBody DeleteRequest request) {
        List<ScoreVO> scoreVOList = scoreService.getMyScoreVoList(request.getId());
        return ResultUtils.success(scoreVOList);
    }

    /**
     * 搜索
     */
    @PostMapping("/search")
    public BaseResponse<List<ScoreVO>> searchScore(@RequestBody SearchRequest request) {
        List<ScoreVO> scoreVOList = scoreService.getScoreVOBySearchText(request.getSearchText());
        return ResultUtils.success(scoreVOList);
    }
}

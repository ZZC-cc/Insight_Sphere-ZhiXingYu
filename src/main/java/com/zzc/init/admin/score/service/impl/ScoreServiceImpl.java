package com.zzc.init.admin.score.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.admin.score.model.dto.ScoreUpdateRequest;
import com.zzc.init.admin.score.model.entity.Score;
import com.zzc.init.admin.score.model.vo.ScoreVO;
import com.zzc.init.admin.score.service.ScoreService;
import com.zzc.init.admin.task.model.vo.TaskVO;
import com.zzc.init.admin.task.service.TaskService;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.mapper.ScoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩服务实现
 */
@Service
@Slf4j
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Resource
    private UserService userService;

    @Resource
    private TaskService taskService;

    @Resource
    private PostService postService;

    /**
     * 创建成绩
     */
    @Override
    public boolean createScore(Long linkId) {
        //linkId只能有一个
        if (this.count(new QueryWrapper<Score>().eq("linkId", linkId)) > 0) {
            throw new RuntimeException("该成绩条已存在，无需重复创建");
        }
        Score score = new Score();
        score.setLinkId(linkId);
        score.setMarkUserId(taskService.getTaskById(linkId).getPrincipalId());
        score.setUserId(taskService.getTaskById(linkId).getUserId());
        return save(score);
    }

    /**
     * 更新成绩
     */
    @Override
    public boolean updateScore(ScoreUpdateRequest request) {
        Score score = getById(request.getId());
        BeanUtils.copyProperties(request, score);
        return updateById(score);
    }

    /**
     * 删除成绩
     */
    @Override
    public boolean deleteScore(Long id) {
        return removeById(id);
    }

    /**
     * 获取全部成绩VO列表
     */
    @Override
    public List<ScoreVO> getAllScoreVoList() {
        List<Score> scoreList = this.list();
        List<ScoreVO> scoreVOList = new ArrayList<>();
        scoreList.forEach(score -> {
            ScoreVO scoreVO = new ScoreVO();
            BeanUtils.copyProperties(score, scoreVO);
            scoreVO.setTask(taskService.getTaskById(score.getLinkId()));
            scoreVO.setUser(userService.getUserByUserId(score.getUserId()));
            scoreVO.setMarkUser(userService.getUserByUserId(score.getMarkUserId()));
            scoreVOList.add(scoreVO);
        });
        return scoreVOList;
    }

    /**
     * 获取用户个人成绩VO列表
     */
    @Override
    public List<ScoreVO> getMyScoreVoList(Long userId) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Score> scoreList = this.list(queryWrapper);
        List<ScoreVO> scoreVOList = new ArrayList<>();
        scoreList.forEach(score -> {
            ScoreVO scoreVO = new ScoreVO();
            BeanUtils.copyProperties(score, scoreVO);
            scoreVO.setTask(taskService.getTaskById(score.getLinkId()));
            scoreVO.setUser(userService.getUserByUserId(score.getUserId()));
            scoreVO.setMarkUser(userService.getUserByUserId(score.getMarkUserId()));
            scoreVOList.add(scoreVO);
        });
        return scoreVOList;
    }

    /**
     * 根据任务名称或文章名称搜索
     */
    @Override
    public List<ScoreVO> getScoreVOBySearchText(String searchText) {
        List<TaskVO> tasksVOBySearchText = taskService.getTasksVOBySearchText(searchText);
        List<ScoreVO> scoreVOList = new ArrayList<>();
        //取出id
        List<Long> taskIds = new ArrayList<>();
        tasksVOBySearchText.forEach(taskVO -> taskIds.add(taskVO.getId()));
        log.info("taskIds:{}", taskIds);
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("linkId", taskIds);
        List<Score> scoreList = this.list(queryWrapper);
        log.info("scoreList:{}", scoreList);
        scoreList.forEach(score -> {
            ScoreVO scoreVO = new ScoreVO();
            BeanUtils.copyProperties(score, scoreVO);
            scoreVO.setTask(taskService.getTaskById(score.getLinkId()));
            scoreVO.setUser(userService.getUserByUserId(score.getUserId()));
            scoreVO.setMarkUser(userService.getUserByUserId(score.getMarkUserId()));
            scoreVOList.add(scoreVO);
        });
        if (scoreVOList.size() == 0) {
            return null;
        }
        return scoreVOList;
    }

}





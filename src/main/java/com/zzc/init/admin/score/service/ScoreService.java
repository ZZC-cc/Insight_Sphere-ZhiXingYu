package com.zzc.init.admin.score.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.score.model.dto.ScoreUpdateRequest;
import com.zzc.init.admin.score.model.entity.Score;
import com.zzc.init.admin.score.model.vo.ScoreVO;

import java.util.List;

/**
 * 成绩服务
 */
public interface ScoreService extends IService<Score> {

    boolean createScore(Long linkId);

    boolean updateScore(ScoreUpdateRequest request);

    boolean deleteScore(Long id);

    List<ScoreVO> getAllScoreVoList();

    List<ScoreVO> getMyScoreVoList(Long userId);

    List<ScoreVO> getScoreVOBySearchText(String searchText);
}

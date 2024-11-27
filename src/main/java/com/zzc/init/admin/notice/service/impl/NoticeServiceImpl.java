package com.zzc.init.admin.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.notice.model.dto.CreateNoticeDto;
import com.zzc.init.admin.notice.model.dto.UpdateNoticeDto;
import com.zzc.init.admin.notice.model.entity.Notice;
import com.zzc.init.admin.notice.service.NoticeService;
import com.zzc.init.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知服务实现
 */
@Service
@Slf4j
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    /**
     * 创建通知
     *
     * @param dto 创建通知数据
     * @return boolean
     */
    @Override
    public boolean create(CreateNoticeDto dto) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(dto, notice);
        return this.save(notice);
    }

    /**
     * 编辑通知
     *
     * @param dto 编辑通知数据
     */
    @Override
    public boolean update(UpdateNoticeDto dto) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(dto, notice);
        notice.setUpdateTime(LocalDateTime.now());
        return this.updateById(notice);
    }

    /**
     * 根据 id 获取通知
     *
     * @param notice_id 通知 id
     * @return Notice
     */
    @Override
    public Notice getNoticeById(long notice_id) {
        return this.getById(notice_id);
    }

    /**
     * 删除通知
     *
     * @param notice_id 通知 id
     */
    @Override
    public boolean deleteNotice(long notice_id) {
        return removeById(notice_id);
    }

    /**
     * 获取最新通知
     */
    @Override
    public Notice news() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                .orderByAsc("sort")
                .orderByDesc("create_time")
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 获取所有通知
     */
    @Override
    public List<Notice> all() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort")
                .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    /**
     * 根据id切换状态
     */
    @Override
    public boolean changeStatus(long notice_id) {
        Notice notice = this.getById(notice_id);
        notice.setStatus(notice.getStatus() == 1 ? 2 : 1);
        return this.updateById(notice);
    }

    /**
     * 多类型搜索
     *
     * @param searchText 搜索文本
     */
    @Override
    public List<Notice> getUsersBySearchText(String searchText) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", searchText)
                .or()
                .like("content", searchText);
        return this.list(queryWrapper);
    }
}

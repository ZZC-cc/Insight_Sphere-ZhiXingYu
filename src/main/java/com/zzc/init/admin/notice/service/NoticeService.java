package com.zzc.init.admin.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.notice.model.dto.CreateNoticeDto;
import com.zzc.init.admin.notice.model.dto.UpdateNoticeDto;
import com.zzc.init.admin.notice.model.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {


    boolean create(CreateNoticeDto dto);

    boolean update(UpdateNoticeDto dto);

    Notice getNoticeById(long notice_id);

    boolean deleteNotice(long notice_id);

    Notice news();

    List<Notice> all();

    boolean changeStatus(long notice_id);

    List<Notice> getUsersBySearchText(String searchText);
}


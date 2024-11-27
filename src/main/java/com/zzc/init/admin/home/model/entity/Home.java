package com.zzc.init.admin.home.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子
 */
@TableName(value = "statistics")
@Data
public class Home implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 访问量
     */
    private Integer visit_count;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

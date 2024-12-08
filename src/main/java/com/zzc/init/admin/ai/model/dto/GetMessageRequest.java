package com.zzc.init.admin.ai.model.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class GetMessageRequest implements Serializable {


    Long sessionId;
    int limit;

}

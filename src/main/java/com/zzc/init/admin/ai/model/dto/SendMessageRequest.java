package com.zzc.init.admin.ai.model.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class SendMessageRequest implements Serializable {


    Long sessionId;
    String role;
    String content;
    Long platformId;
    String model;

}

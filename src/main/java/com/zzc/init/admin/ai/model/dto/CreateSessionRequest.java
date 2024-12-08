package com.zzc.init.admin.ai.model.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class CreateSessionRequest implements Serializable {


    Long userId;
    String model;
}

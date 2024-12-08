package com.zzc.init.admin.ai.model.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class CreateModelRequest implements Serializable {
    private Long platform_id;


    private String model_name;


    private String model_type;

    private String api_key;


    private Double rate;

    private String model_desc;

    private String max_output_tokens;

    private String context_window;
}

package com.li.vo;

import com.li.entity.SparkApplicationParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author hrong
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBaseExtractorVo extends SparkApplicationParam {
    private String inputDB;
    private String outputDB;
}


package com.li.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @Author hrong
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkApplicationParam {
    /**
     * 任务的主类
     */
    private String mainClass;
    /**
     * jar包路径
     */
    private String jarPath;
    @Value("${spark.master:yarn}")
    private String master;
    @Value("${spark.deploy.mode:cluster}")
    private String deployMode;
    @Value("${spark.driver.memory:2g}")
    private String driverMemory;
    @Value("${spark.executor.memory:3g}")
    private String executorMemory;
    @Value("${spark.executor.cores:7}")
    private String executorCores;
    @Value("${spark.num.executors:6}")
    private String executorsNums;
    @Value("${spark.default.parallelism:84}")
    private String parallelism;

    /**
     * 其他配置：传递给spark job的参数
     */
    private Map<String, String> otherConfParams;


    /**
     * 调用该方法可获取spark任务的设置参数
     * @return SparkApplicationParam
     */
    public SparkApplicationParam getSparkApplicationParam(){
        return new SparkApplicationParam(mainClass,jarPath,master,deployMode, driverMemory, executorMemory, executorCores,executorsNums,parallelism,otherConfParams);
    }
}

package com.li.service;

import com.li.entity.SparkApplicationParam;
import com.li.vo.ResultVO;

import java.io.IOException;

/**
 * @Author hrong
 * @description spark任务提交service
 **/
public interface ISparkSubmitService {
    /**
     * 提交spark任务入口
     * @param sparkAppParams spark任务运行所需参数
     * @param otherParams 单独的job所需参数
     * @return 结果
     * @throws IOException          io
     * @throws InterruptedException 线程等待中断异常
     */
    ResultVO submitApplication(SparkApplicationParam sparkAppParams, String... otherParams) throws IOException, InterruptedException;

}


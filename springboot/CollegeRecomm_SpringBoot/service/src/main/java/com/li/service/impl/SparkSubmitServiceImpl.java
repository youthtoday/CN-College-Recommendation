package com.li.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.li.entity.SparkApplicationParam;
import com.li.service.ISparkSubmitService;
import com.li.utils.HttpUtil;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author hrong
 **/
@Service
public class SparkSubmitServiceImpl implements ISparkSubmitService {

    private static Logger log = LoggerFactory.getLogger(SparkSubmitServiceImpl.class);

    @Value("${driver.name:Master}")
    private String driverName;


    @Override
    public ResultVO submitApplication(SparkApplicationParam sparkAppParams, String... otherParams) throws IOException, InterruptedException,FileNotFoundException {
        log.info("spark任务传入参数：{}", sparkAppParams.toString());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Map<String, String> confParams = sparkAppParams.getOtherConfParams();
        SparkLauncher launcher = new SparkLauncher()
                .setAppResource(sparkAppParams.getJarPath())
                .setMainClass(sparkAppParams.getMainClass())
                .setMaster(sparkAppParams.getMaster())
                .setDeployMode(sparkAppParams.getDeployMode())
                .setConf("spark.driver.memory", sparkAppParams.getDriverMemory())
                .setConf("spark.executor.memory", sparkAppParams.getExecutorMemory())
                .setConf("spark.executor.cores", sparkAppParams.getExecutorCores())
                .setConf("spark.num.executors",sparkAppParams.getExecutorsNums())
                .setConf("spark.default.parallelism",sparkAppParams.getParallelism());
        if (confParams != null && confParams.size() != 0) {
            log.info("开始设置spark job运行参数:{}", JSONObject.toJSONString(confParams));
            for (Map.Entry<String, String> conf : confParams.entrySet()) {
                log.info("{}:{}", conf.getKey(), conf.getValue());
                launcher.setConf(conf.getKey(), conf.getValue());
            }
        }
        if (otherParams.length != 0) {
            log.info("开始设置spark job参数:{}", Arrays.toString(otherParams));
//            launcher.addAppArgs("jdbc:mysql://Master:3306/myemployees","jobs" ,"root","HUSTeic2021","json","database");
            launcher.addAppArgs(otherParams);
        }
        log.info("参数设置完成，开始提交spark任务");
        SparkAppHandle handle = launcher.startApplication(new SparkAppHandle.Listener() {
            @Override
            public void stateChanged(SparkAppHandle sparkAppHandle) {
                if (sparkAppHandle.getState().isFinal()) {
                    countDownLatch.countDown();
                }
                log.info("stateChanged:{}", sparkAppHandle.getState().toString());
            }

            @Override
            public void infoChanged(SparkAppHandle sparkAppHandle) {
                log.info("infoChanged:{}", sparkAppHandle.getState().toString());
            }
        });
        log.info("The task is executing, please wait ....");
        //线程等待任务结束
        countDownLatch.await();
        log.info("The task is finished!");
        return new ResultVO(ResStatus.OK,"计算成功!",null);
    }


}

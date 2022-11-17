package com.li.controller;

import com.li.service.ISparkSubmitService;
import com.li.vo.DataBaseExtractorVo;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author hrong
 **/
@Slf4j
@RestController
public class SparkController {
	@Resource
	private ISparkSubmitService iSparkSubmitService;
	/**
	 * 调用service进行远程提交spark任务
	 * @param vo 页面参数
	 * @return 执行结果
	 */
	@ResponseBody
	@PostMapping("/extract/database")
	public ResultVO dbExtractAndLoad2Hdfs(@RequestBody DataBaseExtractorVo vo){
		try {
			return iSparkSubmitService.submitApplication(vo.getSparkApplicationParam(),vo.getInputDB(),vo.getOutputDB());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error("执行出错：{}", e.getMessage());
			return new ResultVO(ResStatus.NO, e.getMessage(),null);
		}
	}

//	@ResponseBody
//	@GetMapping("/recommend")
//	public Object getRecommdColleges(@RequestParam RecommdVO vo){
//		try {
//			return iSparkSubmitService.submitApplication1(vo.getSparkApplicationParam(),
//					vo.getSchool_id(),
//					vo.getSchool_name());
//		}catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//			log.error("执行出错：{}", e.getMessage());
//			return new ResultVO(ResStatus.NO, e.getMessage(),null);
//			}
//	}
}

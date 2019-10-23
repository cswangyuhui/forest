package com.forest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

import com.forest.utils.FileUtil;
import com.forest.common.ResponseResult;
import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.Tree;
import com.forest.service.FileService;
import com.forest.service.PlotService;
import com.forest.service.TreeService;
import com.forest.utils.PageUtil;
import com.forest.utils.PoiUtil;
import com.forest.utils.SelectUtil;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/files")
public class FileController {
	@Autowired
	private FileService fileService;
	@Autowired
	private TreeService treeService;
	@Autowired
	private PlotService plotService;
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@RequestMapping(value="/importTreeExcel",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult importTreeExcel(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtil.convertMultipartFileToFile(multipartFile);
        if (file == null) {
            return new ResponseResult().failure("导入失败");
        }
        int result = fileService.importTreeByExcel(file);
        return new ResponseResult().success("导入成功",result);
    }
	
	@RequestMapping(value="/getSelectOption",method=RequestMethod.GET)
	@ResponseBody
	public ResponseResult getSelectOption() {
		List<String> area = plotService.selectArea();
		List<String> stand = plotService.selectStand();
		List<String> plot = plotService.selectPlot();
		Gson gson = new Gson();
		String jsonStr = "{\"area\":"+gson.toJson(area)+",\"stand\":"+gson.toJson(stand)
		+",\"plot\":"+gson.toJson(plot)+"}";
		System.out.println(jsonStr);
		logger.debug("getSelectOption:{}",jsonStr);
        return new ResponseResult().success("导入成功",jsonStr);
    }
	
	@RequestMapping(value="/exportTreeExcel",method=RequestMethod.GET)
	@ResponseBody
	public void exportTreeExcel(HttpServletRequest request,HttpServletResponse response,@RequestParam String export_area,
			@RequestParam String export_stand,@RequestParam String export_plot,@RequestParam String export_dead) {
		logger.debug("{exportTreeExcel},{}",export_stand);
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isEmpty(export_area) == false && export_area.equals("0") == false)
			map.put("area", export_area);
		if(StringUtil.isEmpty(export_stand) == false && export_stand.equals("0") == false)
			map.put("stand", (export_stand));
		if(StringUtil.isEmpty(export_plot) == false && export_plot.equals("0") == false)
			map.put("plot", export_plot);
		if(StringUtil.isEmpty(export_dead) == false && export_dead.equals("-1") == false)
			map.put("is_dead", Integer.parseInt(export_dead));
		List<Tree> listTree = fileService.exportTreeExcel(map);
		String[] excelHeaders= {"历史编码","唯一编码","大区","小区","样地号","树牌号","树种","胸高直径","树高","活枝高","冠幅东","冠幅西","冠幅南","冠幅北","横坐标","纵坐标","录入时间", "备注","死亡类型","死亡时间"};
		int[] width = {5546,5546,2261,2261,2261,3114,2261,2261,2261,2261,2261,2261,2261,2261,2261,2261,5333,2261,2261,5333};
		String[] fileds = {"history_id","unique_id","area","stand","plot","tree_num","tree_type","DBH","height","BLC","CWw","CWe","CWs","CWn","locationX","locationY","import_time","note","number","dead_time"};
		int [] formats = {1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,5,7,1,1,7};
		try {
            List<Map<String, Object>> excelData = new ArrayList<Map<String, Object>>();
            if (CollectionUtils.isNotEmpty(listTree)) {
                for (Tree tree : listTree) {
                    Map<String, Object> listMap = new HashMap<>();
                    if(tree.getIsDead() == 0) {
                    	GrowthSituation gs = treeService.findLiveTreeByUniqueId(tree.getUniqueId());
                    	listMap.put("unique_id",tree.getUniqueId());
                    	listMap.put("history_id",tree.getHistoryId());
                    	listMap.put("area", tree.getArea());
                    	listMap.put("stand", tree.getStand());
                    	listMap.put("plot", tree.getPlot());
                    	listMap.put("locationX", tree.getLocationX());
                    	listMap.put("locationY", tree.getLocationY());
                    	listMap.put("is_dead", tree.getIsDead());
                    	listMap.put("tree_type", tree.getTreeType());
                    	listMap.put("tree_num", tree.getTreeNum());
                    	listMap.put("DBH", gs.getDBH());
                    	listMap.put("height", gs.getHeight());
                    	listMap.put("BLC", gs.getBLC());
                    	listMap.put("CWw", gs.getCWw());
                    	listMap.put("CWe", gs.getCWe());
                    	listMap.put("CWs", gs.getCWs());
                    	listMap.put("CWn", gs.getCWn());
                    	listMap.put("import_time", DateUtil.getExcelDate(gs.getDatetime()));
                    	listMap.put("note",null);
                    	listMap.put("number",null);
                    	listMap.put("dead_time",null);
                    	excelData.add(listMap);
                    }
                    else if(tree.getIsDead() == 1) {
                    	DeadTree dt = treeService.findDeadTreeByUniqueId(tree.getUniqueId());
                    	listMap.put("unique_id",tree.getUniqueId());
                    	listMap.put("history_id",tree.getHistoryId());
                    	listMap.put("area", tree.getArea());
                    	listMap.put("stand", tree.getStand());
                    	listMap.put("plot", tree.getPlot());
                    	listMap.put("locationX", tree.getLocationX());
                    	listMap.put("locationY", tree.getLocationY());
                    	listMap.put("is_dead", tree.getIsDead());
                    	listMap.put("tree_type", tree.getTreeType());
                    	listMap.put("tree_num", tree.getTreeNum());
                    	listMap.put("DBH", null);
                    	listMap.put("height", null);
                    	listMap.put("BLC", null);
                    	listMap.put("CWw", null);
                    	listMap.put("CWe", null);
                    	listMap.put("CWs", null);
                    	listMap.put("CWn", null);
                    	listMap.put("import_time", null);
                    	listMap.put("note",dt.getNote());
                    	listMap.put("number",dt.getNumber());
                    	listMap.put("dead_time",DateUtil.getExcelDate(dt.getDatetime()));
                    	excelData.add(listMap);
                    }
                }
            }
            String excelName = "用户数据_" + System.currentTimeMillis();
            //执行导出操作
            PoiUtil.exportFile(excelName, excelHeaders, fileds, formats, width, excelData, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
		//return new ResponseResult().success("haha");
	}
}

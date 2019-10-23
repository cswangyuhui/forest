package com.forest.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.forest.common.ResponseResult;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.LiveTree;
import com.forest.pojo.Plot;
import com.forest.pojo.Tree;
import com.forest.service.PlotService;
import com.forest.service.TreeService;
import com.forest.utils.PageUtil;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/plots")
public class PlotController {
	@Autowired
	private TreeService treeService;
	@Autowired
	private PlotService plotService;
	
	private Gson gson = new Gson();
	private JsonParser parse =new JsonParser();
	private static final Logger logger = LoggerFactory.getLogger(PlotController.class);
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getPlotList(@RequestParam Map<String,Object> params,@RequestParam String _search,
			@RequestParam(required=false) String filters){
		logger.debug("{getPlotList},{}",params.toString());
		PageUtil pageUtil = new PageUtil(params);
		return new ResponseResult().success("success",plotService.getPlotPage(pageUtil));
    }
	
	@RequestMapping(value="/plotAdd",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult plotAdd(@RequestParam Map params) {
		logger.info("*plotAdd*"+params.get("txt_area"));
		logger.info("*plotAdd*"+params.get("txt_stand"));
		logger.info("*plotAdd*"+params.get("txt_plot"));
		logger.info("*plotAdd*"+params.get("txt_shape"));
		logger.info("*plotAdd*"+params.get("txt_xdirect"));
		logger.info("*plotAdd*"+params.get("txt_ydirect"));
		logger.info("*plotAdd*"+params.get("txt_longitude"));
		logger.info("*plotAdd*"+params.get("txt_latitude"));
		Float xdirect = 45.0f;
		Float ydirect = -45.0f;
		Double longitude = 110.0;
		Double latitude = 110.0;
		try {
			xdirect = Float.parseFloat((String) params.get("txt_xdirect"));
			ydirect = Float.parseFloat((String) params.get("txt_ydirect"));
			longitude = Double.parseDouble((String) params.get("txt_longitude"));
			latitude = Double.parseDouble((String) params.get("txt_latitude"));
		} catch (Exception e) {
			return new ResponseResult().failure("样地横、纵坐标必须为数值(偏离正北方向的角度值),经纬度必须为数值");
		}
		Plot plot = new Plot((String)params.get("txt_area"), (String)params.get("txt_stand"), 
				(String)params.get("txt_plot"), xdirect, ydirect,(String)params.get("txt_shape"),longitude,latitude);
		try {
			if(plotService.insertPlot(plot) > 0) {
				return new ResponseResult().success("插入成功");
			}
		} catch (Exception e) {
			return new ResponseResult().failure("插入失败，请检查大区、小区、样地号是否与已有记录重复");
		}
		return new ResponseResult().failure("插入失败");
    }
	
	@RequestMapping(value="/plotEdit",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult plotEdit(@RequestParam Map params) {
		logger.info("*plotEdit*"+params.get("txt_area"));
		logger.info("*plotEdit*"+params.get("txt_stand"));
		logger.info("*plotEdit*"+params.get("txt_plot"));
		logger.info("*plotEdit*"+params.get("txt_shape"));
		logger.info("*plotEdit*"+params.get("txt_xdirect"));
		logger.info("*plotEdit*"+params.get("txt_ydirect"));
		logger.info("*plotEdit*"+params.get("txt_longitude"));
		logger.info("*plotEdit*"+params.get("txt_latitude"));
		Float xdirect = 45.0f;
		Float ydirect = -45.0f;
		Double longitude = 110.0;
		Double latitude = 110.0;
		try {
			xdirect = Float.parseFloat((String) params.get("txt_xdirect"));
			ydirect = Float.parseFloat((String) params.get("txt_ydirect"));
			longitude = Double.parseDouble((String) params.get("txt_longitude"));
			latitude = Double.parseDouble((String) params.get("txt_latitude"));
		} catch (Exception e) {
			return new ResponseResult().failure("样地横、纵坐标必须为数值(偏离正北方向的角度值),经纬度必须为数值");
		}
		Plot plot = new Plot((String)params.get("txt_area"), (String)params.get("txt_stand"), 
				(String)params.get("txt_plot"), xdirect, ydirect,(String)params.get("txt_shape"),longitude,latitude);
		try {
			if(plotService.updatePlot(plot) > 0) {
				return new ResponseResult().success("更新成功");
			}
		} catch (Exception e) {
			return new ResponseResult().failure("更新失败，请按要求输入数据");
		}
		return new ResponseResult().failure("更新失败");
    }
	
	@RequestMapping(value="/deletePlot",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deletePlot(@RequestParam String[] paramArea,@RequestParam String[] paramStand,@RequestParam String[] paramPlot) {
		StringBuilder sb = new StringBuilder("");
		int flag = 0;
		for(int i=0;i<paramArea.length;i++)
		{
			logger.info("*deletePlot*"+paramArea[i]+" "+paramStand[i]+" "+paramPlot[i]);
			if(StringUtil.isEmpty(paramArea[i]) == false && StringUtil.isEmpty(paramStand[i]) == false 
					&& StringUtil.isEmpty(paramPlot[i]) == false)
			{
				if(plotService.deletePlot(paramArea[i],paramStand[i],paramPlot[i]) <= 0)
				{
					flag = 1;
					sb.append(" ").append("大区号:").append(paramArea[i])
					.append(",小区号:").append(paramStand[i])
					.append(",样地号:").append(paramPlot[i]).append(":");
				}
			}
			else {
				flag = 1;
			}
		}
		if(flag == 1)
			return new ResponseResult().failure("删除样地 "+sb.toString()+" 失败");
		else {
			return new ResponseResult().success("删除树木成功");
		}
	}
	@RequestMapping(value="/getPlots",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getPlots(){
		List<Plot> plots = plotService.getAllPlots();
		if(plots != null && plots.size() > 0)
		{
			return new ResponseResult().success("success",gson.toJson(plots,new TypeToken<List<Plot>>(){}.getType()));
		}
		return new ResponseResult().failure("未找到任何样地信息");
    }
	
	@RequestMapping(value="/getTreesOfPlot",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult getTreesOfPlot(@RequestParam String area,@RequestParam String stand,@RequestParam String plot){
		logger.info("*getTreesOfPlot*,{},{},{}",area,stand,plot);
		Plot plotMsg = plotService.getPlotByKey(area,stand,plot);
		if (plotMsg != null)
		{
			List<Tree> trees = treeService.getTreesOfPlot(area, stand, plot);
//			String plotJson = gson.toJson(plotMsg,Plot.class);
//			String plotTrees = gson.toJson(trees,new TypeToken<List<Tree>>(){}.getType());
//			logger.info("*getTreesOfPlot*,{}",plotJson);
//			logger.info("*getTreesOfPlot*,{}",plotTrees);
//			String data = "{\"plot\":"+plotJson+",\"trees\":"+plotTrees+"}";
			
			Map<String,Object> map = new HashMap<>();
			map.put("plot", plotMsg);
			map.put("trees", trees);
			List<GrowthSituation> liveTrees = new LinkedList<>();
			for(Tree t : trees) {
				if(t.getIsDead() == 1)
					liveTrees.add(null);
				else {
					GrowthSituation lt = treeService.findLiveTreeByUniqueId(t.getUniqueId());
					liveTrees.add(lt);
				}
				
			}
			map.put("livetrees", liveTrees);
//			String data = gson.toJson(map);
//			logger.info("*getTreesOfPlot*,{}",data);
			if(trees != null && trees.size() > 0)
			{
				return new ResponseResult().success("success", map);
			}
			else return new ResponseResult().failure("在该样地未发现任何树木信息");
		}
		return new ResponseResult().failure("未找到该样地信息");
    }
}
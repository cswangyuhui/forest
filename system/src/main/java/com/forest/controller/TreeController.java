package com.forest.controller;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.forest.common.ResponseResult;
import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.Tree;
import com.forest.pojo.User;
import com.forest.service.TreeService;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;
import com.forest.utils.SelectUtil;
import com.github.pagehelper.StringUtil;

import org.slf4j.Logger;
import com.google.gson.*;



@Controller
@RequestMapping("/trees")
public class TreeController {
	@Autowired
	private TreeService treeService;
	
	private JsonParser parse =new JsonParser();
	
	private static final Logger logger = LoggerFactory.getLogger(TreeController.class);
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getTreeList(@RequestParam Map<String,Object> params,@RequestParam String _search,
			@RequestParam(required=false) String filters){
		logger.debug("***"+params.toString());
		logger.debug("***"+_search);
		logger.debug("***"+filters);
		if(filters != null)
		{
			JsonObject json=(JsonObject) parse.parse(filters);
			JsonArray rules = json.getAsJsonArray("rules");
			for(int i=0;i<rules.size();i++)
			{
				JsonObject rule = (JsonObject) rules.get(i);
				String field = rule.get("field").getAsString();
				int op = SelectUtil.returnOp(rule.get("op").getAsString());
				String data = rule.get("data").getAsString();
				params.put(field+op,data);
			}
		}
		PageUtil pageUtil = new PageUtil(params);
		return new ResponseResult().success("success",treeService.getTreePage(pageUtil));
    }
	
	@RequestMapping(value="/liveTree",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getLiveTreeList(@RequestParam String unique_id){
		//logger.debug("*getLiveTreeList*"+unique_id);
		return new ResponseResult().success("success",treeService.getLiveTreePage(unique_id));
    }
	
	@RequestMapping(value="/deadTree",method=RequestMethod.GET)
	@ResponseBody
	ResponseResult getDeadTreeList(@RequestParam String unique_id){
		//logger.info("*getDeadTreeList*"+unique_id);
		return new ResponseResult().success("success",treeService.getDeadTreePage(unique_id));
    }
	
	@RequestMapping(value="/liveTreeAdd",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult liveTreeAdd(@RequestParam Map params) {
		logger.info("*liveTreeAdd*"+params.get("txt_idu"));
		try {
			if(StringUtil.isEmpty((String) params.get("txt_idu")))
			{
				return new ResponseResult().failure("树木的唯一编号为空");
			}
			String historyId = null;
			if(!StringUtil.isEmpty((String)params.get("txt_idt")))
				historyId = (String)params.get("txt_idt");
			String uniqueId = (String)params.get("txt_idu");
			String treeNum = null;
			if(!StringUtil.isEmpty((String)params.get("txt_treeNum")));
				treeNum = (String)params.get("txt_treeNum");
			Float x = null;
			if(!StringUtil.isEmpty((String) params.get("txt_x")))
				x = Float.parseFloat((String) params.get("txt_x"));
			Float y = null;
			if(!StringUtil.isEmpty((String)params.get("txt_y")))
				y = Float.parseFloat((String)params.get("txt_y"));
			String  treeType = null;
			if(!StringUtil.isEmpty((String)params.get("txt_sp")))
				treeType = (String)params.get("txt_sp");
			String  area = null;
			if(!StringUtil.isEmpty((String)params.get("txt_area")))
				area = (String)params.get("txt_area");
			String stand = null;
			if(!StringUtil.isEmpty((String)params.get("txt_stand")))
				stand = (String)params.get("txt_stand");
			String plot = null;
			if(!StringUtil.isEmpty((String)params.get("txt_plot")))
				plot = (String)params.get("txt_plot");
			Integer isDead = null;
			if(!StringUtil.isEmpty((String)params.get("select_dead")))
				isDead = Integer.parseInt((String)params.get("select_dead"));
			Tree tree = new Tree(historyId,uniqueId,treeNum,x,y,isDead,area,stand,plot,treeType);
			Float  DBH = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dbh"))) {
				DBH = Float.parseFloat((String)params.get("txt_dbh"));
			}
			Float height = null;
			if(!StringUtil.isEmpty((String)params.get("txt_height")))
			{
				height = Float.parseFloat((String)params.get("txt_height"));
			}
			Float BLC = null;
			if(!StringUtil.isEmpty((String)params.get("txt_blc")))
			{
				BLC = Float.parseFloat((String)params.get("txt_blc"));
			}
			Float CWe = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cwe")))
			{
				CWe = Float.parseFloat((String)params.get("txt_cwe"));
			}
			Float CWw = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cww")))
			{
				CWw = Float.parseFloat((String)params.get("txt_cww"));
			}
			Float CWs = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cws")))
			{
				CWs = Float.parseFloat((String)params.get("txt_cws"));
			}
			Float CWn = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cwn")))
			{
				CWn = Float.parseFloat((String)params.get("txt_cwn"));
			}
			GrowthSituation growthSituation = new GrowthSituation(DBH,height,BLC,
					CWe,CWw,CWs,CWn,uniqueId,new Timestamp(System.currentTimeMillis()));
			if(treeService.insertTree(tree) > 0 && treeService.insertGrowthSituation(growthSituation) > 0) {
				return new ResponseResult().success("新增存活树木成功");
			}
			else {
				return new ResponseResult().failure("新增存活树木失败");
			}
		}catch (Exception e) {
			return new ResponseResult().failure("请按要求输入合适的值");
		}
    }

	@RequestMapping(value="/deadTreeAdd",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deadTreeAdd(@RequestParam Map params) {
		logger.info("*liveTreeAdd*"+params.get("txt_idu"));
		try {
			if(StringUtil.isEmpty((String) params.get("txt_idu")))
			{
				return new ResponseResult().failure("树木的唯一编号为空");
			}
			String historyId = null;
			if(!StringUtil.isEmpty((String)params.get("txt_idt")))
				historyId = (String)params.get("txt_idt");
			String uniqueId = (String)params.get("txt_idu");
			String treeNum = null;
			if(!StringUtil.isEmpty((String)params.get("txt_treeNum")));
				treeNum = (String)params.get("txt_treeNum");
			Float x = null;
			if(!StringUtil.isEmpty((String) params.get("txt_x")))
				x = Float.parseFloat((String) params.get("txt_x"));
			Float y = null;
			if(!StringUtil.isEmpty((String)params.get("txt_y")))
				y = Float.parseFloat((String)params.get("txt_y"));
			String  treeType = null;
			if(!StringUtil.isEmpty((String)params.get("txt_sp")))
				treeType = (String)params.get("txt_sp");
			String  area = null;
			if(!StringUtil.isEmpty((String)params.get("txt_area")))
				area = (String)params.get("txt_area");
			String stand = null;
			if(!StringUtil.isEmpty((String)params.get("txt_stand")))
				stand = (String)params.get("txt_stand");
			String plot = null;
			if(!StringUtil.isEmpty((String)params.get("txt_plot")))
				plot = (String)params.get("txt_plot");
			Integer isDead = null;
			if(!StringUtil.isEmpty((String)params.get("select_dead")))
				isDead = Integer.parseInt((String)params.get("select_dead"));
			Tree tree = new Tree(historyId,uniqueId,treeNum,x,y,isDead,area,stand,plot,treeType);
			String note = null;
			if(!StringUtil.isEmpty((String)params.get("txt_note"))) {
				note = (String)params.get("txt_note");
			}
			Integer number = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dead_type"))) {
				number = Integer.parseInt((String)params.get("txt_dead_type"));
			}
			String datetime = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dead_time"))) {
				datetime = (String)params.get("txt_dead_time");
			}
			System.out.println("txt_dead_time"+datetime);
			Timestamp timestamp = Timestamp.valueOf(datetime);
			DeadTree deadTree = new DeadTree(uniqueId,note,timestamp,number);
			if(treeService.insertTree(tree) > 0 && treeService.insertDeadTree(deadTree) > 0) {
				return new ResponseResult().success("新增死亡树木成功");
			}
			else {
				return new ResponseResult().failure("新增死亡树木失败");
			}
		}catch (Exception e) {
			return new ResponseResult().failure("请按要求输入合适的值");
		}
	}
	
	@RequestMapping(value="/deleteTree",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deleteTree(@RequestParam String[] paramId,@RequestParam String[] paramIsDead) {
		StringBuilder sb = new StringBuilder("");
		int flag = 0;
		for(int i=0;i<paramId.length;i++)
		{
			logger.info("*deleteTree*"+paramId[i]+" "+paramIsDead[i]);
			if(StringUtil.isEmpty(paramIsDead[i]) == false && StringUtil.isEmpty(paramId[i]) == false 
					&& paramIsDead[i].equals("0"))
			{
				if(treeService.deleteLiveTree(paramId[i]) <= 0)
				{
					flag = 1;
					sb.append(" ").append(paramId[i]);
				}
			}
			else if(StringUtil.isEmpty(paramIsDead[i]) == false && StringUtil.isEmpty(paramId[i]) == false 
					&& paramIsDead[i].equals("1"))
			{
				if(treeService.deleteDeadTree(paramId[i]) <= 0)
				{
					flag = 1;
					sb.append(" ").append(paramId[i]);
				}
			}
		}
		if(flag == 1)
			return new ResponseResult().failure("删除树木"+sb.toString()+" 失败");
		else {
			return new ResponseResult().success("删除树木成功");
		}
	}
	
	
	@RequestMapping(value="/liveTreeEdit",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult liveTreeEdit(@RequestParam Map params) {
		logger.info("*liveTreeEdit*"+params.get("txt_idu"));
		try {
			if(StringUtil.isEmpty((String) params.get("txt_idu")))
			{
				return new ResponseResult().failure("树木的唯一编号为空");
			}
			String historyId = null;
			if(!StringUtil.isEmpty((String)params.get("txt_idt")))
				historyId = (String)params.get("txt_idt");
			String uniqueId = (String)params.get("txt_idu");
			String treeNum = null;
			if(!StringUtil.isEmpty((String)params.get("txt_treeNum")));
				treeNum = (String)params.get("txt_treeNum");
			Float x = null;
			if(!StringUtil.isEmpty((String) params.get("txt_x")))
				x = Float.parseFloat((String) params.get("txt_x"));
			Float y = null;
			if(!StringUtil.isEmpty((String)params.get("txt_y")))
				y = Float.parseFloat((String)params.get("txt_y"));
			String  treeType = null;
			if(!StringUtil.isEmpty((String)params.get("txt_sp")))
				treeType = (String)params.get("txt_sp");
			String  area = null;
			if(!StringUtil.isEmpty((String)params.get("txt_area")))
				area = (String)params.get("txt_area");
			String stand = null;
			if(!StringUtil.isEmpty((String)params.get("txt_stand")))
				stand = (String)params.get("txt_stand");
			String plot = null;
			if(!StringUtil.isEmpty((String)params.get("txt_plot")))
				plot = (String)params.get("txt_plot");
			Integer isDead = null;
			if(!StringUtil.isEmpty((String)params.get("select_dead")))
				isDead = Integer.parseInt((String)params.get("select_dead"));
			Tree tree = new Tree(historyId,uniqueId,treeNum,x,y,isDead,area,stand,plot,treeType);
			Float  DBH = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dbh"))) {
				DBH = Float.parseFloat((String)params.get("txt_dbh"));
			}
			Float height = null;
			if(!StringUtil.isEmpty((String)params.get("txt_height")))
			{
				height = Float.parseFloat((String)params.get("txt_height"));
			}
			Float BLC = null;
			if(!StringUtil.isEmpty((String)params.get("txt_blc")))
			{
				BLC = Float.parseFloat((String)params.get("txt_blc"));
			}
			Float CWe = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cwe")))
			{
				CWe = Float.parseFloat((String)params.get("txt_cwe"));
			}
			Float CWw = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cww")))
			{
				CWw = Float.parseFloat((String)params.get("txt_cww"));
			}
			Float CWs = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cws")))
			{
				CWs = Float.parseFloat((String)params.get("txt_cws"));
			}
			Float CWn = null;
			if(!StringUtil.isEmpty((String)params.get("txt_cwn")))
			{
				CWn = Float.parseFloat((String)params.get("txt_cwn"));
			}
			GrowthSituation growthSituation = new GrowthSituation(DBH,height,BLC,
					CWe,CWw,CWs,CWn,uniqueId,new Timestamp(System.currentTimeMillis()));
			if(treeService.updateLiveTree(tree, growthSituation) > 0) {
				return new ResponseResult().success("更新存活树木成功");
			}
			else {
				return new ResponseResult().failure("更新存活树木失败");
			}
		}catch (Exception e) {
			return new ResponseResult().failure("请按要求输入合适的值");
		}
    }

	@RequestMapping(value="/deadTreeEdit",method=RequestMethod.POST)
	@ResponseBody
	ResponseResult deadTreeEdit(@RequestParam Map params) {
		logger.info("*deadTreeEdit*"+params.get("txt_idu"));
		try {
			if(StringUtil.isEmpty((String) params.get("txt_idu")))
			{
				return new ResponseResult().failure("树木的唯一编号为空");
			}
			String historyId = null;
			if(!StringUtil.isEmpty((String)params.get("txt_idt")))
				historyId = (String)params.get("txt_idt");
			String uniqueId = (String)params.get("txt_idu");
			String treeNum = null;
			if(!StringUtil.isEmpty((String)params.get("txt_treeNum")));
				treeNum = (String)params.get("txt_treeNum");
			Float x = null;
			if(!StringUtil.isEmpty((String) params.get("txt_x")))
				x = Float.parseFloat((String) params.get("txt_x"));
			Float y = null;
			if(!StringUtil.isEmpty((String)params.get("txt_y")))
				y = Float.parseFloat((String)params.get("txt_y"));
			String  treeType = null;
			if(!StringUtil.isEmpty((String)params.get("txt_sp")))
				treeType = (String)params.get("txt_sp");
			String  area = null;
			if(!StringUtil.isEmpty((String)params.get("txt_area")))
				area = (String)params.get("txt_area");
			String stand = null;
			if(!StringUtil.isEmpty((String)params.get("txt_stand")))
				stand = (String)params.get("txt_stand");
			String plot = null;
			if(!StringUtil.isEmpty((String)params.get("txt_plot")))
				plot = (String)params.get("txt_plot");
			Integer isDead = null;
			if(!StringUtil.isEmpty((String)params.get("select_dead")))
				isDead = Integer.parseInt((String)params.get("select_dead"));
			Tree tree = new Tree(historyId,uniqueId,treeNum,x,y,isDead,area,stand,plot,treeType);
			String note = null;
			if(!StringUtil.isEmpty((String)params.get("txt_note"))) {
				note = (String)params.get("txt_note");
			}
			Integer number = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dead_type"))) {
				number = Integer.parseInt((String)params.get("txt_dead_type"));
			}
			String datetime = null;
			if(!StringUtil.isEmpty((String)params.get("txt_dead_time"))) {
				datetime = (String)params.get("txt_dead_time");
			}
			System.out.println("txt_dead_time"+datetime);
			Timestamp timestamp = Timestamp.valueOf(datetime);
			DeadTree deadTree = new DeadTree(uniqueId,note,timestamp,number);
			if(treeService.updateDeadTree(tree, deadTree) > 0) {
				return new ResponseResult().success("更新死亡树木成功");
			}
			else {
				return new ResponseResult().failure("更新死亡树木失败");
			}
		}catch (Exception e) {
			return new ResponseResult().failure("请按要求输入合适的值");
		}
	}
}

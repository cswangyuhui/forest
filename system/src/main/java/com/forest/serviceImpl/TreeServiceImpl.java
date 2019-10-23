package com.forest.serviceImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.mapper.TreeMapper;
import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.LiveTree;
import com.forest.pojo.Tree;
import com.forest.service.TreeService;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;
@Service("treeService")
public class TreeServiceImpl implements TreeService {

	@Autowired
	private TreeMapper treeMapper;
	public PageResult getTreePage(PageUtil pageUtil) {
		List<Tree> trees = treeMapper.findTree(pageUtil);
		int total = treeMapper.getTotalTree(pageUtil);
		PageResult pageResult = new PageResult(trees, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}
	
	public PageResult getLiveTreePage(String uniqueId) {
		GrowthSituation tree = treeMapper.findLiveTreeByUniqueId(uniqueId);
		List<GrowthSituation> list = new LinkedList<GrowthSituation>();
		list.add(tree);
		int total = 1;
		PageResult pageResult = new PageResult(list, total, 1, 1);
		return pageResult;
	}
	
	public PageResult getDeadTreePage(String uniqueId) {
		DeadTree tree = treeMapper.findDeadTreeByUniqueId(uniqueId);
		List<DeadTree> list = new LinkedList<DeadTree>();
		list.add(tree);
		int total = 1;
		PageResult pageResult = new PageResult(list, total, 1, 1);
		return pageResult;
	}
	
	public int insertTree(Tree tree)
	{
		return treeMapper.insertTree(tree);
	}
	
    public int insertGrowthSituation(GrowthSituation growthSituation)
    {
    	return treeMapper.insertGrowthSituation(growthSituation);
    }

	@Override
	public int insertDeadTree(DeadTree deadTree) {
		return treeMapper.insertDeadTree(deadTree);
	}

	@Override
	public int deleteLiveTree(String uniqueId) {
		return treeMapper.deleteLiveTree(uniqueId);
	}
	
	@Override
	public int deleteDeadTree(String uniqueId) {
		return treeMapper.deleteDeadTree(uniqueId);
	}

	@Override
	public int updateLiveTree(Tree tree, GrowthSituation growthSituation) {
		return treeMapper.updateLiveTree(tree, growthSituation);
	}

	@Override
	public int updateDeadTree(Tree tree, DeadTree deadTree) {
		return treeMapper.updateDeadTree(tree, deadTree);
	}

	@Override
	public GrowthSituation findLiveTreeByUniqueId(String uniqueId) {
		return treeMapper.findLiveTreeByUniqueId(uniqueId);
	}

	@Override
	public DeadTree findDeadTreeByUniqueId(String uniqueId) {
		return treeMapper.findDeadTreeByUniqueId(uniqueId);
	}

	@Override
	public List<Tree> getTreesOfPlot(String area, String stand, String plot) {
		Map<String, String> param = new HashMap<>();
		param.put("area0", area);
		param.put("stand0", stand);
		param.put("plot0", plot);
		List<Tree> trees = treeMapper.findTree(param);
		return trees;
	}
}

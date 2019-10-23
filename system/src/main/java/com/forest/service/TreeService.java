package com.forest.service;

import java.util.List;

import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.Tree;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;

public interface TreeService {
	PageResult getTreePage(PageUtil pageUtil);

	PageResult getLiveTreePage(String unique_id);
	
	PageResult getDeadTreePage(String uniqueId);
	
	int insertTree(Tree tree);
	
	int insertGrowthSituation(GrowthSituation growthSituation);
	
	int deleteLiveTree(String uniqueId);
	
	int deleteDeadTree(String uniqueId);
	
	int insertDeadTree(DeadTree deadTree);
	
	int updateLiveTree(Tree tree, GrowthSituation growthSituation);
	
	int updateDeadTree(Tree tree, DeadTree deadTree);
	
	GrowthSituation findLiveTreeByUniqueId(String uniqueId);
	
	DeadTree findDeadTreeByUniqueId(String uniqueId);
	
	List<Tree> getTreesOfPlot(String area, String stand, String plot);
}

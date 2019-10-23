package com.forest.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.LiveTree;
import com.forest.pojo.Tree;

public interface TreeMapper {
	List<Tree> findTree(Map param);
	List<Tree> exportTree(Map param);
	
	int getTotalTree(Map param);
	//int insertTree(String historyId,String uniqueId,String treeNum,Float locationX,Float locationY,
			//Integer isDead,String area,String stand,String plot,String treeYype);
	int insertTree(Tree tree);
	
	int insertGrowthSituation(GrowthSituation growthSituation);
	
	int insertDeadTree(DeadTree deadTree);
	
	int deleteLiveTree(String uniqueId);
	
	int deleteDeadTree(String uniqueId);
	
	int updateLiveTree(@Param("tree") Tree tree, @Param("gs") GrowthSituation growthSituation);
	
	int updateDeadTree(@Param("tree") Tree tree, @Param("dt") DeadTree deadTree);
	
	int insertLiveTreeByExcel(@Param("tree") List<Tree> tree, @Param("gs") List<GrowthSituation> growthSituation);
	
	int insertDeadTreeByExcel(@Param("tree") List<Tree> tree, @Param("dt") List<DeadTree> deadTree);
	
	GrowthSituation findLiveTreeByUniqueId(String uniqueId);
	
	DeadTree findDeadTreeByUniqueId(String uniqueId);
}

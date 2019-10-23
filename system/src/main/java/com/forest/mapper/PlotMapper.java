package com.forest.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.forest.pojo.Plot;


public interface PlotMapper {
	List<String> selectArea();
	List<String> selectStand();
	List<String> selectPlot();
	
	List<Plot> findPlot(Map param);
	
	int getTotalPlot();
	
	int insertPlot(Plot plot);
	
	int updatePlot(Plot plot);
	
	int deletePlot(@Param("area") String area, @Param("stand") String stand, @Param("plot") String plot);
}

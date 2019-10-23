package com.forest.service;

import java.util.List;

import com.forest.pojo.Plot;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;

public interface PlotService {
	List<String> selectArea();
	List<String> selectStand();
	List<String> selectPlot();
	List<Plot> getAllPlots();
	PageResult getPlotPage(PageUtil pageUtil);
	int insertPlot(Plot plot);
	int updatePlot(Plot plot);
	int deletePlot(String area, String stand, String plot);
	Plot getPlotByKey(String area, String stand, String plot);
}

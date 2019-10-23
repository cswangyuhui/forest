package com.forest.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.mapper.PlotMapper;
import com.forest.pojo.Plot;
import com.forest.pojo.Tree;
import com.forest.service.PlotService;
import com.forest.utils.PageResult;
import com.forest.utils.PageUtil;
@Service("plotService")
public class PlotServiceImpl implements PlotService {
	
	@Autowired
	private PlotMapper plotMapper;
	@Override
	public List<String> selectArea() {
		return plotMapper.selectArea();
	}

	@Override
	public List<String> selectStand() {
		return plotMapper.selectStand();
	}

	@Override
	public List<String> selectPlot() {
		return plotMapper.selectPlot();
	}

	@Override
	public PageResult getPlotPage(PageUtil pageUtil) {
		List<Plot> plots = plotMapper.findPlot(pageUtil);
		int total = plotMapper.getTotalPlot();
		PageResult pageResult = new PageResult(plots, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public int insertPlot(Plot plot) {
		return plotMapper.insertPlot(plot);
	}

	@Override
	public int updatePlot(Plot plot) {
		return plotMapper.updatePlot(plot);
	}

	@Override
	public int deletePlot(String area, String stand, String plot) {
		return plotMapper.deletePlot(area,stand,plot);
	}

	@Override
	public List<Plot> getAllPlots() {
		return plotMapper.findPlot(null);
	}

	@Override
	public Plot getPlotByKey(String area, String stand, String plot) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("area",area);
		param.put("stand",stand);
		param.put("plot",plot);
		List<Plot> plots = plotMapper.findPlot(param);
		if(plot != null && plots.size() == 1)
		{
			return plots.get(0);
		}
		return null;
	}

}

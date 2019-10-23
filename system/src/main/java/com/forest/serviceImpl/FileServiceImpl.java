package com.forest.serviceImpl;

import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.forest.controller.UserController;
import com.forest.mapper.TreeMapper;
import com.forest.pojo.DeadTree;
import com.forest.pojo.GrowthSituation;
import com.forest.pojo.Tree;
import com.forest.service.FileService;
import com.forest.utils.PoiUtil;

@Service("fileService")
public class FileServiceImpl implements FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Autowired
	private TreeMapper treeMapper;
	@Override
	public int importTreeByExcel(File file) {
		XSSFSheet xssfSheet = null;
		try {
			xssfSheet = PoiUtil.getXSSFSheet(file);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		List<Tree> liveTrees = new ArrayList<>();
		List<Tree> deadTrees = new ArrayList<>();
		List<GrowthSituation> gss = new ArrayList<>();
		List<DeadTree> dts = new ArrayList<>();
		 for(int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
	            //按行读取数据
	            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
	            if (xssfRow != null) {
	                String historyId = PoiUtil.getValue(xssfRow.getCell(0));
	                String uniqueId = PoiUtil.getValue(xssfRow.getCell(1));
	                String area = PoiUtil.getValue(xssfRow.getCell(2));
	                String stand = PoiUtil.getValue(xssfRow.getCell(3));
	                String plot = PoiUtil.getValue(xssfRow.getCell(4));
	                String treeNum = PoiUtil.getValue(xssfRow.getCell(5));
	                String treeType = PoiUtil.getValue(xssfRow.getCell(6));
	                Float locationX = xssfRow.getCell(14)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(14)));
	                Float locationY = xssfRow.getCell(15)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(15)));
	                
	                XSSFCell note = xssfRow.getCell(16);
	                XSSFCell deadType = xssfRow.getCell(17);
	                XSSFCell deadTime = xssfRow.getCell(18);
	                if(note == null || deadType == null || deadTime == null)
	                {
	                	Tree tree = null;
	                	tree = new Tree(historyId, uniqueId, treeNum, locationX, locationY, 0, area, stand, plot, treeType);
	                	logger.debug("{},{},{}","importTreeByExcel","livetree",tree);
	                	liveTrees.add(tree);
	                	Float DBH = xssfRow.getCell(7)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(7)));
	                	Float height = xssfRow.getCell(8)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(8)));
	                	Float BLC = xssfRow.getCell(9)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(9)));
	                	Float CWe = xssfRow.getCell(10)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(10)));
	                	Float CWw = xssfRow.getCell(11)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(11)));
	                	Float CWs = xssfRow.getCell(12)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(12)));
		                Float CWn = xssfRow.getCell(13)==null ? null : Float.valueOf(PoiUtil.getValue(xssfRow.getCell(13)));
		                GrowthSituation gs = new GrowthSituation(DBH, height, BLC, CWe, CWw, CWs, CWn, uniqueId, new Timestamp(System.currentTimeMillis()));
		                logger.debug("{},{},{}","importTreeByExcel","GrowthSituation",gs);
		                gss.add(gs);
	                }
	                else {
	                	Tree tree = null;
	                	tree = new Tree(historyId, uniqueId, treeNum, locationX, locationY, 1, area, stand, plot, treeType);
	                	logger.debug("{},{},{}","importTreeByExcel","deadtree",tree);
	                	deadTrees.add(tree);
	                	DeadTree dt = new DeadTree(uniqueId, PoiUtil.getValue(note), Timestamp.valueOf(PoiUtil.getValue(deadTime)), Integer.valueOf(PoiUtil.getValue(deadType)));
	                	logger.debug("{},{},{}","importTreeByExcel","DeadTree",dt);
	                	dts.add(dt);
	                }
	            }
	        }
		 int liveTreeAdd = 0;
		 int deadTreeAdd = 0;
		 if(liveTrees.size() > 0 && liveTrees.size() == gss.size())
		 {
			 liveTreeAdd = treeMapper.insertLiveTreeByExcel(liveTrees, gss);
		 }
		 if(deadTrees.size() > 0 && deadTrees.size() == dts.size())
		 {
			 deadTreeAdd = treeMapper.insertDeadTreeByExcel(deadTrees, dts);
		 }
		return liveTreeAdd+deadTreeAdd;
	}
	private Tree convertXSSFRowToTree(XSSFRow xssfRow) {
        Tree tree = new Tree();
        XSSFCell note = xssfRow.getCell(16);
        XSSFCell deadType = xssfRow.getCell(17);
        XSSFCell deadTime = xssfRow.getCell(18);
        logger.debug("{},{},{}",PoiUtil.getValue(note),PoiUtil.getValue(deadType),PoiUtil.getValue(deadTime));
        return tree;
    }
	@Override
	public List<Tree> exportTreeExcel(Map param) {
		logger.debug("{serviceMap},{}",param);
		List<Tree> trees = treeMapper.exportTree(param);
		for(int i=0;i<trees.size();i++)
		{
			logger.info("*export*"+trees.get(i).toString());
		}
		return trees;
	}

}

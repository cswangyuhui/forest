package com.forest.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.forest.pojo.Tree;

public interface FileService {
	int importTreeByExcel(File file);
	List<Tree> exportTreeExcel(Map param);
}

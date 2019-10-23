package com.forest.pojo;

public class LiveTree {
	private Tree tree;
	private GrowthSituation growthSituation;
	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public GrowthSituation getGrowthSituation() {
		return growthSituation;
	}
	public void setGrowthSituation(GrowthSituation growthSituation) {
		this.growthSituation = growthSituation;
	}
	public LiveTree(Tree tree, GrowthSituation growthSituation) {
		super();
		this.tree = tree;
		this.growthSituation = growthSituation;
	}
	public LiveTree() {
		super();
	}
	
}

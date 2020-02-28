package cc.watchers.pdfgen.genoutline;

/***
 * 
 * 
 * TODO: 目录添加时，在指定页面基础上，支持指定页面内部位置定义
 * 
 */

public class ContentItem {

	
	int level;
	String item;
	int page;
	boolean isParent;
	
	//int revise = 50/100;
	
	public ContentItem(){
		isParent = false;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level < 1) {
			level = 1;
		}
		this.level = level;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = 1;
		}
		this.page = page;
	}

	public boolean isParent() {
		return isParent;
	}
	
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	
}

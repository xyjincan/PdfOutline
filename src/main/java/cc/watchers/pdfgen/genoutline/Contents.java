package cc.watchers.pdfgen.genoutline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Table of Contents
 * 
 */
public class Contents {

	String contents;
	List<ContentItem> list = null;

	public Contents(String contents) {
		this.contents = contents;
		list = new ArrayList<ContentItem>();
	}

	public List<ContentItem> load() {

		list.clear();
		// 读文件
		File f = new File(contents);
		if (f.isFile() && f.exists()) {
			InputStreamReader read;
			try {
				read = new InputStreamReader(new FileInputStream(f), "utf-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				ContentItem pitem = null;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if(line.length() == 0) {
						continue;
					}
					if (line.charAt(0) > '9' || line.charAt(0) < '0') {
						System.out.println("ignore: " + line);
						continue;
					}
					// 中英文分隔符 , ，
					int sindex = line.indexOf(",");
					int lindex = line.lastIndexOf(",");
					if (sindex == lindex) {
						sindex = line.indexOf("，");
						lindex = line.lastIndexOf("，");
					}

					if (sindex == lindex) {
						System.err.println("err: " + line);
						continue;
					}

					try {
						ContentItem item = new ContentItem();
						String tmp = line.substring(0, sindex);
						int level = Integer.parseInt(tmp.trim());
						item.setLevel(level);
						item.setItem(line.substring(sindex + 1, lindex).trim());
						item.setPage(Integer.parseInt(line.substring(lindex + 1, line.length()).trim()));

						if (pitem != null) {
							if (pitem.getLevel() < item.getLevel()) {
								pitem.setParent(true);
							}
						}
						list.add(item);
						pitem = item;
						System.out.println("add: " + line);
					} catch (Exception e) {
						System.err.println("err: " + line);
					}
				}
				read.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return list;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}

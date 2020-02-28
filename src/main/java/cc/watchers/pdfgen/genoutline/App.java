package cc.watchers.pdfgen.genoutline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// 
// 996/1500
// 0.664

// Rectangle A4 = new RectangleReadOnly(595,842);
// 595/842
// 0.70665083135392

// A4纸张
// 21*29.7cm（210mm×297mm）
// 长宽比√2：1

/**
 * 
 * @ 将无目录的pdf文件，通过目录描述数据修改为含目录的pdf文件
 * 
 */

public class App {

	public static void addContents(String pdf, String content) {

		File file = new File(pdf);
		String out_pdf = "new_";
		if (file.getParent() != null) {
			out_pdf = file.getParent() + File.separator + out_pdf + file.getName();
		} else {
			out_pdf = out_pdf + file.getName();
		}
		Contents contents = new Contents(content);
		GenOutline genoutline = new GenOutline(pdf, out_pdf);
		try {
			genoutline.init();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		List<ContentItem> list = contents.load();
		int nowlevel = 1;// 最高级为1级，
		for (int i = 0; i < list.size(); i++) {
			ContentItem item = list.get(i);

			while (nowlevel > item.getLevel()) {// 需要提升目录等级
				genoutline.UpChapter();// 回到上级目录
				nowlevel -= 1;//
			}

			if (item.isParent()) {
				// 该节点下将有子节点
				genoutline.parentChapter(item.getItem(), item.getPage());
			} else {
				genoutline.addContent(item.getItem(), item.getPage());
			}

			if (nowlevel < item.getLevel()) {
				nowlevel += 1;// 每次只能下降一级
			} else {
				nowlevel = item.getLevel();
			}

		}
		genoutline.close();
	}

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String content = null;
		String pdf = null;
		while (true) {
			System.out.print("输入pdf文件地址：");
			pdf = in.nextLine();
			System.out.print("输入书签数据文件地址：");
			content = in.nextLine();
			addContents(pdf, content);
			System.out.println("完成，回车继续");
			System.err.println();
			in.nextLine();
		}

	}

}

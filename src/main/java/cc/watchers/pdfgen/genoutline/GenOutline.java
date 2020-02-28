package cc.watchers.pdfgen.genoutline;

import java.io.File;
import java.io.IOException;
import java.util.List;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class GenOutline {

	String src;
	String dest;

	//
	PdfDocument pdfDocument;
	Document document;

	// 保存 层级 信息 支持
	PdfOutline outlines;

	//
	int indexnum;
	int indexChapter;

	// pdf info
	float pdfHeight = 842;

	public GenOutline(String src, String dest) {
		this.src = src;
		this.dest = dest;

	}

	public void addContent(String item, int pagenum) {

		String anchor = String.format("watchers%03d", indexnum);
		this.addPara(pagenum, anchor);

		PdfOutline kid = outlines.addOutline(item);
		kid.addDestination(PdfDestination.makeDestination(new PdfString(anchor)));
		indexnum += 1;
	}

	// 提升目录级别
	public void UpChapter() {
		if (outlines.getParent() != null) {
			outlines = outlines.getParent();
		}
	}

	// 获取新的顶级目录
	public void rootChapter() {
		outlines = pdfDocument.getOutlines(false);
	}

	/**
	 * 
	 * 添加父目录需要数据
	 */
	public void parentChapter(String item, int pagenum) {

		String anchor = String.format("cc%03d", indexChapter);
		this.addPara(pagenum, anchor);
		outlines = outlines.addOutline(item);// 添加条目并，成为base目录
		outlines.addDestination(PdfDestination.makeDestination(new PdfString(anchor)));

		indexChapter++;
		indexnum += 1;
	}

	private void addPara(int pagenum, String anchor) {

		Paragraph p = new Paragraph();// 空标签作为书签的锚点
		p.setFixedPosition(pagenum, 20, pdfHeight - 20, 200);// 设置锚点位置
		p.setDestination(anchor);
		document.add(p);
	}

	static boolean replace = false;
	
	//
	public void init() throws IOException {

		File file = null;

		file = new File(src);
		if (!file.exists()) {
			System.out.println(src);
			throw new IOException("pdf文件不存在！");
		}
		file = new File(dest);
		if (file.exists() && !replace) {
			System.out.println(dest);
			throw new IOException("目标文件已存在！");
		}

		try {
			pdfDocument = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
			document = new Document(pdfDocument);
			pdfHeight = pdfDocument.getDefaultPageSize().getHeight();

			outlines = pdfDocument.getOutlines(false);
			List<PdfOutline> list = outlines.getAllChildren();
			if (list != null) {
				if (list.size() != 0) {
					// 目录已存在则删除
					pdfDocument.getCatalog().remove(PdfName.Outlines);
					outlines = pdfDocument.getOutlines(true);//
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		indexnum = 1;
		indexChapter = 1;
		//
		outlines = pdfDocument.getOutlines(false);
	}

	public void close() {
		document.close();
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

}

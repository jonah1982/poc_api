package com.soprasteria.modelling.report.framework;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.jfree.chart.JFreeChart;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.report.util.ChartGenerator;

public class PDFReport implements DataModelReportITF {
	public final static String REPORT_DIR = "/nfsshare/report/";
	public final static String REPORT_AUTHER = "Visenti Pte Ltd";
	
	public static Font FONT_H1 = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
	public static Font FONT_H2 = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
	public static Font FONT_NORMAL = new Font(Font.TIMES_ROMAN, 12);
	public static Font FONT_BOLD = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
	
	@Override
	public File getDirectory() throws Exception {
		File dir = new File(REPORT_DIR);
		if(!dir.exists()) Tool.createFolder(dir);
		if(!dir.canRead()) throw new Exception("The report root directory "+dir+" is not readable.");
		if(!dir.canWrite()) throw new Exception("The report root directory "+dir+" is not writable."); 
		return dir;
	}

	@Override
	public File getFile() throws Exception {
		return new File(getDirectory(), getName()+".pdf");
	}
	
	public String getName() {
		return "data";
	}

	@Override
	public String getTitle() {
		return "Hydraulic Report";
	}

	@Override
	public File generate() throws Exception {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(getFile()));
		document.open();
		buildContent(writer, document);
		document.close();
		return getFile();
	}
	
	protected void buildContent(PdfWriter writer, Document document) {
		try {
			addMetaData(document);
			addTitlePage(document);
			addContent(writer, document);
			addFooterPage(document);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	protected void addMetaData(Document document) {
		document.addTitle(getTitle());
		document.addAuthor(REPORT_AUTHER);
		document.addCreator(REPORT_AUTHER);
	}
	
	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	private void addTitlePage(Document document) throws DocumentException {
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 1);
		preface.add(new Paragraph(getTitle(), FONT_H1));
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Report generated by: " + REPORT_AUTHER + ", " + new Date(), FONT_BOLD));
		addEmptyLine(preface, 10);
		document.add(preface);
	}
	
	private void addFooterPage(Document document) throws DocumentException {
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Signed by: _______________________________________________________", FONT_BOLD));
		addEmptyLine(preface, 3);
		document.add(preface);
	}
	
	private void addContent(PdfWriter writer, Document document) throws DocumentException {
		int height = 200;
		writeChartToPDF(writer, document, ChartGenerator.generateBarChart("FCPH Demand"), 0, 400, height);
		writeChartToPDF(writer, document, ChartGenerator.generateBarChart("MNSR Demand"), 0, 400-height, height);
		document.newPage();
	}
	
	public static void writeChartToPDF(PdfWriter writer, Document document, JFreeChart chart, int x, int y, int height) {
		try {
			int width = 600;
			PdfContentByte contentByte = writer.getDirectContent();
			PdfTemplate template = contentByte.createTemplate(width, height);
			Graphics2D graphics2d = template.createGraphics(width, height,new DefaultFontMapper());
			Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,height);
			chart.draw(graphics2d, rectangle2d);
			graphics2d.dispose();
			contentByte.addTemplate(template, x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

package cn.joy.demo.external.office.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelExportPoi {

	public static void main(String[] args) throws Exception {
		// 创建一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个表格
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 创建一个列
		HSSFRow row = sheet.createRow(0);
		// 创建一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 创建一个字体
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 填充单元格
		for (int i = 0; i < 5; i++) {
			// 创建一个单元格
			HSSFCell cell = row.createCell(i);
			switch (i) {
			case 0:
				// 设置普通文本
				cell.setCellValue(new HSSFRichTextString("普通文本"));
				break;
			case 1:
				// 设置为形状
				HSSFClientAnchor a1 = new HSSFClientAnchor(0, 0, 1023, 255, (short) 1, 0, (short) 1, 0);
				HSSFSimpleShape shape1 = patriarch.createSimpleShape(a1);
				// 这里可以设置形状的样式
				shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);

				break;
			case 2:
				// 设置为布尔量
				cell.setCellValue(true);
				break;
			case 3:
				// 设置为double值
				cell.setCellValue(12.5);
				break;
			case 4:
				// 设置为图片
				// URL url= ExcelExport.class.getResource("hello.jpg");
				// insertImage(wb,patriarch,getImageData(ImageIO.read(url)),0,4,1);
				// File imgFile = new File("D:\\图片a.jpg");
				File imgFile = new File("D:\\图片a.jpg");
				if (!imgFile.exists()) {
					System.out.println("图片不存在！");
					return;
				}
				Object[] imgInfo = getImageData(imgFile);
				if (imgInfo != null)
					insertImage(wb, patriarch, (byte[]) imgInfo[0], 0, 4, 1);
				break;

			}

			// 设置单元格的样式
			cell.setCellStyle(style);
		}
		File f = new File("D:\\test.xls");
		f.delete();
		FileOutputStream fout = new FileOutputStream(f);
		// 输出到文件
		wb.write(fout);
		fout.close();
	}

	// 自定义的方法,插入某个图片到指定索引的位置
	private static void insertImage(HSSFWorkbook wb, HSSFPatriarch pa, byte[] data, int row, int column, int index) {
		int x1 = index * 250;
		int y1 = 0;
		int x2 = x1 + 255;
		int y2 = 255;
		HSSFClientAnchor anchor = new HSSFClientAnchor(x1, y1, x2, y2, (short) column, row, (short) column, row);
		anchor.setAnchorType(2);
		pa.createPicture(anchor, wb.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG)).resize(1);
	}

	// 从图片里面得到字节数组
	private static byte[] getImageData(BufferedImage bi) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ImageIO.write(bi, "PNG", bout);
			return bout.toByteArray();
		} catch (Exception exe) {
			exe.printStackTrace();
			return null;
		}
	}

	private static Object[] getImageData(File imgFile) {
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			ImageInputStream iis = ImageIO.createImageInputStream(imgFile);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext())
				return null;
			ImageReader reader = iter.next();
			String imgType = reader.getFormatName();
			System.out.println("image type=" + imgType);
			reader.setInput(iis);
			BufferedImage bufferImg = reader.read(0);
			ImageIO.write(bufferImg, imgType, byteArrayOut);
			iis.close();
			return new Object[] { byteArrayOut.toByteArray(), imgType };
		} catch (Exception exe) {
			exe.printStackTrace();
			return null;
		}
	}
}
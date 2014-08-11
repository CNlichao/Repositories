package com.global;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.lowagie.text.Utilities;

/**
 * @author Liangcha007 将Tiff文件转换成PDF文件
 */
public class TiffToPdf {
	/**
	 * tiff转换成pdf，iText2.0.8.jar
	 * 
	 * @param tiffPath
	 * @param pdfPath
	 * @return
	 */
	public static boolean convert(String tiffPath, String pdfPath) {
		System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath + "]---开始！");
		// 创建一个文档对象
		Document doc = new Document();
		try {
			// 定义输出文件的位置
			try {
				PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
				// 开启文档
				doc.open();
				if (!tiffPath.endsWith(".tif")) {
					Image jpg = Image.getInstance(tiffPath); // 原来的图片的路径
					// 获得图片的高度
					float heigth = jpg.getHeight();
					float width = jpg.getWidth();
					// 合理压缩，h>w，按w压缩，否则按w压缩
					int percent = getPercent(heigth, width);

					// 设置图片居中显示
					jpg.setAlignment(Image.MIDDLE);
					// 按百分比显示图片的比例
					if (width > 1024 || heigth > 786) {
						jpg.scalePercent(percent);
					}
					doc.add(jpg);
				} else
				// tiff多页
				{
					Object localObject1 = null;
					Object localObject2 = null;
					URL paramURL = Utilities.toURL(tiffPath);
					try {
						if (paramURL.getProtocol().equals("file")) {
							localObject2 = paramURL.getFile();
							localObject2 = Utilities
									.unEscapeURL((String) localObject2);
							localObject1 = new RandomAccessFileOrArray(
									(String) localObject2);
						} else {
							localObject1 = new RandomAccessFileOrArray(paramURL);
						}

						int pageNums = TiffImage
								.getNumberOfPages((RandomAccessFileOrArray) localObject1);
						if (pageNums > 0) {
							for (int i = 1; i <= pageNums; i++) {
								localObject2 = TiffImage.getTiffImage(
										(RandomAccessFileOrArray) localObject1,
										i);
								Image jpg = (Image) localObject2;
								// 获得图片的高度
								float heigth = jpg.getHeight();
								float width = jpg.getWidth();
								// 合理压缩，h>w，按w压缩，否则按w压缩
								int percent = getPercent(heigth, width);

								// 设置图片居中显示
								jpg.setAlignment(Image.MIDDLE);
								// 按百分比显示图片的比例
								if (width > 1024 || heigth > 786) {
									jpg.scalePercent(percent);
								}
								doc.add(jpg);
							}
						}
						if (localObject1 != null)
							((RandomAccessFileOrArray) localObject1).close();
					} catch (Exception e) {
						if (localObject1 != null)
							((RandomAccessFileOrArray) localObject1).close();
						System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath
								+ "]失败！\n原因：" + e.getMessage());
						return false;
					}
				}
			} catch (com.itextpdf.text.DocumentException e) {
				e.printStackTrace();
				System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath
						+ "]---失败，已结束！");
				doc.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath
					+ "]失败！,原因：" + e.getMessage());
			e.printStackTrace();
			doc.close();
			return false;
		} catch (IOException e) {
			System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath
					+ "]失败！,原因：" + e.getMessage());
			e.printStackTrace();
			doc.close();
			return false;
		}
		doc.close();
		System.out.println("由文件[" + tiffPath + "]转为[" + pdfPath + "]---结束！");
		return true;
	}

	/**
	 * 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
	 * 
	 * @param h
	 * @param w
	 * @return
	 */
	public static int getPercent(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		if (h > w) {
			p2 = 210 / h * 279;
		} else {
			p2 = 210 / w * 279;
		}
		p = Math.round(p2);
		return p;
	}

	/**
	 * tiff->pdf转换接口
	 * 
	 * @param tiff_path
	 * @param pdf_path
	 * @return
	 */
	public boolean tiff2pdf(String tiff_path, String pdf_path) {
		System.out.println("开始将tiff转换成pdf--时间：" + getStringDate());
		if (convert(tiff_path, pdf_path)) {
			System.out.println("转换结束！");
			return true;
		} else {
			System.out.println("tiff转换成pdf失败！--时间：" + getStringDate());
			return false;
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("下面开始转换.....");
			convert("F:/test.tif", "F:/test.pdf");
			System.out.println("转换结束！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

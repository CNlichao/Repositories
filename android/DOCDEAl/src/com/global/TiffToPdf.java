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
 * @author Liangcha007 ��Tiff�ļ�ת����PDF�ļ�
 */
public class TiffToPdf {
	/**
	 * tiffת����pdf��iText2.0.8.jar
	 * 
	 * @param tiffPath
	 * @param pdfPath
	 * @return
	 */
	public static boolean convert(String tiffPath, String pdfPath) {
		System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath + "]---��ʼ��");
		// ����һ���ĵ�����
		Document doc = new Document();
		try {
			// ��������ļ���λ��
			try {
				PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
				// �����ĵ�
				doc.open();
				if (!tiffPath.endsWith(".tif")) {
					Image jpg = Image.getInstance(tiffPath); // ԭ����ͼƬ��·��
					// ���ͼƬ�ĸ߶�
					float heigth = jpg.getHeight();
					float width = jpg.getWidth();
					// ����ѹ����h>w����wѹ��������wѹ��
					int percent = getPercent(heigth, width);

					// ����ͼƬ������ʾ
					jpg.setAlignment(Image.MIDDLE);
					// ���ٷֱ���ʾͼƬ�ı���
					if (width > 1024 || heigth > 786) {
						jpg.scalePercent(percent);
					}
					doc.add(jpg);
				} else
				// tiff��ҳ
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
								// ���ͼƬ�ĸ߶�
								float heigth = jpg.getHeight();
								float width = jpg.getWidth();
								// ����ѹ����h>w����wѹ��������wѹ��
								int percent = getPercent(heigth, width);

								// ����ͼƬ������ʾ
								jpg.setAlignment(Image.MIDDLE);
								// ���ٷֱ���ʾͼƬ�ı���
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
						System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath
								+ "]ʧ�ܣ�\nԭ��" + e.getMessage());
						return false;
					}
				}
			} catch (com.itextpdf.text.DocumentException e) {
				e.printStackTrace();
				System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath
						+ "]---ʧ�ܣ��ѽ�����");
				doc.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath
					+ "]ʧ�ܣ�,ԭ��" + e.getMessage());
			e.printStackTrace();
			doc.close();
			return false;
		} catch (IOException e) {
			System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath
					+ "]ʧ�ܣ�,ԭ��" + e.getMessage());
			e.printStackTrace();
			doc.close();
			return false;
		}
		doc.close();
		System.out.println("���ļ�[" + tiffPath + "]תΪ[" + pdfPath + "]---������");
		return true;
	}

	/**
	 * �ڲ��ı�ͼƬ��״��ͬʱ���жϣ����h>w����hѹ����������w>h��w=h������£������ѹ��
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
	 * tiff->pdfת���ӿ�
	 * 
	 * @param tiff_path
	 * @param pdf_path
	 * @return
	 */
	public boolean tiff2pdf(String tiff_path, String pdf_path) {
		System.out.println("��ʼ��tiffת����pdf--ʱ�䣺" + getStringDate());
		if (convert(tiff_path, pdf_path)) {
			System.out.println("ת��������");
			return true;
		} else {
			System.out.println("tiffת����pdfʧ�ܣ�--ʱ�䣺" + getStringDate());
			return false;
		}
	}

	/**
	 * ��ȡ��ǰʱ��
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
			System.out.println("���濪ʼת��.....");
			convert("F:/test.tif", "F:/test.pdf");
			System.out.println("ת��������");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

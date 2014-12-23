package com.rick.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	/**
	 * @param srcFiles
	 *            需压缩的文件路径及文件名
	 * @param desFile
	 *            保存的文件名及路径
	 * @return 如果压缩成功返回true
	 */
	public static boolean zip(String[] srcFiles, String desFile) {
		boolean isSuccessful = false;

		String[] fileNames = new String[srcFiles.length];
		for (int i = 0; i < srcFiles.length; i++) {
			fileNames[i] = parse(srcFiles[i]);
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(desFile));
			ZipOutputStream zos = new ZipOutputStream(bos);
			String entryName = null;

			for (int i = 0; i < fileNames.length; i++) {
				entryName = fileNames[i];

				// 创建Zip条目
				ZipEntry entry = new ZipEntry(entryName);
				zos.putNextEntry(entry);

				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(srcFiles[i]));

				byte[] b = new byte[1024];

				while (bis.read(b, 0, 1024) != -1) {
					zos.write(b, 0, 1024);
				}
				bis.close();
				zos.closeEntry();
			}

			zos.flush();
			zos.close();
			isSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isSuccessful;
	}

	/**
	 * @param srcZipFile
	 *            需解压的文件名
	 * @param targetPath
	 *            解压路径
	 * @return 如果解压成功返回true
	 */
	public boolean unzip(String srcZipFile, String targetPath) {
		boolean isSuccessful = false;
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(srcZipFile));
			ZipInputStream zis = new ZipInputStream(bis);

			BufferedOutputStream bos = null;

			// byte[] b = new byte[1024];
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();
				bos = new BufferedOutputStream(new FileOutputStream(targetPath
						+ entryName));
				int b = 0;
				while ((b = zis.read()) != -1) {
					bos.write(b);
				}
				bos.flush();
				bos.close();
			}
			zis.close();
			isSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccessful;
	}

	// 解析文件名
	private static String parse(String srcFile) {
		int location = srcFile.lastIndexOf("/");
		String fileName = srcFile.substring(location + 1);
		return fileName;
	}
}

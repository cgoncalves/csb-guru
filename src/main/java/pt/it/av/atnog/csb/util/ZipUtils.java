package pt.it.av.atnog.csb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class ZipUtils {

	public static void zipDir(File directory, File zipFile) throws Exception {
//		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
//		addDir(directory, out);
//		out.close();
		List<File> fileList = new ArrayList<File>();
		System.out.println("---Getting references to all files in: " + directory.getCanonicalPath());
		getAllFiles(directory, fileList);
		System.out.println("---Creating zip file");
		writeZipFile(directory, fileList, zipFile);
		System.out.println("---Done");
	}
//
//	private static void addDir(File dirObj, ZipOutputStream out) throws IOException {
//		File[] files = dirObj.listFiles();
//		byte[] tmpBuf = new byte[1024];
//
//		for (int i = 0; i < files.length; i++) {
//			if (files[i].isDirectory()) {
//				addDir(files[i], out);
//				continue;
//			}
//			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
//			System.out.println(" Adding: " + files[i].getName());
//			System.out.println(" Adding: " + files[i].getPath());
//			out.putNextEntry(new ZipEntry(files[i].getName()));
//			int len;
//			while ((len = in.read(tmpBuf)) > 0) {
//				out.write(tmpBuf, 0, len);
//			}
//			in.close();
//		}
//		out.closeEntry();
//	}
//	
//	public static void main(String args[]) throws Exception {
//		ZipUtils zu = new ZipUtils();
//		File directory = new File("/Users/cgoncalves/Downloads/ziputils");
//		File zipFile = new File("/Users/cgoncalves/Downloads/ziputils2");
//		zu.zipDir(directory, zipFile);
//	}
	
//	public static void main(String[] args) throws IOException {
//		File directoryToZip = new File("/Users/cgoncalves/Downloads/ziputils");
//		File zipFile = new File(directoryToZip.getName() + ".zip");
//
//		List<File> fileList = new ArrayList<File>();
//		System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
//		getAllFiles(directoryToZip, fileList);
//		System.out.println("---Creating zip file");
//		writeZipFile(directoryToZip, fileList);
//		System.out.println("---Done");
//	}

	private static void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeZipFile(File directoryToZip, List<File> fileList, File zipFile) {

		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			System.out.println(directoryToZip.getName() + ".zip");
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}

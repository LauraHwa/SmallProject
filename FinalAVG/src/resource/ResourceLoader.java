package resource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.WeakHashMap;
	/**
	 * �����࣬�����ṩһЩ��̬��������Ҫ�Ǽ���
	 * ��Դ������
	 * @author czq
	 *
	 */
public abstract class ResourceLoader {
	//���ظ��߳������ĵ�classloader
	final static private ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

	@SuppressWarnings("rawtypes")
	final static private WeakHashMap lazyMap = new WeakHashMap(100);

	public static void clearLazy() {
		lazyMap.clear();
	}

	public  void finalize() {
		clearLazy();
	}

	
	public static InputStream getResourceToInputStream(final String fileName) {
	ByteArrayInputStream in = 	new ByteArrayInputStream(getResource(fileName));
	
		return in;
	}

	public static boolean isExists(String fileName) {
		return new File(fileName).exists();
	}
	/**
	 * ������Դ��������У��ͼ���
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] getResource(final String fileName) {
		String innerName = fileName;
		//System.out.println(innerName);
		String keyName = innerName.replaceAll(" ", "").toLowerCase();
		byte[] result = (byte[]) lazyMap.get(keyName);
		
		if (result == null) {
			BufferedInputStream bufferedInput = null;
			
				
				bufferedInput = new BufferedInputStream(classLoader
						.getResourceAsStream(innerName));
				
			
			System.out.println(innerName);//TODO
			lazyMap.put(keyName, result = getDataSource(bufferedInput));
		
		}
		return result;
	}
	/**
	 * ���ű�ת��Ϊ�ֽ���
	 * @param is
	 * @return
	 */
	final static private byte[] getDataSource(InputStream is) {
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		byte[] arrayByte = new byte[8192];
		try {
			int read;
			
			while ((read = is.read(arrayByte)) >= 0) {
				byteArrayOutputStream.write(arrayByte, 0, read);
			}
			arrayByte = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
					byteArrayOutputStream = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				
			} catch (IOException e) {
			}
		}
		return arrayByte;
	}

}

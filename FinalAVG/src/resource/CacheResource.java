package resource;


import collection.ArrayMap;

/**
 * ���ڻ�����ֶ������ṩһ��ӳ���Ÿ��ֻ���
 * @author czq
 *
 */
public abstract class CacheResource {

	final static public ArrayMap ADV_CHARAS = new ArrayMap(100);

	public static void clearAll() {
		ADV_CHARAS.clear();
	}

}

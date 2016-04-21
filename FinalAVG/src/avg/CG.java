package avg;

import java.awt.Image;
import java.io.Serializable;

import collection.ArrayMap;
import resource.CacheResource;

/**
 * ��Chara������װ��������Ȼ�����AVGScriptȥ��
 * @author czq
 *
 */
public class CG {

	
	/**
	 * ��Ϸ����ͼ
	 */
	private Image backgroundCG;

	public Image getBackgroundCG() {
		return backgroundCG;
	}

	public void setBackgroundCG(Image backgroundCG) {
		this.backgroundCG = backgroundCG;
	}
	/**
	 * ����cg�������б���
	 * @param file
	 * @param role
	 */
	public void addChara(String file, Chara role) {
		CacheResource.ADV_CHARAS.put(file.replaceAll(" ", "").toLowerCase(),
				role);
	}
	
	public void addImage(String file, int x, int y) {
		String keyName = file.replaceAll(" ", "").toLowerCase();
		Chara chara = (Chara) CacheResource.ADV_CHARAS.get(keyName);
		if (chara == null) {
			CacheResource.ADV_CHARAS.put(keyName, new Chara(file, x, y));
		} else {
			chara.setX(x);
			chara.setY(y);
		}
	}

	public Chara removeImage(String file) {
		return (Chara) CacheResource.ADV_CHARAS.remove(file);
	}

	public void clear() {
		CacheResource.ADV_CHARAS.clear();
	}

	public ArrayMap getCharas() {
		return CacheResource.ADV_CHARAS;
	}

}

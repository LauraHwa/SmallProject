//package resource;
//
//import game.IControl;
//
///**
// * ��������Ϸ������һ���µ���
// * @author czq
// *
// */
//public class RunClass {
//
//	private boolean isRun;
//
//	private String className;
//
//	public RunClass(boolean isRun, String name) {
//		this.isRun = isRun;
//		this.className = ClassResource.getClassName(name);
//	}
//	/**
//	 * ����һ���µ���
//	 * @return
//	 */
//	public IControl doInvoke() {
//		try {
//			return (IControl)Class.forName(className).newInstance();
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
//
//	public String getClassName() {
//		return className;
//	}
//
//
//	public boolean isRun() {
//		return isRun;
//	}
//
//	public void setRun(boolean isRun) {
//		this.isRun = isRun;
//	}
//
//}

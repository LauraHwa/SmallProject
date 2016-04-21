package game;



import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * ��Ϸ�������ӿ�
 * @author czq
 *
 */
public interface IControl extends MouseListener, MouseMotionListener,
		KeyListener {

	public abstract void draw(final Graphics g);

	public abstract void next();

	public abstract void setFrame(final int i);

}

package avg;

import java.awt.Color;
import java.awt.Graphics;

import game.GraphicsUtils;
import resource.BaleResource;
import resource.LSystem;

/**
 * ��ʾ�ı�
 * @author czq zsq
 *
 */
public class MessagePrint {

	private char[] showMessages = new char[] { '\0' };
	/**
	 * ����Ĭ����ɫ
	 */
	private Color fontColor = Color.WHITE;
	
	private int interceptMaxString = 0;

	private int interceptCount = 0;

	private int messageSize =LSystem.messageSize;
	/**
	 * Ҫ��ʾ����Ϣ
	 */
	private String messages;

	private int messageCount;
	/**
	 * �Ƿ������Ϣ����
	 */
	private boolean onComplete;

	private int next;

	private StringBuffer messageBuffer = new StringBuffer(messageSize);
	/**
	 * �ı���ʾ�ĳ�ʼx����
	 */
	private int messageLeft  = LSystem.messageLeft;
	/**
	 * �ı���ʾ�ĳ�ʼy����
	 */
	private int messageTop  = LSystem.messageTop;
	/**
	 * �������˿�ʼ��λ��
	 */
	private int nowLeft;
	/**
	 * ������ɫ
	 */
	private Color defaultColor = LSystem.defaultColor;
	
	public MessagePrint() {
		
	}
	
	
	/**
	 * ����Ҫ��ʾ����Ϣ
	 * @param context
	 */
	public void setMessage(String context) {
		this.messages = context;
		this.next = context.length();
		this.onComplete = false;
		this.messageCount = 0;
		this.messageBuffer.delete(0, messageBuffer.length());
	}

	public String getMessage() {
		return messages;
	}
	/**
	 * ������ɫ
	 * @param flagName
	 * @return
	 */
	public static Color getColor(char flagName) {
		if ('r' == flagName || 'R' == flagName) {
			return Color.red;
		}
		if ('b' == flagName || 'B' == flagName) {
			return Color.black;
		}
		if ('l' == flagName || 'L' == flagName) {
			return Color.blue;
		}
		if ('g' == flagName || 'G' == flagName) {
			return Color.green;
		}
		if ('o' == flagName || 'O' == flagName) {
			return Color.orange;
		}
		if ('y' == flagName || 'Y' == flagName) {
			return Color.yellow;
		} else {
			return null;
		}
	}
	/**
	 *  ���������֣��ı���ɫ����<r����/> 
	 * @param g
	 */
	public void draw(Graphics g) {
		int fontSize = g.getFont().getSize();
		//�����м���
		int fontHeight = g.getFontMetrics().getHeight();
		/**
		 * �ı���ʾ����Ի�����ߵ�λ�ü��㡣
		 */
		nowLeft = ((LSystem.WIDTH - messageLeft) / 8 - (fontSize * (messageSize-14)) / 8) ;//messageSize-14=26
		int size = showMessages.length;
		int index = 0;
		int offset = 0;
		boolean newLine = false;
		g.setColor(defaultColor);
		for (int i = 0; i < size; i++) {		
			if (interceptCount < interceptMaxString) {
				interceptCount++;
				continue;
			} else {
				interceptMaxString = 0;
				interceptCount = 0;
			}//��ȡ�����з�ʱ
			if (showMessages[i] == '\n') {
				index = 0;
				offset++;
				newLine = true;
				//�����������ʾ��һ����ɫ�Ļ�
			} else if (showMessages[i] == '<') {
				Color color = getColor(showMessages[i < size - 1 ? i + 1 : i]);
				if (color != null) {
					interceptMaxString = 1;
					fontColor = color;
					g.setColor(fontColor);
				}
				continue;
			} else if (showMessages[i] == '/') {
				if (showMessages[i < size - 1 ? i + 1 : i] == '>') {
					interceptMaxString = 1;
					fontColor = defaultColor ;
					g.setColor(fontColor);
				}
				continue;
			} else if (index > messageSize) {
				index = 0;
				offset++;
				newLine = false;
			}
			int fontLeft = nowLeft + (fontSize * index);
			if (i != size - 1) {
				g.drawString(String.valueOf(showMessages[i]), messageLeft
						+ fontLeft, (offset * fontHeight) + messageTop);
			} else if (!newLine) {
				g.drawImage(BaleResource.creeseIcon, messageLeft + fontLeft,
						(offset * fontHeight) + messageTop, null);
			}
			index++;
		}
		if (messageCount == next) {
			onComplete = true;
		}
		if (!onComplete) {
			if (messageBuffer.length() > 1) {
				messageBuffer.delete(messageBuffer.length() - 1, messageBuffer
						.length());
			}
			GraphicsUtils.wait(20);
		}

	}

	public boolean next() {
		if (!onComplete) {
			char charMessage = '\0';
			if (messageCount == next) {
				onComplete = true;
				return false;
			}
			charMessage = messages.charAt(messageCount);
			messageBuffer.append(charMessage);
			messageBuffer.append("_");
			showMessages = messageBuffer.toString().toCharArray();
			messageCount++;
		} else {
			return false;
		}
		return true;
	}

	public int getMessageLeft() {
		return messageLeft;
	}

	public void setMessageLeft(int messageLeft) {
		this.messageLeft = messageLeft;
	}

	public int getMessageTop() {
		return messageTop;
	}

	public void setMessageTop(int messageTop) {
		this.messageTop = messageTop;
	}
}
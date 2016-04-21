package command;

import java.util.Random;

/**
 * �涨�ű���һЩ���ʽ��������
 * 
 * @author czq
 *
 */
public interface Expression {

	/**
	 * ȫ�������
	 */
	Random GLOBAL_RAND = new Random();
	/**
	 * ��ǰѡ����
	 */
	String V_SELECT_KEY = "SELECT";

	/**
	 * ������
	 */
	String BRACKET_LEFT_TAG = "(";

	/**
	 * ������
	 */
	String BRACKET_RIGHT_TAG = ")";

	/**
	 * �ڲ�����ο�ʼ���
	 */
	String BEGIN_TAG = "begin";

	/**
	 * �ڲ�����ν������
	 */
	String END_TAG = "FuncEnd";

	/**
	 * ����ε��ñ��
	 */
	String CALL_TAG = "call";

	/**
	 * ����ˢ�±��
	 */
	String RESET_CACHE_TAG = "reset";

	/**
	 * �ۼ�����ѡ��
	 */
	String IN_TAG = "in";

	/**
	 * �ۼ����ѡ��
	 */
	String OUT_TAG = "out";

	/**
	 * ���ѡ����
	 */
	String SELECTS_TAG = "selects";

	/**
	 * ѡ����
	 */
	String PRINT_TAG = "print";

	/**
	 * ��������
	 */
	String RAND_TAG = "rand";

	/**
	 * ���ñ������
	 */
	String SET_TAG = "set";

	/**
	 * �����ڲ��ű����
	 */
	String INCLUDE_TAG = "has";

	/**
	 * �жϱ��
	 */
	String IF_TAG = "if";

	/**
	 * �жϽ������
	 */
	String IF_END_TAG = "endif";

	/**
	 * �ж�ת�۱��
	 */
	String ELSE_TAG = "else";

	/**
	 * ע�ͱ��
	 */
	String FLAG_L_TAG = "//";
	/**
	 * ע�ͱ��
	 */
	String FLAG_LS_B_TAG = "/*";
	/**
	 * ע�ͱ��
	 */
	String FLAG_LS_E_TAG = "*/";

	/**
	 * �ָ��ñ�Ƿ�
	 */
	String FLAG = "@";

	/**
	 * �ָ��ñ�Ƿ�
	 */
	char FLAG_CHAR = FLAG.toCharArray()[0];

}

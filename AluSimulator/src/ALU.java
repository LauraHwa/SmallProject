/**
 * 
 * @author czq ����ǿ ѧ��141250017
 *
 */
public class ALU {
	NewMultipler test = new NewMultipler();
	// 1
	/**
	 *  �÷�������ģ���������������������㣬����������Ϊ�����򸡵������������Ͱ����Ӽ��˳���
	 *  ��������������Ϊ����ʱ������������ʾΪ32λ����������������Ӧ���������㷽���������㣻
	 *  ������һ��������Ϊ������ʱ������32λ��IEEE754��ʾ����������������Ӧ�ĸ��������㷽���������㡣
	 * @param formula �ַ�����ʾ�ļ��㹫ʽ��
	 * 					����ʽΪ��������������������=�����硰5+(-7)=����
	 * 				���㹫ʽ�����ҽ���2��������������ʮ���Ʊ�ʾ����������Ϊ����ʱ������������ס��
	 *			 	���ҽ���+-* /�е�1����Ϊ����������=�Ž�����
	 * @return ����������ֵ������Ǹ����������Ϊ��-���������������0������Ҫ����λ��
	 */
	public String calculation(String formula) {
		String operand1;
		String operand2;
		int compute = 0;
		
		if(formula.contains("+")){
			compute = 1;
			operand1 = formula.substring(0, formula.indexOf("+"));
			operand2 = formula.substring(formula.indexOf("+") + 1);
		}else if(formula.contains("*")){
			compute = 3;
			operand1 = formula.substring(0, formula.indexOf("*"));
			operand2 = formula.substring(formula.indexOf("*") + 1);
		}else if(formula.contains("/")){
			compute=4;
			operand1 = formula.substring(0, formula.indexOf("/"));
			operand2 = formula.substring(formula.indexOf("/") + 1);
		}else{
			compute = 2;
			if(formula.startsWith("(")){
				operand1 = formula.substring(0, formula.indexOf(")") + 1);
				operand2 = formula.substring(formula.indexOf(")") + 2);
			}else{
				operand1 = formula.substring(0, formula.indexOf("-"));
				operand2 = formula.substring(formula.indexOf("-") + 1);
			}
		}
		operand2 = operand2.substring(0, operand2.length() - 1);
		if(operand1.contains("(")){
			operand1 = operand1.substring(1, operand1.indexOf(")"));
		}
		if(operand2.contains(")")){
			operand2 = operand2.substring(1, operand2.indexOf(")"));
		}
		
		if(operand1.contains(".") || operand2.contains(".")){
			if(compute == 1){
				return floatTrueValue(floatAddition(ieee754(operand1,32), ieee754(operand2, 32), 23, 8, 8).substring(0, 32), 23, 8);
			}else if(compute == 2){
				return floatTrueValue(floatSubtraction(ieee754(operand1,32), ieee754(operand2, 32), 23, 8, 8).substring(0, 32), 23, 8);
			}else if(compute == 3){
				return floatTrueValue(floatMultiplication(ieee754(operand1,32), ieee754(operand2, 32), 23, 8).substring(0, 32), 23, 8);
			}else if(compute == 4){
				return floatTrueValue(floatDivision(ieee754(operand1,32), ieee754(operand2, 32), 23, 8).substring(0, 32), 23, 8);
				
			}
			
			
			
		} else{
			if(compute == 1){
				return integerTrueValue(integerAddition(integerRepresentation(operand1, 32), integerRepresentation(operand2, 32), '0', 32).substring(0, 32));
			}else if(compute == 2){
				return integerTrueValue(integerSubtraction(integerRepresentation(operand1, 32), integerRepresentation(operand2, 32), 32).substring(0, 32));
			}else if(compute == 3){
				return integerTrueValue(test.newMultipler(integerRepresentation(operand1, 32), integerRepresentation(operand2, 32), 32));
			}else if(compute == 4){
				String temp = integerDivision(integerRepresentation(operand1, 32), integerRepresentation(operand2, 32), 32);
				if(temp == "NaN" || temp =="0"){
					return temp;
				}
				return integerTrueValue(temp.substring(0, 32));
			}
		}
		
		
		return null;
	}

	// 2
	/**
	 * ����ʮ���������Ĳ����ʾ
	 * 
	 * @param number
	 * @param length
	 *            ��ʾ��λ��
	 * @return
	 */
	public String integerRepresentation(String number, int length) {
		boolean isNeagtive = false;
		if (number.startsWith("-")) {
			isNeagtive = true;
			number = number.substring(1);
		}
		long temp = Long.parseLong(number);
		StringBuffer temp2 = new StringBuffer(length);
		// �������ֵ�Ķ����Ʊ�ʾ
		for (;;) {
			if (temp / 2 == 0) {
				temp2.append(Long.toString(temp % 2));
				break;
			} else {
				temp2.append(Long.toString(temp % 2));
				temp = temp / 2;
			}
		}
		// ����0���涨λ��
		for (int i = temp2.length(); i < length; i++) {
			temp2.append("0");
		}
		// ����
		temp2.reverse();
		// �Ը�������
		if (isNeagtive) {
			String result = computeCompletment(temp2.toString());
			temp2.replace(0, length, result);
		}

		return temp2.toString();
	}

	/**
	 * ������������
	 * 
	 * @return
	 */
	private String computeCompletment(String integer) {
		StringBuffer result = new StringBuffer(integer);
		// ����ȡ����һ���һ��1����֮������ֲ����ܵ�Ӱ��
		if (integer.indexOf("1") != -1) {
			int temp3 = result.lastIndexOf("1");
			for (int i = 0; i < temp3; i++) {
				if (result.charAt(i) == '1') {
					result.setCharAt(i, '0');
				} else {
					result.setCharAt(i, '1');
				}
			}

		}
		return result.toString();
	}

	// 3
	/**
	 * ����ʮ���Ƹ������ĸ�������ʾ��ʽ
	 * 
	 * @param number
	 * @param sLength
	 *            ����8
	 * @param eLength
	 *            ����8
	 * @return
	 */
	public String floatRepresentation(String number, int sLength, int eLength) {
		boolean isNegative = false;
		StringBuffer result = new StringBuffer(64);
		StringBuffer decimarParts = new StringBuffer(sLength);
		StringBuffer exponent = new StringBuffer(eLength);
		Long integerPart;
		double decimarPart;
		// С�����ƶ�λ��
		int pointMove = 0;
		if (number.startsWith("-")) {
			isNegative = true;
			number = number.substring(1);
		}
		
		if(number.contains(".")){
		 integerPart = Long
				.parseLong(number.substring(0, number.indexOf(".")));
		 decimarPart = Double.parseDouble("0"
				+ number.substring(number.indexOf(".")));
		}else{
			integerPart = Long.parseLong(number);
			decimarPart = 0.0;
		}
		// ����������ֶ����Ʊ�ʾ
		if (integerPart != 0) {
			for (;;) {
				if (integerPart / 2 == 0) {
					result.append(Long.toString(integerPart % 2));
					break;
				} else {
					result.append(Long.toString(integerPart % 2));
					integerPart = integerPart / 2;
				}
			}
			result.reverse();
			result.delete(0, 1);
			// ���С�����ƶ���λ��
			pointMove = result.length();
		}

		// ���С�����ֵĶ����Ʊ�ʾ��������decimarparts��
		for (;;) {
			if (decimarParts.length() == sLength*10) {
				break;
			} else {
				if ((decimarPart = decimarPart * 2) >= 1) {
					decimarPart = decimarPart - 1;
					decimarParts.append("1");
				} else {
					decimarParts.append("0");
				}
			}

		}
		
		// �����������Ϊ0����Ϊ����
		if (integerPart == 0) {
//			if(decimarParts.indexOf("1") == -1){
//				
//			}
			pointMove = -(decimarParts.indexOf("1") + 1);
			result.delete(0, result.length());
			result.append(decimarParts.substring(decimarParts.indexOf("1") + 1));
		}
		
		// ��ָ������
		int temp = pointMove +  (int)Math.pow(2, eLength - 1) - 1;
		for (;;) {
			//��Ϊ0
			if(decimarParts.indexOf("1") == -1 && integerPart == 0){
				for (int i = 0; i < eLength; i++) {
					exponent.append('0');
				}
				break;
			}
			//��Ϊ�����
			if (temp >= (int)Math.pow(2, eLength) - 1) {
				for (int i = 0; i < eLength; i++) {
					exponent.append('1');
				}
				break;
			}
			//��Ϊ�ǹ����
			if(temp <=0){
				for (int i = 0; i < eLength; i++) {
					exponent.append('0');
				}
				break;
			}
			if (temp / 2 == 0) {
				exponent.append(Integer.toString(temp % 2));
				break;
			} else {
				exponent.append(Integer.toString(temp % 2));
				temp = temp / 2;
			}

		}
		exponent.reverse();
		while(exponent.length() < eLength){
			exponent.insert(0, '0');
		}
		
		//���Ƿ�����������뷽ʽ
		//����һλ���ȥ
		if (exponent.indexOf("1") == -1) {
			
			result.delete(0, result.length());
			decimarParts.delete(0, 126);
			
		}
//		System.out.println(result);
//		System.out.println(decimarParts);
		result.append(decimarParts.toString());
		result.delete(sLength + 1, result.length());
//		System.out.println(result);
		//����
		if (result.charAt(sLength) == '1') {
			int tempNum = result.lastIndexOf("0");
			if(tempNum != -1){
				result.setCharAt(tempNum, '1');
				for (int i = tempNum + 1; i < result.length(); i++) {
					result.setCharAt(i, '0');
				} 
			}else{
				//ȫΪ1����λ�ӵ�����
				for (int i = 0; i < decimarParts.length(); i++) {
					decimarParts.setCharAt(i, '0');
				}
				int t = result.lastIndexOf("0");
				result.setCharAt(t, '1');
				for (int i = t + 1; i < result.length(); i++) {
					result.setCharAt(i, '0');
				}
				
			}
		}
		
		result.insert(0, exponent.toString());
		if(isNegative){
			result.insert(0, '1');
			
		}else{
			result.insert(0, '0');
		}
		
		return result.substring(0, 1+ sLength + eLength).toString();
	}

	// 4
	/**
	 * ��ʮ���Ƹ�������Ieee754 ��ʾ
	 * 
	 * @param number
	 * @param length
	 *            ��ʾλ����Ϊ32 �� 64 λ
	 * @return
	 */
	public String ieee754(String number, int length) {
		if (length == 32) {
			return floatRepresentation(number, 23, 8);
		} else if (length == 64) {
			return floatRepresentation(number, 52, 11);
		}
		return null;
	}

	// 5
	/**
	 * ������������ת��Ϊʮ��������
	 * 
	 * @param operand
	 * @return
	 */
	public String integerTrueValue(String operand) {
		int length = operand.length();
		char[] split = operand.toCharArray();
		long result = 0;
		//û�취����Ҫ�����ֻ��������
		if(split[0] != '0'){
			if (length < 64) {
				result = -(long) ((split[0] - '0') * Math.pow(2, length - 1));
			} else {
				result = Long.MIN_VALUE;
			}
		}
		
		for (int i = 1; i < length; i++) {
			result = result
					+ (long) ((split[i] - '0') * Math.pow(2, length - i - 1));

		}
		
		return Long.toString(result);
	}

	// 6
	/**
	 * �������Ƹ�����ת��Ϊʮ����
	 * 
	 * @param operand
	 * @param sLength
	 *            ��Ч��λ
	 * @param eLength
	 *            ָ��λ
	 * @return
	 */
	public String floatTrueValue(String operand, int sLength, int eLength) {
		boolean isPostive = operand.startsWith("0");
		// ָ���Ķ������ַ���
		String indexStr = operand.substring(1, 1 + eLength);
		// ��Ч���Ķ������ַ���
		String effStr = operand.substring(1 + eLength);
		double result = 0;
		// ��Ϊ���޷��ŵģ�����ǰ��Ӹ�0��ʾ���
		int index = Integer.parseInt(integerTrueValue("0" + indexStr));
		// ��Ч������
		char[] effChars = effStr.toCharArray();
		if (index == 0 && effStr.indexOf("1") == -1) {
			return "0";
		}
		if (index == 255) {
			if (effStr.indexOf("1") == -1) {
				if (isPostive)
					return "+Inf";
				else
					return "-Inf";
			} else {
				return "NaN";
			}
			
		}
		// ���ڷǹ����
		if (index == 0 && effStr.indexOf("1") != -1) {
			for (int i = 0; i < sLength; i++) {
				result = result + (effChars[i] - '0')
						* Math.pow(2, index - 127 - i);
			}
			// ���ڹ����
		} else {
			result = Math.pow(2, index - 127);
			for (int i = 0; i < sLength; i++) {
				result = result + (effChars[i] - '0')
						* Math.pow(2, index - 128 - i);
			}
		}
		if(!isPostive){
			result = -result;
		}
		return Double.toString(result);
	}

	// 7
	/**
	 * �Զ���������λȡ��
	 * 
	 * @param operand
	 * @return
	 */
	public String negation(String operand) {
		char[] temp = operand.toCharArray();
		StringBuffer sbf = new StringBuffer(operand.length());
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == '0') {
				sbf.append("1");
			} else {
				sbf.append("0");
			}
		}
		return sbf.toString();
	}

	// 8
	/**
	 * ģ�����Ʋ���
	 * 
	 * @param operand
	 * @param n
	 * @return
	 */
	public String leftShift(String operand, int n) {
		StringBuffer sbf = new StringBuffer(operand.length());
		sbf.append(operand.substring(n));
		for (int i = 0; i < n; i++) {
			sbf.append("0");
		}
		return sbf.toString();
	}

	// 9
	/**
	 * ģ���������Ʋ���
	 * 
	 * @param operand
	 * @param n
	 * @return
	 */
	public String rightAriShift(String operand, int n) {
		StringBuffer sbf = new StringBuffer(operand.length());
		sbf.append(operand.substring(0, operand.length() - n));
		for (int i = 0; i < n; i++) {
			if (operand.startsWith("1"))
				sbf.insert(0, '1');
			else
				sbf.insert(0, '0');
		}
		return sbf.toString();
	}

	// 10
	/**
	 * �߼����Ʋ���
	 * 
	 * @param operand
	 * @param n
	 * @return
	 */
	public String rightLogShift(String operand, int n) {
		StringBuffer sbf = new StringBuffer(operand.length());
		sbf.append(operand.substring(0, operand.length() - n));
		for (int i = 0; i < n; i++) {
			sbf.insert(0, '0');
		}
		return sbf.toString();
	}

	// 11
	/**
	 * ȫ�ӷ���
	 * 
	 * @param x
	 * @param y
	 * @param c
	 * @return ��һλΪ�ͣ��ڶ�λΪ��λ
	 */
	public String fullAdder(char x, char y, char c) {
		String result = "";
		result = Integer.toString(((x - '0') + (y - '0') + (c - '0')) % 2);

		if ((x - '0') + (y - '0') + (c - '0') - 2 >= 0) {
			result += "1";
		} else {
			result += "0";
		}

		return result;
	}

	// 12
	/**
	 * ģ���λ���н�λ�ӷ���
	 * 
	 * @param operand1
	 *            ������1
	 * @param operand2
	 *            ������2
	 * @param c
	 *            ��ʼ��λ
	 * @return
	 */
	public String claAdder(String operand1, String operand2, char c) {

		char[] x = (new StringBuffer(operand1)).reverse().toString()
				.toCharArray();
		char[] y = (new StringBuffer(operand2)).reverse().toString()
				.toCharArray();
		char[] cs = new char[9];
		StringBuffer result = new StringBuffer(9);

		cs[0] = c;
		//ʹ�õݹ���������н�λ
		for (int i = 0; i < (cs.length - 1); i++) {
			cs[i + 1] = computeC(x, y, c, i);
		}
		for (int i = 0; i < 8; i++) {
			result.append(fullAdder(x[i], y[i], cs[i]).substring(0, 1));
		}
		result.reverse();
		result.append(cs[8]);

		return result.toString();
	}

	/**
	 * ģ�����н�λ�ӷ��������λ��ʹ�ð�λ�����ʱ��С�ġ�������˳����ڼӼ�
	 * 
	 * @param x
	 * @param y
	 * @param c
	 * @param i
	 * @return
	 */
	private char computeC(char[] x, char[] y, char c, int i) {
		if (i == 0) {
			return (char) (((x[0] - '0') & (y[0] - '0') | ((x[0] - '0') | (y[0] - '0'))
					& (c - '0')) + '0');
		} else {
			return (char) ((((x[i] - '0') | (y[i] - '0'))
					& (computeC(x, y, c, i - 1) - '0') | ((x[i] - '0') & (y[i] - '0'))) + '0');
		}
	}

	// 13
	/**
	 * ģ�ⲿ�����н�λ�ӷ���
	 * 
	 * @param operand1
	 * @param operand2
	 * @param c
	 *            ��ʼ��λ
	 * @param length
	 *            �Ĵ������� �����ڵ���8
	 * @return ������+�Ƿ����
	 */
	public String integerAddition(String operand1, String operand2, char c,
			int length) {
		int save = length;
		StringBuffer result = new StringBuffer(length + 1);
		// ���Ƚ��Ĵ��������������Ƚ��й淶
		while (length % 8 != 0) {
			length++;
		}
		if (operand1.length() < length) {
			if (operand1.startsWith("1")) {
				while (operand1.length() < length)
					operand1 = ("1" + operand1).intern();
			} else {
				while (operand1.length() < length)
					operand1 = ("0" + operand1).intern();
			}

		}
		if (operand2.length() < length) {
			if (operand2.startsWith("1")) {
				while (operand2.length() < length)
					operand2 = ("1" + operand2).intern();
			} else {
				while (operand2.length() < length)
					operand2 = ("0" + operand2).intern();
			}

		}
		// ÿ��ȡ��λ�������㣬�����ý���洢������λ������һ������
		String temp;
		for (int i = 0; i < length / 8; i++) {
			temp = claAdder(
					operand1.substring(length - 8 * (i + 1), length - 8 * i),
					operand2.substring(length - 8 * (i + 1), length - 8 * i), c);
			result.insert(0, temp.substring(0, 8));
			c = temp.charAt(8);
		}
		
		//֮ǰ�淶������Ҫ���´���
		result.delete(0, length - save);
		
		// �ж��Ƿ����
		if (operand1.startsWith("0") && operand2.startsWith("0")
				&& (result.charAt(0) == '1') || operand1.startsWith("1")
				&& operand2.startsWith("1") && (result.charAt(0) == '0'))
			result.append('1');
		else
			result.append('0');

		return result.toString();
	}

	// 14
	/**
	 * �÷�������ģ�������Ҫ�����integerAdditionʵ��
	 * @param operand1 ������ �����ʾ
	 * @param operand2 ���� �����ʾ
	 * @param length ��Ų������ļĴ������ȡ�length��С�ڲ��������ȣ���ĳ���������ĳ���С��lengthʱ�����ڸ�λ������λ
	 * @return ����Ϊlength+1 ���ַ������������ҡ�ǰlengthλΪ�����������һλΪ�Ƿ����
	 */
	public String integerSubtraction(String operand1, String operand2,
			int length) {
		return integerAddition(operand1, negation(operand2), '1', length);
	}

	// 15
	/**
	 * �÷�������ģ��Booth�˷���Ҫ�����integerAddition������integerSubtraction������ʵ�֡�
	 * @param operand1 ���������ò����ʾ
	 * @param operand2 �������ò����ʾ��
	 * @param length ��Ų������ļĴ����ĳ��ȡ�length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ��
	 * @return ����Ϊlength*2��Ϊ���������ò����ʾ��
	 */
	public String integerMultiplication(String operand1, String operand2,
			int length) {
		StringBuffer result = new StringBuffer();
		// ��ʼ��
		for (int i = 0; i < length * 2 + 1; i++) {
			result.append('0');
		}
		if (operand1.length() < length) {
			if (operand1.startsWith("1")) {
				while (operand1.length() < length)
					operand1 = ("1" + operand1).intern();
			} else {
				while (operand1.length() < length)
					operand1 = ("0" + operand1).intern();
			}

		}
		if (operand2.length() < length) {
			if (operand2.startsWith("1")) {
				while (operand2.length() < length)
					operand2 = ("1" + operand2).intern();
			} else {
				while (operand2.length() < length)
					operand2 = ("0" + operand2).intern();
			}

		}
		// ģ��N�����Ƽ��Ӽ�����
		char[] y = (operand2 + "0").toCharArray();
		for (int i = 0; i < length; i++) {
			int j = (y[length - i] - y[length - i - 1]);
			if (j == 1) {
				result.replace(
						0,
						length,
						integerAddition(operand1, result.substring(0, length),
								'0', length).substring(0, length));
			}
			if (j == -1) {
				result.replace(
						0,
						length,
						integerSubtraction(result.substring(0, length),
								operand1, length).substring(0, length));
			}

			// ������������
			if (result.charAt(0) == '0')
				result.insert(0, '0');
			else
				result.insert(0, '1');
		}

		return result.toString().substring(0, length * 2);
	}

	// 16
	/**
	 * ģ�������������ָ���������
	 * 
	 * @param operand1
	 * @param operand2
	 * @param length
	 * @return length*2λ ǰ����Ϊ�� �󲿷�Ϊ����
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		StringBuffer result = new StringBuffer(length * 2);
		if (operand2.indexOf("1") == -1 ) {
			return "NaN";
		}
		if (operand1.indexOf("1") == -1) {
			return "0";
		}
		// ��ʼ��
		if (operand1.length() < length) {
			if (operand1.startsWith("1")) {
				while (operand1.length() < length)
					operand1 = ("1" + operand1).intern();
			} else {
				while (operand1.length() < length)
					operand1 = ("0" + operand1).intern();
			}

		}
		if (operand2.length() < length) {
			if (operand2.startsWith("1")) {
				while (operand2.length() < length)
					operand2 = ("1" + operand2).intern();
			} else {
				while (operand2.length() < length)
					operand2 = ("0" + operand2).intern();
			}

		}
		// ��������2Nλ�ı���������result��
		if (operand1.startsWith("1")) {
			for (int i = 0; i < length; i++) {
				result.append('1');
			}
		} else {
			for (int i = 0; i < length; i++) {
				result.append('0');
			}
		}
		result.append(operand1);

		String temp;
		String tempresult;
		for (int i = 0; i < length; i++) {
			// ģ������
			result.deleteCharAt(0);
			// ����ǰlengthλ�Թ��ָ�
			temp = result.toString().substring(0, length);
			// ������������������ж��ӻ��Ǽ�
			if (((operand2.charAt(0) - '0') ^ (result.charAt(0) - '0')) == 1) {
				// ���������鷳�Ķ�����Ϊ�˴���λ��С�������ӷ�λ�������⣬��ӷ���Բ���8λ�Ľ�����չ����ȡ���ʱ�Ӻ���ȡ�𣬻�Ҫ�������λ
				tempresult = integerAddition(
						result.toString().substring(0, length), operand2, '0',
						length).substring(0, operand1.length());
				
				if (tempresult.charAt(0) == temp.charAt(0)) {
					result.append('1');
					result.replace(0, length, tempresult);
				} else {
					result.append('0');
					result.replace(0, length, temp);
				}
			} else {
				tempresult = integerSubtraction(
						result.toString().substring(0, length), operand2,
						length).substring(0, operand1.length());
				
				if (tempresult.charAt(0) == temp.charAt(0)) {
					result.append('1');
					result.replace(0, length, tempresult);
				} else {
					result.append('0');
					result.replace(0, length, temp);
				}
			}
		}
		// ��������뱻�������Ų�ͬ��ת���̵ķ���
		if (((operand1.charAt(0) - '0') ^ (operand2.charAt(0) - '0')) == 1) {
			result.replace(length, length * 2, computeCompletment(result
					.toString().substring(length, length * 2)));
		}
		// ��ת�̺�����λ��
		temp = result.toString().substring(0, length);
		result.replace(0, length,
				result.toString().substring(length, length * 2));
		result.replace(length, length * 2, temp);

		return result.toString();
	}

	// 17
	/**
	 * �÷����� ��ģ�⸡�����ļӷ���Ҫ�����integerAddition��integersubtractionʵ��
	 * 
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 *            β�����ȣ�ȡֵ���ڵ���8
	 * @param eLength
	 *            ָ�����ȣ�ȡֵ���ڵ���8
	 * @param gLength
	 *            ����λ����
	 * @return ����Ϊ1+sLength+eLength+1���ַ������������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ���
	 *         ���1λΪ�Ƿ�������������Ϊ1�������Ϊ0��������þͽ����롣
	 */
	public String floatAddition(String operand1, String operand2, int sLength,
			int eLength, int gLength) {
		if(!operand1.substring(1).contains("1")){
			return operand2;
		}else if (!operand2.substring(1).contains("1")){
			return operand1;
		}
		boolean isNegative = false;
		int isOverFlow = 0;
		int e1 = computeInteger(operand1.substring(1,eLength + 1));
		int e2 = computeInteger(operand2.substring(1,eLength + 1));
		//����λ
		String s1 = "1"+operand1.substring(eLength +1);
		String s2 = "1"+operand2.substring(eLength +1);
		//����С���� ����β��,������λ��������λ�����������ʱ����λû������
		if(e1 != e2){
			if(e1 > e2){
				for (int i = 0; i < e1 - e2; i++) {
					s2 = "0" + s2 ;
				}
				//��������λ����ȥ�����,������0
				if(s2.length() >= sLength + gLength + 1){
					s2 = s2.substring(0, sLength+gLength +1);
				}
				
				if(!s2.substring(0, sLength + 1).contains("1")){
					return operand1;
				}
				e2=e1;
			}else{
				for (int i = 0; i < e2 - e1; i++) {
					s1 ="0" +s1;
				}
				if(s1.length() >= sLength + gLength+1){
					s1 = s1.substring(0, sLength+gLength+1);
				}
				if(!s1.substring(0, sLength + 1).contains("1")){
					return operand2;
				}
				e1=e2;
			}
			
		}
		//����������λ
		while(s2.length() < sLength + gLength + 1){
				s2 = s2 + "0";
		}
		while(s1.length() < sLength + gLength + 1){
			s1 = s1 + "0";
		}
		
		
//		System.out.println(gLength);
		//������β�����
		String result;
		//�ж���λ�ķ���λ�Ƿ���ͬ��ͬ��
		if(operand1.charAt(0) == operand2.charAt(0)){
			//��һλȷ���Ƿ��н�λ
			result = integerAddition("0" +s1, "0"+s2, '0', sLength + gLength + 1 + 1);
			System.out.println("s"+result);
			//������
			if(result.charAt(0) == '1'){
				result = "1" + result;
				e1++;
				if(e1 >(int)Math.pow(2, eLength - 1) - 1){
					isOverFlow = 1;
				}
			}
			//ȥ���ӵ���λ
			result = result.substring(1);
			if(operand1.charAt(0)=='0'){
				isNegative = false;
			}else{
				isNegative = true;
			}
			//������
		}else{
			result = integerAddition("0" + s1, "0" + computeCompletment(s2), '0', sLength + gLength + 1 + 1);
			//��λ����н�λ
			if(result.charAt(0) == '1'){
				isNegative = (operand1.charAt(0) == '1')?true:false;
				result = result.substring(1);
			}else{
				isNegative = (operand1.charAt(0)=='0')? true : false;
				result = result.substring(1);
				result = computeCompletment(result);
				
			}
			
		}
		result = result.substring(0, sLength + gLength + 1);
		//β��Ϊ0
		if(result.substring(0,sLength).indexOf("1") == -1){
			e1 = 0;
		}
		

		
		//��񻯽��
		int sub = e1 - result.indexOf("1");
		//��������
		if(sub <= 0){
			e1 = 0;
			if(sub == 0){
				result = "0" + result;
			}else{
				result = result.substring(Math.abs(sub) - 1);
			}
		}else{
			e1 = sub;
			result = result.substring(result.indexOf("1") + 1);
		}
		
		while(result.length() < sLength + gLength){
			result += "0";
		}
		
		//����
		if (gLength != 0) {
			if (result.charAt(sLength) == '1') {
				int temp = result.substring(0, sLength).lastIndexOf("0");
				StringBuffer sbf = new StringBuffer(result);
				sbf.setCharAt(temp, '1');
				for (int i = temp + 1; i < sbf.length(); i++) {
					sbf.setCharAt(i, '0');
				}
				result = sbf.toString();
			}
			result = result.substring(0, sLength);
//			System.out.println(result.length());
		}
		
		String exponent = integerRepresentation(Integer.toString(e1), eLength);
		if(isNegative){
			return "1" + exponent + result + Integer.toString(isOverFlow);
		}else{
			return "0" + exponent + result + Integer.toString(isOverFlow);
		}
		
	}
	
	// 18
	/**
	 * ģ�⸡��������
	 * @param operand1
	 * @param operand2
	 * @param sLength
	 * @param eLength
	 * @param gLength
	 * @return
	 */
	public String floatSubtraction(String operand1, String operand2,
			int sLength, int eLength, int gLength) {
		
		operand2 = (operand2.charAt(0)=='0'?"1":"0") + operand2.substring(1);
		
		return floatAddition(operand1, operand2, sLength, eLength, gLength);
	}

	// 19
	/**
	 * �÷�������ģ�⸡�����ĳ˷���Ҫ�����integerAddition��integerSubtraction�ȷ�����ʵ�֡�
	 * @param operand1 ���������ö����Ʊ�ʾ��
	 * @param operand2 �������ö����Ʊ�ʾ��
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ���8��
	 * @param eLengthָ���ĳ��ȣ�ȡֵ���ڵ���8��
	 * @return 1+sLength+eLength Ϊ�����ö����Ʊ�ʾ���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ���������þͽ����롣
	 */
	public String floatMultiplication(String operand1, String operand2,
			int sLength, int eLength) {
		//�κ�һ��Ϊ0 ����0
		if(!operand1.substring(1).contains("1")){
			return "0"+operand1.substring(1);
		}
		if (!operand2.substring(1).contains("1")){
			return "0"+operand2.substring(1);
		}
		int e1 = computeInteger(operand1.substring(1,eLength + 1));
		int e2 = computeInteger(operand2.substring(1,eLength + 1));
		int e = e1 + e2 - (int)Math.pow(2, eLength - 1) - 1;
		if(e>= (int)Math.pow(2, eLength)){
			return "exponent over flow";
		}else if(e < 0){
			return "exponent under flow";
		}
		
		//β�����
		String s1 = "1"+operand1.substring(eLength + 1);
		String s2 = "1"+operand2.substring(1+ eLength);
		StringBuffer result = new StringBuffer((sLength+ 1) *2);
		for (int i = 0; i < sLength + 1; i++) {
			result.append("0");
		}
		for (int i = 0; i < sLength + 1; i++) {
			if(s2.charAt(s2.length() - i - 1) == '1'){
				//��ʱ�����֮����������˴�ʱ��λ�������1
				String temp = integerAddition("0" +result.substring(0, sLength+1), "0"+s1, '0', sLength + 1);
				
				temp = temp.substring(0, sLength + 1);
				result.delete(0, sLength + 1);
				result.insert(0, temp);
			}else{
				result.insert(0, '0');
			}
			
			
		}
		
		
		//���
		if(result.indexOf("1") != -1){
			e = e + result.indexOf("1") + 1;
			result.delete(0, result.indexOf("1") +  1);
		}
		//����                
		if(result.charAt(sLength) == '1'){
			int temp = result.substring(0, sLength).lastIndexOf("0");
			if(temp != -1){
				result.setCharAt(temp, '1');
				for (int i = temp + 1; i < result.length(); i++) {
					result.setCharAt(i, '0');
				}
			}else{
				e++;
				for (int i = 0; i < result.length(); i++) {
					result.setCharAt(i, '0');
				}
			}
			
			
		}
		//λ����������
		while(result.length() < sLength){
			result.append('0');
		}
		
		String exponent = integerRepresentation(Integer.toString(e), eLength);
		
		if(operand1.charAt(0) == operand2.charAt(0)){
			return "0" + exponent + result.substring(0, sLength);
		}else{
			return "1" + exponent + result.substring(0, sLength);
		}
	}
//	public String floatMultiplication(String operand1, String operand2,
//			int sLength, int eLength) {
//		//�κ�һ��Ϊ0 ����0
//				if(!operand1.substring(1).contains("1")){
//					return "0"+operand1.substring(1);
//				}
//				if (!operand2.substring(1).contains("1")){
//					return "0"+operand2.substring(1);
//				}
//				int e1 = computeInteger(operand1.substring(1,eLength + 1));
//				int e2 = computeInteger(operand2.substring(1,eLength + 1));
//				int e = e1 + e2 - (int)Math.pow(2, eLength - 1) - 1;
//				if(e>= (int)Math.pow(2, eLength)){
//					return "exponent over flow";
//				}else if(e < 0){
//					return "exponent under flow";
//				}
//				
//				//β�����
////				StringBuffer result = new StringBuffer((sLength+ 1) *2);
////				String s1 = "1"+operand1.substring(eLength + 1);
////				String s2 = "1"+operand2.substring(1+ eLength);
////				result = new StringBuffer(test.newMultipler("0" + s1, "0" +s2, sLength + 1 + 1).substring(2) );
////				System.out.println(result.toString());
////				System.out.println(result.length());
//				
//				String s1 = "1"+operand1.substring(eLength + 1);
//				String s2 = "1"+operand2.substring(1+ eLength);
//				StringBuffer result = new StringBuffer((sLength+ 1) *2);
//				for (int i = 0; i < sLength + 1; i++) {
//					result.append("0");
//				}
//				for (int i = 0; i < sLength + 1; i++) {
//					if(s2.charAt(s2.length() - i - 1) == '1'){
//						//��ʱ�����֮����������˴�ʱ��λ�������1
//						String temp = integerAddition("0" +result.substring(0, sLength+1), "0"+s1, '0', sLength + 1);
//						
//						temp = temp.substring(0, sLength + 1);
//						result.delete(0, sLength + 1);
//						result.insert(0, temp);
//					}else{
//						result.insert(0, '0');
//					}
//					
//					
//				}
//				
//				System.out.println(result);
//				System.out.println(result.length());
//				
//				//���
//				if(result.indexOf("1") != -1){
//					e = e + result.indexOf("1") + 1;
//					result.delete(0, result.indexOf("1") +  1);
//				}
//				//����                
//				if(result.charAt(sLength) == '1'){
//					int temp = result.substring(0, sLength).lastIndexOf("0");
//					if(temp != -1){
//						result.setCharAt(temp, '1');
//						for (int i = temp + 1; i < result.length(); i++) {
//							result.setCharAt(i, '0');
//						}
//					}else{
//						e++;
//						for (int i = 0; i < result.length(); i++) {
//							result.setCharAt(i, '0');
//						}
//					}
//					
//					
//				}
//				//λ����������
//				while(result.length() < sLength){
//					result.append('0');
//				}
//				
//				String exponent = integerRepresentation(Integer.toString(e), eLength);
//				
//				if(operand1.charAt(0) == operand2.charAt(0)){
//					return "0" + exponent + result.substring(0, sLength);
//				}else{
//					return "1" + exponent + result.substring(0, sLength);
//				}
//	}
	// 20
	/**
	 * �÷�������ģ�⸡�����Ļָ�����������Ҫ�����integerAddition��integerSubtraction�ȷ�����ʵ�֡�
	 * @param operand1 ���������ò����ʾ��
	 * @param operand2 �������ò����ʾ��
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ���8
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ���8
	 * @return ����ֵ������Ϊ1+sLength+eLength��Ϊ�̣��ö����Ʊ�ʾ���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ���
	 * 			������þͽ�����
	 */
	public String floatDivision(String operand1, String operand2, int sLength,
			int eLength) {
		String exponent;
		if(!operand1.substring(1).contains("1")){
			return "0" + operand1.substring(1);
		}
		if(!operand2.substring(1).contains("1")){
			if(operand1.charAt(0) == operand2.charAt(0)){
				return "01111111100000000000000000000000";
			}else{
				return "11111111100000000000000000000000";
			}
		}
		int e1 = computeInteger(operand1.substring(1,eLength + 1));
		int e2 = computeInteger(operand2.substring(1,eLength + 1));
		int e = e1 - e2 + (int)Math.pow(2, eLength - 1) - 1;
		if(e > (int)Math.pow(2, eLength)){
			return "exponent over flow";
		}else if(e < 0){
			return "exponent under flow";
		}
		/*
		 * �ָ�������
		 */
//		System.out.println(e1 + " " +e2);
//		String s1 = "1" + operand1.substring(1+eLength);
//		String s2 = "1" + operand2.substring(1+ eLength);
//		System.out.println(s1);
//		System.out.println(s2);
//		String result = integerDivision( s1, s2, sLength + 1  ).substring(0,sLength+1 + 1);
//		System.out.println(result.length());
//		System.out.println(result);
		
		StringBuffer result = new StringBuffer();
		String s1 = "1" + operand1.substring(1+eLength);
		String s2 = "1" + operand2.substring(1+ eLength);
		s1 = "0" + s1;
		s2 = "0" + s2;
//		System.out.println(s1);
//		System.out.println(s2);
		//��ʼ��result
		result.append(s1);
		for (int i = 0; i < sLength + 1; i++) {
			result.append('0');
		}
		String temp;
		String tempResult;
		for (int i = 0; i < sLength + 1 + 1; i++) {
			//����ǰNλ���ָ�
			 temp = result.substring(0,sLength + 1 + 1);
			//��0��ʾ���,�������λ
			tempResult = integerSubtraction("0" + temp, "0"+s2, sLength + 1 + 1 + 1).substring(0, sLength + 1 + 1 +1);
//			System.out.println("tempRe:" + tempResult);
//			System.out.println("result:" + result);
			//
			if(tempResult.charAt(0) =='1'){
				result.append('0');
			}else{
				result.replace(0, sLength + 1 + 1, tempResult.substring(1));
				result.append('1');
			}
			//ģ������
			result.deleteCharAt(0);
			
		}
			//������ȥ
			result.delete(0, sLength+1);
			//���
			String quotient = result.substring(0, sLength + 1);
			if(quotient.contains("1")){
				e = e - quotient.indexOf("1");
				//���1�������һ��
				if(quotient.indexOf("1") != quotient.length() - 1){
					quotient = quotient.substring(quotient.indexOf("1") + 1);
				}else{
					quotient = "0";
				}
				while(quotient.length() < sLength){
					quotient += "0";
				}
			}else{
				
			}
			//����
			
			exponent = integerRepresentation(Integer.toString(e), eLength);
			System.out.println(quotient);
			if(operand1.charAt(0) == operand2.charAt(0)){
				return "0" + exponent + quotient;
			}else{
				return "1" + exponent + quotient;
			}
			
			
	}
	
	
	/**
	 * ����������޷�����������ֵ
	 * @param substring
	 * @return
	 */
	public int computeInteger(String string) {
		string = new StringBuffer(string).reverse().toString();
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '0') {
				continue;
			} else
				result += (int) Math.pow(2, i);
		}
		return result;
	}
	/**
	 * ����ҵ2 �Ľ����� �Ľ��޷��������ĳ˷��Լ��������ĳ˷�
	 * @param operand1
	 * @param operand2
	 * @param length
	 * @return
	 */
	public String NewIntegerMultiplication(String operand1, String operand2,
			int length){
		
		
		
		
		
		
		
		return null;
	}
}

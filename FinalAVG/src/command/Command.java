package command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import resource.LSystem;
import resource.ResourceLoader;
import collection.ArrayMap;

/**
 * �ű������� �̳���Conversion�Ŀ����л��� ��Conversion�Ƕ��̳���һЩת���ķ��� ���ڶ�ȡ�ű���������ת��ΪJava��ִ������
 * 
 * @author czq
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Command extends Conversion implements Serializable {
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ����ű��������ڲ��ű��������ڲ��ĺ���
	 */
	//TODO ��ʱ�ò���
	final static private Map cacheScript = Collections
			.synchronizedMap(new HashMap(1500));
	/**
	 * �ڻ����е���������
	 */
	private String cacheCommandName;
	/**
	 * �����б�
	 */
	final public ArrayMap functions = new ArrayMap(20);
	/**
	 * �����б�
	 */
	final Map setEnvironmentList = Collections.synchronizedMap(new HashMap(20));
	/**
	 * ������֧�б�
	 */
	ArrayMap conditionEnvironmentList = new ArrayMap(30);
	/**
	 * ���ڶ�����������
	 */
	final static private StringBuffer reader = new StringBuffer(3000);
	/**
	 * ����ע����
	 */
	private boolean flaging = false;
	/**
	 * �ж�if��
	 */
	private boolean ifing = false;
	/**
	 * �ڲ��ű���¼�У�֮�󽫱����ã�
	 */
	private boolean functioning = false;
	/**
	 * ��֧��ǣ�if�ж��Ƿ�ɹ���
	 */
	private boolean elseFlag = false;

	/**
	 * �Ƿ��ȡif�ڽ�����䣬���жϺ�����
	 * ���if��else�жϳɹ��������ȡ�ڲ����룬����ֵΪfalse������ȡ�κδ���
	 */
	private boolean backIfBool = false;

	/**
	 * ��������ִ�н������AVGScript��������
	 */
	private String executeCommand;
	/**
	 * ��ǰ������֧��������˵��ǰ������֧��������
	 */
	private String nowPosFlagName;
	/**
	 * �Ƿ�����ű���AVGScript��
	 */
	private boolean addCommand;
	/**
	 * ��ǰ�ű��Ƿ�Ϊ�ڲ��ű�
	 */
	private boolean isInnerCommand;
	/**
	 * �Ƿ����ڶ�ȡ     ���ڶ���ѡ����
	 */
	private boolean isRead;
	/**
	 * �Ƿ����ڵ���
	 */
	private boolean isCall;
//	/**
//	 * �Ƿ��л���Ľű�
//	 */
//	private boolean isCache;
	/**
	 * �Ƿ����ڶ�ȡ��if
	 */
	private boolean if_bool;
	/**
	 * �Ƿ��ȡ��else
	 */
	private boolean elseif_bool;
	/**
	 * �ڲ�ű�
	 */
	private Command innerCommand;
	/**
	 * 	��ǰ�ű���
	 */
	private String commandString;
	/**
	 * ��ʱ�洢���б�
	 */
	private List temps;
	/**
	 * ��ӡ���
	 */
	private List printTags;
	/**
	 * ��������
	 */
	private List randTags;
	/**
	 * �ű������б��С
	 */
	private int scriptSize;

	/**
	 * Ŀǰ��ǵ�λ��//Ŀǰ��ȡ����λ��
	 */
	private int offsetPos;

	/**
	 * �ű������б�
	 */
	private List scriptList;
	/**
	 * �ű���
	 */
	private String scriptName;
	/**
	 * ��if-else���Ƿ�������һ�γɹ�
	 */
	private boolean anyIfSucceed;
	/**
	 * �����һ�γɹ������ٴ�����elseʱ ����������if��䲻�����
	 */
	private boolean neverPrint;
	

	/**
	 * ������������ָ���ű��ļ�
	 * 
	 * @param fileName
	 *            �ű��ļ���
	 */
	public Command(String fileName) {
		formatCommand(fileName);
	}
	/**
	 * ������������ָ���ű��ļ��ͳ�ʼ��Դ
	 * @param fileName
	 * @param resource
	 */
	public Command(String fileName, List resource){
		formatCommand("function", resource);
	}
	public void formatCommand(String fileName) {
		formatCommand(fileName,Command.includeFile(fileName));
		
	}
	/**
	 * ��񻯽ű��ļ���ִ�� ��Ҫִ�б�ǵĸ�ֵ����������Ǻ�����ĵ�ע��
	 * 
	 * @param fileName
	 */
	public void formatCommand(String fileName, List resource) {
		// ɾ�������Ѵ��ڱ����б��������֧��ӳ��
		setEnvironmentList.clear();
		conditionEnvironmentList.clear();
		//Ĭ��ѡ��-1
		setEnvironmentList.put(V_SELECT_KEY, "-1");
		scriptName = fileName;
		scriptList = resource;
		scriptSize = scriptList.size();
		offsetPos = 0;
		flaging = false;
		ifing = false;
//		isCache = true;
		elseFlag = false;
		backIfBool = false;
		anyIfSucceed = false;
		neverPrint = false;

	}

	/**
	 * ����ִ�нű��ļ������ؿ��õĽ����AVGScript
	 * 
	 * @return ��������ִ�н��
	 */
	public synchronized String doExecute() {
	
		executeCommand = null;

		addCommand = true;

		isInnerCommand = (innerCommand != null);

		if_bool = false;

		elseif_bool = false;
		nowPosFlagName = String.valueOf(offsetPos);
		try{
			
			// ���ȫ������
			commandString = ((String) scriptList.get(offsetPos)).trim();
			
			//TODO �ű������ʵ����ʱû�����󣬿���ʵ�ֶ�ȡ��ǰԤ������ű��У�ֱ�ӷ���
			/*if (commandString.startsWith(RESET_CACHE_TAG)) {
				resetCache();
				return executeCommand;
			}*/
			/*if(isCache){
				// ��û�����������
				cacheCommandName = getNowCacheOffsetName();
				// ��ȡ����Ľű�
				Object cache = cacheScript.get(cacheCommandName);
				if (cache != null) {
					return (String) cache;
				}
			}*/
			//����ע��
			if(dealWithComments()){
				return executeCommand;
			}
			/*//��ת���ڼ���
			if(commandString.startsWith("goto")){
				temps = commandSplit(commandString);
				gotoIndex(Integer.parseInt((String)temps.get(1)));
				return executeCommand;
			}
			*/
			//�������if�ж������Ƿ��ȡif�����ڲ�������ʵ����ת��֧�Ĺؼ���
			//TODO  ʵ����ת��֧���ַ�ʽ ��1.ֱ����ת������gotoindex������ʹ�ùؼ���goto����ʵ�֣���Ҫ�������࣬����ֱ������if��endif�������ȵȣ�
			//2.���������ű���ʹ��include�ؼ���  
			/*
			 * ����Ϊif��֧�н����ڲ��ű�����ش���
			 */
			
			int length = conditionEnvironmentList.size();
			if (length > 0) {

				// ȡ����һ��if�ж����Ƿ�Ϊ�棬�������Ƿ������ȡ������ֱ��������һ��if/else�ٶ�ȡ
				Object ifResult = conditionEnvironmentList.get(length - 1);
				if (ifResult != null) {
					backIfBool = ((Boolean) ifResult).booleanValue();
				}
			}
			if (backIfBool) {
				// �����ڲ��ű� TODO
				if (commandString.startsWith(INCLUDE_TAG)) {
					temps = commandSplit(commandString);
					String fileName = (String) temps.get(1);// include + �ڲ��ű���
					if (fileName != null) {
						innerCommand = new Command(fileName);
						isInnerCommand = true;
						//innerCommand.isInnerCommand = true ;
						innerCommand.setVariables(getVariables());
						//�����ӽű�û�и��෽����Сbug
						innerCommand.functions.putAll(functions);
						this.offsetPos++;
						return executeCommand;
						// ִ����ʱ���Զ�����
						
					}
				}
				// ִ���ڲ��ű�
				if (isInnerCommand && !isCall) {
			
					if (innerCommand.scriptHasNext()) {
						 String temp =innerCommand.doExecute();
						return temp;
					} else {// ����ڲ��ű�ִ�����
						setVariables(innerCommand.getVariables());
						innerCommand = null;
						return executeCommand;
					}
				}
			}
			
			// �������ֵ
			setUpRandom();
			// ���ű��ļ��еı���������Ӧֵ
			setUpSET();
			/*
			 * �������Ϊ�ڲ���������������
			 */
			
			//�ڲ��ű������������
			if(commandString.startsWith(END_TAG)){
				functioning = false;
				return executeCommand;
			}
			
			//����begin  ׼����ʼ��ȡ�������ݲ�����function�б��У������ݶ�������ʮ��
			if(commandString.startsWith(BEGIN_TAG)){
				functioning = true;
				temps = commandSplit(commandString);
				functions.put(temps.get(1), new ArrayList(20));
				return executeCommand;
			}
			//��ʼ��ȡ��������,�洢����Ӧ��function��
			if (functioning) {
				((ArrayList) functions.get(functions.size() - 1))
						.add(commandString);
				return executeCommand;
			}
			//ִ�д���εĵ��ñ��call����һЩǰ�ڹ���
			if (commandString.startsWith(CALL_TAG) ) {
				//���if�ж��е�������ж�Ϊ�٣���ִ��
				if(ifing && !elseFlag){
					return executeCommand;
				}
				temps = commandSplit(commandString);
				//����÷����в�������������������б���
				
					if (temps.size() > 2)
						setVariable("item1", (String) temps.get(2));
					if (temps.size() > 3)
						setVariable("item2", (String) temps.get(3));
					if (temps.size() > 4)
						setVariable("item3", (String) temps.get(4));
					if (temps.size() > 5)
						setVariable("item4", (String) temps.get(5));
				
				
				
				//��ȡ��������
				List functionContent = (ArrayList) functions.get((String) temps
						.get(1));
				//�����ڲ��ű����÷������Լ��������ݹ���(������ӽű�������ӽű����ӽű�)
				innerCommand = new Command((String) temps.get(1),
						functionContent);
				//�����ű������ű����ı�����ӳ�����ӽű�
				innerCommand.setVariables(getVariables());
				
//				innerCommand.closeCache();
				isCall = true;
				isInnerCommand = true;
				//���ű���ǽ�����һ�У�������PCִ����������ʱ�Զ���4
				offsetPos++;
				return executeCommand;
			}
			//��ʼִ�е��ú���
			if(isCall&& isInnerCommand){
				//�����ű��ı���ӳ�����ӽű�����Ϊ�ӽű��ı�����Ǹ��ű��ı���������
			    //TODO
				if(innerCommand.scriptHasNext()){
					executeCommand = innerCommand.doExecute();
					
					return executeCommand;
				}else{//�ӽű�ִ��ִ�����
					isCall = false;
					isInnerCommand = false;
					innerCommand = null;
					return executeCommand;
				}
			}
			/*
			 * �������if����ִ����ش���
			 * ע�⣺if+�ո�+���ʽ�����ʽ�в�����ʹ�ÿո񣡣�
			 * else+�ո�+if+�ո�+���ʽ
			 */
			dealWithIf();
			//��ȡѡ���б�
			readItems();
			
			/*
			 * ����Ϊ����ű��жϡ����δ�����������AVGScript
			 */
			//�����if�ж��У����ж��Ƿ�Ӧ�ö�ȡ�����ݷ�֧�Ƿ�ʵ�֣�,Ϊ���������жϣ�backIfbool��elseFlag������Ϊtrue
			if(addCommand && ifing && !neverPrint){
				if(backIfBool  && elseFlag ){//TODO
				executeCommand = commandString;
				}
			}
			if(addCommand && !ifing ){
				executeCommand = commandString;
			}
			//����Ҫ��ʾ���ı�ͼƬ�ȵȣ���outʱ�Ͳ�Ҫ����
			if(executeCommand != null && !commandString.startsWith(OUT_TAG)){
				dealWithPrint();
			}
			/*
			if(isCache){
				cacheScript.put(cacheCommandName, executeCommand);
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
			
		} finally {
			if(!isInnerCommand){
				offsetPos++;
			}
		}
		return executeCommand;
	}
	
	/**
	 * ����һ���жϵķ��������ݴ��������������ж��Ƿ����Ϊ�棬 ���ز���ֵ����nowPosFlagname����ӳ��
	 * һ����ԣ�valueA���ڴ洢����������˵��Ϸ״̬������ĳЩָ�꣩��valueB���ڴ洢��׼ ��������ֵ�ﵽ200������ʽ���ᴥ�������¼��ȵȡ�
	 * ע�⣺���ʽ�в��ܺ��пո� ����mount<40
	 * @param commandString     ��ǰ�ű���
	 * @param nowPosFlagName     ��ǰ���ж����ƣ��������Ƿ�ϲ��ĳĳ
	 * @param setEnvironmentList    �����б�
	 * @param conditionEnvironmentList    �����б�
	 * @return
	 */
	private boolean setUpIF(String commandString, String nowPosFlagName,
			Map setEnvironmentList, Map conditionEnvironmentList) {
		boolean result = false;
		// Ĭ������µ�ǰ����������
		conditionEnvironmentList.put(nowPosFlagName, new Boolean(false));
		// �ָ�ָ
		List commandSplit = commandSplit(commandString);
		//System.out.println(commandSplit);
		//0λ����if��1λ���Ǳ�����2λ�����жϷ���3λ������ֵ
		Object ValueA = (String) commandSplit.get(1);
		
		Object ValueB = (String) commandSplit.get(3);
		
		// ��������б�������Щֵ�����б���ģ�����Ϊ�������ô���ֵ
		ValueA = setEnvironmentList.get(ValueA) == null ? ValueA
				: setEnvironmentList.get(ValueA);
		ValueB = setEnvironmentList.get(ValueB) == null ? ValueB
				: setEnvironmentList.get(ValueB);

		// ��ȡ�ж�����
		String condition = (String) commandSplit.get(2);
		// ������ B�Ǵ����֣�����ת��������Aһ��Ϊ�����������ж��Ƿ�Ϊ�����֣�
		if (!isNumber(ValueB)) {
		
			ValueB = parse(ValueB);
		}

		// �޷��ж�ʱ
		if (ValueA == null || ValueB == null) {
			conditionEnvironmentList.put(nowPosFlagName, new Boolean(false));
		}
		// ������Ϊ���ʱ 
		if (condition.equals("==")) {
			conditionEnvironmentList.put(nowPosFlagName, result = new Boolean(
					ValueA.toString().equals(ValueB.toString())));
		}
		// ������Ϊ����ʱ
		if (condition.equals("!=")) {
			conditionEnvironmentList.put(nowPosFlagName, new Boolean(
					result = !(ValueA.toString().equals(ValueB.toString()))));
		}
		// ��Ϊ����ʱ
		if (condition.equals(">")) {
			conditionEnvironmentList
					.put(nowPosFlagName,
							new Boolean(result = Integer.parseInt(ValueA
									.toString()) > Integer.parseInt(ValueB
									.toString())));
		}
		// ��ΪС��
		if (condition.equals("<")) {
			conditionEnvironmentList
					.put(nowPosFlagName,
							new Boolean(result = Integer.parseInt(ValueA
									.toString()) < Integer.parseInt(ValueB
									.toString())));
		}
		// ��Ϊ���ڵ���
		if (condition.equals(">=")) {
			conditionEnvironmentList
					.put(nowPosFlagName,
							new Boolean(result = Integer.parseInt(ValueA
									.toString()) >= Integer.parseInt(ValueB
									.toString())));
		}
		// ��ΪС�ڵ���
		if (condition.equals("<=")) {
			conditionEnvironmentList
					.put(nowPosFlagName,
							new Boolean(result = Integer.parseInt(ValueA
									.toString()) <= Integer.parseInt(ValueB
									.toString())));
		}
		return result;
	}
	

	/**
	 * �����������doexcute�б����� �ű��ļ�ʹ��rand������ʽ *(����һ���ڳ��ֶ��rand)
	 * ʹ�÷�ʽ��1�����������Ǳ��������δ������������ʱ����ֵΪnull��������1��4��α�����
	 * ����������������������ֵ����rand����ĸ�Ϊ����ֵ 
	 * 2. �������������֣����ʾ����������ɷ�Χ��0 ~ ����������ֵ��
	 */
	private void setUpRandom() {

		// ���Ҫ���ɵ������
		 randTags = getNameTags(commandString, RAND_TAG + BRACKET_LEFT_TAG,
				BRACKET_RIGHT_TAG);
		if (randTags == null) {
			return;
		}
		for (Iterator iterator = randTags.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) setEnvironmentList.get(key);
			// ��������б����ж�Ӧֵ
			if (value != null) {
				Conversion.replaceMatch(commandString, RAND_TAG
						+ BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG, value);
				// �����ж�key�Ƿ�Ϊ���֣�����Ϊ��������ɵķ�Χ
			} else if (isNumber(key)) {
				Conversion.replaceMatch(commandString, RAND_TAG
						+ BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG, Integer
						.toString(GLOBAL_RAND.nextInt(Integer.parseInt(key))));
				// ������δ����ֵʱĬ������1~4�����
			} else {
				Conversion.replaceMatch(commandString, RAND_TAG
						+ BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG,
						Integer.toString(GLOBAL_RAND.nextInt(4) + 1));
			}

		}
	}
	/**
	 * �������ñ���ֵ����Ӧ�ű��ļ��е���set��ͷ����䣬һ��ֻ����һ��set
	 * ʹ�÷�����
	 * 1.set  ���� = ����
	 * 2.set  ���� = ����    (��ʱ����Ӧ��   Ӣ��   ˫����������)
	 * 3.set  ���� = �������ı��ʽ
	 */
	private void setUpSET() {
		/*if (commandString.startsWith(SET_TAG)) {
			List temps = commandSplit(commandString);
			int len = temps.size();
			String result = null;
			if (len == 4) {
				result = temps.get(3).toString();
			} else if (len > 4) {
				StringBuffer sbr = new StringBuffer(len);
				for (int i = 3; i < temps.size(); i++) {
					sbr.append(temps.get(i));
				}
				result = sbr.toString();
			}

			if (result != null) {
				// �滻���б����ַ�
				Set set = setEnvironmentList.entrySet();
				for (Iterator it = set.iterator(); it.hasNext();) {
					Entry entry = (Entry) it.next();
					result = replaceMatch(result, (String) entry.getKey(),
							entry.getValue().toString());
				}
				// ��Ϊ��ͨ�ַ���ʱ
				if (result.startsWith("\"") && result.endsWith("\"")) {
					setEnvironmentList.put(temps.get(1), result.substring(1,
							result.length() - 1));
				} else if (isChinese(result) || isEnglishAndNumeric(result)) {
					setEnvironmentList.put(temps.get(1), result);
				} else {
					// ��Ϊ��ѧ���ʽʱ
					setEnvironmentList.put(temps.get(1), compute.parse(result));

				}
			}
			addCommand = false;
		}*/

		if (commandString.startsWith(SET_TAG) && !functioning) {
			List setList = commandSplit(commandString);
			
			// ���ڴ����ʽ�ұߵ�ֵ ֮���Բ���ֵΪnull����Ϊnull+ string ��Ȼ����� ��null+string��
			String result = ""; 
			// �����ʽ�������ֵ��result
			for (int i = 3; i < setList.size(); i++) {
				result = result + (String) setList.get(i);
			}
			// �����ʽΪ����
			if ((result.startsWith("\"") && result.endsWith("\""))) {
				setEnvironmentList.put(setList.get(1), result.substring(1,result.length() -1));
				return;
			} 
			// �滻���ʽ�����еı���
			Set set = setEnvironmentList.entrySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				result = replaceMatch(result, (String) entry.getKey(),
						(String) entry.getValue());
			}
				
			// �����ʽ����һ���������滻��������ʱ
			
				if(isNumber(result)){
				setEnvironmentList.put(setList.get(1), result );
			}
			// �����ʽΪ�������������������ʱ
			else {
				//TODO
				setEnvironmentList.put(setList.get(1),
						Integer.toString(parse(result)));
				//System.out.println("mount:" +getVariable("mount"));

			
			}
			addCommand = false;
		}
			//����������ű�  TODO where to place
			
		}
	
	
	/**
	 * ����ű��е�ע������ �� returnֵΪ�Ƿ񷵻�commandString
	 */
	private boolean dealWithComments() {
			
			//�������ȫ��ע���У��ж�ע���Ƿ����
			if(flaging){
				if(commandString.startsWith(FLAG_LS_E_TAG) || commandString.endsWith(FLAG_LS_E_TAG)){
					flaging = false;
				}
				return true;
			}
			//�����ע���У��жϱ����Ƿ�Ϊע��
			if(!flaging){
		
				//�����ע����δ����
				if(commandString.startsWith(FLAG_LS_B_TAG) && !commandString.endsWith(FLAG_LS_E_TAG)){
					flaging = true;
					return true;
				}//�����ע�����ڸ��н���
				if(commandString.startsWith(FLAG_L_TAG) || (commandString.startsWith(FLAG_LS_B_TAG)&& commandString.endsWith(FLAG_LS_E_TAG))){
					return true;
				}
			}
			return false;
		
	}
	/**
	 * �����ӡ�ַ��������⣬������������Ӧֵ��ȥ�����ż�print �����ء�
	 */
	private void dealWithPrint() {
		printTags = getNameTags(commandString, PRINT_TAG + BRACKET_LEFT_TAG,
				BRACKET_RIGHT_TAG);
		if (printTags != null) {
			for (Iterator iterator = printTags.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = (String) setEnvironmentList.get(key);
				if (value != null) {
					//���Ҫprint��һ������
					commandString = replaceMatch(commandString, (PRINT_TAG
							+ BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
							.intern(), value);
					
				} else {
					//���Ҫprint����һ������
					commandString = replaceMatch(commandString, (PRINT_TAG
							+ BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
							.intern(), key);
				}

			}
			executeCommand = commandString;
			//System.out.println(executeCommand);
		}
		
	}
	/**
	 * ��ȡѡ���б�,����������ʱ��ѡ��
	 * ������ʽ��
	 * in
	 * ѡ��A
	 * ѡ��B
	 * .....
	 * out
	 */
	private void readItems() {
		//����ѡ���б��ȡ
		if(commandString.startsWith(OUT_TAG)){
			isRead = false;
			addCommand = false;
			executeCommand = (SELECTS_TAG + " " + reader.toString()).intern();
			
		}
		//�ۼ�ѡ���б�
		if(isRead){
			//��ѡ���б�Ϊ����������жϲ��滻
			Set set = setEnvironmentList.entrySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				commandString = replaceMatch(commandString, (String) entry.getKey(),
						(String) entry.getValue());
			}
			reader.append(commandString.trim());
			reader.append(FLAG);
			addCommand = false;
		}
		//��ʼ����ѡ���б�
		if(commandString.startsWith(IN_TAG)){
			isRead = true;
			addCommand = false;
			reader.delete(0, reader.length());
		}
		
		
	}
	/**
	 * ����ű��������е�if-else���
	 */
	private void dealWithIf() {
		
		if_bool = commandString.startsWith(IF_TAG);
		elseif_bool = commandString.startsWith(ELSE_TAG);
		
		if (if_bool) {
			elseFlag = setUpIF(commandString, nowPosFlagName,
					setEnvironmentList, conditionEnvironmentList);
			addCommand = false;
			ifing = true;
		} else if (elseif_bool) {
			// ����Ѿ��гɹ����ˣ����ٴ�����elseʱ���������
			if (anyIfSucceed) {
				neverPrint = true;
			}
			// ��������ж�,����ȷ����һ���ж��Ƿ�ɹ�
			// �����һ��û�гɹ����ж�
			if (!backIfBool && !elseFlag) {
				elseFlag = setUpIF(commandString.substring(7), nowPosFlagName,
						setEnvironmentList, conditionEnvironmentList);
				// System.out.println(commandString + elseFlag + ifing);// TODO
				addCommand = false;

				// ���򣬽�elseFlag��nowPosFlagName��Ӧ����Ϊfalse
			} else {
				elseFlag = setUpIF(commandString.substring(7), nowPosFlagName,
						setEnvironmentList, conditionEnvironmentList);
				//System.out.println(commandString + elseFlag + ifing);
				addCommand = false;
			}

		}
		if(elseFlag){
			anyIfSucceed = true;
		}
		
		if(commandString.startsWith(IF_END_TAG)){
			conditionEnvironmentList.clear();
			ifing = false;
			addCommand = false;
			if_bool = false;
			elseif_bool = false;
			elseFlag = false;
			backIfBool = false;
			neverPrint = false;
			anyIfSucceed = false;
			
		}

	}
	
	/**
	 * ����ָ���ű�����
	 * 
	 * @param fileName  ָ���Ľű��ļ�
	 * @return  ���ű��ļ���ȡ������
	 */
	public static List includeFile(String fileName) {
		InputStream in = null;
		BufferedReader reader = null;
		List result = new ArrayList(1000);
		try {
			
			in = ResourceLoader.getResourceToInputStream(fileName);
			
			reader = new BufferedReader(new InputStreamReader(in,
					LSystem.encoding));
			
			String record = null;
			while ((record = reader.readLine()) != null) {
				//��ȥ�ַ�����ͷ�ͽ�β�Ŀհ�
				record = record.trim();
				if (record.length() > 0) {
					if (!(record.startsWith(FLAG_L_TAG)
							)) {
						result.add(record);
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * �жϽű��Ƿ������������
	 * 
	 * @return
	 */
	public boolean scriptHasNext() {
		return (offsetPos < scriptSize);
	}

	/**
	 * ��ת��ָ������λ��
	 * TODO ��ʱδʵ��
	 * @param offset
	 * @return �ɹ�����true ���򷵻�false
	 */
	public boolean gotoIndex(int offset) {
		if (offset < scriptSize && offset > 0 && offset != offsetPos) {
			offsetPos = offset;
			//System.out.println(true);
			return true;
		}
		return false;
	}

	/**
	 * ע��ѡ����� 
	 * ����㶼ע�룬����һ����
	 * @param type ѡ����
	 */
	public void select(int type) {
		if (innerCommand != null) {
			innerCommand.setVariable(V_SELECT_KEY, String.valueOf(type));
		}
		setVariable(V_SELECT_KEY, String.valueOf(type));
	}

	/**
	 * ����ѡ�����
	 * 
	 * @return
	 */
	public String getSelect() {
		return getVariable(V_SELECT_KEY);
	}

	/**
	 * ���ñ���
	 * 
	 * @param key
	 * @param value
	 */
	public void setVariable(String key, String value) {
		setEnvironmentList.put(key, value);

	}

	/**
	 * ��ñ���ֵ
	 * 
	 * @param key
	 * @return
	 */
	public String getVariable(String key) {
		return (String) setEnvironmentList.get(key);
	}

	/**
	 * ɾ������
	 * 
	 * @param key
	 */
	public void removeVariable(String key) {
		setEnvironmentList.remove(key);
	}

	/**
	 * ����������� ��ʵ���ǽ�mapӳ���е����жԸ��Ƶ������б��� һ���Բ��������������ӳ��
	 * 
	 * @param vars
	 */
	public void setVariables(Map vars) {
		setEnvironmentList.putAll(vars);
	}

	/**
	 * ���ر�������
	 * 
	 * @return
	 */
	public Map getVariables() {
		return setEnvironmentList;
	}
	/**
	 * �ص���һ�γ��ֵ�ָ����ǣ��������Ч��Ϣ��������getNameTags���� �������print��"dfdf"��
	 * ���dfdf����ʱstartStringΪprint�� endStringΪ ��
	 * 
	 * @param messages
	 *            ����������Ϣ������
	 * @param startString
	 *            �Ӱ��������ǵ������ ��ʼ�ַ��� ��ʼ��ȡ һ��ΪΪ��ʶ��+�����ţ��硰print����
	 * @param endString
	 *            �Ӱ��������ǵ������ �����ַ��� ������ȡ һ��Ϊ������
	 * @return ��������Ч��Ϣ����ǵ�һ�������ġ����ʽ��
	 */
	public static String getNameTag(String messages, String startString,
			String endString) {
		List results = getNameTags(messages, startString, endString);
		return (results == null || results.size() == 0) ? null
				: (String) results.get(0);
	}

	/**
	 * �ص���ǻ����Ч��Ϣ������Ϊlist��������һ���صķ��� �������print��"dfdf"��
	 * ���dfdf����ʱstartStringΪprint�� endStringΪ ��
	 * 
	 * @param messages
	 *            ����������Ϣ������
	 * @param startString
	 *            �Ӱ��������ǵ������ ��ʼ�ַ��� ��ʼ��ȡ һ��ΪΪ��ʶ��+�����ţ��硰print����
	 * @param endString
	 *            �Ӱ��������ǵ������ �����ַ��� ������ȡ һ��Ϊ������
	 * @return ��������Ч��Ϣ����ǵ�һ�������ġ����ʽ��
	 */

	public static List getNameTags(String messages, String startString,
			String endString) {
		// ������һ��getNameTags����
		return Command.getNameTags(messages.toCharArray(),
				startString.toCharArray(), endString.toCharArray());
	}

	/**
	 * ��ȡָ��������ݲ�����Ϊlist��һ�ο��Խ�ȡ����
	 * 
	 * �������print��"dfdf"�� ���dfdf����ʱstartStringΪprint�� endStringΪ ��
	 * 
	 * @param messages
	 *            ����������Ϣ������
	 * @param startString
	 *            �Ӱ��������ǵ������ ��ʼ�ַ��� ��ʼ��ȡ һ��ΪΪ��ʶ��+�����ţ��硰print����
	 * @param endString
	 *            �Ӱ��������ǵ������ �����ַ��� ������ȡ һ��Ϊ������
	 * @return ��������Ч��Ϣ����ǵ�һ�������ġ����ʽ��
	 */
	public static List getNameTags(char[] messages, char[] startString,
			char[] endString) {
		// ���ڴ洢��õ���Ϣ
		List tagList = new ArrayList(10);
		// �ֱ�Ϊ��ʼ�ַ����������ַ����ĳ���
		int sLength = startString.length;
		int eLength = endString.length;
		// ���ڱ���Ƿ��Ѷ�����Ҫ����Ϣ
		boolean lookUp = false;
		// ������ʱ����
		StringBuffer usefulMes = new StringBuffer(100);
		// ��ʾ��ʼ�ͽ����ַ��������ڼ�����
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < messages.length; i++) {
			// ����ҵ���ʼ�ַ�������ĸ����ʼ����
			if (startString[startIndex] == messages[i]) {
				startIndex++;
			} else {
				startIndex = 0;
			}
			// ����ȷ���ҵ���ʼ�ַ���
			if (startIndex == sLength) {
				lookUp = true;
				startIndex = 0;
			}
			if (lookUp) {
				usefulMes.append(messages[i]);
			}
			// �Ƿ�ʼ�����ַ���
			if (endString[endIndex] == messages[i]) {
				endIndex++;
			} else {
				endIndex = 0;
			}
			if (endIndex == eLength) {
				lookUp = false;
				endIndex = 0;
			}
			if (usefulMes.length() != 0 && lookUp == false) {
				// ��Ϊ������İ���startString��endString�ĸ�һ���ַ���ɾȥ
				usefulMes = usefulMes.deleteCharAt(usefulMes.length() - 1);
				usefulMes = usefulMes.deleteCharAt(0);
				tagList.add(usefulMes.toString());
				// ����ַ������� ��ע�⣬ɾ��ʱ���һ���ǲ��������ģ����������deleteCharAt��һ��~���������ٶ��ˣ�������
				usefulMes.delete(0, usefulMes.length());
			}
		}

		return tagList;
	}
	/**
	 * ��̬���� ����ָ���ű��ļ�����Ϊlist����ɾȥ�س� ���ո�������������@�����շ���List 
	 * 
	 * @param src 
	 * @return
	 */
	public static List commandSplit(final String src) {
		//����string��intern�������������Ч���õ�
		String[] cmds;
		String result = src;
		result = result.replace("\r", "");//  ��\r���س�
		result = (FLAG + result).intern();
		result = result.replaceAll(" ", FLAG);
		result = result.replace("\t", FLAG);
		result = result.replace("<=", (FLAG + "<=" + FLAG).intern());
		result = result.replace(">=", (FLAG + ">=" + FLAG).intern());
		result = result.replace("==", (FLAG + "==" + FLAG).intern());
		result = result.replace("!=", (FLAG + "!=" + FLAG).intern());
		if (result.indexOf("<=") == -1) {
			result = result.replace("<", (FLAG + "<" + FLAG).intern());
		}
		if (result.indexOf(">=") == -1) {
			result = result.replace(">", (FLAG + ">" + FLAG).intern());
		}
		result = result.substring(1);
		cmds = result.split(FLAG);
		return Arrays.asList(cmds);
		
	}
//	/**
//	 * �򿪽ű�����
//	 */
//	public void openCache() {
//		isCache = true;
//	}
//	/**
//	 * �رսű�����
//	 */
//	public void closeCache() {
//		isCache = false;
//	}

	/**
	 * ��õ�ǰ�ű��л�����
	 * 
	 * @return
	 */
	public String getNowCacheOffsetName() {
		return (scriptName + FLAG + offsetPos + FLAG + commandString)
				.toLowerCase();
	}

	/**
	 * �����ű�����
	 * 
	 */
	public static void resetCache() {
		cacheScript.clear();
	}

	/**
	 * �Ƿ����ڶ�ȡѡ���б���
	 * 
	 * @return
	 */
	public boolean isRead() {
		return isRead;
	}

	/**
	 * ���ö�ȡ���
	 * 
	 * @param isRead
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * ���ص�ǰ�Ķ������ݼ���(ѡ���)
	 * 
	 * @return
	 */
	public synchronized String[] getReads() {
		String result = reader.toString();
		result = result.replace(SELECTS_TAG, "");
		return split(result, FLAG);
	}

	/**
	 * ����ָ�������Ķ�������
	 *  ��ʱ��Ϊ��ѡ��֮�󷵻���Ӧֵ
	 * @param index
	 * @return
	 */
	public synchronized String getRead(int index) {
		try {
			return getReads()[index];// ����getreads���������еĵ�index��
		} catch (Exception e) {
			return null;
		}
	}
	public static void main(String[] args) {
		Command command = new Command("script/script1.txt");
		for (Iterator it = command.batchToList().iterator(); it.hasNext();) {
			System.out.println(it.next());
		}
}
	/**
	 * �˷���ֻ���ڲ��� ������ִ�нű��ļ���������List
	 * 
	 * @return
	 */
	public List batchToList() {
		List resultList = new ArrayList(scriptSize);

		for (; scriptHasNext();) {
			String execute = this.doExecute();
			if (execute != null) {
				resultList.add(execute);
			}
		}
		return resultList;
	}
	
}
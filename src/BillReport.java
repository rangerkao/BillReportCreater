import java.awt.datatransfer.SystemFlavorMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






















import javax.swing.JTextArea;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import bill.bean.BillData;
import bill.bean.BillSubData;
import bill.bean.Charge;
import bill.bean.ChargeDetail;
import bill.bean.Invoice;
import bill.bean.InvoiceDetail;
import bill.bean.Usage;
import bill.bean.UsageDetail;
import bill.bean.UsageDiscount;

public class BillReport{
	
	public BillReport(){ 
		/**
		 * 1:S2T 
		 * 11: 201507新需求，S2T不顯示圖案
		 * 2:NTT
		 * 3:FET
		 * 4:iGlomo
		 */
		/**
		 * 1 A4
		 * 2 Letter
		 */
		process(filePath+"/"+"S2T_201607_PDF_with_Usage_UTF8.txt",1,1);
	}
	JTextArea textPane=null;
	
	public BillReport(JTextArea textPane,String input,String output,String type,String type2){
		this.textPane=textPane;
		exportPath = output;
		templatePath="";
		process(input,Integer.valueOf(type),Integer.valueOf(type2));
		textPane.setText(textPane.getText()+"\n"+"Converting end");
	}
	public BillReport(String[] arg){
		exportPath = arg[1];
		templatePath="";
		
		if(arg.length<2){
			print("Parameter too less!");
			return;
		}
		
		Integer type2=1;
		if(arg.length>=5)
			type2=Integer.valueOf(arg[4]);
		
		String charSet=null;
		if(arg.length>=4){
			charSet=arg[3];
			print("Set charSet "+charSet);
		}
		process(arg[0],Integer.valueOf(arg[2]),type2,charSet);
	}
	private static String FileName;
	//private static final String filePath =BillReport.class.getClassLoader().getResource("").toString().replace("file:", "")+ "source/";
	
	String filePath="C:/Users/ranger.kao/Desktop";
	String templatePath="G:/MegaSync/workspace/BillReportCreater/src/";
	String exportPath="C:/Users/ranger.kao/Desktop/bill";
	
	public static void main(String[] args){
		
		args = new String[]{"C:/Users/ranger.kao/Desktop/S2T_201607_PDF_with_Usage_UTF8.txt","C:/Users/ranger.kao/Desktop/bill","1","UTF-8"};
		
		if(args.length<3)
			new BillReport();
		else
			new BillReport(args);
			
	}
	
	public void print(String s){
		System.out.println(s);
		if(textPane!=null)
			textPane.setText(textPane.getText()+"\n"+s);
	}
	public void process(String fileName,int type){
		process(fileName,type,null,null);
	}
	public void process(String fileName,int type,Integer type2){
		process(fileName,type,type2,null);
	}
	
	public void process(String fileName,int type,Integer type2,String charSet){
		
		String sType2="";
		
		switch(type2){
			case 2:
				sType2 = "billLetter";
				break;
			default :	
				sType2 = "bill";
		}
		
		FileName=fileName;
		
		//FileName="S2T_201404_PDF_with_Usage.txt";
		
		//FileName="85266400998.txt";
		
		
		print("filePath:"+FileName);
		
		BufferedReader reader = null;
		
		String str = null;
		String[] data;
		
		List<BillData> result = new ArrayList<BillData>();

		try {
			if(charSet!=null)
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(FileName),charSet)); // 指定讀取文件的編碼格式，以免出現中文亂碼
			else
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(FileName))); // 指定讀取文件的編碼格式，以免出現中文亂碼


			int count=0;
			
			
			BillData bill = null;
			//sub list
			BillSubData bs=null;
			
			//tag now is U1 or U2
			String Utag=null;
			
			
			while ((str = reader.readLine()) != null) {
				data=str.split("\t");
				String s= data[0];
				
				List<String> list=new ArrayList<String>();
				for (int i = 0; i < data.length; i++) {
					if(data[i]==null)
						data[i]="";
					list.add(data[i]);
				}

				//I(J),C(D),U(R)
				if(type==1||type==11||type==2){
					if("I".equalsIgnoreCase(s)){
						print("proccess "+(++count)+" file.");
						bill = new BillData();
						result.add(bill);
						
						bill.setI(new Invoice(data));
					}else if(bill!=null){
						if("J".equalsIgnoreCase(s)){
							list.add(3, null);//OrderSequence null
							bill.getJ().add(new InvoiceDetail(list));
						}else if("C".equalsIgnoreCase(s)){
							//ServiceCode,priceplan
							bs = new BillSubData(data[2],data[10]);
							bill.getBS().add(bs);
							bs.setC(new Charge(list));
						}else if("D".equalsIgnoreCase(s)){
							list.add(3,null);//CategorySequence null
							bs.getD().add(new ChargeDetail(list));
						}else if("U".equalsIgnoreCase(s)){
							bs.setU(new Usage(list));
						}else if("R".equalsIgnoreCase(s)){
							bs.getR().add(new UsageDetail(list));
						}
					}
				}else if(type==3){//U1(R),U2(R),P
					if("U1".equalsIgnoreCase(s)){
						print("proccess "+(++count)+" file.");
						bill = new BillData();
						result.add(bill);
						
						bs = new BillSubData();
						bill.getBS().add(bs);

						bs.setU1(new Usage(list));
						Utag="U1";
						bs.setR1(new ArrayList<UsageDetail>());				
						
					}else if(bill!=null){
						if("U2".equalsIgnoreCase(s)){
							bs.setU2(new Usage(list));
							Utag="U2";
							bs.setR2(new ArrayList<UsageDetail>());
						}else if("R".equalsIgnoreCase(s)){
							if("U1".equalsIgnoreCase(Utag)){
								bs.getR1().add(new UsageDetail(list));
							}else if("U2".equalsIgnoreCase(Utag)){
								bs.getR2().add(new UsageDetail(list));
							}
						}else if("P".equalsIgnoreCase(s)){
							bs.setP(new UsageDiscount(list));
						}
					}
				}else if(type==4){//I(J),C(D),U1(R),U2(R)
					if("I".equalsIgnoreCase(s)){
						print("proccess "+(++count)+" file.");
						bill = new BillData();
						result.add(bill);
						
						bill.setI(new Invoice(data));
					}else if(bill!=null){
						if("J".equalsIgnoreCase(s)){
							list.add(3, null);//OrderSequence null
							bill.getJ().add(new InvoiceDetail(list));
						}else if("C".equalsIgnoreCase(s)){
							//ServiceCode,priceplan
							bs = new BillSubData(data[2],data[10]);
							bill.getBS().add(bs);
							bs.setC(new Charge(list));
						}else if("D".equalsIgnoreCase(s)){
							list.add(3,null);//CategorySequence null
							bs.getD().add(new ChargeDetail(list));
						}else if("U1".equalsIgnoreCase(s)){
							bs.setU1(new Usage(list));
							Utag="U1";
							bs.setR1(new ArrayList<UsageDetail>());				
							
						}else if("U2".equalsIgnoreCase(s)){
							bs.setU2(new Usage(list));
							Utag="U2";
							bs.setR2(new ArrayList<UsageDetail>());

						}else if("R".equalsIgnoreCase(s)){
							if("U1".equalsIgnoreCase(Utag)){
								bs.getR1().add(new UsageDetail(list));
							}else if("U2".equalsIgnoreCase(Utag)){
								bs.getR2().add(new UsageDetail(list));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			print(e.getMessage());
		}finally {
			try {
			
			if(reader!=null)reader.close();
			
			} catch (IOException e) {
			
			print(e.getMessage());
			
			}
		}
		
		print("read file "+FileName+" finish!");

		String templateName=null;
		
		
		switch(type){
			case 1:
			//20150703 add
			case 11:
			case 2:
				templateName=sType2+"/template1/billreport.jrxml";
				break;
			case 3:
				templateName=sType2+"/template2/billreport2.jrxml";
				
				break;
			case 4:
				templateName=sType2+"/template3/billreport3.jrxml";
				break;
			default:
					
		}
		
		for(BillData bd: result){
			for(BillSubData bs : bd.getBS()){
				Map<String,ArrayList<UsageDetail>> map = RProcess(bs.getR());
				bs.setVoiceUsageCharges(map.get("voice"));
				bs.setSmsCharges(map.get("sms"));
				bs.setGprsUsageCharges(map.get("gprs"));
				bs.setMmsCharges(map.get("mms"));
				
				Map<String,ArrayList<UsageDetail>> map1 = RProcess(bs.getR1());
				bs.setR1voiceUsageCharges(map1.get("voice"));
				bs.setR1smsCharges(map1.get("sms"));
				bs.setR1gprsUsageCharges(map1.get("gprs"));
				bs.setR1mmsCharges(map1.get("mms"));
				
				Map<String,ArrayList<UsageDetail>> map2 = RProcess(bs.getR2());
				bs.setR2voiceUsageCharges(map2.get("voice"));
				bs.setR2smsCharges(map2.get("sms"));
				bs.setR2gprsUsageCharges(map2.get("gprs"));
				bs.setR2mmsCharges(map2.get("mms"));
				
				if(bs.getU1()!=null)
					bs.setU1total(bs.getU1().getTotalCharge());
				if(bs.getU2()!=null)
					bs.setU2total(bs.getU2().getTotalCharge());
				if(bs.getC()!=null)
					bs.setTotalAmount(bs.getC().getTotalAmount());
				if(bs.getU()!=null)
					bs.setTotalUsageCharge(Double.parseDouble(bs.getU().getTotalCharge()));
			}
		}
		
		try {
			jasperFile=JasperCompileManager.compileReportToFile(templatePath+templateName);
			print("Load template success!");
			
			print(""+result.size());
			
			if(result!=null){
				for(int i=0;i<result.size();i++){
					try {
						print("create "+(i+1));
						if(templateName!=null)
							creatPDF(result.get(i),type);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						print(e.getMessage());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						print(e.getMessage());
					}
				}
			}			
		} catch (JRException e1) {
			print(e1.getMessage());
		}

	}
	String dateString ;
	String templateName;
	String jasperFile;
	
	private void creatPDF(BillData data,int type) throws JRException, ParseException{

		
		Map<String,Object> map = null;
		
		String fileName="default";
		
		SimpleDateFormat sdf = null;
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		
		
		String ds=null;
		if(data.getI()!=null)
			ds = data.getI().getCycleBeginDate();
		else
			ds = data.getBS().get(0).getU1().getCycleBeginDate();
		
		if(type==4)
			sdf = new SimpleDateFormat("yyyy/MM/dd");//iGlomo
		else
			sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		
		Date d = sdf.parse(ds);
		dateString = sdf2.format(d);

		switch(type){
			case 1:
			//20150703 add
			case 11:
			case 2:
				map = setReportParameter1(data,type);
				fileName = data.getI().getAccountNum()+"_"+dateString+"_"+data.getI().getAccountName();
				break;
			case 3:
				map = setReportParameter2(data,type);
				fileName = data.getBS().get(0).getU1().getAccountNum()+"_"+dateString+"_"+data.getBS().get(0).getU1().getAccountName();
				break;
			case 4:
				map = setReportParameter3(data,type);
				fileName = data.getI().getAccountNum()+"_"+dateString+"_"+data.getI().getAccountName();
				break;
			default:
		}
		
		if(map!=null){
			/*String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,map,new JREmptyDataSource());
			//String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,null,new JREmptyDataSource());
			//String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,map,new JRBeanCollectionDataSource(data.getBS()));
			print("create file success!"+fileName+".pdf");	
			JasperExportManager.exportReportToPdfFile(jrprintFile,exportPath+"/"+fileName+".PDF");*/
			JasperPrint jp = JasperFillManager.fillReport(jasperFile, map, new JREmptyDataSource());
			List<JasperPrint> jpl = new ArrayList<JasperPrint>();
			jpl.add(jp);
			
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setPdfVersion(PdfVersionEnum.VERSION_1_5);
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jpl));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exportPath+"/"+fileName+".PDF"));
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			print("create file "+fileName+" success!");
		}else{
			print("set parameter fail!");
		}
		
	}
	
	public String checkRtype(String name){
		String result = "";
		if(	"語音通話費".equalsIgnoreCase(name)||
			"Voice Calling Charges".equalsIgnoreCase(name)||
			"Voice Usage Charges".equalsIgnoreCase(name)){
			result = "voice";
		}else 
		if(	"SMS Charges".equalsIgnoreCase(name)||
			"簡訊服務費".equalsIgnoreCase(name)){
			result = "sms";
		}else 
		if(	"GPRS Usage Charges".equalsIgnoreCase(name)){
			result = "gprs";
		}else 
		if(	"MMS Charges".equalsIgnoreCase(name)){
			result = "mms";
		}
		return result;
	}
	
	private Map<String,ArrayList<UsageDetail>> RProcess(List<UsageDetail> uList){

				ArrayList<UsageDetail> voiceUsageCharges=new ArrayList<UsageDetail>();	
				ArrayList<UsageDetail> smsCharges=new ArrayList<UsageDetail>();
				ArrayList<UsageDetail> gprsUsageCharges=new ArrayList<UsageDetail>();
				ArrayList<UsageDetail> mmsCharges=new ArrayList<UsageDetail>();
			

				for(UsageDetail u : uList){
					String type = checkRtype(u.getChargeItemName());
					if(	"voice".equalsIgnoreCase(type)){
						voiceUsageCharges.add(u);	
					}else if("sms".equalsIgnoreCase(type)){
						smsCharges.add(u);
					}else if("gprs".equalsIgnoreCase(type)){
						gprsUsageCharges.add(u);
					}else if("mms".equalsIgnoreCase(type)){
						mmsCharges.add(u);
					}
				}
				
				Map<String,ArrayList<UsageDetail>> r = new HashMap<String,ArrayList<UsageDetail>>();
				r.put("voice", voiceUsageCharges);
				r.put("sms", smsCharges);
				r.put("gprs", gprsUsageCharges);
				r.put("mms", mmsCharges);

		return r;
	}
	
	private Map<String,Object> setReportParameter1(BillData data,int type){
		//參數設置
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("reportType",type);
		
		String 
		imageName="",
		contactTitle="",
		contactInfo="",
		customerServiceNumber="",
		generalInfo = "";
		
		if(type==1||type==11){
			imageName="sim2travel.jpg";
			contactTitle="How to contact us:";
			contactInfo="Call +886-960-840-112"+"\n"
					+ "\n"
					+ "Or write:"+"\n"
					+ "P.O. Box 81-875 Taipei"+"\n"
					+ "Taipei City 10599"+"\n"
					+ "Taiwan R.O.C.";
			customerServiceNumber="+886-960-840-112";
			generalInfo = "1. All charges in this bill are in Hong Kong Dollars (HKD).\n"
					+ "\n"
					+ "2. \"Monthly Service Charges\" may be billed in advance or arrears depending on selected services.\n"
					+ "\n"
					+ "3. Usage charges are billed in arrears. Certain usage may appear more than one month in arrears.\n"
					+ "\n"
					+ "4. Calls to our Customer Service Centers may be monitored to ensure high quality service to our customers.\n"
					+ "\n"
					+ "5. Questions About Your Bill or Service: If you have any questions about your bill, or concerns about your service,\n"
					+ "   please contact our Customer Care representatives at:+886-960-840-112 or you may write to Customer Care at:\n"
					+ "   P.O. Box 81-875 Taipei, Taipei City 10599, Taiwan R.O.C.\n"
					+ "\n"
					+ "6. For more information, please contact our 24-hour customer service representatives:\n"
					+ "   •Taiwan: +886960840112\n"
					+ "   •Hong Kong: +85266400112\n"
					+ "   •China: +8613910480112\n"
					+ "   •You can also reach us via your Sim2Travel phone: simply dial *123# or access the \"Sim2Travel\" menu option from "
					+ "your Sim2Travel handset and press the \"Service access\" option followed by the \"Customer service\" option.\n"
					+ "   •Visit our website http://www.sim2travel.com\n"
					+ "\n"
					+ "\n"
					+ "\n"
					+ "With the Sim2Travel (Blue Ocean Alliance & rMVNO) promotional package, you will enjoy:\n"
					+ "•Maintaining in one SIM card, local numbers in China, Hong Kong and Taiwan which you can be reached at any time, "
					+ "even when traveling outsid of our partner networks\n"
					+ "•Free Fwd to S2T, allowing you to forward all calls from your home mobile or landline numbers to your Sim2Travel "
					+ "local numbers, so you are always in touch with your home friends, family members and business associates, even when "
					+ "traveling abroad.\n"
					+ "•Free Fwd to Home, allowing all of your Sim2Travel locall(Blue Ocean Alliance &rMVNO) numbers to remain active, even "
					+ "when you are back in your home country, so you never miss a call from your friends, family members and business "
					+ "associates from abroad.\n"
					+ "•Up to 70% off traditional roaming rates.\n"
					+ "•A centralized voice mailbox for all of your Sim2Travel local numbers AND your diverted home mobile number calls.\n"
					+ "•Sending and receiving of SMS in the same manner as traditional GSM operators.";
		}else if(type==2){
			imageName="HKNetLogo_4C.PNG";
			contactTitle="Customer Service Hotlines:";
			contactInfo="(HK)+852 3793 0110"+"\n"
					+ "(CN)+86 139 1037 0330"+"\n"
					+ "(TW)+886 972 900 330"+"\n"
					+ "(SG)+65 8478 0330"+"\n"
					+ "(TH)+66 90198 0330"+"\n"
					+ "(ID)+62 8557 490 0330"+"\n"
					+ "Email: sim@ntt.com.hk";
			customerServiceNumber="+852 3793 0110";
			generalInfo = "1. All charges in this bill are in Hong Kong Dollars (HKD).\n"
					+ "\n"
					+ "2. \"Monthly Service Charges\" may be billed in advance or arrears depending on selected services.\n"
					+ "\n"
					+ "3. Usage charges are billed in arrears. Certain usage may appear more than one month in arrears.\n"
					+ "\n"
					+ "4. Calls to our Customer Service Centers may be monitored to ensure high quality service to our customers.\n"
					+ "\n"
					+ "5. Questions About Your Bill or Service: If you have any questions about your bill, or concerns about your service,\n"
					+ "   please call our customer service hotlines or email us.\n"
					+ "\n"
					+ "6.";
		}
		map.put("GeneralInfo", generalInfo);
		
		//圖片
		map.put("imageName", imageName);
		
		//聯絡資訊
		map.put("contactTitle", contactTitle);
		map.put("contactInfo", contactInfo);
		
		//客服電話
		map.put("customerServiceNumber", customerServiceNumber);
		
		//地址
		map.put("address for",
				data.getI().getPostalCode()+"\n"
				+data.getI().getBillingAddressLine1()+"\n"
				+data.getI().getBillingAddressLine2()+"\n"
				+"\n"
				+data.getI().getAddressee()+"  先生/小姐");
		
		/*map.put("address for1", data.getI().getPostalCode());
		map.put("address for2", data.getI().getBillingAddressLine1());
		map.put("address for3", data.getI().getBillingAddressLine2());
		map.put("address for4", data.getI().getAddressee()+"  先生/小姐");*/
		
		String inportantN = "Dear Value Subscribers, Sim2Travel’s local-number advantage continues to provide you with "
				+ "enhanced voice quality at competitive local rates. We are alwaysstriving to provide you the best in "
				+ "worldwide communication. As your business grows, we know you demand the strongest tools to help you "
				+ "get localized wherever the opportunities take you. Currently, we have CMCC(China), CMCC HK(Hong Kong)"
				+ ", FET(Taiwan) and CTM (Macau) in our Blue Ocean Alliance. In the coming months, Sim2Travel will be "
				+ "rolling-out many new and exciting partner networks, including more countries with local numbers and mobile features!\n"
				+ "21 Selected Networks around the World!\n"
				+ "Besides our partner networks, Sim2Travel selected 21 networks in other 21 different countries to "
				+ "provide very competitive roaming price for you. These selected networks include: AT&T in US, Globe "
				+ "Telecom in Philippines, Vodafone in Australia, Vodafone D2 in Germany, T-Mobile in UK, Bouygues Telecom "
				+ "in France, Vodafone Omnitel in Italy, Telefonica Moviles in Spain, DoCoMo 3G in Japan, Starhub in Singapore, "
				+ "Vodafone in Netherlands, MTS in Russia, GPC Vinaphone in Vietnam, TRUE Move in Thailand, Hutchison in "
				+ "Indonesia, DiGi Telecom in Malaysia, Mobily in Saudi Arabia, SKT 3G in South Korea, MTN in South Africa, "
				+ "T-Mobile in Czech, and Tele2 in Sweden. Sim2Travel suggests you to select these networks when you travel to "
				+ "these countries listed above, to enjoy the best voice quality and low roaming rate at the same time!\n"
				+ "\n";
		
		map.put("InportantN", inportantN);
		
		String glossaryT = "Called Destination\n"
				+ "Caller Destination\n"
				+ "Call Duration\n"
				+ "CHNCT\n"
				+ "CHNOT\n"
				+ "FTH\n"
				+ "FTHA\n"
				+ "FTHD\n"
				+ "HKGPP\n"
				+ "Incoming\n"
				+ "IN_P\n"
				+ "IN_NP\n"
				+ "INA_P\n"
				+ "INA_NP\n"
				+ "IND_P\n"
				+ "IND_NP\n"
				+ "LA_P\n"
				+ "LA_NP\n"
				+ "LAA_P\n"
				+ "LAA_NP\n"
				+ "LAD_P\n"
				+ "LAD_NP\n"
				+ "Number calling/Called\n"
				+ "Outgoing\n"
				+ "SMS\n"
				+ "SMSA\n"
				+ "SMSD\n"
				+ "Total Charge\n"
				+ "UR\n"
				+ "URD\n"
				+ "VMC_P\n"
				+ "VMC_NP\n"
				+ "VMCA_P\n"
				+ "VMCA_NP\n"
				+ "VMCD_P\n"
				+ "VMCD_NP\n"
				+ "GPRS\n"
				+ "MMS";
		map.put("GlossaryT", glossaryT);
		
		String glossaryE = "Destination of the caller\n"
				+ "Location of the caller\n"
				+ "Duration of the call\n"
				+ "China (Shanghai , Beijing, Gongdong)\n"
				+ "China (Other Providence)\n"
				+ "Fwd To Home Charges\n"
				+ "Fwd To Home Charges Adjustment\n"
				+ "Fwd To Home Charges Discount\n"
				+ "Hong Kong Peoples telecom\n"
				+ "Reciving an incoming call\n"
				+ "International Call Charges_Partner Network\n"
				+ "International Call Charges_Non-Partner Network\n"
				+ "International Call Charges Adjustment _Partner Network\n"
				+ "International Call Charges Adjustment_Non-Partner Network\n"
				+ "International Call Charges Discount_Partner Network\n"
				+ "International Call Charges Discount _Non-Partner Network\n"
				+ "Local Airtime Charges_Partner Network\n"
				+ "Local Airtime Charges_Non-Partner Network\n"
				+ "Local Airtime Charges Adjustment_Partner Network\n"
				+ "Local Airtime Charges Adjustment_Non-Partner Network\n"
				+ "Local Airtime Charges Discount_Partner Network\n"
				+ "Local Airtime Charges Discount_Non-Partner Network\n"
				+ "Number calling/Called\n"
				+ "Making an outgoing call\n"
				+ "SMS Charges\n"
				+ "SMS Charges Adjustment\n"
				+ "SMS Charges Discount\n"
				+ "Total Charge\n"
				+ "Usage Redeemable\n"
				+ "Usage Redeemable Adjustment\n"
				+ "Voice Mail Charges_Partner Network\n"
				+ "Voice Mail Charges_Non-Partner Network\n"
				+ "Voice Mail Charges Adjustment_Partner Network\n"
				+ "Voice Mail Charges Adjustment_Non-Partner Network\n"
				+ "Voice Mail Charges Discount_Partner Network\n"
				+ "Voice Mail Charges Discount_Non-Partner Network\n"
				+ "GPRS Internet Connection Usage Charge\n"
				+ "MMS Charges";
		map.put("GlossaryE", glossaryE);
		
		//資料
		map.put("Statement for", data.getI().getCustomerName());
		
		String accountName = data.getI().getAccountName();
		if(accountName!=null){
			accountName = mark(accountName,accountName.length()-5,accountName.length());
		}
		map.put("Account Number", accountName);
			
		map.put("Billing Period", data.getI().getCycleBeginDate()+"~"+data.getI().getCycleEndDate());
		map.put("Currency", "HKD");
		
		//封面項目
		map.put("Previous Balance", new Double(data.getI().getAccountBalance()));
		map.put("Payment Received", new Double(data.getI().getPaymentPosted()));

		//Build Usage Charge
		map.put("Balance", data.getJ());
		map.put("applied date", data.getI().getDueDate());		
		map.put("PaymentMethod", data.getI().getPaymentMethod());
		
		
		String payInfo="";
		if("2".equals("")){
			payInfo = "Sim2Travel (Blue Ocean Alliance & rMVNO) accepts payment by VISA or MasterCard only. Each Sim2Travel bill will be charged to the credit card that you have provided.";
		}else{
			payInfo = "Sim2Travel (Blue Ocean Alliance & rMVNO) accepts payment via bank wire transfer. The total amount due must be paid in Hong Kong Dollars (HKD) via bank wire transfer. before / on the 25 of the current month. Payment MUST include the full name and bill number. Transfer should be made directly to our billing partner Sim2Travel Inc.";

		}
		map.put("PayInfo", payInfo);
		
		
		map.put("InvoiceNo", data.getI().getInvoiceNo());
		
		/*

		//Build Charge Detail
		map.put("currentTotal", data.getBS().get(0).getTotalCurrentCharge());
		map.put("D", data.getBS().get(0).getD());
		
		
		//Build Usage Detail
		map.put("R", data.getBS().get(0).getR());

		map.put("R1", data.getBS().get(0).getR1());
		map.put("R2", data.getBS().get(0).getR2());
		map.put("R3", data.getBS().get(0).getR3());
		
		map.put("Total Usage Charges",data.getBS().get(0).getTotalUsageCharge());*/
		
		
		//new
		map.put("BS", data.getBS());

		map.put("SUBREPORT_DIR", templatePath+"/bill/template1/");

		return map;
	}
	
	private Map<String,Object> setReportParameter2(BillData data,int type){
		//參數設置
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("reportType",type);
				
		//資料
		map.put("Statement for", data.getBS().get(0).getU1().getCustomerName());
		map.put("Account Number", data.getBS().get(0).getU1().getAccountName());
		//map.put("PaymentMethod", data.getBS().get(0).getU1());
		//map.put("InvoiceNo", data.getI().getInvoiceNo());
		
		
		String serviceCode = data.getBS().get(0).getU1().getServiceCode();
		
		if(serviceCode!=null && serviceCode.length()>=5){
			serviceCode = 
					serviceCode.substring(0, serviceCode.length()-5)
					+ "*****";
			data.getBS().get(0).getU1().setServiceCode(serviceCode);
		}
		
		map.put("serviceCode", data.getBS().get(0).getU1().getServiceCode());
		map.put("Billing Period", data.getBS().get(0).getU1().getCycleBeginDate()+"~"+data.getBS().get(0).getU1().getCycleEndDate());
		map.put("Currency", "NTD");
		
		map.put("U1total", data.getBS().get(0).getU1total());
		map.put("U2total", data.getBS().get(0).getU2total());
		
		map.put("r1voiceUsageCharges",data.getBS().get(0).getR1voiceUsageCharges());
		map.put("r1smsCharges",data.getBS().get(0).getR1smsCharges() );
		map.put("r1gprsUsageCharges", data.getBS().get(0).getR1gprsUsageCharges());
		map.put("r1mmsCharges", data.getBS().get(0).getR1mmsCharges());
		
		map.put("r2voiceUsageCharges", data.getBS().get(0).getR2voiceUsageCharges());
		map.put("r2smsCharges", data.getBS().get(0).getR2smsCharges());
		map.put("r2gprsUsageCharges", data.getBS().get(0).getR2gprsUsageCharges());
		map.put("r2mmsCharges", data.getBS().get(0).getR2mmsCharges());
		
		map.put("SUBREPORT_DIR", templatePath+"/bill/template2/");
		//圖片
		map.put("imageName", "sim2travel.jpg");
		if(data.getBS().get(0).getP()!=null){
			map.put("CHNCTDiscountTime", data.getBS().get(0).getP().getChnctDiscountTime());
			map.put("CHNCTDiscountAmount", data.getBS().get(0).getP().getChnctDiscountAmount());
			map.put("CHNOTDiscountTime", data.getBS().get(0).getP().getChnotDiscountTime());
			map.put("CHNOTDiscountAmount", data.getBS().get(0).getP().getChnotDiscountAmount() );
			map.put("MACDiscountTime", data.getBS().get(0).getP().getMacDiscountTime());
			map.put("MACDiscountAmount", data.getBS().get(0).getP().getMacDiscountAmount());
			
			map.put("TotalDiscount", data.getBS().get(0).getP().getTotalDiscount());
		}
		
		String billItemInfoT ="Called Destination\n"
				+ "Caller Destination\n"
				+ "Duration\n"
				+ "CHNCT\n"
				+ "CHNOT\n"
				+ "FTH\n"
				+ "FTHA\n"
				+ "FTHD\n"
				+ "HKGPP\n"
				+ "Incoming\n"
				+ "IN_P\n"
				+ "IN_NP\n"
				+ "INA_P\n"
				+ "INA_NP\n"
				+ "IND_P\n"
				+ "IND_NP\n"
				+ "LA_P\n"
				+ "LA_NP\n"
				+ "LAA_P\n"
				+ "LAA_NP\n"
				+ "LAD_P\n"
				+ "LAD_NP\n"
				+ "Number calling/Called\n"
				+ "Outgoing\n"
				+ "SMS\n"
				+ "SMSA\n"
				+ "SMSD\n"
				+ "Total Charges\n"
				+ "UR\n"
				+ "URA\n"
				+ "VMC_P\n"
				+ "VMC_NP\n"
				+ "VMCA_P\n"
				+ "VMCA_NP\n"
				+ "VMCD_P\n"
				+ "VMCD_NP\n"
				+ "國際服務費\n"
				+ "全球卡境外通信費\n"
				+ "全球卡通信費優惠\n"
				+ "\n"
				+ "優惠單位數\n";
		map.put("BillItemInfoT", billItemInfoT);
		
		String billItemInfoTE ="Destination of the caller\n"
				+ "Location of the caller\n"
				+ "Duration of the call\n"
				+ "China (Shanghai , Beijing, Gongdong)\n"
				+ "China (Other Providence)\n"
				+ "Fwd To Home Charges\n"
				+ "Fwd To Home Charges Adjustment\n"
				+ "Fwd To Home Charges Discount\n"
				+ "Hong Kong Peoples telecom\n"
				+ "Reciving an incoming call\n"
				+ "International Call Charges_Partner Network\n"
				+ "International Call Charges_Non-Partner Network\n"
				+ "International Call Charges Adjustment _Partner Network\n"
				+ "International Call Charges Adjustment_Non-Partner Network\n"
				+ "International Call Charges Discount_Partner Network\n"
				+ "International Call Charges Discount _Non-Partner Network\n"
				+ "Local Airtime Charges_Partner Network\n"
				+ "Local Airtime Charges_Non-Partner Network\n"
				+ "Local Airtime Charges Adjustment_Partner Network\n"
				+ "Local Airtime Charges Adjustment_Non-Partner Network\n"
				+ "Local Airtime Charges Discount_Partner Network\n"
				+ "Local Airtime Charges Discount_Non-Partner Network\n"
				+ "Number calling/Called\n"
				+ "Making an outgoing call\n"
				+ "SMS Charges\n"
				+ "SMS Charges Adjustment\n"
				+ "SMS Charges Discount\n"
				+ "Total Charge\n"
				+ "Usage Redeemable\n"
				+ "Usage Redeemable Adjustment\n"
				+ "Voice Mail Charges_Partner Network\n"
				+ "Voice Mail Charges_Non-Partner Network\n"
				+ "Voice Mail Charges Adjustment_Partner Network\n"
				+ "Voice Mail Charges Adjustment_Non-Partner Network\n"
				+ "Voice Mail Charges Discount_Partner Network\n"
				+ "Voice Mail Charges Discount_Non-Partner Network";
		map.put("BillItemInfoTE", billItemInfoTE);
		
		String billItemInfoE ="受話端所在地\n"
				+ "發話端所在地\n"
				+ "通話時間\n"
				+ "中國 (上海，北京，廣東三省)\n"
				+ "中國其他省份\n"
				+ "回國轉接服務費\n"
				+ "回國轉接服務費調整\n"
				+ "回國轉接服務費扣抵\n"
				+ "香港萬眾電信\n"
				+ "接聽來電\n"
				+ "合作夥伴通話費_國際\n"
				+ "非合作夥伴通話費_國際\n"
				+ "調整合作夥伴通話費_國際\n"
				+ "調整非合作夥伴通話費_國際\n"
				+ "折扣合作夥伴通話費_國際\n"
				+ "折扣非合作夥伴通話費_國際\n"
				+ "合作夥伴通話費_當地\n"
				+ "非合作夥伴通話費_當地\n"
				+ "調整合作夥伴通話費_當地\n"
				+ "調整非合作夥伴通話費_當地\n"
				+ "折扣合作夥伴通話費_當地\n"
				+ "折扣非合作夥伴通話費_當地\n"
				+ "撥打號碼/接聽號碼\n"
				+ "撥打電話\n"
				+ "簡訊服務費\n"
				+ "簡訊服務費調整\n"
				+ "簡訊服務費折扣\n"
				+ "合計費用\n"
				+ "可扣抵通信費\n"
				+ "可扣抵通信費調整\n"
				+ "非合作夥伴通話費_聽取語音信箱\n"
				+ "合作夥伴通話費_聽取語音信箱\n"
				+ "調整合作夥伴通話費_聽取語音信箱\n"
				+ "調整非合作夥伴通話費_聽取語音信箱\n"
				+ "折扣合作夥伴通話費_聽取語音信箱\n"
				+ "折扣非合作夥伴通話費_聽取語音信箱\n"
				+ "在合作夥伴網路接聽來自台灣的轉接來電\n"
				+ "境外國際漫遊通話費用\n"
				+ "優惠的部份是指在香港、中國-上海，北京，廣東三省(CHNCT）、中國其他省份(CHNOT)接聽來自台灣的轉接來電\n"
				+ "全球卡資費是以ㄧ分鐘為一個單位(未滿一分鐘將以一分鐘收費)";
		map.put("BillItemInfoE", billItemInfoE);
		
		String info ="全球卡重要消息\n"
				+ "\n"
				+ "兩段收費\n"
				+ "電信收費是依據國際交換機傳送的通話紀錄的時間來計費。由於國際電信業者系統的更新，從今年五月一日開始，"
				+ "國際受話變成兩段收費(一段為國際IDD電話、一段為國際受話)，兩段收費的總費用與原收費相同不變，但是變成兩筆通信明細。\n"
				+ "這兩筆通信明細來自不同的國際交換機（一段為國際IDD交換機、一段為國際行動電話網路的交換機），因此當您掛斷電話時，"
				+ "這兩種不同的國際交換機接收到您掛斷電話的時間可能不相同，因而這兩筆通信明細在您的帳單中會有些微時間秒差，"
				+ "同時也可能產生不同的通話時間長度。\n"
				+ "倘若您對帳單內容有疑問，我們很樂意協助您查詢！\n"
				+ "International call charges are base on the international switchboards’ recorded time frame.  Starting from 05/01/08, "
				+ "although the charges for receiving an international call remains the same, but due to our partner operator’s system "
				+ "adjustment, the charge for receiving an international call will appear in two call detail records (International IDD "
				+ "charge & Local airtime charge) on your call detail report.\n"
				+ "These two call detail records are from two different international switchboards (International IDD switchboard & Local "
				+ "operator switchboard).  The recorded time frame might be slightly different (in seconds) according to each switchboards "
				+ "system. As a result, you might experience a time difference (in seconds) in your call duration and call time.\n"
				+ "\n"
				+ "全球卡出國轉接號碼\n"
				+ "自2008/10/01起，因系統調整原因，全球卡出國轉接號碼+886931000300將更改為+85248500600。您的設定方式與轉接費用維持不變。\n"
				+ "From 2008/10/01, however the charges for forwarding calls still remain the same. But due to system adjustment, the "
				+ "forward to S2T access number will be change from +886931000300 to +85248500600.\n"
				+ "\n"
				+ "通信明細\n"
				+ "為了提供您更清楚明確的閱讀通信明細，從2008/9/1開始，全球卡通信明細陳列方式將變更，以提供您更好的電信服務。\n"
				+ "1. 舊有通信明細的分類方式以「合作夥伴國家」和「非合作夥伴國家」的方式排列；從2008/9/1起，通信明細分類方式將改為「國際服務費」"
				+ "和「全球卡境外通信費」。\n"
				+ "2. 所有通信明細依照上述分類後，將依通話時間排序，方便您對照所有通話的時間。若有任何問題請洽全球卡客服 +886277381258 或 "
				+ "+886960847009。\n"
				+ "國際服務費：在合作夥伴國家接聽從台灣轉接的來電的通話費\n"
				+ "全球卡境外通信費：包含合作夥伴國家與非合作夥伴國家的通話費\n"
				+ "Starting from 2008/9/1 World Card’s call detail report’s display order and column will be adjusted.\n"
				+ "1. World Card’s call detail report’s column will be adjusted from “Partner Networks” and “Non-Partner Network” to "
				+ "\"Roaming Charges\" and \"IDD transit from Taiwan\".\n"
				+ "2. World Card’s call detail report display order will be adjusted according to the calls’ date and time.  "
				+ "Please contact your World Card customer service representative at +886277381258 or +886960847009 for further assistant.\n"
				+ "Roaming Charges: Includes your roaming charges in partner networks or non-partner networks.\n"
				+ "IDD transit from Taiwan: IDD transit from Taiwan while receiving the calls in partner networks.";
		map.put("Info", info);
		
		return map;
	}
	private Map<String,Object> setReportParameter3(BillData data,int type){
		//參數設置
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("SUBREPORT_DIR", templatePath+"/bill/template3/");
		//地址
		map.put("address for",
				data.getI().getPostalCode()+"\n"
				+data.getI().getBillingAddressLine1()+"\n"
				+data.getI().getBillingAddressLine2()+"\n"
				+"\n"
				+data.getI().getAddressee()+"  收");
		
		
		//封面項目
		map.put("AccountBalance", data.getI().getAccountBalance());
		map.put("PaymentPosted", data.getI().getPaymentPosted());
		map.put("NewTotalAmount", data.getI().getTotalAmount());
		map.put("TotalAmountDue", data.getI().getTotalAmountDue());
		map.put("DueDate", data.getI().getDueDate());
		map.put("CustomerName", data.getI().getCustomerName());
		map.put("PaymentMethod", data.getI().getPaymentMethod());
		
		map.put("InvoiceNo", data.getI().getInvoiceNo());
		
		
		String accountName = data.getI().getAccountName();
		
		if(accountName!=null && accountName.length()>=8){
			accountName = 
					accountName.substring(0, accountName.length()-7)
					+ "***"
					+ accountName.substring(accountName.length()-4, accountName.length());

		}
		
		map.put("AccountName", accountName);
		
		
		for(BillSubData bs : data.getBS()){
			String serviceCode = bs.getServiceCode();

			if(serviceCode!=null && serviceCode.length()>=8){
				serviceCode = 
						serviceCode.substring(0, serviceCode.length()-7)
						+ "***"
						+ serviceCode.substring(serviceCode.length()-4, serviceCode.length());
			}
			bs.setServiceCode(serviceCode);
		}

		map.put("ServiceCode", data.getBS().get(data.getBS().size()-1).getServiceCode());
		map.put("Priceplan", data.getBS().get(0).getC().getPriceplan());

		map.put("Billing Period", data.getI().getCycleBeginDate()+"~"+data.getI().getCycleEndDate());
		map.put("Currency", "NTD");
		map.put("J", data.getJ());
		
		map.put("D", data.getBS().get(0).getD());
		map.put("TotalAmount", data.getBS().get(0).getC().getTotalAmount());

		map.put("BS", data.getBS());

		return map;
	}
	
	public String mark(String str,int start,int end){
		
		if(str.length()<end)
			return null;
		
		String star="";
		
		for(int i=start;i<end;i++){
			star+="*";
		}
		
		str = str.substring(0, start) + star + str.substring(end, str.length());
		
		return str;
	}
}

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
		process(filePath+"/"+"New Bill/Source/S2T_201501_PDF_with_Usage/S2T_201501_PDF_with_Usage.txt",1,1,"utf8");
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
			System.out.println("Parameter too less!");
			return;
		}
		
		Integer type2=1;
		if(arg.length>=5)
			type2=Integer.valueOf(arg[4]);
		
		String charSet=null;
		if(arg.length>=4){
			charSet=arg[3];
			System.out.println("Set charSet "+charSet);
		}
		process(arg[0],Integer.valueOf(arg[2]),type2,charSet);
	}
	private static String FileName;
	//private static final String filePath =BillReport.class.getClassLoader().getResource("").toString().replace("file:", "")+ "source/";
	
	String filePath="C:/Users/ranger.kao/Desktop";
	String templatePath="G:/Dropbox/workspace/BillReportCreater/src/";
	String exportPath="C:/Users/ranger.kao/Desktop/bill";
	
	public static void main(String[] args){
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
		customerServiceNumber="";
		
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
		}
		
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

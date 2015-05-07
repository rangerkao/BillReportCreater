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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
		 * 2:NTT
		 * 3:FET
		 * 4:iGlomo
		 */
		process(filePath+"/"+"New Bill/Source/S2T_201501_PDF_without_Usage/S2T_201501_PDF_without_Usage.txt",1);
	}
	JTextArea textPane=null;
	
	public BillReport(JTextArea textPane,String input,String output,String type){
		this.textPane=textPane;
		exportPath = output;
		templatePath="";
		process(input,Integer.valueOf(type));
		textPane.setText(textPane.getText()+"\n"+"Converting end");
	}
	public BillReport(String[] arg){
		exportPath = arg[1];
		templatePath="";
		process(arg[0],Integer.valueOf(arg[2]));
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
		
		FileName=fileName;
		
		//FileName="S2T_201404_PDF_with_Usage.txt";
		
		//FileName="85266400998.txt";
		
		
		print("filePath:"+FileName);
		
		BufferedReader reader = null;
		
		String str = null;
		String[] data;
		
		List<BillData> result = new ArrayList<BillData>();

		try {
			//reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+FileName),"unicode")); // 指定讀取文件的編碼格式，以免出現中文亂碼
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FileName))); // 指定讀取文件的編碼格式，以免出現中文亂碼

			
			
			int count=0;
			
			BillData r = null;
			BillSubData bs=null;
			while ((str = reader.readLine()) != null) {
				data=str.split("\t");
				String s= data[0];
				
				List<String> list=new ArrayList<String>();
				for (int i = 0; i < data.length; i++) {
					if(data[i]==null)
						data[i]="";
					list.add(data[i]);
				}

				
				if("I".equalsIgnoreCase(s)&& type!=3){

					/*if(count!=0){
						result.add(r);	
					}*/
					count++;
					r = new BillData();
					print("proccess "+count+" file.");
					
					result.add(r);

					r.setI(new Invoice(data));
				}else if("J".equalsIgnoreCase(s)&& type!=3){
					if(r==null) continue;
					list.add(3, null);//OrderSequence null
					r.getJ().add(new InvoiceDetail(list));
				}else if("C".equalsIgnoreCase(s)&& type!=3){
					if(r==null) continue;
					//ServiceCode
					bs = new BillSubData(data[2],data[10]);
					r.getBS().add(bs);
					bs.setC(new Charge(list));
				}else if("D".equalsIgnoreCase(s)&& type!=3){
					if(r==null) continue;
					list.add(3,null);//CategorySequence null
					bs.getD().add(new ChargeDetail(list));
				}else if("U".equalsIgnoreCase(s)){
					if(r==null) continue;
					bs.setU(new Usage(list));
				}else if("R".equalsIgnoreCase(s)){
					if(r==null) continue;
					bs.getR().add(new UsageDetail(list));
				}else if("U1".equalsIgnoreCase(s)&& type==3){
					count++;
					r = new BillData();
					bs = new BillSubData();
					r.getBS().add(bs);
					result.add(r);
					print("proccess "+count+" file.");
					bs.setU1(new Usage(list));
				}else if("U2".equalsIgnoreCase(s)&& type==3){
					bs.setU2(new Usage(list));
					//before set R2,move R data to R1
					bs.setR1(bs.getR());
					//clear R's data
					bs.setR(new ArrayList<UsageDetail>());
				}else if("P".equalsIgnoreCase(s)){
					bs.setR2(bs.getR());
					bs.setP(new UsageDiscount(list));
				}else if("U1".equalsIgnoreCase(s)&& type!=3){
					bs.setU1(new Usage(list));
				}else if("U2".equalsIgnoreCase(s)&& type!=3){
					bs.setU2(new Usage(list));
					//before set R2,move R data to R1
					bs.setR1(bs.getR());
					//clear R's data
					bs.setR(new ArrayList<UsageDetail>());
				}
			//print(str);
			}
/*			
			if(r!=null){
				result.add(r);	
			}*/
			
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
			case 2:
				templateName="billLetter/template1/billreport.jrxml";
				dataProcess1(result);
				break;
			case 3:
				templateName="bill/template2/billreport2.jrxml";
				dataProcess1(result);
				break;
			case 4:
				templateName="bill/template3/billreport3.jrxml";
				dataProcess1(result);
				break;
			default:
					
		}
		
		try {
			jasperFile=JasperCompileManager.compileReportToFile(templatePath+templateName);
			print("Load template success!");
			
			if(result!=null){
				for(int i=0;i<result.size();i++){
					try {
						print("create "+i);
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		
		
		String ds=null;
		if(data.getI()!=null)
			ds = data.getI().getCycleBeginDate();
		else
			ds = data.getBS().get(0).getU1().getCycleBeginDate();
		
		Date d = sdf.parse(ds);
		dateString = sdf2.format(d);

		switch(type){
			case 1:
			case 2:
				map = setReportParameter1(data,type);
				fileName = data.getI().getAccountNum()+"_"+dateString+"_"+data.getI().getAccountName();
				break;
			case 3:
				map = setReportParameter2(data,type);
				fileName = data.getBS().get(0).getU1().getAccountNum()+"_"+dateString+"_"+data.getBS().get(0).getU1().getAccountName();
				break;
			case 4:
				if(data.getBS().get(0).getR()!=null)
					data.getBS().get(0).setR2(data.getBS().get(0).getR());
				
				map = setReportParameter3(data,type);
				fileName = data.getI().getAccountNum()+"_"+dateString+"_"+data.getI().getAccountName();
				break;
			default:
		}
		
		if(map!=null){
			print("set parameter success!");
			String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,map,new JREmptyDataSource());
			//String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,null,new JREmptyDataSource());
			//String jrprintFile=JasperFillManager.fillReportToFile(jasperFile,map,new JRBeanCollectionDataSource(data.getBS()));
			print("create file success!"+fileName+".pdf");

			JasperExportManager.exportReportToPdfFile(jrprintFile,exportPath+"/"+fileName+".pdf");
		}else{
			print("set parameter fail!");
		}
	}
	
	private void dataProcess1(List<BillData> result){
		for(BillData r:result){
			
			for(BillSubData bs:r.getBS()){
				Double totalCurrentCharge=0D;
				for(ChargeDetail d:bs.getD()){
					if(d.getAmount()!=null)//判對是否為浮點數型態
						totalCurrentCharge=totalCurrentCharge+Double.parseDouble(d.getAmount());
				}
				bs.setTotalCurrentCharge(totalCurrentCharge);
				
				if(bs.getC()!=null)
					bs.setTotalAmount(bs.getC().getTotalAmount());

				Double tR1=0D;
				Double tR2=0D;
				Double tR3=0D;
				Double tR4=0D;
				
				List<UsageDetail> voiceUsageCharges=new ArrayList<UsageDetail>();	
				List<UsageDetail> smsCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> gprsUsageCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> mmsCharges=new ArrayList<UsageDetail>();
				
				List<UsageDetail> r1voiceUsageCharges=new ArrayList<UsageDetail>();	
				List<UsageDetail> r1smsCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> r1gprsUsageCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> r1mmsCharges=new ArrayList<UsageDetail>();
				
				List<UsageDetail> r2voiceUsageCharges=new ArrayList<UsageDetail>();	
				List<UsageDetail> r2smsCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> r2gprsUsageCharges=new ArrayList<UsageDetail>();
				List<UsageDetail> r2mmsCharges=new ArrayList<UsageDetail>();

				for(UsageDetail u : bs.getR()){
					if("Voice Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						voiceUsageCharges.add(u);	
						tR1=Double.parseDouble(u.getSubTotalCharges());
					}else if("SMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						smsCharges.add(u);
						tR2=Double.parseDouble(u.getSubTotalCharges());
					}else if("GPRS Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						gprsUsageCharges.add(u);
						tR3=Double.parseDouble(u.getSubTotalCharges());
					}else if("MMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						mmsCharges.add(u);
						tR4=Double.parseDouble(u.getSubTotalCharges());
					}
				}
				bs.setVoiceUsageCharges(voiceUsageCharges);
				bs.setSmsCharges(smsCharges);
				bs.setGprsUsageCharges(gprsUsageCharges);
				bs.setMmsCharges(mmsCharges);
				bs.setTotalUsageCharge(tR1+tR2+tR3+tR4);

				
				for(UsageDetail u : bs.getR1()){
					if("語音通話費".equalsIgnoreCase(u.getChargeItemName())||
							"Voice Calling Charges".equalsIgnoreCase(u.getChargeItemName())||
							"Voice Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						r1voiceUsageCharges.add(u);
					}else if("簡訊服務費".equalsIgnoreCase(u.getChargeItemName())||
							"SMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						r1smsCharges.add(u);
					}else if("GPRS Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						r1gprsUsageCharges.add(u);
					}else if("MMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						r1mmsCharges.add(u);
					}
				}
				if(bs.getU1()!=null)
					bs.setU1total(bs.getU1().getTotalCharge());
				bs.setR1voiceUsageCharges(r1voiceUsageCharges);
				bs.setR1smsCharges(r1smsCharges);
				bs.setR1gprsUsageCharges(r1gprsUsageCharges);
				bs.setR1mmsCharges(r1mmsCharges);
				
				for(UsageDetail u : bs.getR2()){
					if("Voice Calling Charges".equalsIgnoreCase(u.getChargeItemName())||
							"Voice Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						r2voiceUsageCharges.add(u);	
					}else if("SMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						r2smsCharges.add(u);
					}else if("GPRS Usage Charges".equalsIgnoreCase(u.getChargeItemName())){
						r2gprsUsageCharges.add(u);
					}else if("MMS Charges".equalsIgnoreCase(u.getChargeItemName())){
						r2mmsCharges.add(u);
					}
				}
				if(bs.getU2()!=null)
					bs.setU2total(bs.getU2().getTotalCharge());
				bs.setR2voiceUsageCharges(r2voiceUsageCharges);
				bs.setR2smsCharges(r2smsCharges);
				bs.setR2gprsUsageCharges(r2gprsUsageCharges);
				bs.setR2mmsCharges(r2mmsCharges);
				
			}
		}
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
		
		if(type==1){
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
		
		map.put("U1total", data.getBS().get(0).getU1().getTotalCharge());
		map.put("U2total", data.getBS().get(0).getU2().getTotalCharge());
		
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

		map.put("R1", data.getBS().get(0).getR1());
		map.put("R", data.getBS().get(0).getR());
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

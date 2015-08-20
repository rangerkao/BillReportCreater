package bill.bean;

import java.util.ArrayList;
import java.util.List;

public class BillSubData {

	private String serviceCode;
	private String priceplan;
	private Charge c;
	private List<ChargeDetail> d;
	private Usage u;
	private List<UsageDetail> r;
	
	//for report preproccess
	private Double totalCurrentCharge;
	private Double totalUsageCharge;
	
	//c's total
	private String totalAmount;

	private String u1total;
	private String u2total;
	
	//for template 3 
	private Usage u1;
	private List<UsageDetail> r1;
	private Usage u2;
	private List<UsageDetail> r2;
	private UsageDiscount p;
	
	private List<UsageDetail> voiceUsageCharges;
	private List<UsageDetail> smsCharges;
	private List<UsageDetail> gprsUsageCharges;
	private List<UsageDetail> mmsCharges;
	
	private List<UsageDetail> r1voiceUsageCharges;
	private List<UsageDetail> r1smsCharges;
	private List<UsageDetail> r1gprsUsageCharges;
	private List<UsageDetail> r1mmsCharges;
	
	private List<UsageDetail> r2voiceUsageCharges;
	private List<UsageDetail> r2smsCharges;
	private List<UsageDetail> r2gprsUsageCharges;
	private List<UsageDetail> r2mmsCharges;
	
	
	public BillSubData(){
		create();
	}
	
	public BillSubData(String serviceCode,String priceplan){
		create();
		this.serviceCode = serviceCode;
		this.priceplan = priceplan;
	}
	
	

	public BillSubData(String serviceCode, Charge c, List<ChargeDetail> d,
			Usage u, List<UsageDetail> r) {
		create();
		this.serviceCode = serviceCode;
		this.c = c;
		this.d = d;
		this.u = u;
		this.r = r;
	}
	
	public void create(){
		d=new ArrayList<ChargeDetail>();
		r=new ArrayList<UsageDetail>();

		r1=new ArrayList<UsageDetail>();
		r2=new ArrayList<UsageDetail>();
		
		voiceUsageCharges=new ArrayList<UsageDetail>();	
		smsCharges=new ArrayList<UsageDetail>();
		gprsUsageCharges=new ArrayList<UsageDetail>();
		mmsCharges=new ArrayList<UsageDetail>();
		
		r1voiceUsageCharges=new ArrayList<UsageDetail>();	
		r1smsCharges=new ArrayList<UsageDetail>();
		r1gprsUsageCharges=new ArrayList<UsageDetail>();
		r1mmsCharges=new ArrayList<UsageDetail>();
		
		r2voiceUsageCharges=new ArrayList<UsageDetail>();	
		r2smsCharges=new ArrayList<UsageDetail>();
		r2gprsUsageCharges=new ArrayList<UsageDetail>();
		r2mmsCharges=new ArrayList<UsageDetail>();
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Charge getC() {
		return c;
	}

	public void setC(Charge c) {
		this.c = c;
	}

	public List<ChargeDetail> getD() {
		return d;
	}

	public void setD(List<ChargeDetail> d) {
		this.d = d;
	}

	public Usage getU() {
		return u;
	}

	public void setU(Usage u) {
		this.u = u;
	}

	public List<UsageDetail> getR() {
		return r;
	}

	public void setR(List<UsageDetail> r) {
		this.r = r;
	}

	public Double getTotalCurrentCharge() {
		return totalCurrentCharge;
	}

	public void setTotalCurrentCharge(Double totalCurrentCharge) {
		this.totalCurrentCharge = totalCurrentCharge;
	}

	public Double getTotalUsageCharge() {
		return totalUsageCharge;
	}

	public void setTotalUsageCharge(Double totalUsageCharge) {
		this.totalUsageCharge = totalUsageCharge;
	}

	public String getPriceplan() {
		return priceplan;
	}

	public void setPriceplan(String priceplan) {
		this.priceplan = priceplan;
	}

	public Usage getU1() {
		return u1;
	}

	public void setU1(Usage u1) {
		this.u1 = u1;
	}

	public List<UsageDetail> getR1() {
		return r1;
	}

	public void setR1(List<UsageDetail> r1) {
		this.r1 = r1;
	}

	public Usage getU2() {
		return u2;
	}

	public void setU2(Usage u2) {
		this.u2 = u2;
	}

	public List<UsageDetail> getR2() {
		return r2;
	}

	public void setR2(List<UsageDetail> r2) {
		this.r2 = r2;
	}

	public UsageDiscount getP() {
		return p;
	}

	public void setP(UsageDiscount p) {
		this.p = p;
	}

	public List<UsageDetail> getVoiceUsageCharges() {
		return voiceUsageCharges;
	}

	public void setVoiceUsageCharges(List<UsageDetail> voiceUsageCharges) {
		this.voiceUsageCharges = voiceUsageCharges;
	}

	public List<UsageDetail> getSmsCharges() {
		return smsCharges;
	}

	public void setSmsCharges(List<UsageDetail> smsCharges) {
		this.smsCharges = smsCharges;
	}

	public List<UsageDetail> getGprsUsageCharges() {
		return gprsUsageCharges;
	}

	public void setGprsUsageCharges(List<UsageDetail> gprsUsageCharges) {
		this.gprsUsageCharges = gprsUsageCharges;
	}

	public List<UsageDetail> getMmsCharges() {
		return mmsCharges;
	}

	public void setMmsCharges(List<UsageDetail> mmsCharges) {
		this.mmsCharges = mmsCharges;
	}

	public List<UsageDetail> getR1voiceUsageCharges() {
		return r1voiceUsageCharges;
	}

	public void setR1voiceUsageCharges(List<UsageDetail> r1voiceUsageCharges) {
		this.r1voiceUsageCharges = r1voiceUsageCharges;
	}

	public List<UsageDetail> getR1smsCharges() {
		return r1smsCharges;
	}

	public void setR1smsCharges(List<UsageDetail> r1smsCharges) {
		this.r1smsCharges = r1smsCharges;
	}

	public List<UsageDetail> getR1gprsUsageCharges() {
		return r1gprsUsageCharges;
	}

	public void setR1gprsUsageCharges(List<UsageDetail> r1gprsUsageCharges) {
		this.r1gprsUsageCharges = r1gprsUsageCharges;
	}

	public List<UsageDetail> getR1mmsCharges() {
		return r1mmsCharges;
	}

	public void setR1mmsCharges(List<UsageDetail> r1mmsCharges) {
		this.r1mmsCharges = r1mmsCharges;
	}

	public List<UsageDetail> getR2voiceUsageCharges() {
		return r2voiceUsageCharges;
	}

	public void setR2voiceUsageCharges(List<UsageDetail> r2voiceUsageCharges) {
		this.r2voiceUsageCharges = r2voiceUsageCharges;
	}

	public List<UsageDetail> getR2smsCharges() {
		return r2smsCharges;
	}

	public void setR2smsCharges(List<UsageDetail> r2smsCharges) {
		this.r2smsCharges = r2smsCharges;
	}

	public List<UsageDetail> getR2gprsUsageCharges() {
		return r2gprsUsageCharges;
	}

	public void setR2gprsUsageCharges(List<UsageDetail> r2gprsUsageCharges) {
		this.r2gprsUsageCharges = r2gprsUsageCharges;
	}

	public List<UsageDetail> getR2mmsCharges() {
		return r2mmsCharges;
	}

	public void setR2mmsCharges(List<UsageDetail> r2mmsCharges) {
		this.r2mmsCharges = r2mmsCharges;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getU1total() {
		return u1total;
	}

	public void setU1total(String u1total) {
		this.u1total = u1total;
	}

	public String getU2total() {
		return u2total;
	}

	public void setU2total(String u2total) {
		this.u2total = u2total;
	}

	
	
	
}

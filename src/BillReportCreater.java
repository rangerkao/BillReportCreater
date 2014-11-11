

public class BillReportCreater {

	public static void main(String[] args){
		args=new String[]{"85266400998.txt"};
		if(args.length>0){
			for(String name: args){
				BillReport r =new BillReport();
				r.process(name);
				System.out.println("File Create Finished!");
			}
		}else{
			System.out.println("No fileName submit!");
		}
		
	}
}

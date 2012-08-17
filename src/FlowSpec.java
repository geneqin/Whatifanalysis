import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


public class FlowSpec {
	
	//public static void main(String args[]) throws IOException{
		 
	    /*
	    Use System.out.println() to print on console.
	    */
	private String flowfile;
	private String processorfile;
	
	private  List<Flow> flowlist;
    private List<Processor> prolist;
    private Network nw;
    
    public Set<Integer> nodeset;


	public List<Flow> getFlowlist() {
		return flowlist;
	}


	public boolean setFlowlist(List<Flow> flowlist) {
		if(flowlist==null)
			return false;
		this.flowlist = flowlist;
		return true;
	}
	


	public FlowSpec(String s1, String s2, Network nw)throws IOException{
		
		flowfile=s1;
		processorfile=s2;
		this.nw=nw;
		nodeset = new HashSet<Integer>();
		this.prolist =new ArrayList<Processor>();
		System.out.println("Belows are the Maude Specification :");
	    FileReader reader = new FileReader(flowfile);//flow.txt
	    BufferedReader br = new BufferedReader(reader);
	    FileReader reader2= new FileReader(processorfile);//processor.txt
		BufferedReader br2= new BufferedReader(reader2);
	   
	    
	//    FileWriter fw = new FileWriter("net2.maude");
      //  BufferedWriter bw = new BufferedWriter(fw);    
              
	   
	     this.flowlist=new ArrayList<Flow>();
	     
	    
	    for (;;)  
		      try {

		        //Read one line (throws EOFException if there are no more lines);
		        //  create a StringTokenizer for it; use the StringTokenizer to
		        //  extract the first name on the line (the name of the candidate)
		        String line = br.readLine();
		        if(line==null)
		        	break;
		        String[] stringArray=line.split(" ");
		        if(stringArray.length!=6) // route id type tp itemlength lamda
			    	System.out.println("invalid num of paras for flow: "+stringArray.length);
		        String[] routeArray=stringArray[0].split(",");		        

			    
			    int id=Integer.parseInt(stringArray[1]);
			    int type=Integer.parseInt(stringArray[2]);
			    float tp=Float.parseFloat(stringArray[3]);
			    float itemlength=Float.parseFloat(stringArray[4]);
			    float lamda=Float.parseFloat(stringArray[5]);
			    List<Integer> route=new ArrayList<Integer>();
			    List<Double> nics=new ArrayList<Double>();
			    for (int i=0; i<routeArray.length; i++){
			    	route.add(Integer.parseInt(routeArray[i]));
			    	if(i<routeArray.length-1)
			    		nics.add(nw.graph.adj[Integer.parseInt(routeArray[i])][Integer.parseInt(routeArray[i+1])]);
			    	}
		      
			    Flow f1;
			   
			    f1=new Flow(route, nics, id, type, tp, itemlength, lamda);
			    flowlist.add(f1);
			   
			    
			     }catch (EOFException e) {break;}
	    
	    for(;;)
	    	  try{
	    		    String line2=br2.readLine();
	    		    if(line2==null){
	    		    	System.out.println(" .");
			        	break;
	    		    }
				    String[] stringArray2=line2.split(" ");
				    if(stringArray2.length!=4){
				    	System.out.println("invalid para for processor"+stringArray2[0]);
				    	return ;
				    }
				    int nodeid=Integer.parseInt(stringArray2[0]);
				    nodeset.add(nodeid);
				    int nic=Integer.parseInt(stringArray2[1]);
				    nodeset.add(nic);
				    float qlenth=Integer.parseInt(stringArray2[2]);
				    float xrate=Float.parseFloat(stringArray2[3]);
				    int total=0;
				    for(Iterator<Flow> it = flowlist.iterator(); it.hasNext();){
				    	for(Iterator<Integer> subit=it.next().route.iterator();subit.hasNext();){
				    		if(subit.next()==nodeid&&subit.hasNext()){
				    			int nexthop=subit.next();
				    			if (nw.graph.adj[nodeid][nexthop]==100&&nic==1) //nic 1 is the ethernet card
				    				total++;
				    			if (nw.graph.adj[nodeid][nexthop]==10&&nic==2) //nic 2 is the infra wifi card
				    				total++;
				    			if (nw.graph.adj[nodeid][nexthop]==2&&nic==3) // nic 3 is the ad hoc wifi/bt card
				    				total++;
				    	}
				    		}
							
				    		
				    	}
				        
				    
				    Processor p=new Processor(nodeid, nic, 100f, 0.0f, qlenth,xrate,total);
				    prolist.add(p);    
				   
				    
	    	}catch (EOFException e){break;}
		   
		  
	       br.close();
		   reader.close();
		   br2.close();
		   reader2.close();
		   
		   
		
		   
		
	}

	public void ResetProc(Network nw) throws IOException{
		   
		   for(Iterator<Processor> p=this.prolist.iterator();p.hasNext();){
			   p.next().TotalFlow=0; // first clear all totalflow number
		   }
		   int[][] flownumber= new int[nw.nodeNum][3];
		   Flow f=new Flow();
		   for(Iterator<Flow> fl=this.flowlist.iterator();fl.hasNext();){
			   f=fl.next();
			   for(int i=0; i<f.route.size()-1;i++){
				   if(nw.graph.adj[f.route.get(i)][f.route.get(i+1)]==100){//can also be got by accessing f.nics
					   flownumber[f.route.get(i)][0]++;
				   }
				   else if(nw.graph.adj[f.route.get(i)][f.route.get(i+1)]==10){
					   flownumber[f.route.get(i)][1]++;
				   }
				   else if(nw.graph.adj[f.route.get(i)][f.route.get(i+1)]==2){
					   flownumber[f.route.get(i)][2]++;
				   }
			   }
		   }
		   
		   for(Iterator<Processor> p=this.prolist.iterator();p.hasNext();){
			   Processor proc=p.next();
			   proc.TotalFlow=flownumber[proc.NodeId][proc.NicType-1]; // first clear all totalflow number
		   }
		 
		 
		
		
	}
	
	public void generator(int failednode)throws IOException{
	    
	    
	    FileWriter fw = new FileWriter("30nodes.maude",true);
        BufferedWriter bw = new BufferedWriter(fw);    
        bw.write("op net"+failednode+" : -> Configuration .");
        bw.newLine();
        bw.flush();    
        bw.write("eq net"+failednode+" = ");
        bw.newLine();
        bw.flush();
        
		int i=0;
		   for(Iterator<Flow> it = flowlist.iterator(); it.hasNext();){
			   
			   	Flow f=it.next();
			    int id=f.flowId;
			    int type=f.type;
			    float tp=f.tp;
			    float itemlength=f.itemlenth;
			    float lamda=f.prob;
			    List<Integer> route=new ArrayList<Integer>();
			    route=f.route;
			    int nic=0;
			    i=0;
			    for (Iterator<Integer> rt=route.iterator();rt.hasNext();){
			    	
			    	if(i<(f.nics.size())){
				    	if(f.nics.get(i)==100)
				    		nic=1;
				    	if(f.nics.get(i)==10)
				    		nic=2;
				    	if(f.nics.get(i)==2)
				    		nic=3;
			    	}
			    	
			    	if(i==0){//first sub flow and bernoulli arrival
			    		
				    	System.out.println("SF("+id+" | "+(i+1)+" | "+" N( "+route.get(i)+" ) : Nic("+nic+") -> N( "+route.get(i+1)+" ) : Type("+type+") : false | " +
				    			"PoiPara( "+lamda+" , "+itemlength+" ) | delay : 0.0, throughput : "+tp+
				    			", jitter : 0.0, packetloss : 0.0, itemlenth : "+itemlength+" | unknown)");
				    	bw.write("SF("+id+" | "+(i+1)+" | "+" N( "+route.get(i)+" ) : Nic("+nic+") -> N( "+route.get(i+1)+" ) : Type("+type+") : false | " +
				    			"PoiPara( "+lamda+" , "+itemlength+" ) | delay : 0.0, throughput : "+tp+
				    			", jitter : 0.0, packetloss : 0.0, itemlenth : "+itemlength+" | unknown)");
				    	bw.newLine();
				    	bw.flush();
			    	}
			    	
			    	if(i>0&&i<route.size()-1) {
			    		System.out.println("SF("+id+" | "+(i+1)+" | "+" N( "+route.get(i)+" ) : Nic("+nic+") -> N( "+route.get(i+1)+" ) : Type("+type+") : false | " +
				    			"PoiPara( 0.0 , 0.0 ) | unknown | unknown)");
			    		bw.write("SF("+id+" | "+(i+1)+" | "+" N( "+route.get(i)+" ) : Nic("+nic+") -> N( "+route.get(i+1)+" ) : Type("+type+") : false | " +
				    			"PoiPara( 0.0 , 0.0 ) | unknown | unknown)");
			    		bw.newLine();
			    		bw.flush();
			    	}
				      
			    	i++;
			    	rt.next();
			    }
		    
		    }
		   
		   for (Iterator<Processor> pr=prolist.iterator();pr.hasNext();){
			    Processor p=pr.next();
			    int nodeid=p.NodeId;
			    int nic=p.NicType;
			    float qlenth=p.Qlength;
			    float xrate=p.Xrate;
			    int total=p.TotalFlow;
		  
			    System.out.println("N( "+nodeid+" : Nic("+nic+") | "+ "P(0.0) | M(0.0) | X("+
			    xrate+") | Q("+qlenth+") | PoiPara( 0.0 , 0.0 ) { "+
			    		total+" | 0 })");
			    
			    bw.write("N( "+nodeid+" : Nic("+nic+") | "+ "P(0.0) | M(0.0) | X("+
			    xrate+") | Q("+qlenth+") | PoiPara( 0.0 , 0.0) { "+
			    		total+" | 0 })");
			    bw.newLine();
			    bw.flush();
			    
			    
		   }
		        bw.write(" .");
				bw.newLine();
				bw.flush();
		  

	}
	
	public static void main(String args[]) throws IOException{
	
	//	Network nw=new Network("network30.txt");
		//FlowSpec fs=new FlowSpec("flow30.txt","processor30new.txt",nw);
		//fs.generator(0);
	}
}
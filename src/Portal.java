import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Portal {

	void generateprocessor(String networkfile) throws IOException{
		    FileReader reader = new FileReader(networkfile);//flow.txt
		    BufferedReader br = new BufferedReader(reader);
		    FileWriter fw = new FileWriter("processor30.txt");
		    BufferedWriter bw = new BufferedWriter(fw);    
		    
		       
		    for (;;)  
			      try {

			        String line = br.readLine();
			        boolean nic1=false;
			        boolean nic2=false;
			        boolean nic3=false;
			        if(line==null)
			        	break;
			        String[] stringArray=line.split(" ");
			        if(stringArray.length!=3) // route id type tp itemlength lamda
				    	System.out.println("invalid num of paras for flow: "+stringArray.length);
			        String[] nei=stringArray[1].split(",");		        
			        String[] rate=stringArray[2].split(",");		        
				    
				    int id=Integer.parseInt(stringArray[0]);
				    for(int i=0; i<rate.length;i++){
				    	if(Integer.parseInt(rate[i])==100)
				    		nic1=true;
				    	
				    	if(Integer.parseInt(rate[i])==10)
				    		nic2=true;				    	
				    	if(Integer.parseInt(rate[i])==2)
				    		nic3=true;		    	
				    }
				    
				    if(nic1){
				    	bw.write(id+" "+"1"+" "+"50 "+"100");
				    	bw.newLine();
				    }
				    if(nic2){
				    	bw.write(id+" "+"2"+" "+"50 "+"10");
				    	bw.newLine();
				    }
				    if(nic3){
				    	bw.write(id+" "+"3"+" "+"50 "+"2");
				    	bw.newLine();
				    }
				    
			      }catch(IOException e) {break;}
		    
		    reader.close();
		    br.close();
		    bw.close();
		    fw.close();
		
		
	}
	public static void main(String args[]) throws IOException{
		Network nw=new Network("network30.txt");
		FlowSpec fs=new FlowSpec("flow30.txt","processor30.txt",nw);
		int failednode=0;
		fs.generator(failednode); 
     	List<Flow> Flowlist= new ArrayList<Flow>();
		Flowlist=fs.getFlowlist();
		for(Iterator<Integer> s=fs.nodeset.iterator();s.hasNext();){
			failednode=s.next();
			if(fs.setFlowlist(nw.ResetFlow(failednode, Flowlist))){// resetflow(failednode, currentflowlist)
				fs.ResetProc(nw);
				fs.generator(failednode);
			}
			nw.Restore();
		}
		
	//	Portal p=new Portal();
		//p.generateprocessor("network30.txt"); // generate processor.txt
	}
}

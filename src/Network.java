/*
Implemented by Zhijing 2012.08.01
This class first reads network topology from the txt file, and then calculate the shortest path between two given nodes.
by modifying the topology, this class can calculates the new path between two nodes. 

*/

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Network {
	
	public  final int INF=1000;
	public AdjMatrixEdgeWeightedDigraph graph;
	private static double[][] dist;
	private static int[][] next;
	private static List<HashMap> path;
	private static HashMap<Integer, Integer> link;
	
	private double[][] distbackup;
	private int[][] nextbackup;
	private double [][] adjbackup;
	
	int nodeNum=30;
	
	public Network(){
			this.graph=new AdjMatrixEdgeWeightedDigraph(10,20);	
			
	}
	
	public Network(String filename) throws IOException{
		
		
		
		dist=new double[nodeNum][nodeNum];
		next=new int[nodeNum][nodeNum];
		link=new HashMap<Integer,Integer>();
		path=new ArrayList<HashMap>();
		
		
		adjbackup=new double[nodeNum][nodeNum];
		
		FileReader reader = new FileReader(filename);
		BufferedReader br = new BufferedReader(reader);
		FileReader reader2 = new FileReader(filename);
		BufferedReader br2 = new BufferedReader(reader);
	/*	 while (br2.readLine() != null) {
             nodeNum++;
         }
		*/ 
		this.graph=new AdjMatrixEdgeWeightedDigraph(nodeNum);
		 
	    for (;;)
		    try {
		        String line = br.readLine();
		        
		        if(line==null){
		        	
		 		   break;
		        }
		        String[] stringArray=line.split(" ");
		        if(stringArray.length!=3)
			    	System.out.println("invalid num of paras for flow: "+stringArray.length);
		        int currentNode=Integer.parseInt(stringArray[0]);
		        String[] neiArray=stringArray[1].split(",");	
		        String[] neiweiArray=stringArray[2].split(",");
		        if(neiArray.length!=neiweiArray.length){
		        	System.out.println("neibor number is not consistent with weight");
		        	return ;
		        }
		        
		        for(int i=0; i<neiArray.length;i++){
		        	this.graph.AddEdge(currentNode, Integer.parseInt(neiArray[i]), Integer.parseInt(neiweiArray[i]));
		        	System.out.println("add "+currentNode+
		        			","+Integer.parseInt(neiArray[i]));
		        }
		        	
 
		    }catch (IOException e) {System.out.println("error==");break;}
	    
	    
	    for(int i=0; i<nodeNum;i++)
	    	   for(int j=0; j<nodeNum;j++)
	    		   this.adjbackup[i][j]=this.graph.adj[i][j];//store the original network information
	       br.close();
		   reader.close();
		   br2.close();
		   reader2.close();
	       
	    
		  
	}
	
	public void Restore(){
		 for(int i=0; i<nodeNum;i++)
	    	   for(int j=0; j<nodeNum;j++)
	    		   this.graph.adj[i][j]=this.adjbackup[i][j];//store the original network information
	     
	}
	public void BuildShortTable(int v, int w){
		for(int i=0;i<nodeNum;i++)
			for(int j=0; j<nodeNum;j++){
				if(graph.adj[i][j]==0||graph.adj[i][j]==-1)
					dist[i][j]=INF;//use 100 rather than -1 to indicating the unreachable is to easily compute path
				else{
					dist[i][j]=1;////weight is 1, which means use the hop count as metric
				}
			}
		
		for(int i=1;i<nodeNum;i++)
			for(int n=1; n<nodeNum;n++){
				for(int m=1; m<nodeNum;m++)
					if(dist[n][m]>dist[n][i]+dist[i][m]){
						dist[n][m]=dist[n][i]+dist[i][m];
						next[n][m]=i;
				}	
			}
		path.clear();
		FSP(v,w);
			
			
		//PrintSP(v,w);
	}
	//find shoetest path
	private int FSP(int v, int w){
		
		
		if (dist[v][w]==INF){
			System.out.println("No path");
			return 0;
		}
				
		int intermediate=next[v][w];
		if(intermediate==0){
			System.out.println("direct shortest path: "+v+"->"+w);
			link.put(v, w);
			path.add(link);
			return 0;
		}
		else{
			
			FSP(v,intermediate);
			FSP(intermediate,w);
		}
		
		return intermediate;
		
	}
	
	private void PrintSP(int v, int w){
		int source=v;
		
		for(Iterator<HashMap> it = path.iterator(); it.hasNext();){
			int dest=(Integer) it.next().get(source);
			System.out.print(source+"->");
			source=dest;
			
		}
		
		System.out.println(source);
		
	}
	
	public List<Flow>  ResetFlow(int nodeid, List<Flow> flowlist){
		System.out.println("node "+nodeid+" failed");
		graph.NodeFailed(nodeid);
		List<Flow> newflowlist=new ArrayList<Flow>();
		Flow currentflow=new Flow();
		
		for(Iterator<Flow> fl=flowlist.iterator();fl.hasNext();){
			int src=0;
			int dest=0;
			List<Integer> route=new ArrayList<Integer>();
			List<Integer> newroute= new ArrayList<Integer>();
			List<Double> nics=new ArrayList<Double>();
			currentflow=fl.next();
			route=currentflow.route;
			src=route.get(0);
			for(Iterator<Integer> rt=route.iterator();rt.hasNext();){
				dest=rt.next();
			}
			if(nodeid==src||nodeid==dest){
				System.out.println("Flow "+currentflow.flowId+" will be lost since src/dest dead");
			}
				
			BuildShortTable(src,dest);
			if(path.size()==0){
				System.out.println("No path between "+src+" and "+dest);
				System.out.println("");
				return null;
			}
				
			for(Iterator<HashMap> it = path.iterator(); it.hasNext();){
				int next=(Integer) it.next().get(src);
				nics.add(graph.adj[src][next]);
				System.out.print(src+"->");
				newroute.add(src);
				src=next;
				
			}
			newroute.add(src);
			currentflow.route=newroute;
			currentflow.nics=nics;
			System.out.println(src);
			newflowlist.add(currentflow);
		}
	
		return newflowlist;
	}
	
	

}

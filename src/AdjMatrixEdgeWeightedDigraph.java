
/*************************************************************************
 *  Compilation:  javac AdjMatrixEdgeWeightedDigraph.java
 *  Execution:    java AdjMatrixEdgeWeightedDigraph V E
 *  Dependencies: StdOut.java
 *
 *  An edge-weighted digraph, implemented using an adjacency matrix.
 *  Parallel edges are disallowed; self-loops are allowd.
 *  
 *************************************************************************/




public class AdjMatrixEdgeWeightedDigraph {
    private int V;
    private int E;
    public double[][] adj;
    // empty graph with V vertices
    public AdjMatrixEdgeWeightedDigraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        this.adj = new double[V][V];
    }

    // random graph with V vertices and E edges
    public AdjMatrixEdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        if (E > V*V) throw new RuntimeException("Too many edges");
        this.V=V;
        this.E=0;
        // can be inefficient
        while (this.E != E) {
            int v = (int) (V * Math.random());
            int w = (int) (V * Math.random());
            double weight = (Math.round(100 * Math.random()) +1)/ 100.0;
            if(v!=0&&w!=0&&v!=w)
            	adj[v][w]=weight;
            this.E++;
        }
    }
    
    

    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }




    
     
     public void NodeFailed(int v){
    	     	
    	for (int i=0;i<V;i++){
    		adj[v][i]=-1;
    		adj[i][v]=-1;    		
    	}
    	
    	    	
    }
     
     public void AddEdge(int v, int w, double weight){
    	 
    	 if(this.adj[v][w]>0) throw new RuntimeException("edge already exists");  		 
    	 this.adj[v][w]=weight;
    	 E++;
    	 
     }
     
    
     public void print(){
    	 System.out.println(V+" "+E);
    	 int current=0;
    	 int current1=0;
    	 for(int i=0; i<V; i++){
    		 System.out.print(i+" ");
    		 current=1;
    		 current1=1;
    		 for(int n=0; n<V;n++){
    			 if(adj[i][n]>0){
    				 if(current==0)
    				 System.out.print(","+n);
    				 else
    			     System.out.print(n);
    				 current=0;
    			 }   			 
    		 }
    		 System.out.print(" ");
    		 
    		 for(int n=0; n<V;n++){
    			 if(adj[i][n]>0){
    				 if(current1==0)
        				 System.out.print(","+adj[i][n]);
        				 else
        			     System.out.print(n);
        				 current1=0;
    			 }
    		 }
    		 System.out.println(" ");
    	 }
     }
    // test client
    public static void main(String[] args) {
    	
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(10, 90);
        G.print();
       
    }

}


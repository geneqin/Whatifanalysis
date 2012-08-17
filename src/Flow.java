import java.util.ArrayList;
import java.util.List;


public class Flow {

	List<Integer> route;
	List<Double> nics;
	int flowId;
	int type;
	float tp;
	float itemlenth;
	int num; //maximum number of packets in one time slot
	float prob; //probability in bernoulli or mmrp
	Flow(){
		route=new ArrayList<Integer>();	
		nics=new ArrayList<Double>();
		flowId=0;
		type=0;
		tp=0;
		itemlenth=0;
		prob=0;
		
	}
	Flow(List<Integer> fl, List<Double> nic, int id, int t, float p, float length, float lamda){
		//bernouli process has two paras n, probability
		route=fl;
		nics=nic;
		flowId=id;
		type=t;
		tp=p;
		itemlenth=length;
		prob=lamda;
		
	}
	
}

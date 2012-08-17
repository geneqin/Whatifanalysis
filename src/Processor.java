
public class Processor {
	
	int NodeId;
	int NicType;
	float Powerlevel;
	float Mobility;
	float Qlength;
	float Xrate;
	int TotalFlow;
	
	Processor(int id, int nic, float level, float m, float q, float rate, int t){
		NodeId=id;
		NicType=nic;
		Powerlevel=level;
		Mobility=m;
		Qlength=q;
		Xrate=rate;
		TotalFlow=t;
		
	}

}

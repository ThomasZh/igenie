package com.oct.ga.gatekeeper.loadbalance;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public class RandomReplicaHandler implements ReplicaHandler
{
//	protected RemoteReference chooseReplica(RemoteReference remotereference, Method method, Object aobj[]) {
//	        RandomReplicaHandler randomreplicahandler = this;
//	        //JVM INSTR monitorenter ;
//	        ReplicaList replicalist;
//	        int i;
//	        replicalist = getReplicaList();
//	        i = replicalist.size();
//	        if(i == 0)
//	            return remotereference;
//	        int j;
//	        double d = Math.random() * (double)i + 0.5D;  // Ëæ»úÊý
//	        j = (int)Math.round(d) - 1;
//	        replicalist.get(j);
//	        randomreplicahandler;
//	        //JVM INSTR monitorexit ;
//	        return;
//	        Exception exception;
//	        exception;
//	        throw exception;
//	    }

	@Override
	public StpServerInfoJsonBean chooseReplica(String clientVersion)
	{
		// TODO Auto-generated method stub
		return null;
	}
}

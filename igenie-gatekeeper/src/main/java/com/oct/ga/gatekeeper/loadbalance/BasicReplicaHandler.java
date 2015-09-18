package com.oct.ga.gatekeeper.loadbalance;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public class BasicReplicaHandler implements ReplicaHandler
{
//	protected RemoteReference chooseReplica(RemoteReference remotereference, Method method, Object aobj[])
//    {
//        ReplicaList replicalist = replicaList;
//        //JVM INSTR monitorenter ;
//        int i = replicaList.size();
//        if(i == 0)
//            return remotereference;
//        current = (current + 1) % i;//选下一个
//        replicaList.get(current);
//        replicalist;
//        //JVM INSTR monitorexit ;
//        return;
//        Exception exception;
//        exception;
//        throw exception;
//    }

	@Override
	public StpServerInfoJsonBean chooseReplica(String clientVersion)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
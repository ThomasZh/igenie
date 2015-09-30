package com.oct.ga.gatekeeper.loadbalance;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public class WeightBasedReplicaHandler implements ReplicaHandler
{
//	protected RemoteReference chooseReplica(RemoteReference remotereference, Method method, Object aobj[])
//	    {
//	        RichReplicaList richreplicalist = (RichReplicaList)getReplicaList();
//	        RemoteReference remotereference1 = remotereference;
//	        synchronized(richreplicalist)
//	        {
//	            if(infoArray == null || infoArray.length == 0)
//	                reinitializeWeightInfo(remotereference.getHostID());
//	            if(size != 0)
//	            {
//	                for(int i = lastIndex; i < size + lastIndex; i++)
//	                {
//	                    listIter = ++listIter % size;
//	                    if(listIter == 0)
//	                        loopCounter = ++loopCounter % maxIter;
//	                    int j = i % size;
//	                    if(infoArray[j].getNormalizedWeight() <= loopCounter)
//	                        continue;
//	                    remotereference1 = richreplicalist.findReplicaHostedBy(infoArray[j].getID());
//	                    if(remotereference1 == null)
//	                        continue;
//	                    lastIndex = ++j;
//	                    break;
//	                }
//	            }
//	        }
//	        return remotereference1;
//	    }
//	    
//	    
//	    maxIter:
//	    
//	    
//	     void resetAndNormalizeWeights()
//	    {
//	        int i = 0x7fffffff; //32
//	        int j = 0x80000000; //32
//	        boolean flag = true;
//	        synchronized(this)
//	        {
//	            serverInfoArray = ServerInfoManager.theOne().getServerInfos();
//	            for(int k = 0; k < serverInfoArray.length; k++)
//	            {
//	                int i1 = serverInfoArray[k].getLoadWeight();
//	                flag &= i1 % 10 == 0;
//	                if(i > i1)
//	                    i = i1;   
//	                if(i1 > j)
//	                    j = i1;  
//	            }
//	            int l = 0;
//	            if(serverInfoArray.length > 0)
//	                while(serverInfoArray[l].getLoadWeight() % i == 0 && ++l < serverInfoArray.length) ;
//	            if(l > serverInfoArray.length)
//	            {
//	                normalizeWeights(i, serverInfoArray);
//	                loopIter = j / i;
//	            } else
//	            if(flag)
//	            {
//	                normalizeWeights(10, serverInfoArray);
//	                loopIter = j / 10;
//	            } else
//	            {
//	                normalizeWeights(5, serverInfoArray);
//	                loopIter = j / 5;
//	            }
//	        }
//	    }


		@Override
		public StpServerInfoJsonBean chooseReplica(String clientVersion)
		{
			// TODO Auto-generated method stub
			return null;
		}
}

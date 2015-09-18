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
//	                    listIter = ++listIter % size;//用于判断是否重新计算当前调度的权值
//	                    if(listIter == 0)
//	                        loopCounter = ++loopCounter % maxIter;//当前调度的权值
//	                    int j = i % size;
//	                    if(infoArray[j].getNormalizedWeight() <= loopCounter)
//	                        continue;
//	                    remotereference1 = richreplicalist.findReplicaHostedBy(infoArray[j].getID());//大于当前调度的权值
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
//	        int i = 0x7fffffff; //32位最大整数
//	        int j = 0x80000000; //32位最小整数
//	        boolean flag = true;
//	        synchronized(this)
//	        {
//	            serverInfoArray = ServerInfoManager.theOne().getServerInfos();
//	            for(int k = 0; k < serverInfoArray.length; k++)
//	            {
//	                int i1 = serverInfoArray[k].getLoadWeight();
//	                flag &= i1 % 10 == 0;
//	                if(i > i1)
//	                    i = i1;   //最小权重，尚未格式化
//	                if(i1 > j)
//	                    j = i1;  //最大权重，尚未格式化
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

package com.oct.ga.gatekeeper.loadbalance;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;

public interface ReplicaHandler
{
	public StpServerInfoJsonBean chooseReplica(String clientVersion);
}

package com.oct.ga.gatekeeper.loadbalance;

public class ReplicaFactory
{
	public ReplicaHandler make(String type)
	{
		if (type.equals("basic"))
			return new BasicReplicaHandler();
		else if (type.equals("random"))
			return new RandomReplicaHandler();
		else if (type.equals("weight"))
			return new WeightBasedReplicaHandler();
		else
			return null;
	}
}

package com.chalmers.feedlr.listeners;

public interface AuthListener {
	
	public void onAuthorizationComplete();
	
	public void onAuthorizationFail();
}

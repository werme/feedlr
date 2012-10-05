package com.chalmers.feedlr.listener;

public interface AuthListener {
	
	public void onAuthorizationComplete();
	
	public void onAuthorizationFail();
}

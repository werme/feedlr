/**
 * @author Robin Hammar�ng
 */

package com.chalmers.feedlr.model;

import android.test.AndroidTestCase;

public class UserTest extends AndroidTestCase {
	
	
	private User user;
	/**
	 * This is called before each test.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		user = new User();
		user.setUserName("Olle");
		user.setProfileImageURL("www.google.se");
	}

	/**
	 * Tests the constructor, if the object is created properly. 
	 * The various objects should have been created with suitable values.
	 */
	public void testPreconditions() {
		assertTrue(user.getUserName().equalsIgnoreCase("Olle"));
		assertTrue(user.getProfileImageURL().equalsIgnoreCase("www.google.se"));
	}

	/**
	 * This is called after each test, to insure that each test is ran
	 * individually.
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		user = null;
	}

}

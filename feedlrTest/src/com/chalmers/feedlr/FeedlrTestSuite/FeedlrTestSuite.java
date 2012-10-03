/**
 * @author Robin Hammaräng
 */

package com.chalmers.feedlr.FeedlrTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * A test suite containing all tests for feedlr.
 */

public class FeedlrTestSuite extends TestSuite {

	public static Test suite() {
		return new TestSuiteBuilder(FeedlrTestSuite.class)
				.includeAllPackagesUnderHere().build();
	}
}

package com.iyzico.challenge.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
	FlightControllerIntegrationTest.class,
	SeatControllerIntegrationTest.class,
	FlightServiceUnitTest.class,
	SeatServiceUnitTest.class,
	IyzicoPaymentIntegrationTest.class
})

public class IyzicoChallengeTestSuite {
}
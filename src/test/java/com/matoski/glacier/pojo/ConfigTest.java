package com.matoski.glacier.pojo;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.matoski.glacier.cli.Arguments;

public class ConfigTest {

    private Arguments arguments;

    @Before
    public void setUp() throws Exception {
	arguments = new Arguments();
	arguments.amazonKey = RandomStringUtils.randomAlphanumeric(49);
	arguments.amazonSecretKey = RandomStringUtils.randomAlphanumeric(49);
	arguments.amazonVault = RandomStringUtils.randomAlphanumeric(10);
	arguments.amazonRegion = RandomStringUtils.randomAlphanumeric(5);
    }

    @Test
    public final void test_FromArguments() {
	Config config = Config.fromArguments(arguments);
	assertEquals(config.getKey(), arguments.amazonKey);
	assertEquals(config.getSecretKey(), arguments.amazonSecretKey);
	assertEquals(config.getRegion(), arguments.amazonRegion);
	assertEquals(config.getVault(), arguments.amazonVault);

    }

}

package com.eill.llg;

import org.apache.catalina.User;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserControllerTest.class,
        User.class,
})

public class FeatureTestSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
}
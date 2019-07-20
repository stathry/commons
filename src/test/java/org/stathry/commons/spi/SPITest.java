package org.stathry.commons.spi;

import org.junit.Test;

import java.util.ServiceLoader;

/**
 * SPITest
 *
 * @author dongdaiming(董代明)
 * @date 2019-07-09
 */
public class SPITest {

    @Test
    public void test() {
        ServiceLoader<HelloSpiService> services = ServiceLoader.load(HelloSpiService.class);
        for(HelloSpiService service : services) {
            System.out.println(service.hello("spi"));
        }
    }
}

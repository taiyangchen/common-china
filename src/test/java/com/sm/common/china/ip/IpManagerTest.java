/**
 * 
 */
package com.sm.common.china.ip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sm.common.libs.core.LoggerSupport;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月19日 上午12:34:47
 */
public class IpManagerTest extends LoggerSupport {

  private IpManager ipManager;

  @Test
  public void getIpInfo() {
    ipManager = new LazyLoadIpFactory().getDefaultIpManager();
    IpEntry ipEntry = ipManager.getIpInfo("218.62.120.7");
    assertEquals(ipEntry.getCountry(), "中国");
    assertEquals(ipEntry.getProvince(), "吉林");
    assertEquals(ipEntry.getCity(), "白山");
    assertEquals(ipEntry.getAddress(), "吉林白山抚松县 新世纪网吧");

    ipEntry = ipManager.getIpInfo("12.111.69.128");
    assertEquals(ipEntry.getCountry(), "美国");
    assertEquals(ipEntry.getAddress(), "Springfield College");

    ipManager = new LazyLoadIpFactory().getIpManager(IpData.CZ);
    ipEntry = ipManager.getIpInfo("218.62.120.7");
    assertEquals(ipEntry.getAddress(), "吉林省白山市 抚松县 新世纪网吧");

  }

  // @Test
  // public void testNew() {
  // // 30.27.36.93
  // ipManager = new LazyLoadIpFactory().getDefaultIpManager();
  // IpEntry ipEntry = ipManager.getIpInfo("30.27.36.93");
  // logger.info(ipEntry.getCountry());
  // logger.info(ipEntry.getProvince());
  // logger.info(ipEntry.getCity());
  // logger.info(ipEntry.getAddress());
  //
  // }

  @Test
  public void getIpLocation() {
    ipManager = new LazyLoadIpFactory().getDefaultIpManager();
    String location = ipManager.getIpLocation("218.62.120.7");
    assertEquals(location, "吉林白山抚松县 新世纪网吧");

    location = ipManager.getIpLocation("12.111.69.128");
    assertEquals(location, "Springfield College");

    ipManager = new LazyLoadIpFactory().getIpManager(IpData.CZ);

    location = ipManager.getIpLocation("218.62.120.7");
    assertEquals(location, "吉林省白山市 抚松县 新世纪网吧");

  }

}

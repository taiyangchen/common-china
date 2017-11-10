package com.sm.common.china.ip;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sm.common.libs.algorithms.BinarySearch;
import com.sm.common.libs.core.LoggerSupport;
import com.sm.common.libs.util.CollectionUtil;
import com.sm.common.libs.util.ExceptionUtil;
import com.sm.common.libs.util.IpUtil;
import com.sm.common.libs.util.ReaderUtil;

/**
 * Ip所在地查询实现
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月16日 上午1:00:28
 */
class DefaultIpManager extends LoggerSupport implements IpManager, IpLib {

  private long[] ipArray;
  private String[] ipInfoArray;
  // ip库文件,压缩后还是有3M左右，所以不包在一起，单独拷贝文件
  private boolean init;
  private int total; // 总条数
  private IpData ipData;

  public DefaultIpManager() {
    this(IpData.YAHOO);
  }

  public DefaultIpManager(IpData ipData) {
    this.ipData = ipData;
  }

  /**
   * 初始化方法
   */
  public void init() {
    if (!init) {
      loadIpLib();
    }
  }

  /**
   * 重新加载
   */
  public void reload() {
    loadIpLib();
  }

  /**
   * 重新加载指定的IP数据库
   * 
   * @param ipData yahoo:yahooIP数据库；cz:纯真IP数据库
   */
  public void reload(IpData ipData) {
    this.setIpData(ipData);
    loadIpLib();
  }

  private List<String> loadRowData() {

    try {
      return ReaderUtil.readLinesAndClose(this.getClass().getResourceAsStream(ipData.getDataFile()));
    } catch (IOException e) {
      logger.error("loadIpLib error", e);
      throw ExceptionUtil.toRuntimeException(e);
    }
  }

  /**
   * 初始化IP库，为了提升查找速度，把IP数据加载到了内存中。
   */
  private synchronized void loadIpLib() {
    long start = System.currentTimeMillis();

    List<String> lines = loadRowData();
    transformData(lines);

    long end = System.currentTimeMillis();
    logger.info("load IP lib finished. have {}records, spend {}ms.", total, (end - start));
    init = true;
  }

  private void transformData(List<String> lines) {
    if (CollectionUtil.isEmpty(lines)) {
      throw new RuntimeException("load IP lib fail,no data found!");
    }

    ipArray = new long[ITEM_SIZE];
    ipInfoArray = new String[ITEM_SIZE];

    for (Iterator<String> iter = lines.iterator(); iter.hasNext(); total++) {
      String line = iter.next();
      String[] items = StringUtils.split(line, ipData.getSplitRegex());
      if (ipData.lineItems() != items.length) {
        iter.remove();
      }

      ipArray[total] = IpUtil.encodeIp(items[0].trim());
      ipInfoArray[total] = StringUtils.substringAfter(line, ipData.getSplit()).trim();

    }
  }

  private IpEntry getIpLocation(long ip) {
    // FIXME Arrays.binarySearch居然出错
    // int pos = Arrays.binarySearch(ipArray, ip);
    int pos = BinarySearch.rank(ip, ipArray);
    if (pos < 0) {
      return null;
    }
    String info = ipInfoArray[pos];

    return convert(info, ip);
  }

  private IpEntry convert(String info, long ip) {
    String[] items = StringUtils.split(info, ipData.getSplitRegex());
    long ipLong = IpUtil.encodeIp(items[0].trim());

    if (ip <= ipLong) {
      return ipData.create(items);
    }

    return null;
  }

  public IpEntry getIpInfo(String ip) {
    long ipLong = IpUtil.encodeIp(ip);
    if (ipLong > 0) {
      return getIpLocation(ipLong);
    }

    return null;
  }

  public String getIpLocation(String ip) {
    IpEntry entry = getIpInfo(ip);

    if (entry != null) {
      return entry.getAddress();
    }

    return "";
  }

  public boolean isInit() {
    return init;
  }

  public void setIpData(IpData ipData) {
    this.ipData = ipData;
  }
}

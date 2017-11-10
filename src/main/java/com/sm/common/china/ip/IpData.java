/**
 * 
 */
package com.sm.common.china.ip;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月16日 下午4:10:13
 */
public enum IpData implements IpLib {
  YAHOO("yip.dat", ",", ",") {
    @Override
    public int lineItems() {
      return 6;
    }

    @Override
    public IpEntry create(String[] items) {
      IpEntry entry = new IpEntry();

      entry.setCountry(getInfo(items[1]));
      entry.setProvince(getInfo(items[2]));
      entry.setCity(getInfo(items[3]));

      // 拼接包含省市的地址
      StringBuilder builder = new StringBuilder();
      if (CHINA.equals(entry.getCountry())) {
        if (!"null".equals(entry.getProvince())) {
          builder.append(entry.getProvince());
        }
        if (!"null".equals(entry.getCity())) {
          builder.append(entry.getCity());
        }
      }
      if (items.length == 5) {
        builder.append(getInfo(items[4]));
      }

      entry.setAddress(builder.toString());
      return entry;
    }
  },
  CZ("czip.dat", "$", "\\" + "$") {
    @Override
    public int lineItems() {
      return 3;
    }

    @Override
    public IpEntry create(String[] items) {
      IpEntry entry = new IpEntry();
      entry.setAddress(getInfo(items[1]));

      return entry;
    }
  };

  private String dataFile;

  private String split;

  private String splitRegex;

  IpData(String dataFile, String split, String splitRegex) {
    this.dataFile = dataFile;
    this.split = split;
    this.splitRegex = splitRegex;
  }

  public String getDataFile() {
    return dataFile;
  }

  public void setDataFile(String dataFile) {
    this.dataFile = dataFile;
  }

  public String getSplit() {
    return split;
  }

  public void setSplit(String split) {
    this.split = split;
  }

  public String getSplitRegex() {
    return splitRegex;
  }

  public void setSplitRegex(String splitRegex) {
    this.splitRegex = splitRegex;
  }

  public abstract int lineItems();

  public abstract IpEntry create(String[] items);

  private static String getInfo(String line) {
    String ret = StringUtils.EMPTY;

    if (StringUtils.isNotEmpty(line) && !"null".equals(line) && !CZ88.equals(line)) {
      ret = StringUtils.replace(line, CZ88, StringUtils.EMPTY).trim();
    }

    return ret;
  }
}

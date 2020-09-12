package com.market.server.utils;

public class RedisKeyFactory {

  public enum Key {
    PRODUCT,
  }
  
  // 인스턴스화 방지
  private RedisKeyFactory() {}
  
  private static String generateKey(String id, Key key) { return id + ":" + key; }
  
  public static String generateProductKey(String userId) {
    return generateKey(userId, Key.PRODUCT);
  }

}

package com.wujt.mongo.version4.service;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
public interface Mongo4Service {

    void testMongoTransaction(Boolean tx1) throws Exception;
    void testWatch(String saveuUri, String saveCollection, String watchUri, String watchCollection);
}

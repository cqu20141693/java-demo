package com.wujt.mongo.version4;

import com.wujt.mongo.version4.model.ThingsModel;
import com.wujt.mongo.version4.service.Mongo4Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.data.mongodb.SessionSynchronization.ALWAYS;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/13
 */
@RestController
@RequestMapping("v4")
@Slf4j
public class Mongo4Controller {

    private String collectionName = "things-model";

    @Autowired
    private MongoTemplate template;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private Mongo4Service mongo4Service;
    @PostConstruct
    public void init() {
        if (!template.collectionExists(collectionName)) {
            template.createCollection(collectionName);
        }
    }

    /**
     * 事务测试接口
     * @param tx1
     * @param tx2
     * @return
     */
    @GetMapping("testTX")
    public Boolean tstTX(@RequestParam(value = "tx1", defaultValue = "false") Boolean tx1,
                         @RequestParam(value = "tx2", defaultValue = "false") Boolean tx2) {

        template.setSessionSynchronization(ALWAYS);

// ...

        TransactionTemplate txTemplate = new TransactionTemplate(manager);

        // TransactionTemplate.execute( ... )执行事务管理的时候，传入的参数有两种选择：
        //1、TransactionCallback  需要返回值，比如读
        //2、TransactionCallbackWithoutResult ： 不需要返回值

        try {
            txTemplate.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    ThingsModel model = new ThingsModel();
                    model.getTestDat();
                    template.insert(model, collectionName);
                    if (tx1) {
                        throw new NullPointerException("hello");
                    }
                    Query query = new Query();
                    Criteria criteria = new Criteria();
                    criteria.and("identifier").is("test");
                    query.addCriteria(criteria);
                    Update update = new Update();
                    update.set("type", 1);
                    template.updateMulti(query, update, collectionName);
                }
            });
        }catch (Exception e){
            log.info("tx1 occur exception.");
        }

        Query query = new Query().addCriteria(Criteria.where("identifier").is("test"));
        List<ThingsModel> models = template.find(query, ThingsModel.class,collectionName);
        log.info("size={}", models.size());
        try {
           mongo4Service.testMongoTransaction(tx2);
        } catch (Exception e) {
            log.info("tx2 occur exception.");
        }
        Long count = template.count(new Query().addCriteria(new Criteria().and("identifier").is("开发")), collectionName);
        log.info("count={}",count);
        return true;

    }

    @GetMapping("testChangeStream")
    public Boolean testChangeStream(@RequestParam("value")String value){

        return true;
    }
}

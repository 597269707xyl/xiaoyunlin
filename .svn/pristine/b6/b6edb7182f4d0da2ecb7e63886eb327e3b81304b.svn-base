package com.zdtech.platform.simserver;

import com.zdtech.platform.framework.entity.NxyFuncUsecase;
import com.zdtech.platform.framework.entity.NxyFuncUsecaseData;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseDataDao;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.service.WLSimService;
import com.zdtech.platform.simserver.utils.reflect.InvokedClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lyj on 2018/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class JunitTest {

    @Test
    public void testConfig(){
        JdbcTemplate jdbcTemplate = SpringContextUtils.getBean(JdbcTemplate.class);
        NxyFuncUsecaseDataDao dataDao = SpringContextUtils.getBean(NxyFuncUsecaseDataDao.class);
        WLSimService wlSimService = SpringContextUtils.getBean(WLSimService.class);
        Long itemId = 29320L;  //第一级测试项Id
        String itemIdStrList = jdbcTemplate.queryForObject(String.format("select getChildList(%s)", itemId), String.class);
        itemIdStrList = itemIdStrList.substring(1);
        String no = "NCS2-GL-CKCX-035";  //用例标识
        String sql = "select id from nxy_func_usecase where case_number='%s' and item_id in (%s)";
        Long usecaseId = jdbcTemplate.queryForObject(String.format(sql, no, itemIdStrList), Long.class);
        List<NxyFuncUsecaseData> dataList = dataDao.findByUsecaseId(usecaseId);
        for(NxyFuncUsecaseData data : dataList){
            String xml = data.getMessageMessage();
            String newxml = wlSimService.buildAutoSendMsgConfig(itemId, xml);
            System.out.println("替换配置文件前：" + xml);
            System.out.println("替换配置文件后：" + newxml);
        }
    }

    @Test
    public void test(){
        Set<String> set = new LinkedHashSet<>();
        for(int i=0; i< 100; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j<100; j++){
                        InvokedClass invokedClass = new InvokedClass();
                        String msgId = invokedClass.nextMsgid("");
                        set.add(msgId);
                        System.out.println(msgId);
                    }
                }
            });
            thread.start();
        }

        while (true) {
            if(Thread.activeCount()==1) {
                System.out.println(set.size());
                break;
            }
        }
    }
}
//201903270000238259
//201807216700006339
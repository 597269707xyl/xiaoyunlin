package com.zdtech.platform.framework.persistence;

import com.zdtech.platform.framework.utils.ClassScanners;
import com.zdtech.platform.framework.utils.SysUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lcheng on 2015/5/14.
 * 系统Repository（Dao）的工具类
 */
public class Repositorys {

    private static Set<Class> allEntities;
//    private static String[] repoDirs = new String[]{"com.zdtech.tman.core.repository", "com.zdtech.tman.repository"};
    private static String[] repoDirs = new String[]{"com.zdtech.platform.framework.entity"};
    private static Object lock = new Object();
    private static Map<String, RepQueryDefInfo> repoMap;

    private Repositorys() {
    }

    private static void init() {
        synchronized (lock) {
            if (allEntities == null) {
                allEntities = new HashSet<>();
                repoMap = new HashMap<>();
            }
            for (String pkg : repoDirs) {
                allEntities.addAll(ClassScanners.scanClassForPackage(Package.getPackage(pkg)));
            }
            for (Class entity : allEntities) {
                QueryDef def = (QueryDef) entity.getAnnotation(QueryDef.class);
                if (def != null) {
                    String daoName = def.daoName();
                    Object daoObj = SysUtils.getBean(daoName);
                    RepQueryDefInfo info = new RepQueryDefInfo(def.queryTag(), entity,
                            def.genericQueryFields(), daoObj);
                    info.setSortFields(def.sortFields());
                    info.setDirection(def.direction());
                    repoMap.put(def.queryTag(), info);
                }
            }
        }
    }

    public static RepQueryDefInfo getRepQueryDefInfo(String queryTag) {
        init();
        return repoMap.get(queryTag);
    }
}

package com.zdtech.platform.framework.utils;

import com.zdtech.platform.framework.entity.Seq;
import com.zdtech.platform.framework.repository.SeqDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author lcheng
 * @date 2015/6/16
 */
public class SeqIds {

    private static Logger log = LoggerFactory.getLogger(SeqIds.class);
    private static long DEFAULT_STEP = 20;
    private static int MAX_ENTRIES = 1000;

    private static Map<String, Queue<Long>> pool = new HashMap<>();
    private static Map<String, Long[]> currentMap = new HashMap<>();
    private static SeqDao dao;

    private SeqIds() {
    }

    public static Long getSeqIdNoPreFetch(String sid) {
        Long result = getFromUnused(sid);
        if (result == null) {
            if (dao == null) {
                dao = SysUtils.getBean("seqDao", SeqDao.class);
            }
            Seq seq = dao.findOne(sid);
            if (seq == null) {
                seq = new Seq();
                seq.setSid(sid);
                seq.setNextval(1);
                dao.save(seq);
            } else {
                long nextval = seq.getNextval();
                nextval++;
                seq.setNextval(nextval);
                dao.save(seq);
            }
            result = seq.getNextval();
        }
        return result;
    }

    public static Long getSeqId(String sid) {
        Long result = getFromUnused(sid);
        if (result == null) {
            synchronized (currentMap) {
                if (dao == null) {
                    dao = SysUtils.getBean("seqDao", SeqDao.class);
                }
                Long[] state = currentMap.get(sid);
                if (state != null) {
                    state[2] = state[2]++;
                    result = state[2];

                    if (result > state[1]) {
                        Seq seq = dao.findOne(sid);
                        seq.setNextval(result + DEFAULT_STEP - 1);
                        dao.save(seq);
                        state[0] = result;
                        state[1] = state[1] + DEFAULT_STEP;
                        state[2] = result;
                    }
                } else {
                    if (currentMap.size() > MAX_ENTRIES) {
                        flushAndClean();
                    }
                    Seq seq = dao.findOne(sid);
                    if (seq == null) {
                        state = new Long[]{1L, DEFAULT_STEP, 1L};
                        currentMap.put(sid, state);
                        seq = new Seq(sid, (DEFAULT_STEP + 1));
                        dao.save(seq);
                        result = 1L;
                    } else {
                        long start = seq.getNextval();
                        long end = start + DEFAULT_STEP - 1;
                        result = start;

                        state = new Long[]{start, end, start};
                        currentMap.put(sid, state);
                        seq.setNextval(end + 1);
                        dao.save(seq);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 从未用的池中取出Id值
     *
     * @param sid
     * @return
     */
    public static Long getFromUnused(String sid) {
        synchronized (pool) {
            Queue<Long> notUsed = pool.get(sid);
            if (notUsed == null) {
                return null;
            } else {
                return notUsed.poll();
            }
        }
    }

    /**
     * 当有未用的ID返回时，添加到队列中。
     *
     * @param sid
     * @param uid
     */
    public static void giveBackToUnused(String sid, Long uid) {
        synchronized (pool) {
            Queue queue = pool.get(sid);
            if (queue == null) {
                queue = new LinkedList();
                queue.offer(uid);
                if (pool.size() > MAX_ENTRIES) {
                    pool.clear();
                }
                pool.put(sid, queue);
            } else {
                queue.offer(uid);
            }
        }
    }

    /**
     * 刷新数据到数据库，同时清空Map
     */
    public static void flushAndClean() {
        synchronized (currentMap) {
            List<Seq> seqs = new ArrayList<>();
            for (String key : currentMap.keySet()) {
                Long[] data = currentMap.get(key);
                Seq seq = new Seq(key, data[2] + 1);
                seqs.add(seq);
            }
            dao.save(seqs);
            currentMap.clear();
        }
    }

    /**
     * 清除内存中的不用的Key，防止对象过多，造成内存浪费
     *
     * @param keys
     */
    public static void evict(List<String> keys) {
        synchronized (pool) {
            for (String key : keys) {
                if (pool.get(key) != null) {
                    pool.get(key).clear();
                    pool.remove(key);
                    log.debug("SeqIds 清除不用的Keys值 {}.", key);
                }
            }
        }
    }
}

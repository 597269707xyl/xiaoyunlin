package com.zdtech.platform.simserver.server.simulator;

import com.zdtech.platform.simserver.server.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SimulatorServerMgr
 *
 * @author panli
 * @date 2016/7/28
 */
public class SimulatorServerMgr {
    private static ConcurrentMap<String,Server> simulatorMap = new ConcurrentHashMap<>();

    public static void addSimulator(String id,Server simulator){
        simulatorMap.put(id,simulator);
    }
    public static void addSimulator(Long simInsId,Long adapterId,Server simulator){
        String id = String.format("%d%d",simInsId,adapterId);
        addSimulator(id,simulator);
    }
    public static void  deleteSimulator(String id){
        simulatorMap.remove(id);
    }
    public static void deleteSimulator(Long simInsId,Long adapterId){
        String id = String.format("%d%d",simInsId,adapterId);
        deleteSimulator(id);
    }
    public static Server getSimulator(String id){
        return simulatorMap.get(id);
    }
    public static Server getSimulator(Long simInsId,Long adapterId){
        String id = String.format("%d%d",simInsId,adapterId);
        return getSimulator(id);
    }
    public static Map<String,Server> getSimulators(){
        return  simulatorMap;
    }

    public static ConcurrentMap<String,Server> getAllServer(){
        return simulatorMap;
    }

}

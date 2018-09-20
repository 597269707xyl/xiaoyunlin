package com.zdtech.platform.simserver.net;

import com.zdtech.platform.simserver.net.config.ServerConfig;

/**
 * OperationData
 *
 * @author panli
 * @date 2017/3/21
 */
public class OperationData {
    public enum DataType{
        MB,FE
    }
    private DataType dataType;
    private String message;
    private boolean isReplyed;
    private ServerConfig config;

    public OperationData(DataType dataType,String message, ServerConfig config,boolean isReplyed) {
        this.dataType = dataType;
        this.message = message;
        this.isReplyed = isReplyed;
        this.config = config;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReplyed() {
        return isReplyed;
    }

    public void setReplyed(boolean replyed) {
        isReplyed = replyed;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public void setConfig(ServerConfig config) {
        this.config = config;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}

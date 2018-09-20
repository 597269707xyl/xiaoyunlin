package com.zdtech.platform.simserver.utils;

public class Constants {


	public static final String SPRING_CONTEXT_FILE="spring-config.xml";
    public static final String SYS_CONF_CATEGORY ="SYS_CONF";
	public static final String SIMULATOR_SERVER_CATEGORY = "SIMULATOR_SERVER";
    public static final String SIMULATOR_SERVER_IP ="IP";
	public static final String SIMULATOR_SERVER_PORT ="PORT";
    public static final String SIMULATOR_SERVER_CORE_POOL_SIZE ="CORE_POOL_SIZE";
    public static final String SIMULATOR_SERVER_MAX_POOL_SIZE ="MAX_POOL_SIZE";
    public static final String SIMULATOR_SERVER_KEEP_ALIVE_SECONDS ="KEEP_ALIVE_SECONDS";
    public static final String SIMULATOR_SERVER_WORK_QUEUE ="WORK_QUEUE";
    public static final String SIMULATOR_SERVER_MANUAL_REPLY_URL = "MANUAL_REPLY_URL";
    public static final String SIMULATOR_SERVER_LOG_NOTIFY_URL = "LOG_NOTIFY_URL";
    public static final String SIMULATOR_SERVER_WEBSERVICE_URL = "WEBSERVICE_URL";

    public static final String SIMULATOR_PROTOCOL_TCP_SHORTCONN="TCPSHORTCONN";
    public static final String SIMULATOR_PROTOCOL_WEBSERVICE="webservice";
    public static final String SIMULATOR_TCP_MODE_FULLDUPLEX = "FULLDUPLEX";
    public static final String SIMULATOR_TCP_MODE_HALFDUPLEX = "HALFDUPLEX";
    public static final String SIMULATOR_PROTOCOL_HTTPS = "HTTPS";
    public static final String SIMULATOR_PROTOCOL_MQ = "MQ";
    public static final String SIMULATOR_PROTOCOL_TCP="TCP";

    public static final String MESSAGE_TYPE_XML = "XML";

    public static final String MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT = "default";
    public static final String MESSAGE_RESPONSE_FIELD_TYPE_REQVALUE = "requestvalue";
    public static final String MESSAGE_RESPONSE_FIELD_TYPE_INVOKE = "methodvalue";
    public static final String MESSAGE_RESPONSE_FIELD_TYPE_EQLVALUE = "equalvalue";
}

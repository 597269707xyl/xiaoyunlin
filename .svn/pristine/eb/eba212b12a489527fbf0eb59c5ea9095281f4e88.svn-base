package com.zdtech.platform.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyj on 2018/8/3.
 */
public class MapUtils {

    ///transaction/header/ver=02&/transaction/header/msg/msgCd=NPS.142.001.01&/transaction/header/msg/sndAppCd=MPS&/transaction/header/msg/sndMbrCd=$sendCYID&/transaction/header/msg/sndDt=20171213
    public static Map<String, String> getMapByStr(String str){
        Map<String, String> map = new HashMap<>();
        if(str.isEmpty()) return map;
        if(str.startsWith("\"") && str.endsWith("\"")){
            str = str.substring(1, str.length()-1);
        }
        str = str.replaceAll("%2", "/");
        String[] arr = str.split("&/");
        for(String m : arr){
            int index = m.indexOf("=");
            if(m.length()==index){
                map.put("/"+m.substring(0,index), "");
            } else {
                map.put("/"+m.substring(0,index), m.substring(index+1));
            }
//            String[] newArr = m.split("=");
//            if(newArr.length==2){
//                map.put(newArr[0], newArr[1]);
//            } else {
//                map.put(newArr[0], "");
//            }
        }
        return map;
    }
}

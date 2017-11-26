package com.zheng.webclick.hive.mr.parsers;

import com.zheng.webclick.exception.ExceptionCode;
import com.zheng.webclick.exception.ServiceRuntimeException;
import com.zheng.webclick.hive.mr.bean.WebLogBean;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * 日志实体解析器
 * Created by zhenglian on 2017/11/26.
 */
public class WebLogBeanParser {
    public static SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
    public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    /**
     * 列分隔符
     */
    public static final String SPLIT_DELIMITER = "\001";
    
    public static WebLogBean parse(String line) {
        if (StringUtils.isEmpty(line)) {
            return null;
        }

        WebLogBean bean = new WebLogBean();
        String[] arr = line.split(" ");
        if (arr.length < 12) { // 这里表示记录的数据必须要包含所有的数据，否则认为是脏数据
            bean.setValid(false);
            return bean;
        }
        
        bean.setRemote_addr(arr[0]);
        bean.setRemote_user(arr[1]);
        String time_local = formatDate(arr[3].substring(1));
        if(null==time_local) {
            time_local="-invalid_time-";
        }
        bean.setTime_local(time_local);
        bean.setRequest(arr[6]);
        bean.setStatus(arr[8]);
        bean.setBody_bytes_sent(arr[9]);
        bean.setHttp_referer(arr[10]);

        // 获取客户端浏览器系统信息
        StringBuilder sb = new StringBuilder();
        for(int i= 11;i<arr.length;i++){
            sb.append(arr[i]);
        }
        bean.setHttp_user_agent(sb.toString());

        if (Integer.parseInt(bean.getStatus()) >= 400) {// 大于400，HTTP错误
            bean.setValid(false);
        }

        if("-invalid_time-".equals(bean.getTime_local())){
            bean.setValid(false);
        }
        return bean;
    }

    public static String formatDate(String time_local) {
        try {
            return df2.format(df1.parse(time_local));
        } catch (Exception e) {
            return null;
        }
    }

    public static void filtStaticResource(WebLogBean bean, Collection<String> pages) {
        if (!pages.contains(bean.getRequest())) {
            bean.setValid(false);
        }
    }

    public static Date toDate(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        
        try {
            return df2.parse(timeStr);
        } catch (ParseException e) {
            throw new ServiceRuntimeException(ExceptionCode.COMMON.DATE_FORMAT_EXCEPTION, "日期格式化错误");
        }
    }

    public static long timeDiff(String time1, String time2) {
        Date d1 = toDate(time1);
        Date d2 = toDate(time2);
        long millSeconds = d1.getTime() - d2.getTime();
        return millSeconds;

    }
}

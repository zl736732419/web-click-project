package com.zheng.webclick.hive.mr.parsers;

import com.zheng.webclick.exception.ExceptionCode;
import com.zheng.webclick.exception.ServiceRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 解析valid-url-prefix.conf文件
 * 获取正确的访问路径前缀
 * Created by zhenglian on 2017/11/26.
 */
public class ValidUrlPrefixParser {

    private static final String CONFIG_NAME = "valid-url-prefix.conf";

    /**
     * 获取合法的url配置
     *
     * @return
     */
    public static Collection<String> parse() {
        InputStream input = ValidUrlPrefixParser.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        Set<String> lines = new HashSet<>();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) { // 这里过滤掉注释行
                    continue;
                }
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            throw new ServiceRuntimeException(ExceptionCode.IOCode.IO_EXCEPTION, "配置文件valid-url-prefix.conf读取异常");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ServiceRuntimeException(ExceptionCode.IOCode.IO_EXCEPTION, "流关闭异常");
            }
        }

        return lines;
    }
    
    public static void main(String[] args) {
        System.out.println(ValidUrlPrefixParser.parse());
    }

}

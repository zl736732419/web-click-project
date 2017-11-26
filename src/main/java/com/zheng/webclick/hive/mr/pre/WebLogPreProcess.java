package com.zheng.webclick.hive.mr.pre;

import com.zheng.webclick.hive.mr.bean.WebLogBean;
import com.zheng.webclick.hive.mr.parsers.ValidUrlPrefixParser;
import com.zheng.webclick.hive.mr.parsers.WebLogBeanParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * step 1: 日志清洗处理
 * 排除脏数据和非正确地址的用户请求
 * Created by zhenglian on 2017/11/26.
 */
public class WebLogPreProcess {
    
    static class WebLogPreProcessMapper extends Mapper<LongWritable, Text, WebLogBean, NullWritable> {

        Collection<String> valids = new HashSet<>();

        /**
         * 加载客户自定义的有效url访问路径，用于过滤无效日志记录
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            valids = ValidUrlPrefixParser.parse();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            WebLogBean bean = WebLogBeanParser.parse(line);
            if (!Optional.ofNullable(bean).isPresent()) {
                return;
            }
            
            // 过滤js/css/图片等静态资源文件
            WebLogBeanParser.filtStaticResource(bean, valids);
            if (!bean.isValid()) {
                return;
            }
            context.write(bean, NullWritable.get());
        }
    }
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(WebLogPreProcess.class);
        job.setMapperClass(WebLogPreProcessMapper.class);
        job.setOutputKeyClass(WebLogBean.class);
        job.setOutputValueClass(NullWritable.class);

        Path output = new Path(args[1]);

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, output);
        
        job.setNumReduceTasks(0);
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

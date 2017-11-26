package com.zheng.webclick.hive.mr.bean;

import com.zheng.webclick.hive.mr.parsers.WebLogBeanParser;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 点击流模型visit表
 */
public class VisitBean implements Writable {

    /**
     * 用户会话
     */
    private String session;
    /**
     * 用户访问ip
     */
    private String remote_addr;
    /**
     * 进入页面的访问时间
     */
    private String inTime;
    /**
     * 离开页面的结束时间
     */
    private String outTime;
    /**
     * 进入页面
     */
    private String inPage;
    /**
     * 离开页面
     */
    private String outPage;
    /**
     * 来源地址信息
     */
    private String referal;
    /**
     * 访问页面数
     */
    private int pageVisits;

    public void set(String session, String remote_addr, String inTime, String outTime, String inPage, String outPage, String referal, int pageVisits) {
        this.session = session;
        this.remote_addr = remote_addr;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inPage = inPage;
        this.outPage = outPage;
        this.referal = referal;
        this.pageVisits = pageVisits;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInPage() {
        return inPage;
    }

    public void setInPage(String inPage) {
        this.inPage = inPage;
    }

    public String getOutPage() {
        return outPage;
    }

    public void setOutPage(String outPage) {
        this.outPage = outPage;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public int getPageVisits() {
        return pageVisits;
    }

    public void setPageVisits(int pageVisits) {
        this.pageVisits = pageVisits;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.session = in.readUTF();
        this.remote_addr = in.readUTF();
        this.inTime = in.readUTF();
        this.outTime = in.readUTF();
        this.inPage = in.readUTF();
        this.outPage = in.readUTF();
        this.referal = in.readUTF();
        this.pageVisits = in.readInt();

    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(session);
        out.writeUTF(remote_addr);
        out.writeUTF(inTime);
        out.writeUTF(outTime);
        out.writeUTF(inPage);
        out.writeUTF(outPage);
        out.writeUTF(referal);
        out.writeInt(pageVisits);

    }

    @Override
    public String toString() {
        return session + WebLogBeanParser.SPLIT_DELIMITER 
                + remote_addr + WebLogBeanParser.SPLIT_DELIMITER 
                + inTime + WebLogBeanParser.SPLIT_DELIMITER 
                + outTime + WebLogBeanParser.SPLIT_DELIMITER 
                + inPage + WebLogBeanParser.SPLIT_DELIMITER 
                + outPage + WebLogBeanParser.SPLIT_DELIMITER 
                + pageVisits + WebLogBeanParser.SPLIT_DELIMITER
                + referal;
    }
}

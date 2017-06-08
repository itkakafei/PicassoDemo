package testsdcard.cai.maiyu.picassodemo.entity;

/**
 * Created by maiyu on 2017/6/7.
 * 实体类；主题，大体，图片路径
 */

public class NewsItem {
    private String subject ;        //新闻主题
    private String summary ;        //新闻内容
    private String cover ;          //新闻图片

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

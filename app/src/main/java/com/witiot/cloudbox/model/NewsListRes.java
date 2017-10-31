package com.witiot.cloudbox.model;

import com.google.gson.annotations.SerializedName;
import com.witiot.cloudbox.utils.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lixin on 2017/8/26.
 */

public class NewsListRes extends BaseRes {


    /**
     * dat : {"pageindex":1,"total":6,"pageSize":10,"pages":1,"rows":[{"id":21,"author":"Liu","title":"测试文章标题","articleDesc":"测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题","content":"%E6%B5%","kind":1,"source":"","forum":"就医指南","channel":"育儿频道","articleType":1,"videoUrl":"","beginTime":"","endTime":"","visitCount":0,"favoriteCount":0,"status":1,"createTime":"2017-08-26 16:51:13","createId":"admin","msg":"","msg1":"","msg2":"","msg3":""}]}
     */

    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        /**
         * pageindex : 1
         * total : 6
         * pageSize : 10
         * pages : 1
         * rows : [{"id":21,"author":"Liu","title":"测试文章标题","articleDesc":"测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题","content":"%E6%B5%","kind":1,"source":"","forum":"就医指南","channel":"育儿频道","articleType":1,"videoUrl":"","beginTime":"","endTime":"","visitCount":0,"favoriteCount":0,"status":1,"createTime":"2017-08-26 16:51:13","createId":"admin","msg":"","msg1":"","msg2":"","msg3":""}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<NewsBean> rows;

        public int getPageindex() {
            return pageindex;
        }

        public void setPageindex(int pageindex) {
            this.pageindex = pageindex;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<NewsBean> getRows() {
            return rows;
        }

        public void setRows(List<NewsBean> rows) {
            this.rows = rows;
        }

        public static class NewsBean implements Serializable{
            /**
             * id : 21
             * author : Liu
             * title : 测试文章标题
             * articleDesc : 测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题
             * content : %E6%B5%
             * kind : 1
             * source :
             * forum : 就医指南
             * channel : 育儿频道
             * articleType : 1
             * videoUrl :
             * beginTime :
             * endTime :
             * visitCount : 0
             * favoriteCount : 0
             * status : 1
             * createTime : 2017-08-26 16:51:13
             * createId : admin
             * msg :
             * msg1 :
             * msg2 :
             * msg3 :
             */

            private int id;
            private String author;
            private String title;
            private String articleDesc;
            private String content;
            private int kind;
            private String source;
            private String forum;
            private String channel;
            private int articleType;
            private String pictureUrl;
            private String beginTime;
            private String endTime;
            private int visitCount;
            private int favoriteCount;
            private int status;
            private String createTime;
            private String createId;
            @SerializedName("msg")
            private String msgX;
            private String msg1;
            private String msg2;
            private String msg3;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getArticleDesc() {
                return articleDesc;
            }

            public void setArticleDesc(String articleDesc) {
                this.articleDesc = articleDesc;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getKind() {
                return kind;
            }

            public void setKind(int kind) {
                this.kind = kind;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getForum() {
                return forum;
            }

            public void setForum(String forum) {
                this.forum = forum;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public int getArticleType() {
                return articleType;
            }

            public void setArticleType(int articleType) {
                this.articleType = articleType;
            }

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getVisitCount() {
                return visitCount;
            }

            public void setVisitCount(int visitCount) {
                this.visitCount = visitCount;
            }

            public int getFavoriteCount() {
                return favoriteCount;
            }

            public void setFavoriteCount(int favoriteCount) {
                this.favoriteCount = favoriteCount;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            private SimpleDateFormat dtFormat = new SimpleDateFormat("MM月dd日");

            public String getCreateTime() {
                return dtFormat.format(TimeUtils.string2Date(createTime));
            }
            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateId() {
                return createId;
            }
            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public String getMsgX() {
                return msgX;
            }

            public void setMsgX(String msgX) {
                this.msgX = msgX;
            }

            public String getMsg1() {
                return msg1;
            }

            public void setMsg1(String msg1) {
                this.msg1 = msg1;
            }

            public String getMsg2() {
                return msg2;
            }

            public void setMsg2(String msg2) {
                this.msg2 = msg2;
            }

            public String getMsg3() {
                return msg3;
            }

            public void setMsg3(String msg3) {
                this.msg3 = msg3;
            }
        }
    }
}

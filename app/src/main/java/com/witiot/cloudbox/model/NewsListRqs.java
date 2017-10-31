package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class NewsListRqs extends BaseRqs{

    private NewsListRqs.DatBean dat;

    public NewsListRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(NewsListRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private DatBean.Paramlist paramlist;

        private String orderby;

        private String pageindex;

        private String pagesize;

        public String getPageindex() {
            return pageindex;
        }

        public void setPageindex(String pageindex) {
            this.pageindex = pageindex;
        }

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public DatBean.Paramlist getParamlist() {
            return paramlist;
        }

        public void setParamlist(DatBean.Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public static class Paramlist {
//            医教中心：articleType":1,"kind":”1”
//            手术意外险： articleType":"1","kind":"2"

            private  String beginTime ;private  String  endTime ;private  String  title,source ;private  String  author ,forum ;private  String  channel ;private  String  articleType ;private  String  status ;private  String  createId;
            private  String kind;
            private  String id;
            private String hospitalId;
            private String departId;

            public String getHospitalId() {
                return hospitalId;
            }

            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }

            public String getDepartId() {
                return departId;
            }

            public void setDepartId(String departId) {
                this.departId = departId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
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

            public String getArticleType() {
                return articleType;
            }

            public void setArticleType(String articleType) {
                this.articleType = articleType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreateId() {
                return createId;
            }

            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

        }
    }

}

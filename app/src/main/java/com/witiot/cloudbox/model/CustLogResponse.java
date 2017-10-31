package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by lixin on 2017/9/2.
 */

public class CustLogResponse extends BaseRes{

    /**
     * dat : {"pageindex":1,"total":6,"pageSize":10,"pages":1,"rows":[{"id":4,"author":"云头柜","title":"六味地黄丸","advertDesc":"同仁堂六位地黄丸（浓缩丸）","content":"同仁堂六味地黄丸 300丸\n生产地：北京同仁堂科技发展股份有限公司制药厂 批号：15032681 功能主治：滋阴补肾 温馨提示：请仔细阅读说明书并按说明使用或在药师指导下购买使用","kind":null,"price":44.5,"position":1,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.yiyaojd.com/3156378.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:36:49","createId":"admin","msg":null,"seqIndex":1},{"id":5,"author":"云头柜","title":"汇仁牌 肾宝片","advertDesc":"汇仁牌 肾宝片 0.7g*126片（调和阴阳 温阳补肾 扶正固本） 1盒","content":"汇仁肾宝片,中国发明专利产品,温阳补肾.扶正固本,补得进,固得牢,是用于事后补充\u201c肾透支\u201d的高档调理滋补品!","kind":null,"price":322,"position":2,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"http://item.jd.com/10604155328.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:41:02","createId":"admin","msg":null,"seqIndex":2},{"id":6,"author":"云头柜","title":"善存维生素咀嚼片","advertDesc":"善存（Centrum）小佳维咀嚼片 儿童维生素 保健品（香甜柠檬味）1.95g*80片 惠氏出品","content":"善存小佳维80片\n【功效成分及含量】(每片含)维生素A：100μg、维生素E：1.6mg、维生素C：19mg、维生素B12：0.3μg、维生素B1：0.22mg、维生素B6：0.15mg、维生素B2： 0.22mg、维生素D：2.5μg、钙：144mg、锌：3.5mg、烟酸：2.2mg、镁：45mg、叶酸：60μg、生物素：3.9μg、铜：0.27mg、β-胡萝卜素：43.3μg、泛酸：0.95mg、铁：3.0mg\n【保健功能】补充多种维生素及矿物质\n【保 质 期】24个月\n【生产厂家】惠氏制药有限公司\n【产品规格】1.95g*80粒\n【食用方法及食用量】每次2片，一日一次或每次1片，一日2次，嚼食，随餐食用或餐后即食，效果更佳\n【储藏方法】遮光，密封，室温，置干燥处\n【适宜人群】需要补充多种维生素和矿物质的4--10儿童\n【批准文号】国食健字G20110254\n【注意事项】本品不能代替药物；不宜超过推荐量或与同类营养补充剂同时食用","kind":null,"price":119,"position":3,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.jd.com/556502.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:43:10","createId":"admin","msg":null,"seqIndex":3},{"id":7,"author":"云头柜","title":"善存维生素复合片(女士)","advertDesc":"善存 Centrum 复合维生素片 （女士款）平衡营养缓解压力 120粒 膳食补充剂","content":"商品名称：善存 Centrum 复合维生素片 （女士款）平衡营养缓解压力 120粒 膳食补充剂商品编号：1968882223店铺： 沃尔玛全球购官方旗舰店商品毛重：220.00g货号：WMT300054755575包装方式：普通装产品类型：片剂蓝帽标识：营养膳食补充剂（非食健字）国产/进口：进口价位：100-199元适用人群：成人主要成分：复合维生素","kind":null,"price":109,"position":4,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.jd.hk/2144389.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:47:51","createId":"admin","msg":null,"seqIndex":4},{"id":8,"author":"云头柜","title":"美国Big Jim男性保健品","advertDesc":"【官方授权】美国Big Jim男性保健品强精固肾增又大粗延时持久非牡蛎片淫羊藿海狗丸勃起药 坚挺装一个疗程 精装 60粒 3瓶","content":"商品名称：【官方授权】美国Big Jim男性保健品强精固肾增又大粗延时"}]}
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
         * rows : [{"id":4,"author":"云头柜","title":"六味地黄丸","advertDesc":"同仁堂六位地黄丸（浓缩丸）","content":"同仁堂六味地黄丸 300丸\n生产地：北京同仁堂科技发展股份有限公司制药厂 批号：15032681 功能主治：滋阴补肾 温馨提示：请仔细阅读说明书并按说明使用或在药师指导下购买使用","kind":null,"price":44.5,"position":1,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.yiyaojd.com/3156378.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:36:49","createId":"admin","msg":null,"seqIndex":1},{"id":5,"author":"云头柜","title":"汇仁牌 肾宝片","advertDesc":"汇仁牌 肾宝片 0.7g*126片（调和阴阳 温阳补肾 扶正固本） 1盒","content":"汇仁肾宝片,中国发明专利产品,温阳补肾.扶正固本,补得进,固得牢,是用于事后补充\u201c肾透支\u201d的高档调理滋补品!","kind":null,"price":322,"position":2,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"http://item.jd.com/10604155328.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:41:02","createId":"admin","msg":null,"seqIndex":2},{"id":6,"author":"云头柜","title":"善存维生素咀嚼片","advertDesc":"善存（Centrum）小佳维咀嚼片 儿童维生素 保健品（香甜柠檬味）1.95g*80片 惠氏出品","content":"善存小佳维80片\n【功效成分及含量】(每片含)维生素A：100μg、维生素E：1.6mg、维生素C：19mg、维生素B12：0.3μg、维生素B1：0.22mg、维生素B6：0.15mg、维生素B2： 0.22mg、维生素D：2.5μg、钙：144mg、锌：3.5mg、烟酸：2.2mg、镁：45mg、叶酸：60μg、生物素：3.9μg、铜：0.27mg、β-胡萝卜素：43.3μg、泛酸：0.95mg、铁：3.0mg\n【保健功能】补充多种维生素及矿物质\n【保 质 期】24个月\n【生产厂家】惠氏制药有限公司\n【产品规格】1.95g*80粒\n【食用方法及食用量】每次2片，一日一次或每次1片，一日2次，嚼食，随餐食用或餐后即食，效果更佳\n【储藏方法】遮光，密封，室温，置干燥处\n【适宜人群】需要补充多种维生素和矿物质的4--10儿童\n【批准文号】国食健字G20110254\n【注意事项】本品不能代替药物；不宜超过推荐量或与同类营养补充剂同时食用","kind":null,"price":119,"position":3,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.jd.com/556502.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:43:10","createId":"admin","msg":null,"seqIndex":3},{"id":7,"author":"云头柜","title":"善存维生素复合片(女士)","advertDesc":"善存 Centrum 复合维生素片 （女士款）平衡营养缓解压力 120粒 膳食补充剂","content":"商品名称：善存 Centrum 复合维生素片 （女士款）平衡营养缓解压力 120粒 膳食补充剂商品编号：1968882223店铺： 沃尔玛全球购官方旗舰店商品毛重：220.00g货号：WMT300054755575包装方式：普通装产品类型：片剂蓝帽标识：营养膳食补充剂（非食健字）国产/进口：进口价位：100-199元适用人群：成人主要成分：复合维生素","kind":null,"price":109,"position":4,"source":"京东","advertType":1,"videoUrl":"advert/b8b7acc4b66743ffae0795b2d60912bb.jpg","advertUrl":"https://item.jd.hk/2144389.html","visitCount":0,"status":1,"createTime":"2017-08-25 10:47:51","createId":"admin","msg":null,"seqIndex":4},{"id":8,"author":"云头柜","title":"美国Big Jim男性保健品","advertDesc":"【官方授权】美国Big Jim男性保健品强精固肾增又大粗延时持久非牡蛎片淫羊藿海狗丸勃起药 坚挺装一个疗程 精装 60粒 3瓶","content":"商品名称：【官方授权】美国Big Jim男性保健品强精固肾增又大粗延时"}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<CustLogBean> rows;

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

        public List<CustLogBean> getRows() {
            return rows;
        }
        public void setRows(List<CustLogBean> rows) {
            this.rows = rows;
        }

        public static class CustLogBean  {

            private int id;
            private String customerId;
            private String deviceId;
            private int operType;
            private String operTime;
            private String operPage;
            private String operContent;

            public int getId() {
                return id;
            }
            public void setId(int id) {
                this.id = id;
            }

            public String getCustomerId() {
                return customerId;
            }
            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getDeviceId() {
                return deviceId;
            }
            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public int getOperType() {
                return operType;
            }
            public void setOperType(int operType) {
                this.operType = operType;
            }

            public String getOperTime() {
                return operTime;
            }
            public void setOperTime(String operTime) {
                this.operTime = operTime;
            }

            public String getOperPage() {
                return operPage;
            }
            public void setOperPage(String operPage) {
                this.operPage = operPage;
            }

            public String getOperContent() {
                return operContent;
            }
            public void setOperContent(String  operContent) {
                this.operContent = operContent;
            }

        }
    }
}

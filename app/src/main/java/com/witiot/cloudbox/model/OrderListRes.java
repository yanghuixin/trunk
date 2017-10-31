package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by lixin on 2017/9/3.
 */

public class OrderListRes extends BaseRes{

    /**
     * dat : {"pageindex":1,"total":25,"pageSize":10,"pages":3,"rows":[{"id":43,"orderType":0,"orderId":"1d8aefb527ec44be80fd900f4afd58c3","deviceId":"f8579312f7cf88bc","beginTime":null,"endTime":null,"orginalAmount":18,"discount":null,"amount":18,"status":0,"orderTime":"2017-09-02 22:21:50","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":42,"orderType":1,"orderId":"18d222f5784741779974f044cdd98203","deviceId":"f8579312f7cf88bc","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":35,"status":0,"orderTime":"2017-09-02 22:18:45","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":41,"orderType":0,"orderId":"d148430049f04f5386d704e5e2564bb0","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":18,"discount":null,"amount":18,"status":0,"orderTime":"2017-09-02 16:50:17","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":40,"orderType":1,"orderId":"4a84a6b0eb1847d1a8a6dd6d25aacf14","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":0.01,"status":2,"orderTime":"2017-09-01 11:26:33","customerId":"13716543250","payTime":"2017-09-01 11:27:21","payId":"13716543250","msg":null},{"id":39,"orderType":1,"orderId":"f6902a1ef18142c397f902e5543710b6","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":13.5,"status":0,"orderTime":"2017-09-01 11:24:20","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":38,"orderType":1,"orderId":"85f8d5c39305449b8cfae690daec8b7b","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":20,"status":0,"orderTime":"2017-09-01 11:23:49","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":37,"orderType":1,"orderId":"e52224479d2249df8d93219a0e94ffaa","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":20,"status":0,"orderTime":"2017-09-01 11:20:27","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":36,"orderType":1,"orderId":"c1a34595528147cbb339b0cdfbce5d4b","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":13.5,"status":0,"orderTime":"2017-09-01 11:19:58","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":35,"orderType":1,"orderId":"430d786ab13645dca0e0e8ebc291f3b3","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":5,"status":0,"orderTime":"2017-09-01 11:19:18","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":34,"orderType":1,"orderId":"251b86c3a62e46c48d6104e84ea07691","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":5,"status":0,"orderTime":"2017-09-01 10:59:10","customerId":"13716543250","payTime":null,"payId":null,"msg":null}]}
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
         * total : 25
         * pageSize : 10
         * pages : 3
         * rows : [{"id":43,"orderType":0,"orderId":"1d8aefb527ec44be80fd900f4afd58c3","deviceId":"f8579312f7cf88bc","beginTime":null,"endTime":null,"orginalAmount":18,"discount":null,"amount":18,"status":0,"orderTime":"2017-09-02 22:21:50","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":42,"orderType":1,"orderId":"18d222f5784741779974f044cdd98203","deviceId":"f8579312f7cf88bc","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":35,"status":0,"orderTime":"2017-09-02 22:18:45","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":41,"orderType":0,"orderId":"d148430049f04f5386d704e5e2564bb0","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":18,"discount":null,"amount":18,"status":0,"orderTime":"2017-09-02 16:50:17","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":40,"orderType":1,"orderId":"4a84a6b0eb1847d1a8a6dd6d25aacf14","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":0.01,"status":2,"orderTime":"2017-09-01 11:26:33","customerId":"13716543250","payTime":"2017-09-01 11:27:21","payId":"13716543250","msg":null},{"id":39,"orderType":1,"orderId":"f6902a1ef18142c397f902e5543710b6","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":13.5,"status":0,"orderTime":"2017-09-01 11:24:20","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":38,"orderType":1,"orderId":"85f8d5c39305449b8cfae690daec8b7b","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":20,"status":0,"orderTime":"2017-09-01 11:23:49","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":37,"orderType":1,"orderId":"e52224479d2249df8d93219a0e94ffaa","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":20,"status":0,"orderTime":"2017-09-01 11:20:27","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":36,"orderType":1,"orderId":"c1a34595528147cbb339b0cdfbce5d4b","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":13.5,"status":0,"orderTime":"2017-09-01 11:19:58","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":35,"orderType":1,"orderId":"430d786ab13645dca0e0e8ebc291f3b3","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":5,"status":0,"orderTime":"2017-09-01 11:19:18","customerId":"13716543250","payTime":null,"payId":null,"msg":null},{"id":34,"orderType":1,"orderId":"251b86c3a62e46c48d6104e84ea07691","deviceId":"88f962421ecf2f68","beginTime":null,"endTime":null,"orginalAmount":0,"discount":0,"amount":5,"status":0,"orderTime":"2017-09-01 10:59:10","customerId":"13716543250","payTime":null,"payId":null,"msg":null}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<OrderBean> rows;

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

        public List<OrderBean> getRows() {
            return rows;
        }
        public void setRows(List<OrderBean> rows) {
            this.rows = rows;
        }

        public static class OrderBean {
            /**
             * id : 43
             * orderType : 0
             * orderId : 1d8aefb527ec44be80fd900f4afd58c3
             * deviceId : f8579312f7cf88bc
             * beginTime : null
             * endTime : null
             * orginalAmount : 18.0
             * discount : null
             * amount : 18.0
             * status : 0
             * orderTime : 2017-09-02 22:21:50
             * customerId : 13716543250
             * payTime : null
             * payId : null
             * msg : null
             */

            private int id;
            private int orderType;
            private String orderId;
            private String deviceId;
            private String beginTime;
            private String endTime;

            private int buyCount;
            private double orginalAmount;
            private String discount;
            private double amount;

            private int status;
            private String orderTime;
            private String customerId;
            private String payTime;
            private String payId;
            private String msg;

            public int getId() {
                return id;
            }
            public void setId(int id) {
                this.id = id;
            }

            public int getOrderType() {
                return orderType;
            }
            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public String getOrderId() {
                return orderId;
            }
            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getDeviceId() {
                return deviceId;
            }
            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
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

            public int getBuyCount() {
                return buyCount;
            }
            public void setBuyCount(int orginalAmount) {
                this.buyCount = buyCount;
            }

            public double getOrginalAmount() {
                return orginalAmount;
            }
            public void setOrginalAmount(double orginalAmount) {
                this.orginalAmount = orginalAmount;
            }

            public String getDiscount() {
                return discount;
            }
            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public double getAmount() {
                return amount;
            }
            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getStatus() {
                return status;
            }
            public void setStatus(int status) {
                this.status = status;
            }

            public String getOrderTime() {
                return orderTime;
            }
            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getCustomerId() {
                return customerId;
            }
            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getPayTime() {
                return payTime;
            }
            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public String getPayId() {
                return payId;
            }
            public void setPayId(String payId) {
                this.payId = payId;
            }

            public String getMsg() {
                return msg;
            }
            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }
}

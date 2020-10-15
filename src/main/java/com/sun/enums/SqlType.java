package com.sun.enums;

public enum SqlType {
        insert {
            @Override
            public String getTableName (String sqlStr) {
                int into_ = sqlStr.indexOf("into ") + 5;
                String substring = sqlStr.substring(into_);
                int i = substring.indexOf(" ");
                return substring.substring(0, i).trim();
            }

            @Override
            public boolean sync() {
                return true;
            }
        },


        update {
            @Override
            public String getTableName (String sqlStr) {
                int length = "update ".length();
                int i = sqlStr.indexOf(" set");
                return sqlStr.substring(length, i).trim();
            }

            @Override
            public boolean sync() {
                return true;
            }
        },


        delete {
            @Override
            public String getTableName (String sqlStr) {
                int from = sqlStr.indexOf("from ") + 5;
                int where = sqlStr.indexOf(" where");
                if (where == -1) {
                    return sqlStr.substring(from).trim();
                } else {
                    return sqlStr.substring(from, where).trim();
                }
            }

            @Override
            public boolean sync() {
                return true;
            }
        },


        select {
            @Override
            public String getTableName (String sqlStr) {
                int from = sqlStr.indexOf("from ") + 5;
                int where = sqlStr.indexOf(" where");
                if (where == -1) {
                    return sqlStr.substring(from).trim();
                } else {
                    return sqlStr.substring(from, where).trim();
                }
            }

            @Override
            public boolean sync() {
                return false;
            }
        };

        public static void main(String[] args) {
//            String tableName = select.getTableName("SELECT bh,rybh,zp,sbcj,sbxh,tzlx,cjsj,xgsj,zt,lxxq,rltz,zpxh FROM yw_swtz");
            String tableName = insert.getTableName("INSERT INTO sys_role_zy ( zybh,role_id,id ) VALUES( '210514','db90f2845a2e4e0ba0db37e58cbce19c','d5f2d33c5b8d49e2b0ab185b7db85496' )");
//            String tableName = update.getTableName("UPDATE jls_zjry SET rybh = rybh,xm = '吴思思',xbdm = '2',csrq = '1990-3-7 8:00:00',mzdm = '03',zjlxdm = '11',zjh = '330102199003077480',gjdm = '156',jgdm = '330102',hkszddm = '330102',hkszdxz = '浙江省杭州市上城区',xzjddm = '110100',xzjdxz = '山东青岛',whcddm = '10',sfdm = '5',jyaq = '正常',jsbm = '3702011210011100300',jgcsbm = '3702011210000000000',zzmmdm = '13',aqdjbm = '2',ztdm = '2',xmpy = 'WuSisi',rsrq = '2020-10-14 0:00:00',rsyydm = '11',badwlxdm = '2',badw = '101',sydw = '101',syr = '李师师',sypzdm = '1',sypzwsh = '青南公（）拘字（2020）号',ajlb = '3',yfbh = '1065',xgr = '303004',gyqx = '2020-10-13 0:00:00',rybz = '1,5,3,6,2',jlrq = '2020-10-14 8:00:00',zyay = '3',syrdh = '15692326800',sfzdry = '1',syre = '李师师',sydwlx = '2',xgsj = '2020-10-14 16:50:14' WHERE rybh = '370201121202010140016'");
//            String tableName = delete.getTableName("delete from Yw_Gz_Ceping where WORK_ID = #{workId}");
            System.out.println(tableName);
        }

        public static SqlType getType(String sqlStr) {
            String type = sqlStr.trim().split(" ")[0];
            return valueOf(type);
        }

        public String getTableName(String sqlStr) {
            return null;
        }

        public boolean sync() {
            return false;
        }
    }
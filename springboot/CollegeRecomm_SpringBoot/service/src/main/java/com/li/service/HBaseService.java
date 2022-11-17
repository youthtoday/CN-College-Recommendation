package com.li.service;

import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * @ClassName HBaseService
 * @Description TODO
 * @Author L
 * @Date Create by 2020/1/8
 */

public class HBaseService {
    private Logger log = LoggerFactory.getLogger(HBaseService.class);

    /**
     * 管理员可以做表以及数据的增删改查功能
     */
    private Admin admin = null;
    private Connection connection = null;

    public HBaseService(Configuration conf) {
        try {
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (IOException e) {
            log.error("获取HBase连接失败!");
        }
    }

    /**
     * 创建表 create <table>, {NAME => <column family>, VERSIONS => <VERSIONS>}
     *
     * @param tableName
     * @param columnFamily
     * @return 是否创建成功
     */
    public boolean creatTable(String tableName, List<String> columnFamily) {
        try {
            //列族column family
            List<ColumnFamilyDescriptor> cfDesc = new ArrayList<>(columnFamily.size());
            columnFamily.forEach(cf -> {
                cfDesc.add(ColumnFamilyDescriptorBuilder.newBuilder(
                        Bytes.toBytes(cf)).build());
            });
            //表 table
            TableDescriptor tableDesc = TableDescriptorBuilder
                    .newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(cfDesc).build();
            if (admin.tableExists(TableName.valueOf(tableName))) {
                log.debug("table Exists!");
            } else {
                admin.createTable(tableDesc);
                log.debug("create table Success!");
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("创建表{0}失败", tableName), e);
            return false;
        } finally {
            close(admin, null, null);
        }
        return true;
    }

    /**
     * 查询所有表的表名
     *
     * @return 表名集合
     */
    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        try {
            TableName[] tableNames = admin.listTableNames();
            for (TableName tableName : tableNames) {
                System.out.println(tableName);
                result.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            log.error("获取所有表的表名失败", e);
        } finally {
            close(admin, null, null);
        }
        return result;
    }

    /**
     * 遍历查询指定表中的所有数据
     *
     * @param tableName
     * @return
     */
    public Map<String, Map<String, String>> getResultScanner(String tableName) {
        Scan scan = new Scan();
        return this.queryData(tableName, scan);
    }

    /**
     * 通过表名及过滤条件查询数据
     *
     * @param tableName
     * @param scan
     * @return
     */
    public Map<String, Map<String, String>> queryData(String tableName, Scan scan) {
        // <rowKey,对应的行数据>
        Map<String, Map<String, String>> result = new HashMap<>();
        ResultScanner rs = null;
        //获取表
        Table table = null;
        try {
            table = getTable(tableName);
            rs = table.getScanner(scan);
            int n = 0;
            for (Result r : rs) {
                // 每一行数据
                Map<String, String> columnMap = new HashMap<>();
                String rowKey = null;
                // 行键，列族和列限定符一起确定一个单元（Cell）
                for (Cell cell : r.listCells()) {
                    if (rowKey == null) {
                        rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    }

                    columnMap.put(
                            //列限定符
                            Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                            //列族
                            Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
                if (rowKey != null && n < 10) {
                    result.put(rowKey, columnMap);
                }
                n++;
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}", tableName), e);
        } finally {
            close(null, rs, table);
        }
        return result;
    }



    /**
     * 根据表名获取table
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * 通过学校名和专业名获取专业简介
     * @param sname
     * @param mname
     * @return
     */
    public String queryDataByName(String sname,String mname){
        ResultScanner rs = null;
        String rowKey = null;
        Table table = null;
        String majorinfo = null;
        //获取表
        try {
                table = getTable("school");;
                Map<String, Object> map = readJsonFile("./sc_name_sc_id.json");
                rowKey = (String) map.get(sname);
                Get get = new Get(rowKey.getBytes());
                Result result = table.get(get);//学校的所有数据
                majorinfo = Bytes.toString(result.getValue(Bytes.toBytes("majors"), Bytes.toBytes(mname)));
            } catch (IOException e) {
                log.error(MessageFormat.format("查询指定表中的数据失败,tableName:{0}", "test0"), e);
            } finally {
                close(null, rs, table);
            }
        return majorinfo;
        }

    /**
     * 通过学校名获取新闻链接
     * @param sname
     * @return
     */
    public Map<String,String > queryHrefByName(String sname){
        ResultScanner rs = null;
        String rowKey = null;
        Table table = null;
        Map<String ,String > hrefmap = new HashMap<>();
        //获取表
        try {
            table = getTable("school");;
            Map<String, Object> map = readJsonFile("./sc_name_sc_id.json");
            rowKey = (String) map.get(sname);
            Get get = new Get(rowKey.getBytes());
            Result result = table.get(get);//行数据
            NavigableMap<byte[], byte[]> hrefs1 = result.getFamilyMap(Bytes.toBytes("herfs"));//获取hrefs的map
            for (byte[] bytes:hrefs1.navigableKeySet()){
                String title = Bytes.toString(bytes);
                System.out.println(title);
                String href = Bytes.toString(hrefs1.get(bytes));
                hrefmap.put(title,href);
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("查询指定表中的数据失败,tableName:{0}", "test0"), e);
        } finally {
            close(null, rs, table);
        }
        return hrefmap;
    }

    /**
     * Json文件转
     * @param fileName
     * @return
     */
    public static Map<String, Object> readJsonFile(String fileName) {
        Gson gson = new Gson();
        String json = "";
        try {
            File file = new File(fileName);
            Reader reader = new InputStreamReader(new FileInputStream(file), "utf-8");
            int ch = 0;
            StringBuffer buffer = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                buffer.append((char) ch);
            }
            reader.close();
            json = buffer.toString();
            return gson.fromJson(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭流
     *
     * @param admin
     * @param rs
     * @param table
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("关闭Admin失败", e);
            }

            if (rs != null) {
                rs.close();
            }

            if (table != null) {
                rs.close();
            }

            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    log.error("关闭Table失败", e);
                }
            }
        }
    }
}

<template>
  <div class="topBar">
    <div class="title">
      <h1>中国大学专业排名</h1>
      <!-- <div class="subTitle">美术学的专业排名为 ：</div> -->
      <h2>{{ subjectName }} 专业排名</h2>
    </div>
  </div>
  <div class="content">
    <dir class="subject-container">
      <div class="subjectLeft">
        <el-row class="tac">
          <el-col>
            <el-menu
              :default-openeds="opens"
              default-active="哲学"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose"
            >
              <el-sub-menu :index="index" v-for="(item, index) in allMajors">
                <template #title>
                  <el-icon><location /></el-icon>
                  <span>{{ item.cname }}</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item
                    :index="detailItem.name"
                    v-for="detailItem in item.majors"
                    @click="queryRanking(detailItem.id, detailItem.name)"
                    >{{ detailItem.name }}</el-menu-item
                  >
                </el-menu-item-group>
              </el-sub-menu>
            </el-menu>
          </el-col>
        </el-row>
        <!-- <li class="subjectItem" v-for="item in allMajors">asd</li> -->
      </div>
      <div class="subjectRight">
        <div class="topContent">
          <div class="topItem">专业评级</div>
          <div class="topItem" style="width: 200px">专业排名</div>
          <div class="topItem">学校名称</div>
          <div class="topItem" style="margin-left: 290px">学校类型</div>
          <div class="topItem" style="margin-left: auto">省份</div>
          <div class="topItem" style="margin-left: auto">排名</div>
        </div>
        <div class="example-pagination-block">
          <el-pagination
            layout="prev, pager, next"
            :total="totalPages"
            :pager-count="10"
            hide-on-single-page="true"
            @current-change="handlePage"
          />
        </div>
        <ul>
          <li
            class="schoolItem-ranking"
            v-for="item in schoolList"
            :key="item.id"
          >
            <div class="items">{{ item.result }}</div>
            <div class="items" style="width: 200px">{{ item.percent }}</div>
            <div class="majorCot">
              <div class="unvImg">
                <img :src="item.url" alt="" />
              </div>
              <div class="textBox" @click="gotoSchoolDetailMsg(item.name)">
                <div class="schoolName">{{ item.name }}</div>
                <div class="nameEn">{{ item.nameEn }}</div>
                <div
                  class="schoolSubName"
                  v-if="item.dual_class_name === '双一流'"
                >
                  双一流
                </div>
                <div class="schoolSubName" v-if="item.f211 === 1">211</div>
                <div class="schoolSubName" v-if="item.f985 === 1">985</div>
              </div>
            </div>
            <div class="items schoolType">{{ item.type_name }}</div>
            <div class="items schoolPro">
              {{ item.province_name }}
            </div>
            <div class="items schoolRanking">{{ item.ranking }}</div>
          </li>
        </ul>
      </div>
    </dir>
  </div>
</template>

<script>
import bus from "../../eventBus";
import myAxios from "../../js/myAxios";
const { axios, baseUrl } = myAxios;
export default {
  data() {
    return {
      allMajors: [],
      schoolList: [],
      subjectName: "",
      schoolSubName: [],
      opens: [0],
      currentPage: 1,
      subjectXid: 1,
      totalElements: "",
      totalPages: "",
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    },
    async queryRanking(subjectXid, subjectName) {
      this.subjectXid = subjectXid;
      this.subjectName = subjectName;
      const currentPage = this.currentPage;
      // console.log("xid=" + subjectXid);
      const res = await axios({
        method: "get",
        url: `${baseUrl}mrank?xid=${subjectXid}&pageSize=10&pageNum=${currentPage}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      // res 为拿到的学校排名
      const { totalElements, totalPages } = res?.data?.data;
      const schoolList = res?.data?.data.content || [];
      schoolList.forEach((item, index) => {
        // 这里图片不能放在assets里面，会被webpack打包，不能正确解析出来
        item.url = `../../../images/school_${item.school_id}.jpg`;
        item.ranking = index + 1 + (currentPage - 1) * 10;
        // item.url = require(`../assets/images/school_${item.school_id}.jpg`)
      });
      this.schoolList = schoolList;
      this.totalElements = totalElements;
      this.totalPages = totalPages * 10;
    },
    async gotoSchoolDetailMsg(schoolName) {
      const searchRes = await axios({
        // 发起搜索请求
        method: "get",
        url: `${baseUrl}colleges/list/${schoolName}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      const schoolDetail = JSON.stringify(
        searchRes?.data?.data?.content[0] || {}
      );
      console.log(schoolDetail);
      this.$router.push({
        name: "searchRes",
        query: { schoolName, schoolDetail },
      });
    },
    handlePage(pageNum) {
      this.currentPage = pageNum;
      this.queryRanking(this.subjectXid, this.subjectName);
    },
  },
  async created() {
    // 这里有个异步的问题 不知道怎么处理
    // 进来先获得登录态，然后获得xid
    const res = await axios({
      method: "post",
      url: `${baseUrl}user/login`,
      data: {
        username: "xs",
        password: "123",
      },
    });
    const schoolArr = res?.data?.data?.allMajors || [];
    this.allMajors = schoolArr;
    this.queryRanking(1, "哲学");
    // console.log(schoolArr);

    // schoolArr.forEach((item) => {
    //   // 这里图片不能放在assets里面，会被webpack打包，不能正确解析出来
    //   item.url = `../../../images/school_${item.school_id}.jpg`;
    //   // item.url = require(`../assets/images/school_${item.school_id}.jpg`)
    // });
    // console.log(schoolArr);
    // this.allMajors = schoolArr;
    // this.allMajors = res?.data?.data.allMajors || [];
    // 优化 图片放在静态资源，map手动添加url
    // console.log(111);
    // console.log(this.allMajors);
    // this.$forceUpdate();
  },
};
</script>

<style lang="less" scoped>
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}
.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
:deep(.el-pagination) {
  margin-top: 15px;
  padding-left: 330px;
}
.topBar {
  position: relative;
  min-height: 180px;
  width: 1519px;
  background: url(../../assets/topBarBg.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  padding-top: 28px;
  .title {
    // display: flex;
    // flex-direction: column;
    // align-items: center;
    width: 1180px;
    margin: 0 auto;
    // margin-top: 80px;
    h1 {
      font-size: 32px;
      font-weight: 400;
      font-family: PingFangSC-Semibold;
      line-height: 45px;
      color: #fff;
      margin: 0 12px 0 0;
    }
    h2 {
      margin-top: 16px;
      // font-size: 32px;
      font-weight: 400;
      font-family: PingFangSC-Semibold;
      line-height: 45px;
      color: #fff;
      // margin: 0 12px 0 0;
    }
    .subTitle {
      margin-top: 16px;
      width: 880px;
      overflow: hidden;
      font-family: PingFangSC-Regular;
      font-size: 14px;
      line-height: 22px;
      color: hsla(0, 0%, 100%, 0.7);
    }
  }
}
.content {
  width: 1519px;
  // max-height: 1500px;
  min-height: 562px;
  background-color: rgba(244, 243, 244);
  .subject-container {
    display: flex;
    width: 1285px;
    min-height: 562px;
    // max-height: 1500px;
    overflow: hidden;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #ebeaeb;
    margin: 0 auto;
    margin-bottom: 24px;
    .subjectLeft {
      // height: 100px;
      width: 200px;
      border-right: 1px solid #e6e6e6;
      :deep(.el-menu) {
        border-right: none;
      }
    }
    .subjectRight {
      flex: 1;
      .topContent {
        display: flex;
        background-color: #f8f8f8;
        height: 50px;
        align-items: center;
        font-family: PingFangSC-Regular;
        font-size: 14px;

        .topItem {
          text-align: center;
          width: 100px;
          color: #605e60;
        }
      }
      .schoolItem-ranking {
        display: flex;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid #eaeaea;
        height: 111px;
        .items {
          width: 100px;
          text-align: center;
          font-size: 14px;
        }
        .majorCot {
          position: relative;
          display: flex;
          flex: 1;
          height: 100%;
          align-items: center;
          .unvImg {
            margin-left: 16px;
            height: 60px;
            width: 60px;
            img {
              width: 100%;
              height: 100%;
            }
          }
          .textBox {
            height: 60px;
            margin-left: 12px;
            .schoolName {
              font-family: PingFangSC-Regular;
              font-size: 16px;
              line-height: 22px;
              color: #383638;
            }
            .nameEn {
              font-family: PingFangSC-Regular;
              font-size: 14px;
              color: rgb(142, 140, 142);
            }
            .schoolSubName {
              float: left;
              margin-right: 10px;
              font-family: PingFangSC-Regular;
              font-size: 14px;
              color: #8d8b8d;
            }
          }
          .textBox:hover .schoolName {
            color: red;
            cursor: pointer;
          }
        }
        .schoolType {
          position: absolute;
          left: 1010px;
        }
        .schoolPro {
          position: absolute;
          // right: 265px;
          left: 1157px;
        }
        .schoolRanking {
          position: absolute;
          left: 1340px;
          width: 22px;
          display: inline-block;
          min-width: 14px;
          height: 22px;
          background-color: #efefef;
          border-radius: 4px;
          font-family: Helvetica;
          font-size: 14px;
          color: #919191;
          margin: auto;
          padding: 0 4px;
          line-height: 22px;
        }
      }
      .schoolItem-ranking:hover {
        box-shadow: 0 3px 12px 0 rgb(0 0 0 / 10%);
      }
    }
  }
}
</style>
<template>
  <div class="topBar">
    <div class="title">
      <h1>中国大学软科排名</h1>
      <!-- <div class="subTitle">美术学的专业排名为 ：</div> -->
      <h2>软科排名</h2>
    </div>
  </div>
  <div class="mainBody">
    <div class="content">
      <div class="contentTitle">
        <div class="leftItem"></div>
        <div class="title">中国大学排名</div>
        <div class="inputSelect">
          <el-select
            v-model="initialPro"
            class="m-2 selectItem"
            placeholder="Select"
            style="width: 150px"
          >
            <el-option
              v-for="item in province"
              :key="item.value"
              :label="item.label"
              :value="item"
              @click="querySchoolMsg"
            />
          </el-select>
          <el-select
            v-model="initialType"
            class="m-2 selectItem"
            placeholder="Select"
            style="margin-left: 20px"
          >
            <el-option
              v-for="item in UniversityType"
              :key="item.value"
              :label="item.label"
              :value="item"
              @click="querySchoolMsg"
            />
          </el-select>
        </div>
      </div>
      <div class="schoolBox">
        <div class="schoolContent">
          <div class="schoolContentTitle">
            共查询到{{ totalElements }}所院校
          </div>
          <ul>
            <li
              class="schoolItem"
              v-for="(item, index) in schoolArr"
              :key="index"
              @click="querySchool(item.sname)"
            >
              <div class="schoolLogo">
                <img :src="item.url" alt="" />
              </div>
              <div class="schoolRight">
                <div class="schoolTitle">{{ item.sname }}</div>
                <div class="schoolSubTitle">{{ item.snameEn }}</div>
                <ul class="schoolType">
                  <li class="schoolTypeItem">{{ item.type }}</li>
                  <li class="schoolTypeItem">{{ item.province }}</li>
                  <li class="schoolTypeItem">{{ item.univTags }}</li>
                </ul>
                <div class="ranking" :class="handleClass(item.ranking)">
                  {{ item.ranking }}
                </div>
              </div>
            </li>
          </ul>
          <div class="example-pagination-block">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="totalPages"
              :pager-count="8"
              hide-on-single-page="true"
              @current-change="changePage"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import myAxios from "../../js/myAxios";
import selectItem from "../../js/selectItem";
// import { province, UniversityType } from "../../js/selectItem";
const { province, UniversityType } = selectItem;
const { axios, baseUrl } = myAxios;
export default {
  name: "softRanking",
  data() {
    return {
      inputContent: "",
      initialPro: "全部省份",
      initialType: "全部类型",
      province,
      UniversityType,
      schoolArr: [],
      imageUrl: "",
      currentPage: 1,
      totalPages: "",
      totalElements: "",
    };
  },
  methods: {
    async querySchoolMsg() {
      const inputContent = this.inputContent;
      const currentPage = this.currentPage || 1;
      const province = this.initialPro;
      const type = this.initialType;
      const res = await axios({
        method: "get",
        url: `${baseUrl}rkrank?province=${province}&type=${type}&pageNum=${currentPage}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      const { totalElements, totalPages } = res?.data?.data;
      const schoolArr = res?.data?.data?.content || [];
      schoolArr.forEach((item, index) => {
        // 这里图片不能放在assets里面，会被webpack打包，不能正确解析出来
        item.url = `../images/school_${item.school_id}.jpg`;
        // item.url = require(`../assets/images/school_${item.school_id}.jpg`)
        // 动态添加排名
        item.ranking = index + 1 + (currentPage - 1) * 10;
      });
      this.schoolArr = schoolArr;
      this.totalElements = totalElements;
      this.totalPages = totalPages * 10;
    },
    async querySchool(schoolName) {
      // console.log(schoolName);
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
    changePage(currentPage) {
      this.currentPage = currentPage;
      this.querySchoolMsg();
    },
    handleClass(rank) {
      // 处理前三名的类样式
      switch (rank) {
        case 1:
          return {
            first: true,
          };
        case 2:
          return {
            second: true,
          };
        case 3:
          return {
            third: true,
          };
      }
    },
  },
  created() {
    const typeName = this.$route.params.typeName || "全部类型";
    this.initialType = typeName === "全部类型" ? typeName : `${typeName}类`;
    this.querySchoolMsg();
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
  padding-left: 360px;
}
.topBar {
  position: relative;
  min-height: 180px;
  width: 1519px;
  background: url(../../assets/softBg.png);
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
.mainBody {
  width: 1519px;
  min-height: 500px;
  background-color: rgba(244, 243, 244);
  padding-top: 20px;
  .content {
    width: 1200px;
    padding: 0 16px;
    margin: 0 auto;
    // background-color: #fff;
    min-height: 500px;
    .contentTitle {
      position: relative;
      display: flex;
      align-items: center;
      height: 40px;
      .leftItem {
        position: absolute;
        left: -15px;
        width: 4px;
        height: 18px;
        background-color: #e5002d;
        border-radius: 0 2px 2px 0;
      }
      .title {
        line-height: 22px;
        font-family: PingFangSC-Semibold;
        font-size: 16px;
        color: #383638;
        //   text-indent: 10px;
      }
      .inputSelect {
        display: flex;
        margin-left: auto;
        position: relative;
        top: 6px;
        z-index: 1;
        height: 30px;

        :deep(.selectItem) {
          width: 120px;
        }
      }
    }
  }
}
.schoolBox {
  min-height: 460px;
  width: 100%;
  background-color: rgba(244, 243, 244);
  padding: 16px 0;
  .schoolContent {
    // width: 876px;
    height: 100%;
    margin: 0 auto;
    background-color: rgba(244, 243, 244);
    .schoolContentTitle {
      // margin-top: 16px;
      font-family: PingFang SC;
      font-size: 14px;
      color: #605e60;
    }
    .schoolItem {
      position: relative;
      display: flex;
      margin-top: 16px;
      padding: 20px 0 20px 20px;
      background-color: #fff;
      border-radius: 4px;
      .schoolLogo {
        width: 60px;
        height: 60px;
        img {
          width: 100%;
        }
      }
      .schoolRight {
        position: relative;
        display: flex;
        flex-direction: column;
        flex: 1;
        margin-left: 20px;
        .schoolTitle {
          font-family: PingFangSC-Semibold;
          font-size: 18px;
          color: #383638;
          display: flex;
          align-items: center;
        }
        .schoolSubTitle {
          font-family: PingFangSC-Regular;
          font-size: 14px;
          color: #8e8c8e;
        }
        .schoolType {
          display: flex;
          margin-top: 3px;
          .schoolTypeItem {
            height: 20px;
            background-color: #f8f8f8;
            border-radius: 4px;
            font-family: PingFangSC-Regular;
            font-size: 12px;
            line-height: 20px;
            color: #8d8b8d;
            padding: 2px 8px;
            margin-left: 8px;
          }
        }
        .schoolTypeItem:first-child {
          margin-left: 0;
        }
        .ranking {
          display: flex;
          align-items: center;
          justify-content: center;
          position: absolute;
          right: 55px;
          top: 25px;
          width: 22px;
          height: 22px;
          background-color: #efefef;
          color: #919191;
          border-radius: 4px;
          font-family: Helvetica;
          font-size: 14px;
          margin: auto;
        }
        .first {
          background: url("../../assets/first.svg");
          color: #fff;
          background-repeat: no-repeat;
          background-size: cover;
        }
        .second {
          background: url("../../assets/second.svg");
          color: #fff;
          background-repeat: no-repeat;
          background-size: cover;
        }
        .third {
          background: url("../../assets/third.svg");
          color: #fff;
          background-repeat: no-repeat;
          background-size: cover;
        }
      }
    }
  }
}
.schoolItem:hover {
  box-shadow: 0 3px 12px 0 rgb(0 0 0 / 10%);
  cursor: pointer;
  .schoolRight:hover {
    .schoolTitle {
      color: red;
    }
  }
}
</style>
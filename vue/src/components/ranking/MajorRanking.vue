<template>
  <div class="topBar">
    <div class="title">
      <h1>{{ mname }}</h1>
      <div class="subTitle">{{ isWhat }}</div>
      <div class="recMajors">
        <p style="position: absolute; left: 0">相似专业推荐:</p>
        <ul>
          <li v-for="item in recMajors" @click="updateMajor(item.name)">
            {{ item.name }}
          </li>
        </ul>
      </div>
    </div>
  </div>
  <div class="mainBody">
    <div class="content">
      <div class="contentTitle">
        <div class="leftItem"></div>
        <div class="title">专业排名</div>
      </div>
      <div class="schoolBox">
        <div class="schoolContent">
          <div class="schoolContentTitle">
            共查询到{{ toTalElements }}所院校
          </div>
          <ul>
            <li
              class="schoolItem"
              v-for="(item, index) in schoolArr"
              :key="index"
              @click="querySchool(item.name)"
            >
              <div class="schoolLogo">
                <img :src="item.url" alt="" />
              </div>
              <div class="schoolRight">
                <div class="schoolTitle">{{ item.name }}</div>
                <div class="schoolSubTitle">{{ item.nameEn }}</div>
                <ul class="schoolType">
                  <li class="schoolTypeItem">{{ item.type_name }}</li>
                  <li class="schoolTypeItem">{{ item.province_name }}</li>
                  <li class="schoolTypeItem">{{ item.dual_class_name }}</li>
                  <li class="schoolTypeItem" v-if="item.f985 === 1">985</li>
                  <li class="schoolTypeItem" v-if="item.f211 === 1">211</li>
                </ul>
                <div class="ranking" :class="handleClass(item.ranking)">
                  {{ item.ranking }}
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="example-pagination-block">
          <el-pagination
            layout="prev, pager, next"
            background
            :total="toTalPages"
            :pager-count="8"
            @current-change="handlePage"
            hide-on-single-page="true"
          />
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
      mname: "",
      toTalElements: "",
      toTalPages: "",
      isWhat: "",
      recMajors: [],
    };
  },
  methods: {
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
      this.$router.push({
        name: "searchRes",
        query: { schoolName, schoolDetail },
      });
    },
    async handlePage(currentPage) {
      const { mname } = this;
      const searchRes = await axios({
        method: "get",
        url: `${baseUrl}majors/${mname}?pageNum=${currentPage}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      let schoolArrInit = searchRes?.data?.data?.colleges?.content;
      const schoolArr = [];
      const map = {};
      const { toTalElements, toTalPages } = searchRes?.data?.data?.colleges;
      schoolArrInit.forEach((item, index) => {
        // 这里图片不能放在assets里面，会被webpack打包，不能正确解析出来
        item.url = `../images/school_${item.school_id}.jpg`;

        if (!map[item.name]) {
          // 去重操作
          schoolArr.push(item);
          map[item.name] = item.name;
        }
      });
      schoolArr.forEach((item, index) => {
        item.ranking = index + 1 + (currentPage - 1) * 10;
      });
      this.schoolArr = schoolArr;
      this.toTalElements = toTalElements;

      this.toTalPages = toTalPages * 10;
    },
    async initData() {
      const { mname } = this.$route.query;
      const majorDetails = await axios({
        method: "get",
        url: `${baseUrl}majors/${mname}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      const { toTalElements, toTalPages } = majorDetails?.data?.data?.colleges;
      const recMajors = majorDetails?.data?.data?.recommms.slice(1, 20); // 表中第一个相似专业就是自己，所以拿掉
      const major = majorDetails?.data?.data?.major;
      const isWhat = major?.is_what;
      this.isWhat = isWhat;
      this.recMajors = recMajors;
      this.toTalElements = toTalElements;
      this.toTalPages = toTalPages;
      this.mname = mname;
      this.handlePage(1);
    },
    updateMajor(mname) {
      console.log(mname);
      this.$router.push({
        name: "MajorRanking",
        query: { mname },
      });
    },
    handleClass(rank) {
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
    this.initData();
  },
  watch: {
    // 路由跳转不刷新问题
    $route(to, from) {
      //监听路由是否变化
      if (to.query.mname != from.query.mname) {
        //变化后的操作
        this.initData();
        // this.init(); //重新加载数据
      }
    },
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
  background: url(../../assets/majorRanking.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  padding-top: 28px;
  .title {
    width: 1180px;
    margin: 0 auto;

    h1 {
      font-size: 32px;
      font-weight: 400;
      font-family: PingFangSC-Semibold;
      line-height: 45px;
      color: #fff;
      margin: 0px 12px 0 0;
    }
    .majorName {
      //   color: orange;
      color: #fff;
      position: absolute;
      font-size: 50px;
      left: 515px;
      top: 2px;
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
  .recMajors {
    position: relative;
    height: 34px;

    overflow: hidden;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: left;
    font-size: 14px;
    padding: 20px;
    padding-left: 100px;
    li {
      height: 20px;
      overflow: hidden;
      line-height: 22px;
      display: inline-block;
      padding: 7px 10px;
      border-radius: 4px;
      background-color: #1c345e;
      color: #fff;
      margin-left: 15px;
      cursor: pointer;
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
        .ranking {
          display: flex;
          align-items: center;
          justify-content: center;
          position: absolute;
          right: 55px;
          top: 45px;
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
        .schoolTypeItem:first-child {
          margin-left: 0;
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
<template>
  <div class="mainBg">
    <el-container class="max-auto inputBox">
      <el-input
        placeholder="请输入内容"
        v-model="inputContent"
        clearable
        class="inputContent"
        @keydown.enter="keydown"
      >
      </el-input>
      <div class="search" @click="searchIt">搜索</div>
    </el-container>
    <div class="max-auto selectMid" style="width: 240px; margin-bottom: 12px">
      <el-radio v-model="radio" label="1" size="large">搜索大学</el-radio>
      <el-radio v-model="radio" label="2" size="large">搜索专业</el-radio>
    </div>
    <div class="hotSearch max-auto">
      <p style="position: absolute; left: 0">热门搜索:</p>
      <ul>
        <li v-for="(item, index) in hotSeatchSchool" @click="hotSearchIt(item)">
          {{ item }}
        </li>
      </ul>
    </div>
    <div class="hotSearch max-auto" style="margin-top: 30px">
      <p style="position: absolute; left: 0">推荐专业:</p>
      <ul>
        <li
          v-for="(item, index) in recommendMajor"
          @click="gotoMajorRanking(item)"
        >
          {{ item }}
        </li>
      </ul>
    </div>
    <div class="bottomContainer">
      <div class="bottomContent">
        <div style="float: left; display: flex">
          <div class="ranking-cn">校园风光</div>
          <div class="ranking-en">Campus Scenery</div>
        </div>
      </div>
    </div>
  </div>
  <div style="width: 1519px">
    <div class="right-main" style="margin-top: 16px">
      <div class="title">
        <div class="change" v-if="logined">
          <img src="../assets/change.svg" alt="" />
          <span @click="change">换一批</span>
        </div>
        <div v-if="!logined" class="nologin">登陆后可查看</div>
      </div>
      <div class="recommend-container" v-if="logined">
        <!-- 这个地方做成轮播效果吧 -->
        <ul ref="swiper">
          <li
            class="school-item"
            v-for="(item, index) in recommendSchool"
            :key="index"
          >
            <div class="top-bg">
              <img :src="schoolImg(item?.school_id, 'imagesbk', 'bk')" />
            </div>
            <!-- <div class="school-img">
              校徽
              <img :src="schoolImg(item.school_id, 'images')" alt="" />
            </div> -->
            <div class="mid-content">
              <div class="school-text" @click="gotoSchool(item?.name)">
                <div class="cn-name">{{ item?.name }}</div>
                <div class="en-name">{{ item?.nameEn }}</div>
              </div>
              <ul class="school-tag">
                <li class="tag">{{ item?.province_name }}</li>
                <li class="tag">{{ item?.type_name }}</li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="mainBottom max-auto">
      <ul>
        <li class="mainBottomContent">
          <div class="leftArea">
            <div class="leftTitle">中国大学排名</div>
            <div class="leftSubTit">Best Chinese Universities Ranking</div>
          </div>
          <div class="rightArea">
            <ul class="subjects">
              <li
                class="subjectsItem"
                v-for="(item, i) in rankingItem"
                :key="i"
              >
                <span @click="gotoRankingPage(item)">{{ item }}</span>
              </li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import myAxios from "../js/myAxios";
import selectItem from "../js/selectItem";
import animate from "../js/animate";
import bus from "../eventBus";
const { animateFun } = animate;
const { UniversityType } = selectItem;
const { axios, baseUrl } = myAxios;
export default {
  name: "Main",
  data() {
    return {
      inputContent: "",
      hotSeatchSchool: ["清华大学", "华中科技大学", "电子科技大学", "武汉大学"],
      recommendMajor: ["经济学", "电子信息", "计算机科学与技术", "物理学"],
      rankingItem: [],
      radio: "1",
      recommendSchool: [],
      nums: 0,
      logined: false, //判断用户是否已经登录
    };
  },
  methods: {
    async searchIt() {
      const radio = this.radio; // 判断搜索大学还是专业
      const token = sessionStorage.getItem("token");
      const searchName = this.inputContent;
      if (radio === "1") {
        const searchRes = await axios({
          // 发起搜索请求
          method: "get",
          url: `${baseUrl}colleges/list/${searchName}`,
          headers: {
            token,
          },
        });
        if (searchRes.data.code === 401) {
          this.$message("请先登录再使用");
          return;
        }
        const schoolDetail = JSON.stringify(
          searchRes?.data?.data?.content[0] || {}
        );
        const schoolName = searchRes?.data?.data?.content[0]?.name || "";
        this.$router.push({
          name: "searchRes",
          query: { schoolName, schoolDetail },
        });
      } else if (radio === "2") {
        // 搜索大学专业的情况
        const searchRes = await axios({
          method: "get",
          url: `${baseUrl}majors/${searchName}`,
          headers: {
            token,
          },
        });
        if (searchRes.data.code === 401) {
          this.$message("请先登录再使用");
          return;
        }
        const mname = searchRes?.data?.data?.major?.name;
        // console.log(searchDetail);
        this.$router.push({
          name: "MajorRanking",
          query: { mname },
        });
      } else {
        alert("请选择搜索类型");
      }
    },
    async hotSearchIt(schoolName) {
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
    keydown() {
      this.searchIt();
    },
    gotoRankingPage(typeName) {
      this.$router.push({
        name: "SoftRanking",
        params: { typeName },
      });
    },
    change() {
      const swiper = this.$refs.swiper;
      this.nums++;
      if (this.nums === 8) {
        swiper.style.left = 0;
        this.nums = 1;
      }
      // 760的话9个学校没问题，如果学校多了需要重新设置
      animateFun(swiper, -360 * this.nums, 15);
    },
    gotoMajorRanking(mname) {
      console.log(mname);
      this.$router.push({
        name: "MajorRanking",
        query: { mname },
      });
    },
    async gotoSchool(schoolName = "清华大学") {
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
    schoolImg(schoolId, images, bk = "") {
      return `../../${images}/school${bk}_${schoolId}.jpg`;
    },
    initData() {
      // 这里把综合类等后面的类字取消，并把其他 全部取消。
      this.rankingItem =
        UniversityType.map((val) => {
          return val[val.length - 1] === "类"
            ? val
                .split("")
                .slice(0, val.length - 1)
                .join("")
            : val;
        }).slice(1, UniversityType.length - 1) || [];
      if (sessionStorage.getItem("userMsg")) {
        // 如果用户已经登录
        this.logined = true;
        const userMsg = JSON.parse(sessionStorage.getItem("userMsg") || "{}");
        const hotSearch = (userMsg?.data?.hotSearch || []).map((item) => {
          // 处理一下后端的数据
          return item.hotword;
        });
        const recommListObj = userMsg?.data?.recommList || {};
        const recommList = [];
        console.log(userMsg);
        for (let k in recommListObj) {
          if (k !== "id") {
            recommList.push(recommListObj[k]);
          }
        }

        this.recommendSchool = recommList || [];
        this.hotSeatchSchool = hotSearch || this.hotSeatchSchool;
      }
    },
    async update() {
      if (sessionStorage.getItem("token")) {
        const username = sessionStorage.getItem("username");
        const password = sessionStorage.getItem("password");
        const resMsg = await axios({
          method: "post",
          url: `${baseUrl}user/login`,
          data: {
            username,
            password,
          },
        });
        const res = resMsg.data;
        sessionStorage.setItem("userMsg", JSON.stringify(res));
      }
    },
  },
  created() {
    this.update();
    this.initData();
    bus.on("hasLogin", () => {
      this.logined = true;
      this.initData();
    });
  },
};
</script>

<style lang="less" scoped>
.max-auto {
  margin: 0 auto;
}
.mainBg {
  position: relative;
  // background: url("../assets/test.png");
  // background: url("../assets/testBg.png");
  background: url("../assets/mainBg.jpg");
  background-size: cover;
  width: 1519px;
  height: 330px;
  padding-top: 50px;
  .inputBox {
    position: relative;
    display: flex;
    // top: -70px;
    align-items: center;
    background-color: rgba(96, 110, 231, 0.59);
    width: 660px;
    height: 80px;
    border-radius: 8px;
    margin-bottom: 15px;
    :deep(.inputContent > .el-input__inner) {
      height: 55px;
      width: 550px;
      margin-left: 12px;
      padding: 0 10px;
      color: #333;
      border: none;
      border-radius: 8px;
      font-size: 16px;
    }
    .search {
      width: 60px;
      font-size: 18px;
      color: #fff;
      letter-spacing: 0;
      text-align: center;
      padding: 10px 15px;
      margin-left: 3px;
      cursor: pointer;
    }
  }
  :deep(.el-radio__input.is-checked + .el-radio__label) {
    // 修改elementui默认样式
    color: rgba(64, 158, 255);
  }
  :deep(.el-radio__label) {
    color: #fff;
  }
  .hotSearch {
    position: relative;
    height: 34px;
    width: 660px;
    overflow: hidden;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
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
  .bottomContainer {
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
    bottom: 0;
    height: 60px;
    width: 1070px;
    padding-left: 20px;
    padding-right: 20px;
    background-color: #fff;
    border-radius: 8px 8px 0 0;
    .bottomContent {
      height: 36px;
      padding-top: 24px;
      width: 1070px;

      background-color: #fff;
      border-radius: 8px 8px 0 0;
      border-bottom: 1px solid #ccc;
      .ranking-cn {
        font-size: 24px;
        color: #333;
        letter-spacing: 0;
      }
      .ranking-en {
        margin-left: 18px;
        font-size: 18px;
        color: #aeaeae;
        letter-spacing: 0;
        line-height: 39px;
      }
    }
  }
}
.mainBottom {
  width: 1070px;
  padding: 20px;
  background-color: #fff;
  overflow: hidden;
  .mainBottomContent {
    display: flex;
    width: 100%;
    background-size: 100% auto;
    background-repeat: no-repeat;
    margin-bottom: 10px;
    padding: 10px 0;
    overflow: hidden;
    border-radius: 8px;
    // background-image: url(/_nuxt/img/bcmr-bg.b4fb3d9.png);
    // background-color: #849b49;
    background-color: rgba(132, 155, 73, 0.9);
    .leftArea {
      width: 65%;
      border-right: 1px solid #fff;
      padding: 0 30px;
      position: relative;
      min-height: 182px;
      box-sizing: border-box;
      color: #fff;
      overflow: hidden;
      .leftTitle {
        font-size: 34px;
        margin-top: 15px;
        line-height: 48px;
        height: 48px;
        overflow: hidden;
      }
      .leftSubTit {
        font-size: 20px;
        margin-top: 30px;
        line-height: 28px;
        height: 28px;
        overflow: hidden;
      }
    }
    .rightArea {
      float: right;
      width: 34%;
      height: 182px;

      .subjects {
        display: flex;
        flex-wrap: wrap;
        padding-left: 40px;
        vertical-align: middle;
        text-align: left;

        .subjectsItem {
          width: 75px;
          height: 44px;
          padding: 5px 0;
          color: #fff;
          font-size: 18px;
          line-height: 34px;
          height: 34px;
          overflow: hidden;
        }
        .subjectsItem span {
          cursor: pointer;
        }
        .subjectsItem > span:hover {
          color: red;
        }
      }
    }
  }
}
.right-main {
  position: relative;
  background-color: #fff;
  border-radius: 4px;
  padding: 0 16px 16px;
  width: 1070px;
  left: 50%;
  transform: translate(-50%, 0px);
  .title {
    position: relative;
    display: flex;
    align-items: center;
    height: 45px;
    margin-left: -16px;
    .nologin {
      position: absolute;
      left: 445px;
      color: rgba(174, 174, 192);
      font-size: 20px;
    }
    .left {
      width: 4px;
      height: 16px;
      background-color: #e5002d;
      border-radius: 0 2px 2px 0;
    }
    span {
      margin-left: 12px;
      font-family: PingFangSC-Semibold;
      font-size: 16px;
      color: #312f31;
    }
    .change {
      position: absolute;
      top: 12px;
      right: 16px;
      cursor: pointer;
      img {
        width: 13px;
        height: 13px;
      }
      span {
        font-family: PingFang SC;
        font-size: 14px;
        color: #d60b0a;
        margin-left: 4px;
      }
    }
    .detailMajorTitle {
      display: block;

      margin: 0 auto;
      text-align: center;
      font-size: 24px;
      font-weight: 700;
      color: #434343;
      margin-bottom: 6px;
    }
  }
  .sub_tags {
    text-align: center;
    margin-bottom: 26px;
    .tag_item {
      font-size: 12px;
      color: #fe6500;
      padding: 4px 12px;
      background: #fff9f4;
      margin-right: 12px;
    }
  }
  .recommend-container {
    position: relative;
    width: 100%;
    overflow: hidden; //超出部分滚动
    padding: 5px;
    height: 350px;

    ul {
      position: absolute;
      left: 0;
      display: flex;
      width: 1200%;
      .school-item {
        position: relative;
        margin-right: 15px;
        width: 345px;
        // display: flex;
        // flex-direction: column;
        // justify-content: center
        padding-top: 12px;
        background-color: #f8f8f8;
        border-radius: 4px;
        min-height: 282px;
        z-index: 100;
        .top-bg {
          margin: 0 auto;
          // margin-top: 12px;
          width: 359px;
          height: 245px;
          border-radius: 4px;
          img {
            width: 100%;
            height: 100%;
          }
        }
        .mid-content {
          // margin: 0 auto;
          margin-left: 25px;
          width: 216px;
          margin-top: 12px;
          padding-bottom: 4px;
          .school-text {
            display: flex;
            flex-direction: column;
            cursor: pointer;
            .cn-name {
              font-family: PingFangSC-Semibold;
              font-size: 16px;
              color: #312f31;
            }
            .en-name {
              font-family: PingFangSC-Regular;
              font-size: 12px;
              color: #8e8c8e;
              margin-top: 4px;
            }
          }
          .school-text:hover .cn-name {
            color: red;
          }
          .school-tag {
            position: static;
            margin-top: 10px;
            width: 100%;
            display: flex;
            justify-content: flex-start;
            align-items: center;
            flex-wrap: wrap;
            margin-right: -10px;
            .tag {
              height: 16px;
              background-color: #fff;
              border-radius: 4px;
              padding: 2px 7px;
              font-family: PingFangSC-Regular;
              font-size: 12px;
              color: #8d8b8d;
              white-space: nowrap;
              margin-right: 8px;
              margin-bottom: 8px;
            }
          }
        }

        .school-img {
          box-shadow: 0 4px 12px 0 rgb(0 0 0 / 12%);
          width: 55px;
          height: 55px;
          border-radius: 4px;
          background-color: #fff;
          display: flex;
          justify-content: center;
          align-items: center;
          position: absolute;
          top: 215px;
          right: 15px;
          img {
            width: 55px;
            height: 55px;
          }
        }
      }
    }
  }
  table {
    width: 100%;
    thead {
      display: table;
      width: 748px;
      table-layout: fixed;
      tr {
        background-color: #f9f9f9;
        font-family: PingFangSC-Regular;
        font-size: 14px;
        color: #605e60;
        height: 40px;
      }
    }
    tbody {
      display: table;
      width: 748px;
      table-layout: fixed;
      // overflow: auto;
      tr {
        cursor: pointer;
        td {
          font-family: PingFangSC-Regular;
          font-size: 14px;
          color: #312f31;
          text-align: center;
          height: 40px;
        }
      }

      tr:nth-child(2n) {
        background-color: #f9f9f9;
      }
      tr:hover td {
        color: red;
      }
    }
  }
}
</style>
<template>
  <!-- <h1>{{ schoolName }}</h1>
  <h2>{{ schoolDetail }}</h2> -->
  <div id="bg">
    <div class="universityName">
      <div class="items">
        <span @click="gobackUni">院校</span> —— {{ schoolName }}
      </div>
    </div>
    <div class="main-container">
      <div class="left-container">
        <div class="school-msg">
          <!-- <img src="../../images/school_140.jpg" alt="" class="schoo-img" /> -->
          <img :src="school_id" alt="" class="schoo-img" />
          <div class="name-cn">{{ schoolName }}</div>
          <div class="name-en">{{ nameEn }}</div>
          <img src="../assets/shadow.png" alt="" class="shadow" />
          <ul class="school-labels">
            <!-- <li v-if="schoolDetail.dual_class_name === '双一流'">双一流</li> -->
            <!-- <li v-if="belong">教育部直属</li> -->
            <li>{{ typeName }}</li>
            <li v-if="is985">985</li>
            <li v-if="is211">211</li>
            <li v-if="isdualSchool">双一流</li>
            <li>{{ natureName }}</li>
          </ul>
          <div class="extra-msg">
            <div class="school-pro">
              <img src="../assets/province.svg" alt="" />
              {{ provinceName }}
              <span style="margin-left: 150px">教育部直属</span>
            </div>
            <div class="detail-msg">
              <img src="../assets/website.svg" alt="" />
              <a :href="school_site" target="_blank"> {{ school_site }}</a>
              <!-- {{ school_site }} -->
            </div>
          </div>
          <div class="school-introduction">
            <div class="title"><span>学校简介</span></div>
            <p>
              {{ detailContent }}
            </p>
          </div>
        </div>
      </div>
      <div class="right-container">
        <div class="right-main" v-if="displayMajorItem">
          <div class="title" style="padding-top: 20px">
            <span class="detailMajorTitle">{{ majorMsg.majorName }}</span>
            <div class="change">
              <!-- <img src="../assets/change.svg" alt="" /> -->
              <span @click="this.displayMajorItem = !this.displayMajorItem"
                >&lt 返回</span
              >
            </div>
          </div>
          <div class="sub_tags">
            <span class="tag_item">{{ majorMsg.year }}</span>
            <span class="tag_item">{{ majorMsg.type }}</span>
          </div>

          <div class="recommend-container" style="height: 443px">
            <p v-html="majorMsg.detailMsg" class="majorDetailMsg"></p>
          </div>
        </div>
        <div class="right-main" v-if="!displayMajorItem">
          <div class="title">
            <div class="left"></div>
            <span>开设专业</span>
          </div>
          <table>
            <thead>
              <tr>
                <th style="width: 80px">类别</th>
                <th style="width: 80px">年限</th>
                <th style="width: auto; text-align: center">专业名称</th>
              </tr>
            </thead>
          </table>
          <!-- 这里让thead固定在上面 -->
          <div class="recommend-container">
            <table>
              <tbody>
                <tr v-for="item in majorList" @click="displayMajorDetail(item)">
                  <td style="width: 80px">{{ item.type_name }}</td>
                  <td style="width: 80px">{{ item.limit_year }}</td>
                  <td style="width: auto; text-align: center">
                    {{ item.special_name }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="right-main" v-if="displayMajorItem">
          <div class="title">
            <div class="left"></div>
            <span
              >与<span style="color: red">{{ schoolName }}</span
              ><span style="font-size: 18px; font-weight: 600">{{
                majorMsg.majorName
              }}</span>
              相似的学校专业有：</span
            >
          </div>
          <table>
            <thead>
              <tr>
                <th style="width: 200px">学校名称</th>
                <th style="width: auto; text-align: center">专业</th>
              </tr>
            </thead>
          </table>
          <!-- 这里让thead固定在上面 -->
          <div class="recommend-container">
            <table>
              <tbody>
                <tr
                  v-for="item in majorRecommcs"
                  @click="gotoSchool(item.college?.name)"
                >
                  <td style="width: 200px">{{ item.college?.name }}</td>
                  <td style="width: auto; text-align: center">
                    <span
                      style="margin-right: 15px"
                      v-for="majorItem in item.majors"
                      >{{ majorItem.name }}</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="right-main" style="margin-top: 16px">
          <div class="title">
            <div class="left"></div>
            <span>相似院校</span>
            <div class="change">
              <img src="../assets/change.svg" alt="" />
              <span @click="change(2)">换一批</span>
            </div>
          </div>
          <div class="recommend-container">
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
                <div class="school-img">
                  <!-- <img src="../../images/school_138.jpg" alt="" /> -->
                  <!-- 动态计算图片 -->
                  <img :src="schoolImg(item?.school_id, 'images')" alt="" />
                </div>
                <div class="mid-content">
                  <div class="school-text" @click="gotoSchool(item?.name)">
                    <div class="cn-name">
                      {{ item?.name }}
                    </div>
                    <div class="en-name">{{ item?.nameEn }}</div>
                  </div>
                  <ul class="school-tag">
                    <li class="tag">{{ item?.province_name }}</li>
                    <li class="tag">{{ item?.type_name }}</li>
                  </ul>
                  <ul class="school-tag">
                    <li class="tag" v-for="tag in item?.tags">{{ tag }}</li>
                  </ul>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import myAxios from "../js/myAxios";
import animate from "../js/animate";
const { axios, baseUrl } = myAxios;
const { animateFun } = animate;
export default {
  name: "result",
  data() {
    const { schoolName } = this.$route.query;
    let { schoolDetail } = this.$route.query;
    // 坑 结构赋值这里不可以直接 || {}兜底，因为左边一直都是存在的
    schoolDetail = typeof schoolDetail === "string" ? schoolDetail : "{}";
    schoolDetail = JSON.parse(schoolDetail);
    return {
      schoolName,
      schoolDetail,
      is985: false,
      is211: false,
      belong: false,
      provinceName: "",
      typeName: "",
      natureName: "",
      nameEn: "",
      isdualSchool: false,
      detailContent: "",
      nums: 0,
      id: "",
      recommendSchool: [],
      school_site: "",
      majorList: [],
      displayMajorItem: false,
      majorMsg: {},
      majorRecommcs: [],
      // school_id: "",
    };
  },
  async created() {
    this.initData();
  },
  computed: {
    school_id() {
      // 使用计算属性动态计算图片的src
      return `../../images/school_${this.id}.jpg`;
    },
  },
  methods: {
    change(n) {
      let swiper;
      if (n === 1) {
        swiper = this.$refs.swiperMajor;
      } else {
        swiper = this.$refs.swiper;
      }
      this.nums++;
      if (this.nums === 4) {
        swiper.style.left = 0;
        this.nums = 1;
      }
      // 760的话9个学校没问题，如果学校多了需要重新设置
      animateFun(swiper, -760 * this.nums);
    },
    disposeMsg(target) {
      // 对推荐对象进行处理，返回一个数组，方便v-for遍历。
      const res = [];
      const keys = Object.keys(target);
      for (let i = 0; i < keys.length - 2; i++) {
        res.push(target[keys[i]]);
      }
      for (let i = 0; i < 3; i++) {
        res.push(res[i]);
      }
      // 把tags返回成数组的形式
      return res.map((item) => {
        const temp = item;
        // 这里前面添加有个引用的问题，前面修改了后面也会修改 所以需要if一下
        if (item?.tags?.length === 0) {
          temp.tags = [];
        } else if (typeof item?.tags === "string") {
          temp.tags = item?.tags.split(",");
        }
        return temp;
      });
    },
    schoolImg(schoolId, images, bk = "") {
      return `../../${images}/school${bk}_${schoolId}.jpg`;
    },
    async gotoSchool(schoolName) {
      const token = sessionStorage.getItem("token");
      this.displayMajorItem = false;
      const searchRes = await axios({
        // 发起搜索请求
        method: "get",
        url: `${baseUrl}colleges/list/${schoolName}`,
        headers: {
          token,
        },
      });
      const schoolDetail = JSON.stringify(
        searchRes?.data?.data?.content[0] || {}
      );

      this.$router.push({
        name: "searchRes",
        query: { schoolName, schoolDetail },
      });
      // 这里需要刷新页面才可以实现路由跳转 奇怪
    },
    async initData() {
      const { schoolName } = this.$route.query;
      let { schoolDetail } = this.$route.query;
      // 坑 结构赋值这里不可以直接 || {}兜底，因为左边一直都是存在的
      schoolDetail = typeof schoolDetail === "string" ? schoolDetail : "{}";
      schoolDetail = JSON.parse(schoolDetail);
      this.schoolName = schoolName;
      this.id = schoolDetail.school_id;
      this.is985 = schoolDetail.f985 === 1;
      this.is211 = schoolDetail.f211 === 1;
      this.belong = schoolDetail.belong === "教育部";
      this.provinceName = schoolDetail.province_name;
      this.typeName = schoolDetail.type_name;
      this.isdualSchool = schoolDetail.dual_class_name === "双一流";
      this.natureName = schoolDetail.nature_name;
      this.nameEn = schoolDetail.nameEn;
      this.detailContent = schoolDetail.content;
      // 这个页面再发起一次详细信息的请求
      const detailMsg = await axios({
        method: "get",
        url: `${baseUrl}colleges/${this.id}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      if (detailMsg.data.code === 401) {
        this.$message("请先登录");
        return;
      }

      this.school_site = detailMsg?.data?.data?.details?.school_site;

      const recommed = detailMsg?.data?.data.recommcs;
      console.log(recommed);
      const major = detailMsg?.data?.data.majors;
      const recommendSchool = this.disposeMsg(recommed); // 对推荐的信息进行处理
      this.majorList = major;
      this.recommendSchool = recommendSchool;
      //  this.schoolImg()
    },
    gobackUni() {
      this.$router.push("/University");
    },
    async displayMajorDetail(item) {
      // 展示专业的详细信息
      const { scode, spcode } = item;
      const schoolName = this.schoolName; // 大学名称
      const majorMsg = this.majorMsg;
      majorMsg.majorName = item?.special_name || "专业名称";
      majorMsg.year = item?.limit_year;
      majorMsg.type = item?.type_name;
      const detailRes = await axios({
        method: "get",
        // url: `${baseUrl}colleges/details?sname=${schoolName}&mname=${majorMsg.majorName}`,
        url: `${baseUrl}colleges/details?scode=${scode}&spcode=${spcode}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      const recommcs = detailRes.data.data.recommcs;
      majorMsg.detailMsg = detailRes.data.data.minfo;
      this.displayMajorItem = !this.displayMajorItem;
      console.log(recommcs);
      this.majorRecommcs = recommcs.map((item) => {
        item.majors = item.majors.slice(0, 4);
        return item;
      });
    },
  },
  watch: {
    // 路由跳转不刷新问题
    $route(to, from) {
      //监听路由是否变化
      if (to.query.schoolName != from.query.schoolName) {
        //变化后的操作
        this.initData();
        // this.init(); //重新加载数据
      }
    },
  },
};
</script>

<style lang="less" scoped>
::-webkit-scrollbar {
  // 滚动条选择器
  display: none;
}
#bg {
  // position: relative;
  background-color: rgba(244, 243, 244);
  min-height: 800px;
  .universityName {
    width: 1200px;
    margin: 0 auto;
    overflow: hidden;
    display: flex;
    align-items: center;
    height: 50px;
    .items {
      color: #8d8b8d;
      white-space: nowrap;
      font-size: 16px;
      span:hover {
        cursor: pointer;
        color: red;
      }
    }
  }
  .main-container {
    position: relative;
    margin: 0 auto;
    width: 1200px;
    min-height: 800px;
    .left-container {
      position: absolute;
      // top: 70px;
      float: left;
      // position: -webkit-sticky;
      // position: sticky;
      background-color: #fff;
      width: 382px;
      padding: 32px 0;
      border-radius: 4px;
      // height: calc(100vh - 208px);
      height: 520px;
      min-height: 456px;
      margin-bottom: 20px;
      // overflow: scroll;
      overflow-y: scroll;

      .school-msg {
        display: flex;
        flex-direction: column;
        align-items: center;
        .schoo-img {
          display: block;
          width: 74px;
          height: 74px;
        }
        .name-cn {
          margin-top: 12px;
          font-family: PingFangSC-Semibold;
          font-size: 24px;
          color: #312f31;
        }
        .name-en {
          margin-top: 4px;
          font-family: PingFangSC-Regular;
          font-size: 14px;
          color: #605e60;
        }
        .shadow {
          display: block;
          width: 100%;
          margin-top: 10px;
        }
        .school-labels {
          display: flex;
          margin-top: 5px;
          width: 334px;
          flex-wrap: wrap;
          li {
            // background: #f4f3f4;
            font-size: 14px;
            border-radius: 4px;
            padding: 2.5px 6px;
            margin-right: 8px;
          }
        }
        .extra-msg {
          background-color: #f8f8f8;
          border-radius: 4px;
          margin-top: 20px;
          font-family: pingfang-SC;
          font-size: 14px;
          color: #312f31;
          padding: 12px;
          img {
            width: 20px;
            margin-right: 5px;
            vertical-align: bottom;
          }
          .school-pro {
            width: 334px;
          }
          .detail-msg {
            margin-top: 12px;
            a {
              font-family: pingfang-SC;
              font-size: 14px;
              color: #312f31;
              text-decoration: none;
            }
            a:hover {
              color: red;
            }
          }
        }
        .school-introduction {
          margin-top: 20px;
          .title {
            width: 334px;
            font-family: PingFangSC-Semibold;
            font-size: 16px;
            color: #312f31;
            span {
              border-bottom: 2px solid #d60b0a;
            }
          }
          p {
            width: 334px;
            font-family: PingFangSC-Regular;
            font-size: 14px;
            color: #605e60;
            margin-top: 12px;
            text-align: justify;
            line-height: 22px;
            // overflow: scroll;
          }
        }
      }
    }
    .right-container {
      width: 780px;
      position: relative;
      left: 415px;
      .right-main {
        position: relative;
        background-color: #fff;
        border-radius: 4px;
        padding: 0 16px 16px;
        .title {
          position: relative;
          display: flex;
          align-items: center;
          height: 45px;
          margin-left: -16px;
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
          overflow: auto; //超出部分滚动
          padding: 5px;
          height: 300px;

          ul {
            position: absolute;
            left: 0;
            display: flex;
            width: 1200%;
            .school-item {
              position: relative;
              margin-right: 15px;
              width: 240px;
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
                width: 216px;
                height: 126px;
                border-radius: 4px;
                img {
                  width: 100%;
                  height: 100%;
                }
              }
              .mid-content {
                margin: 0 auto;
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
                  .majorName {
                    margin-top: 12px;

                    span {
                      display: block;
                      margin-bottom: 5px;
                      font-family: PingFangSC-Semibold;
                      font-size: 18px;
                      color: #312f31;
                    }
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
                width: 50px;
                height: 50px;
                border-radius: 4px;
                background-color: #fff;
                display: flex;
                justify-content: center;
                align-items: center;
                position: absolute;
                top: 114px;
                right: 15px;
                img {
                  width: 40px;
                  height: 40px;
                }
              }
            }
          }
          .majorDetailMsg {
            color: rgba(0, 0, 0, 0.65);
            font-size: 14px;
            font-variant: tabular-nums;
            line-height: 1.5;
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
    }
  }
}
</style>
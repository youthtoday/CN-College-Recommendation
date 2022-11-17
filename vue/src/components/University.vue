<template>
  <div class="inputBox">
    <div class="inputFather">
      <el-input
        v-model="inputContent"
        placeholder="请输入院校"
        class="inputItem"
      />
      <el-button class="btn" @click="querySchoolMsg">搜索</el-button>
    </div>
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
      <div class="schoolContentTitle">共查询到{{ totalElements }}所院校</div>
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
              <li class="schoolTypeItem">{{ item.nature_name }}</li>
              <li class="schoolTypeItem">{{ item.province_name }}</li>
              <li class="schoolTypeItem">{{ item.type_name }}</li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
    <div class="example-pagination-block">
      <el-pagination
        layout="prev, pager, next"
        background
        :total="totalPages"
        :pager-count="8"
        @current-change="handlePage"
        hide-on-single-page="true"
      />
    </div>
  </div>
</template>

<script>
import selectItem from "../js/selectItem";
import myAxios from "../js/myAxios";
const { axios, baseUrl } = myAxios;
const { province, UniversityType } = selectItem;
export default {
  name: "University",
  data() {
    return {
      inputContent: "",
      initialPro: "全部省份",
      initialType: "全部类型",
      province,
      UniversityType,
      schoolArr: [],
      imageUrl: "",
      totalElements: "",
      totalPages: "",
      currentPage: "",
    };
  },
  methods: {
    async querySchoolMsg() {
      const currentPage = this.currentPage;
      const inputContent = this.inputContent;
      const province = this.initialPro;
      const type = this.initialType;
      const res = await axios({
        method: "get",
        url: `${baseUrl}colleges/list/${inputContent}?province=${province}&type=${type}&pageNum=${currentPage}`,
        headers: {
          token: sessionStorage.getItem("token"),
        },
      });
      // console.log(province);
      // console.log(
      //   `${baseUrl}colleges/list/${inputContent}?province=${province}&type=${type}&pageNum=${currentPage}`
      // );
      // console.log(res);
      const schoolArr = res?.data?.data?.content || [];
      const { totalElements, totalPages } = res?.data?.data;
      schoolArr.forEach((item) => {
        item.url = `../images/school_${item.school_id}.jpg`;
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
      this.$router.push({
        name: "searchRes",
        query: { schoolName, schoolDetail },
      });
    },
    async handlePage(currentPage) {
      this.currentPage = currentPage;
      this.querySchoolMsg();
    },
  },
  created() {
    this.querySchoolMsg(1);
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
  margin-top: 20px;
  padding-left: 520px;
}
.inputBox {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 105px;
  width: 876px;
  margin: 0 auto;
  padding: 40px 0 24px;
  .inputFather {
    display: flex;
    :deep(.inputItem > .el-input__inner) {
      height: 43px;
      border: 1px solid #d7d7d7;
      padding-left: 10px;
      padding-right: 10px;
      font-size: 16px;
      color: #333;
      width: 765px;
      border-radius: 4px 0 0 4px;
    }
    .btn {
      padding: 0 15px;
      font-size: 16px;
      background: #d60b0a;
      color: #fff;
      font-weight: 500;
      vertical-align: middle;
      border-radius: 4px;
      height: 43px;
      width: 115px;
      border: none;
      cursor: pointer;
      background-image: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0OCA0OCI+PGRlZnM+PHN0eWxlPi5jbHMtMXtmaWxsOiNmZmZmZmY7fTwvc3R5bGU+PC9kZWZzPjx0aXRsZT5pY29uPC90aXRsZT48ZyBpZD0i5Zu+5bGCXzIiIGRhdGEtbmFtZT0i5Zu+5bGCIDIiPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTQzLDQwLjQ2bC00Ljg5LTQuODlhMTksMTksMCwxLDAtMi41NCwyLjU0TDQwLjQ2LDQzQTEuNzksMS43OSwwLDAsMCw0Myw0MywxLjc4LDEuNzgsMCwwLDAsNDMsNDAuNDZaTTIzLjQ3LDM4Ljg3YTE1LjQsMTUuNCwwLDEsMSwxMC44OS00LjUxQTE1LjI5LDE1LjI5LDAsMCwxLDIzLjQ3LDM4Ljg3WiIvPjwvZz48L3N2Zz4=);
      background-repeat: no-repeat;
      background-size: 20px, 20px;
      background-position: left 19px top 13px;
      box-sizing: border-box;
      :deep(span) {
        // display: block;
        margin-left: 12px;
      }
    }
  }
  .inputSelect {
    display: flex;
    height: 30px;

    :deep(.selectItem) {
      width: 120px;
    }
  }
}
.schoolBox {
  min-height: 460px;
  width: 100%;
  background-color: rgba(244, 243, 244);
  padding: 16px 0;
  .schoolContent {
    width: 876px;
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
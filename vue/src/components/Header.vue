<template>
  <div class="headerBottom">
    <el-menu
      :default-active="$route.path"
      class="el-menu-demo itemHeader"
      mode="horizontal"
      @select="handleSelect"
      router
    >
      <!-- <router-link to="/main"> -->
      <el-menu-item index="/main">首页</el-menu-item>
      <!-- </router-link> -->

      <el-sub-menu index="2">
        <template #title>大学排名</template>
        <el-menu-item index="/SoftRanking">中国大学排名</el-menu-item>
        <el-menu-item index="/SubjectRanking">中国大学专业排名</el-menu-item>
        <!-- <el-menu-item index="2-3">中国大学学科排名</el-menu-item>
        <el-sub-menu index="2-4">
          <template #title>其他排名</template>
          <el-menu-item index="2-4-1">中国两岸大学排名</el-menu-item>
          <el-menu-item index="2-4-2">中国专科院校排名</el-menu-item>
        </el-sub-menu> -->
      </el-sub-menu>
      <el-menu-item index="/University">院校</el-menu-item>
      <el-menu-item index="/About">关于我们</el-menu-item>
      <el-button
        class="loginButton"
        type="text"
        @click="displayLogin"
        v-if="!hasLogin"
        >登录</el-button
      >
      <div class="hasLogined" v-if="hasLogin">
        <span>已登录</span>
      </div>
      <el-button
        v-if="!hasLogin"
        class="loginButton registerBtn"
        type="text"
        @click="displayRegister"
        >新用户注册</el-button
      >
    </el-menu>
  </div>
</template>
    
<script >
import bus from "../eventBus";
export default {
  name: "header",
  data() {
    return {
      hasLogin: false,
      username: "",
    };
  },
  methods: {
    displayLogin() {
      bus.emit("showLogin");
    },
    displayRegister() {
      bus.emit("showRegister");
    },
  },
  created() {
    if (sessionStorage.getItem("token")) {
      this.hasLogin = true;
      const username = sessionStorage.getItem("username");
      this.username = username;
    }
    bus.on("hasLogin", () => {
      this.hasLogin = true;
    });
  },
};
</script>
    
<style scoped lang="less">
// a {
//   font-size: 14px;
//   text-decoration: none;
//   line-height: 56px;
// }
.headerBottom {
  border-bottom: 1px solid #000;
  .itemHeader {
    position: relative;
    // border-bottom: 1px solid #000;
    // height: 45px;
    width: 1200px;
    margin: 0 auto;
    .loginButton {
      position: absolute;
      height: 100%;
      padding: 0 20px;
      // top: 13px;
      right: 20px;
      color: black;
      transition: all 0.3s;
    }
    .hasLogined {
      position: absolute;
      height: 100%;
      padding: 0 20px;
      right: 20px;

      span {
        color: #ccc;
        font-size: 14px;
        line-height: 55px;
      }
    }
    .registerBtn {
      right: 100px;
    }
    .loginButton:hover {
      color: #409eff;
      background-color: #ecf5ff;
    }
  }
}
</style>
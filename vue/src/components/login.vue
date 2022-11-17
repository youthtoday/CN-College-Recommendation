<template>
  <transition name="fade">
    <div class="Mask" v-if="isShow">
      <div class="mainBox" v-if="isShow">
        <div class="topContent">
          <div class="title">登录体验更多功能</div>
          <div class="subTitle"></div>
          <el-button
            :icon="Close"
            size="small"
            circle
            class="close"
            @click="closeIt"
          ></el-button>
        </div>
        <div class="userLoginInfo">
          <label for="username" class="username">用户名 :</label>
          <input
            id="username"
            class="usernameInput"
            placeholder="请输入用户名"
            v-model="username"
            autocomplete="off"
          />

          <label for="username" class="username password">密码 :</label>
          <input
            id="password"
            type="password"
            class="usernameInput"
            placeholder="请输入密码"
            v-model="password"
          />

          <el-button type="danger" class="login" @click="login">登录</el-button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import { getCurrentInstance } from "vue";
import myAxios from "../js/myAxios";
const { axios, baseUrl } = myAxios;
import { Close } from "@element-plus/icons-vue";
import bus from "../eventBus";
export default {
  name: "login",
  data() {
    return {
      isShow: false,
      username: "",
      password: "",
      Close,
      token: "",
    };
  },
  methods: {
    closeIt() {
      this.isShow = false;
    },
    async login(e) {
      let target = e.target;
      if (target.nodeName === "SPAN") {
        target = e.target.parentNode;
      }
      target.blur();
      const { username, password } = this;
      const resMsg = await axios({
        method: "post",
        url: `${baseUrl}user/login`,
        data: {
          username,
          password,
        },
      });
      const res = resMsg.data;
      if (res.code === 1001) {
        this.$message("用户不存在");
      } else if (res.code === 1002) {
        this.$message("密码错误");
      } else if (res.code === 1000) {
        const token = res.data.token;
        this.$message(`登录成功，欢迎您——${username}`);
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("userMsg", JSON.stringify(res));
        sessionStorage.setItem("username", username);
        sessionStorage.setItem("password", password);
        bus.emit("hasLogin", username, password);
        this.token = token; // 暂时看是用不到，先放着
        this.closeIt();
      } else {
        this.$message(`发生了未知错误`);
      }
    },
  },
  created() {
    bus.on("showLogin", () => {
      this.isShow = true;
    });
    axios({
      method: "post",
      url: `${baseUrl}user/login`,
      data: {
        username: "xs",
        password: "123",
      },
    }).then((val) => {
      bus.emit("message", val);
    });
  },
};
</script>

<style lang="less" scoped>
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.Mask {
  position: fixed;
  inset: 0px;
  z-index: 900;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  overflow: auto;
  .mainBox {
    position: absolute;
    z-index: 1000;
    left: 50%;
    top: 10%;
    transform: translate(-50%, 0);
    width: 520px;
    height: 380px;
    margin: 40px auto;
    background-color: #ccc;
    font-size: 14px;
    line-height: 1.5;
    border-radius: 10px;
    .topContent {
      position: relative;
      height: 92px;
      background-color: #ccc;
      border-radius: 10px;
      text-align: center;
      color: #8d8b8d;
      .title {
        font-size: 20px;
        line-height: 28px;
        color: #312f31;
        padding-top: 36px;
        padding-bottom: 8px;
      }

      .close {
        position: absolute;
        top: 0;
        right: 0;
        margin-right: 2px;
      }
    }
    .userLoginInfo {
      width: 400px;
      height: 269px;
      margin: 0 auto;
      margin-top: 20px;

      .username {
        display: block;
        margin-bottom: 2px;
        color: #605e60;
        font-size: 16px;
      }
      .usernameInput {
        box-sizing: border-box;
        height: 40px;
        width: 100%;
        padding: 6px 11px;
        font-size: 16px;
        background-color: #fff;
        background-image: none;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        transition: all 0.3s;
      }
      .password {
        margin-top: 30px;
      }
      .login {
        height: 40px;
        // background-color: var(--el-color-danger);
        border-radius: 4px;
        // color: #fff;
        // outline: 0;
        // border: none;
        width: 100%;
        font-size: 16px;
        margin-top: 28px;
        text-align: center;
        line-height: 40px;
      }
    }
  }
}
</style>
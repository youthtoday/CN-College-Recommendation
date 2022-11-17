<template>
  <transition name="fade">
    <div class="Mask" v-if="isShow">
      <div class="mainBox" v-if="isShow">
        <div class="topContent">
          <div class="title">新用户注册</div>
          <div class="subTitle">部分功能仅注册后可使用</div>
          <span class="delay" @click="closeIt">体验功能，稍后注册</span>
          <span class="gotoLogin" @click="gotoLogin">已有账号,立即登录</span>
          <el-button
            :icon="Close"
            size="small"
            circle
            class="close"
            @click="closeIt"
          ></el-button>
        </div>
        <div class="userLoginInfo">
          <el-form
            :model="ruleForm"
            status-icon
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
            class="demo-ruleForm"
          >
            <div style="display: flex">
              <el-form-item label="性别">
                <el-select v-model="ruleForm.gender" placeholder="请选择性别">
                  <el-option
                    v-for="(item, index) in gender"
                    :key="index"
                    :label="item"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="民族" style="margin-left: -40px">
                <el-select
                  v-model="ruleForm.nationality"
                  placeholder="请选择民族"
                >
                  <el-option
                    v-for="(item, index) in nationality"
                    :label="item"
                    :value="item"
                    :key="index"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display: flex">
              <el-form-item label="省份">
                <el-select v-model="ruleForm.province" placeholder="请选择省份">
                  <el-option
                    v-for="(item, index) in province"
                    :label="item"
                    :value="item"
                    :key="index"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="期望分数"
                prop="expect"
                style="margin-left: -28px"
              >
                <el-input v-model="ruleForm.expect"></el-input>
              </el-form-item>
            </div>
            <el-form-item label="学科">
              <el-select v-model="ruleForm.subjects" placeholder="请选择学科">
                <el-option
                  v-for="(item, index) in subjects"
                  :label="item"
                  :value="item"
                  :key="index"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                type="password"
                v-model="ruleForm.password"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input
                type="password"
                v-model="ruleForm.checkPass"
                autocomplete="off"
              ></el-input>
            </el-form-item>

            <el-form-item style="margin-top: 25px">
              <el-button type="primary" @click="submitForm('ruleForm')"
                >提交</el-button
              >
              <el-button
                @click="resetForm('ruleForm')"
                style="margin-left: 130px"
                >重置</el-button
              >
            </el-form-item>
          </el-form>
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
import selectItem from "../js/selectItem";
const { gender, nationality, province } = selectItem;
import bus from "../eventBus";
export default {
  name: "register",
  data() {
    const checkUsername = async (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入用户名"));
      }
      const { username } = this.ruleForm;
      const userNamePattern = /^[a-zA-Z0-9_]{4,16}$/;
      if (userNamePattern.test(username)) {
        // 用户名通过正则检测
        callback();
        // const res = await axios({
        //   method: "post",
        //   url: `${baseUrl}user/regist/name`,
        //   data: {
        //     username,
        //   },
        // });
        // console.log(res);
        // if (res?.data?.code === 1000) {
        //   // 用户名可注册
        //   callback();
        // } else {
        //   return callback(new Error(`用户名已被注册`));
        // }
      } else {
        return callback(
          new Error("用户名只能包含数字、字符、下滑线、且长度4-16位")
        );
      }
    };
    const validatePass = (rule, value, callback) => {
      const { password } = this.ruleForm;
      const passPattern = /^(?=.{6,})(?=.*\d)(?=.*[a-z]).*$/;
      if (value === "") {
        callback(new Error("请输入密码"));
      } else if (!passPattern.test(password)) {
        return callback(new Error("密码必须同时包括小写字母与数字且至少6位"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    const validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    const expectScore = (rule, value, callback) => {
      value = parseInt(value); // 先强制转为数字型
      if (!value) {
        return callback(new Error("请输入期望分数"));
      } else if (typeof value !== "number") {
        return callback(new Error("请输入数字"));
      } else if (value < 0 || value > 750) {
        return callback(new Error("分数超出范围"));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        username: "",
        password: "",
        checkPass: "",
        gender: "",
        nationality: "",
        subjects: "",
        expect: "",
        province: "",
      },
      rules: {
        password: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }],
        username: [{ validator: checkUsername, trigger: "blur" }],
        expect: [{ validator: expectScore, trigger: "blur" }],
      },
      isShow: false,
      username: "",
      password: "",
      confirmPas: "",
      Close,
      gender,
      nationality,
      province,
    };
  },
  methods: {
    closeIt() {
      this.isShow = false;
    },
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        const { username, password } = this.ruleForm;
        const ruleForm = this.ruleForm;
        const { gender, nationality } = this;
        // valid true 检验通过 false 校验失败
        if (valid) {
          const res = await axios({
            method: "post",
            url: `${baseUrl}user/regist`,
            data: {
              ...ruleForm,
            },
          });
          if (res.data.code === 1000) {
            // 进行登录操作
            const resMsg = await axios({
              method: "post",
              url: `${baseUrl}user/login`,
              data: {
                username,
                password,
              },
            });
            const user = resMsg.data;
            this.$alert("注册成功", "快去体验吧！", {
              confirmButtonText: "确定",
              callback: (action) => {
                this.$message({
                  type: "info",
                  message: `已自动登录`,
                });
              },
            });
            this.resetForm("ruleForm");
            this.isShow = false;
            const token = user.data.token;
            sessionStorage.setItem("token", token);
            sessionStorage.setItem("userMsg", JSON.stringify(user));
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("password", password);
            bus.emit("hasLogin", username, password);
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      const ruleForm = this.ruleForm;
      this.$refs[formName].resetFields();
      (ruleForm.gender = ""),
        (ruleForm.nationality = ""),
        (ruleForm.province = "");
    },
    gotoLogin() {
      this.isShow = false;
      bus.emit("showLogin");
    },
  },
  created() {
    bus.on("showRegister", () => {
      this.isShow = true;
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
    width: 435px;
    // height: 305px;
    margin: 40px auto;
    background-color: #ccc;
    font-size: 14px;
    line-height: 1.5;
    border-radius: 10px;
    .topContent {
      position: relative;
      height: 75px;
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
      .delay {
        position: absolute;
        right: 30px;
        top: 10px;
        cursor: pointer;
        font-size: 12px;
        color: rgb(214, 11, 10);
        // color: rgb(254, 101, 0);
      }
      .gotoLogin {
        position: absolute;
        cursor: pointer;
        left: 10px;
        top: 10px;
        font-size: 12px;
      }
      .gotoLogin:hover {
        // color: rgb(254, 101, 0);
        color: rgb(214, 11, 10);
      }
    }
    .userLoginInfo {
      width: 400px;
      // margin: 0 auto;
      margin-top: 30px;
    }
  }
}
</style>
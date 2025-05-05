<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-title">
        欢迎注册高校就业管理系统</div>
      <div class="register-form">
        <el-form :model="form">
          <el-form-item>
            <el-input v-model="form.username" prefix-icon="el-icon-user" style="width: 80%"
              placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" show-password prefix-icon="el-icon-lock" style="width: 80%"
              placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.email"  style="width: 80%" placeholder="请输入Email"></el-input>
          </el-form-item>
          <el-form-item>
            <template>
              <el-radio v-model="form.role" label="ROLE_COMPANY">企业</el-radio>
              <el-radio v-model="form.role" label="ROLE_STUDENT">学生</el-radio>
            </template>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.phoneNumber" style="width: 80%" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.realName" style="width: 80%" placeholder="请输入真实姓名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 80%; margin-top: 10px" type="primary" @click="register()">注册</el-button>
            <el-button style="width: 80%; margin-top: 10px;margin-right: 10px;" @click="cancel()">取消</el-button>
          </el-form-item>
        </el-form>

      </div>
    </div>
  </div>

</template>

<style>
.register-container {
  width: 100%;
  height: 100vh;
  background-image: url('@/assets/img/login_bg.jpg'); /* 替换为图片路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
}

.register-box {
  width: 400px;
  height: 650px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  border-radius: 15px; /* 圆角 */
  margin-top: 50px;
}
.register-title {
  width: 100%;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: #333;
  margin-top: 20px;
  margin-bottom: 50px;
}
.register-form {
  margin-top: 10px;
  text-align: center;
}
</style>

<script>

import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {}
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  created() {
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    register() {
      request.post("/admin/register", this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            message: '注册成功',
            type: 'success'
          });
          this.$router.push("/login");
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
    },
    cancel() {
      this.$router.push("/login");
    }
  }
}
</script>
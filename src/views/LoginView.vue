<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        欢迎登录高校就业管理系统
      </div>
      <div class="login-form">
        <el-form :model="form">
          <el-form-item>
            <el-input v-model="form.username" prefix-icon="el-icon-user"
             style="width: 80%"
              placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" 
            show-password prefix-icon="el-icon-lock" 
            style="width: 80%"
              placeholder="请输入密码"></el-input>
          </el-form-item>
          <div class="button-group">
            <el-button type="primary" @click="login()">登录</el-button>
            <el-button type="primary" @click="register()">注册</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: {}
    }
  },
  methods: {
    login() {
      request.post("/admin/login", this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            message: '登录成功',
            type: 'success'
          });
          localStorage.setItem("user", JSON.stringify(res.data));
          this.$router.push("/");
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
    },
    register() {
      this.$router.push("/register");
    }
  }
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  background-image: url('@/assets/img/login_bg.jpg'); /* 替换为图片路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  height: 350px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  border-radius: 15px; /* 圆角 */
}

.login-title {
  width: 100%;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: #333;
  margin-top: 20px;
  margin-bottom: 50px;
}

.login-form {
  margin-top: 10px;
  text-align: center;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 100%;
}

.button-group {
  width: 80%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  padding-top: 10px;
}

.button-group .el-button {
  width: 48%; /* 留一点间隙 */
  margin: 0;
}
</style>
<template>
<div>
  <div style="width: 400px; height: 650px; margin: 150px auto; background-color:rgba(107,149,224,0.5); border-radius: 10px">
    <div style="width: 100%; height: 100px; font-size: 30px; line-height: 100px; text-align: center; color: #4a5ed0">欢迎注册高校就业管理系统</div>
    <div style="margin-top: 25px; text-align: center; height: 320px;">
      <el-form :model="form">
        <el-form-item>
          <el-input v-model="form.username" prefix-icon="el-icon-user" style="width: 80%" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" show-password prefix-icon="el-icon-lock" style="width: 80%" placeholder="请输入密码"></el-input>
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
          <el-button style="width: 80%; margin-top: 10px;margin-right: 10px;"  @click="cancel()">取消</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</div>
  
</template>

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
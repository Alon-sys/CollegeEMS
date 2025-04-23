<template>
  <div>
  <el-container >
    <!-- 头部 -->
    <el-header :style="{ backgroundColor: '#687179', height: '60px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 20px' }">
      <div style="display: flex; align-items: center;">
        <img src="@/assets/img/logo.png" alt="" style="width: 50px; height: 50px; object-fit: contain; margin-top: 0;">
        <span style="font-size: 20px; margin-left: 10px; color: aliceblue;">高校就业管理系统</span>
    
      </div>
      <div style="display: flex; align-items: center;">
        <el-dropdown style="height: 60px; line-height: 60px;">
          <span class="el-dropdown-link" style="color: aliceblue; font-size: 16px; cursor: pointer; transition: color 0.3s;">
            {{ user.username }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <div @click="goToPersonalCenter()" style="padding: 10px 20px; transition: background-color 0.3s;">个人中心</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout()" style="padding: 10px 20px; transition: background-color 0.3s;">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
  <el-container>
    <!-- 侧边栏 -->
    <el-aside style="overflow: hidden; min-height: 100vh; background-color: #545c64; width: 250px">
      <el-menu :default-active="$route.path" router background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
  <el-menu-item index="/">
    <i class="el-icon-s-home"></i>
    <span slot="title">系统首页</span>
  </el-menu-item>
  <el-submenu index="2" v-if="user.role === 'ROLE_ADMIN'">
    <template slot="title" >
      <i class="el-icon-location" ></i><span>用户管理</span>
    </template>
    <el-menu-item-group>
      <el-menu-item index="/admin">用户信息管理</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
  <el-submenu index="3">
    <template slot="title">
      <i class="el-icon-location"></i><span>信息管理</span>
    </template>
    <el-menu-item-group>
      <el-menu-item index="/student">学生信息</el-menu-item>
      <el-menu-item index="/company">公司信息</el-menu-item>
      <el-menu-item index="/recruit">招聘信息</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
</el-menu>
    </el-aside>
    <!-- 主体内容 -->
    <el-main>

      <router-view></router-view>
    
    </el-main>
  </el-container>
</el-container>
  </div>
</template>

<style>
.el-menu{
  border-right: none !important;
}
</style>

<script>
  export default {
    name: 'ManageView',
    data() {
      return {
        user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) :{}, // 从 localStorage 中获取用户信息
      };
    },
    methods: {
      goToPersonalCenter() {
        // 跳转到个人中心页面
        this.$router.push("/personal"); 
      },
      logout() {
             // 退出登录的逻辑
             localStorage.removeItem("user");
        this.$message({
          message: '账号已退出登录',
          type: 'success'
        });
        this.$router.push("/login"); // 跳转到登录页面
      }
    }
  };
  </script>
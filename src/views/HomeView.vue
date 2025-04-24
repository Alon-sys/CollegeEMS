<template>
  <div>
    <!-- 头部 -->
    <el-header :style="{ backgroundColor: '#687179', height: '60px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 20px' }">
      <div style="display: flex; align-items: center;">
        <img src="@/assets/img/logo.png" alt="" style="width: 50px; height: 50px; object-fit: contain; margin-top: 0;">
        <span style="font-size: 20px; margin-left: 10px; color: aliceblue;">高校就业管理系统</span>
        <el-menu mode="horizontal" background-color="#687179" text-color="#fff" active-text-color="#ffd04b">
       <el-menu-item @click="viewRecruit()" style="transition: color 0.3s;">招聘信息</el-menu-item>
          <el-menu-item v-if="user.role === 'ROLE_ADMIN'" @click="backgroundManagement()" style="transition: color 0.3s;">后台管理</el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <el-dropdown>
          <span class="el-dropdown-link">
            {{ user.username }}
            <i class="el-icon-arrow-down el-icon-s-tools"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <div @click="goToPersonalCenter()">个人中心</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout()">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <!-- 主体 -->
     <el-main style="padding: 0;">
    <div class="main-content" style="padding: 20px;">
      <el-card class="welcome-card" style="max-width: 800px; margin: 0 auto; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); border-radius: 4px;">
        <template #header>
          <div class="card-header" style="font-size: 24px; font-weight: bold; padding: 10px 20px;">欢迎使用高校就业管理系统</div>
        </template>
      </el-card>
      <!-- 就业统计信息 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold;">{{ employmentCount }}</div>
            <div style="font-size: 16px;">已就业学生数</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold;">{{ averageSalary }}</div>
            <div style="font-size: 16px;">平均薪资</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold;">{{ topIndustry }}</div>
            <div style="font-size: 16px;">热门行业</div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 就业公告列表 -->
      <el-card style="margin-top: 20px;">
        <template #header>
          <div style="font-size: 20px; font-weight: bold;">最新就业公告</div>
        </template>
        <el-list :data="announcements" style="padding: 10px;">
          <template #item="{ item }">
            <el-list-item>
              <div style="font-size: 16px;">{{ item.title }}</div>
              <div style="font-size: 14px; color: #999;">{{ item.date }}</div>
            </el-list-item>
          </template>
        </el-list>
      </el-card>
    </div>
  </el-main>
  </div>
</template>
<style>
.el-menu{
  border-bottom: none !important;
}

.center-icon{
  display: flex;
  align-items: center;
  

}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  color: aliceblue;
  font-size: 16px;
  cursor: pointer;
  transition: color 0.3s;
}
</style>

<script>

export default {
  name: 'HomeView',
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    form:{},
   
    };
  },
  methods: {
    goToPersonalCenter() {
      // 跳转到个人中心页面的逻辑
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
    },
    backgroundManagement() {
      // 跳转到后台管理页面的逻辑
      this.$router.push("/manage");
    },
    successUpload(res) {
        this.form.logo = res.data;
        },
  }
};
</script>    
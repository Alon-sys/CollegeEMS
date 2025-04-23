<template>
    <div>
        <!-- 头部 -->
    <el-header :style="{ backgroundColor: '#687179', height: '60px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 20px' }">
      <div style="display: flex; align-items: center;">
        <img src="@/assets/img/logo.png" alt="" style="width: 50px; height: 50px; object-fit: contain; margin-top: 0;">
        <span @click="goHome()" style="font-size: 20px; margin-left: 10px; color: aliceblue;">高校就业管理系统</span>
        <el-menu mode="horizontal" background-color="#687179" text-color="#fff" active-text-color="#ffd04b">
          
          <el-menu-item @click="viewStatistics()" style="transition: color 0.3s;">就业统计</el-menu-item>
          <el-menu-item @click="viewAnnouncements()" style="transition: color 0.3s;">就业公告</el-menu-item>
          <el-menu-item v-if="user.role === 'ROLE_ADMIN'" @click="backgroundManagement()" style="transition: color 0.3s;">后台管理</el-menu-item>
        </el-menu>
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

    <!-- 主体 -->
    <div style="margin-top: 60px; padding: 20px;">
      <!-- 用户信息卡片 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>个人信息</span>
          <el-button style="float: right;" type="primary" size="mini" @click="dialogFormVisible = true">编辑</el-button>
        </div>
        <div>
          <p><strong>用户名：</strong>{{ user.username }}</p>
          <p><strong>邮箱：</strong>{{ user.email }}</p>
          <p><strong>角色：</strong>{{ user.role }}</p>
          <p><strong>电话号码：</strong>{{ user.phoneNumber }}</p>
          <p><strong>真实姓名：</strong>{{ user.realName }}</p>
        </div>
      </el-card>

      <!-- 编辑用户信息对话框 -->
      <el-dialog title="编辑个人信息" :visible.sync="dialogFormVisible">
        <el-form :model="form" :label-width="formLabelWidth">
          <el-form-item label="用户名">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="角色">
            <el-input v-model="form.role"></el-input>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="form.phoneNumber"></el-input>
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="form.realName"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUserInfo">保存</el-button>
        </div>
      </el-dialog>
    </div>
    </div>
    
</template>

<script>
export default {
  name: "PersonalView",
  data() {
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) :{},
      formLabelWidth: '120px',
      dialogFormVisible: false,
      form: {
        username: '',
        email: '',
        role: '',
        phoneNumber: '',
        realName: ''
      },
      params:{
        username:'',
        email:'',
        role:'',
        phoneNumber:'',
        realName:''
      },
      total: 0,
      pageNum: 1,
      pageSize: 10,
    };
  },
  methods: {
    backgroundManagement() {
      this.$router.push('/admin');
    },
    viewStatistics() {
      this.$router.push('/statistics');
    },
    viewAnnouncements() {
      this.$router.push('/announcements');
    },
    goToPersonalCenter() {
      this.$router.push('/personal');
    },
    logout() {
      localStorage.removeItem('user');
      this.$router.push('/login');
    },
    saveUserInfo() {
      // 模拟保存用户信息
      this.user = { ...this.user, ...this.form };
      this.dialogFormVisible = false;
      this.$message.success("个人信息已更新！");
    },
    goHome()
    {
      this.$router.push("/");
    },
  }
};
</script>
<template>
  <div class="home-container" style="width: 100%; min-width: 1200px;">
    <el-header :style="{
      backgroundColor: '#687179',
      height: '60px',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 20px'
    }">
      <div style="display: flex; align-items: center;">
        <img src="@/assets/img/logo.png" alt="" style="width: 50px;
        height: 50px;
         object-fit: contain; 
         margin-top: 0;">
        <span style="font-size: 20px; 
        margin-left: 10px; color: aliceblue;">
          高校就业管理系统
        </span>
        <el-menu mode="horizontal" background-color="#687179" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item @click="backgroundManagement()" style="transition: color 0.3s;">
            后台管理
          </el-menu-item>
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

    <!-- 主体内容 -->
    <el-main>
      <div class="main-content">
        <!-- 欢迎卡片 -->
        <div class="welcome-section">
          <el-card class="welcome-card">
            <h1>欢迎使用高校就业管理系统</h1>
            <p class="welcome-subtitle">为您提供全面的就业数据分析和信息服务</p>
          </el-card>
        </div>

        <!-- 数据统计卡片 -->
        <div class="stats-section">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-card class="stat-card" shadow="hover">
                <div class="stat-value">{{ employmentCount }}</div>
                <div class="stat-label">已就业学生数</div>
                <i class="stat-icon el-icon-user-solid"></i>
              </el-card>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-card class="stat-card" shadow="hover">
                <div class="stat-value">{{ averageSalary }}<span class="unit">元</span></div>
                <div class="stat-label">平均薪资</div>
                <i class="stat-icon el-icon-coin"></i>
              </el-card>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-card class="stat-card" shadow="hover">
                <div class="stat-value">{{ topIndustry }}</div>
                <div class="stat-label">热门行业</div>
                <i class="stat-icon el-icon-trophy"></i>
              </el-card>
            </el-col>
          </el-row>
        </div>



        <!-- 公告和快速入口 -->
        <div class="bottom-section">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="16" :lg="16">
              <el-card class="announcement-card">
                <template #header>
                  <div class="card-header">
                    <span>最新就业公告</span>
                    <el-button type="text" @click="viewAllAnnouncements">查看全部</el-button>
                  </div>
                </template>
                <el-table :data="announcements" style="width: 100%" :show-header="false">
                  <el-table-column prop="title" label="标题">
                    <template #default="scope">
                      <div class="announcement-item">
                        <span class="announcement-title">{{ scope.row.title }}</span>
                        <el-button type="text" @click="viewEditor(scope.row.content)">查看</el-button>
                        <span class="announcement-date">{{ formatDate(scope.row.time) }}</span>
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
            <el-card class="quick-actions-card">
              <template #header>
                <div class="card-header">快捷操作</div>
              </template>
              <div class="quick-actions-grid">
                <el-button type="primary" class="quick-action-btn" @click="uploadResume">
                  <div class="btn-content">
                    <i class="el-icon-upload"></i>
                    <span>上传简历</span>
                  </div>
                </el-button>
                <el-button type="success" class="quick-action-btn" @click="viewJobs">
                  <div class="btn-content">
                    <i class="el-icon-search"></i>
                    <span>查看职位</span>
                  </div>
                </el-button>
                <el-button type="info" class="quick-action-btn" @click="viewCalendar">
                  <div class="btn-content">
                    <i class="el-icon-date"></i>
                    <span>查看日历</span>
                  </div>
                </el-button>
                <el-button type="warning" class="quick-action-btn" @click="consult">
                  <div class="btn-content">
                    <i class="el-icon-chat-line-round"></i>
                    <span>就业咨询</span>
                  </div>
                </el-button>
              </div>
            </el-card>
          </el-row>
        </div>
      </div>
    </el-main>
    <el-dialog title="公告内容" :visible.sync="editorVisible" width="50%">
      <div v-html="this.viewData" class="w-e-text"></div>
    </el-dialog>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  width: 100%;
  min-width: 1200px;
  overflow-x: hidden;
}

.header-right {
  display: flex;
  align-items: center;
  color: white;
}

.el-dropdown-link {
  cursor: pointer;
  font-size: 16px;
  margin-left: 20px;
  color: white !important;
}

.el-dropdown-menu__item {
  color: #606266 !important;
}

.el-dropdown-menu__item:hover {
  color: #409EFF !important;
  background-color: #ecf5ff !important;
}

.el-main {
  width: 100%;
  min-width: 1200px;
  padding: 20px;
  box-sizing: border-box;
}

.main-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
}

/* 欢迎卡片 */
.welcome-section {
  margin-bottom: 24px;
}

.welcome-card {
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: white;
  text-align: center;
  border: none;
  border-radius: 8px;
  padding: 30px 20px;
}

.welcome-card h1 {
  font-size: 28px;
  margin-bottom: 10px;
  font-weight: bold;
}

.welcome-subtitle {
  font-size: 16px;
  opacity: 0.9;
}

/* 数据统计卡片 */
.stats-section {
  margin-bottom: 24px;
}

.stat-card {
  position: relative;
  height: 140px;
  border-radius: 8px;
  margin-bottom: 20px;
  overflow: hidden;
}

.stat-card:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-value .unit {
  font-size: 16px;
  margin-left: 5px;
}

.stat-label {
  font-size: 16px;
  color: #909399;
}

.stat-icon {
  position: absolute;
  right: 20px;
  bottom: 20px;
  font-size: 50px;
  color: rgba(64, 158, 255, 0.1);
}

/* 公告卡片 */
.announcement-card {
  border-radius: 8px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.announcement-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  color: #606266;
  cursor: pointer;
  transition: color 0.3s;
  width: 200px;
}

.announcement-title:hover {
  color: #409EFF;
}

.announcement-date {
  color: #909399;
  font-size: 14px;
}

/* 快捷操作卡片 */
.quick-actions-card {
  border-radius: 8px;
  margin-bottom: 20px;
  padding: 20px;
}

/* 网格布局 - 2x2固定布局 */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 16px;
  width: 100%;
}

/* 按钮统一样式 */
.quick-action-btn {
  height: 100px;
  width: 100%;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s ease;
}

.quick-action-btn:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 按钮内容容器 */
.btn-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
}

/* 图标样式 */
.btn-content i {
  font-size: 28px;
  margin-bottom: 8px;
  color: inherit;
}

/* 文字样式 */
.btn-content span {
  font-size: 16px;
  display: block;
  width: 100%;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .quick-actions-grid {
    grid-template-columns: 1fr;
    grid-template-rows: repeat(4, 1fr);
  }

  .quick-action-btn {
    height: 80px;
    flex-direction: row;
    justify-content: flex-start;
    padding: 0 20px;
  }

  .btn-content {
    flex-direction: row;
  }

  .btn-content i {
    margin-bottom: 0;
    margin-right: 12px;
    font-size: 24px;
  }
}
</style>
<script>
import request from '@/utils/request';
import dayjs from 'dayjs';

import E from 'wangeditor'

let editor
function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new E('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadImgServer = 'http://localhost:8888/api/files/wang/upload'
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

export default {
  name: 'HomeView',
  data() {
    return {
      viewData: '',
      editorVisible: false,
      user: JSON.parse(localStorage.getItem('user')) || {},
      form: {},
      employmentCount: 1286,
      averageSalary: 8560,
      topIndustry: '互联网',
      announcements: []
    };
    
  },
  //页面创建完成时执行
  created() {
    this.findByNotice();

  },
  methods: {
    // 日期格式化方法
    formatDate(data) {
      return dayjs(data).format('YYYY-MM-DD');
    },
    findByNotice() {
      // 查询公告
      request.get('/notice', { params: this.params })
        .then(response => {  // 这里使用response而不是res
          this.announcements = response.data || [];  // 安全访问data和list
          this.total = response.data?.total || 0;
        })
        .catch(error => {
          console.error('获取公告失败:', error);
          this.$message.error('获取公告失败');
        });
    },
    viewEditor(data) {
      // 查看公告
      this.viewData = data;
      this.editorVisible = true;
    },
    goToPersonalCenter() {
      this.$router.push("/personal");
    },
    logout() {
      localStorage.removeItem("user");
      this.$message({
        message: '账号已退出登录',
        type: 'success'
      });
      this.$router.push("/login");
    },
    backgroundManagement() {
      this.$router.push("/manage");
    },
    viewAllAnnouncements() {
      // 查看所有公告
      this.$router.push("/notice");
    },
    uploadResume() {
      // 上传简历
      if (this.user.role !== 'ROLE_COMPANY') {
        this.$router.push("/resume");
      }
      else {
        this.$message({
          message: '企业不能上传简历',
          type: 'warning'
        });
      }

    },
    viewJobs() {
      // 查看职位
      this.$router.push("/recruit");
    },
    viewCalendar() {
      // 查看日历
      this.$router.push("/calendar");
    },
    consult() {
      // 就业咨询
      this.$alert('请拨打:010-12345678', '就业咨询', {
        confirmButtonText: '确定',
        callback: action => {
          this.$message({
            type: 'success',
            message: "祝你就业顺利！"
          });
        }
      });
    }

  }
};
</script>
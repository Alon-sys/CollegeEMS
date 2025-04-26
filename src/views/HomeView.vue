<template>
  <div class="home-container" style="width: 100%; min-width: 1200px;">
    <!-- 头部保持不变 -->
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
          <el-menu-item @click="viewRecruit()" style="transition: color 0.3s;">
            招聘信息
          </el-menu-item>
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
                    <template #default="{ row }">
                      <div class="announcement-item">
                        <span class="announcement-title">{{ row.title }}</span>
                        <span class="announcement-date">{{ row.date }}</span>
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="8" :lg="8">
              <el-card class="quick-actions-card">
                <template #header>
                  <div class="card-header">快捷操作</div>
                </template>
                <div class="quick-actions">
                  <el-button type="primary" icon="el-icon-upload" @click="uploadResume">
                    <span class="btn-content">
                      <i class="el-icon-upload"></i>
                      <span>上传简历</span>
                    </span>
                  </el-button>
                  <el-button type="success" icon="el-icon-search" @click="viewJobs">
                    <span class="btn-content">
                      <i class="el-icon-search"></i>
                      <span>查看职位</span>
                    </span>
                  </el-button>
                  <el-button type="info" icon="el-icon-date" @click="viewCalendar">
                    <span class="btn-content">
                      <i class="el-icon-date"></i>
                      <span>查看日历</span>
                    </span>
                  </el-button>
                  <el-button type="warning" icon="el-icon-chat-line-round" @click="consult">
                    <span class="btn-content">
                      <i class="el-icon-chat-line-round"></i>
                      <span>就业咨询</span>
                    </span>
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-main>
  </div>
</template>

<style scoped>
/* 主容器样式 - 确保全屏高度和最小宽度 */
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  width: 100%;
  min-width: 1200px;
  /* 防止页面过窄导致布局错乱 */
  overflow-x: hidden;
  /* 隐藏横向滚动条 */
}

/* 主内容区域样式 */
.el-main {
  width: 100%;
  min-width: 1200px;
  padding: 20px;
  box-sizing: border-box;
  /* 包含padding在内的宽度计算 */
}

/* 内容容器 - 居中显示并限制最大宽度 */
.main-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  /* 为子元素定位提供基准 */
}

/* ========== 欢迎卡片区域 ========== */
.welcome-section {
  margin-bottom: 24px;
  /* 与下方元素保持间距 */
}

.welcome-card {
  background: linear-gradient(135deg, #409EFF, #67C23A);
  /* 渐变背景 */
  color: white;
  /* 文字颜色 */
  text-align: center;
  /* 文字居中 */
  border: none;
  /* 去除边框 */
  border-radius: 8px;
  /* 圆角 */
  padding: 30px 20px;
  /* 内边距 */
}

.welcome-card h1 {
  font-size: 28px;
  margin-bottom: 10px;
  font-weight: bold;
  /* 加粗标题 */
}

.welcome-subtitle {
  font-size: 16px;
  opacity: 0.9;
  /* 轻微透明效果 */
}

/* ========== 数据统计卡片区域 ========== */
.stats-section {
  margin-bottom: 24px;
  /* 与下方元素保持间距 */
}

.stat-card {
  position: relative;
  /* 为图标定位提供基准 */
  height: 140px;
  /* 固定高度 */
  border-radius: 8px;
  /* 圆角 */
  margin-bottom: 20px;
  /* 卡片间距 */
  overflow: hidden;
  /* 隐藏溢出内容 */
  transition: transform 0.3s;
  /* 悬停动画效果 */
}

.stat-card:hover {
  transform: translateY(-5px);
  /* 悬停时上移效果 */
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  /* 主色调 */
  margin-bottom: 10px;
}

.stat-value .unit {
  font-size: 16px;
  margin-left: 5px;
  /* 单位与数值间距 */
}

.stat-label {
  font-size: 16px;
  color: #909399;
  /* 次级文字颜色 */
}

.stat-icon {
  position: absolute;
  /* 绝对定位 */
  right: 20px;
  /* 右侧距离 */
  bottom: 20px;
  /* 底部距离 */
  font-size: 50px;
  /* 大图标尺寸 */
  color: rgba(64, 158, 255, 0.1);
  /* 半透明效果 */
}

/* ========== 公告卡片区域 ========== */
.announcement-card {
  border-radius: 8px;
  margin-bottom: 20px;
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  /* 两端对齐 */
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

/* 单个公告项样式 */
.announcement-item {
  display: flex;
  justify-content: space-between;
  /* 标题和日期分列两侧 */
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  /* 底部边框 */
}

.announcement-item:last-child {
  border-bottom: none;
  /* 最后一项无边框 */
}

.announcement-title {
  color: #606266;
  cursor: pointer;
  /* 鼠标手型 */
  transition: color 0.3s;
  /* 颜色过渡效果 */
}

.announcement-title:hover {
  color: #409EFF;
  /* 悬停变色 */
}

.announcement-date {
  color: #909399;
  /* 次级文字颜色 */
  font-size: 14px;
  /* 较小字号 */
}

/* ========== 快捷操作卡片区域 ========== */
.quick-actions-card {
  border-radius: 12px;
  /* 更大的圆角更现代 */
  margin-bottom: 20px;
  padding: 20px;
  /* 增加内边距 */
  border: none;
  /* 去除默认边框 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  /* 更柔和的阴影 */
  transition: all 0.3s ease;
  /* 添加整体过渡效果 */
}

.quick-actions-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  /* 悬停时阴影加深 */
  transform: translateY(-2px);
  /* 轻微上浮效果 */
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  /* 修正网格计算方式 */
  gap: 16px;
}

.quick-actions .el-button {
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  padding: 16px 12px;
  /* 增加水平padding */
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
  outline: 1px dashed blue;

  /* 确保所有按钮尺寸一致 */
  min-width: 0;
  width: 100%;
  box-sizing: border-box;
}

/* 按钮悬停效果 */
.quick-actions .el-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 按钮图标样式 */
/* 确保图标容器大小一致 */
.quick-actions .el-button i {
  font-size: 26px;
  margin-bottom: 8px;
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quick-actions .el-button:hover i {
  transform: scale(1.1);
  /* 悬停时图标放大 */
}

/* 文字部分添加宽度限制 */
.quick-actions .el-button span {
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 按钮背景光效 */
.quick-actions .el-button::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(to bottom right,
      rgba(255, 255, 255, 0.3),
      rgba(255, 255, 255, 0));
  transform: rotate(30deg);
  transition: all 0.6s ease;
  opacity: 0;
}

.quick-actions .el-button:hover::after {
  opacity: 1;
  animation: shine 1.5s ease infinite;
  /* 光效动画 */
}

@keyframes shine {
  0% {
    left: -50%;
    top: -50%;
  }

  100% {
    left: 150%;
    top: 150%;
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .quick-actions {
    grid-template-columns: 1fr;
    /* 小屏幕下单列布局 */
  }

  .quick-actions .el-button {
    height: 80px;
    /* 小屏幕下稍矮的按钮 */
    flex-direction: row;
    /* 水平布局图标和文字 */
    justify-content: flex-start;
    padding: 0 20px;
  }

  .quick-actions .el-button i {
    margin-bottom: 0;
    margin-right: 12px;
    font-size: 24px;
  }
}
</style>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      form: {},
      employmentCount: 1286,
      averageSalary: 8560,
      topIndustry: '互联网',
      announcements: [
        { title: '2023届毕业生就业双选会通知', date: '2023-05-15' },
        { title: '关于举办简历制作讲座的通知', date: '2023-05-10' },
        { title: '知名企业校园招聘会安排', date: '2023-05-08' },
        { title: '就业指导中心暑期值班安排', date: '2023-05-05' }
      ]
    };
  },
  methods: {
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
    viewRecruit() {
      // 查看招聘信息
    },
    successUpload(res) {
      this.form.logo = res.data;
    },
    viewAllAnnouncements() {
      // 查看所有公告
    },
    uploadResume() {
      // 上传简历
    },
    viewJobs() {
      // 查看职位
    },
    viewCalendar() {
      // 查看就业日历
    },
    consult() {
      // 就业咨询
    }
  }
};
</script>
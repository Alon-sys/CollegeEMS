<template>
  <div class="home-container">
    <!-- 头部导航栏 -->
    <el-header class="header">
      <div class="header-left">
        <img src="@/assets/img/logo.png" alt="系统logo" class="logo">
        <span class="system-name">高校就业管理系统</span>
        <el-menu
          mode="horizontal"
          background-color="#687179"
          text-color="#fff"
          active-text-color="#ffd04b"
          :default-active="activeMenu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="job">招聘信息</el-menu-item>
          <el-menu-item v-if="user.role === 'ROLE_ADMIN'" index="statistics">就业统计</el-menu-item>
          <el-menu-item index="announcements">就业公告</el-menu-item>
          <el-menu-item v-if="user.role === 'ROLE_ADMIN'" index="admin">后台管理</el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <el-dropdown trigger="hover">
          <span class="user-info">
            <el-avatar :size="36" :src="user.avatar || require('@/assets/img/default-avatar.png')"></el-avatar>
            <span class="username">{{ user.username }}</span>
            <i class="el-icon-arrow-down"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToPersonalCenter">
                <i class="el-icon-user"></i>个人中心
              </el-dropdown-item>
              <el-dropdown-item divided @click="logout">
                <i class="el-icon-switch-button"></i>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主体内容 -->
    <div class="main-content">
      <!-- 欢迎卡片 -->
      <el-card class="welcome-card" shadow="hover">
        <div class="welcome-content">
          <h1>欢迎使用高校就业管理系统</h1>
          <p>这里是学校就业指导中心，为您提供最新的招聘信息、就业政策和职业发展指导</p>
          <el-button type="primary" @click="viewJobInfo">查看最新招聘</el-button>
        </div>
      </el-card>

      <!-- 就业数据统计 -->
      <div class="data-overview">
        <h2 class="section-title">就业数据概览</h2>
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ employmentCount }}</div>
                <div class="data-label">已就业学生数</div>
                <div class="data-trend">
                  <span class="trend-up">↑ 5.2%</span> 较上月
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ averageSalary }}</div>
                <div class="data-label">平均薪资</div>
                <div class="data-trend">
                  <span class="trend-up">↑ 8.1%</span> 较去年
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ employmentRate }}%</div>
                <div class="data-label">就业率</div>
                <div class="data-trend">
                  <span class="trend-up">↑ 3.5%</span> 较去年
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ topIndustry }}</div>
                <div class="data-label">热门行业</div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ topCompany }}</div>
                <div class="data-label">热门企业</div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <el-card shadow="hover" class="data-card">
              <div class="data-item">
                <div class="data-value">{{ jobFairCount }}</div>
                <div class="data-label">招聘会场次</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 最新公告和招聘信息 -->
      <el-row :gutter="20" class="info-section">
        <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>最新就业公告</span>
                <el-button type="text" @click="viewAnnouncements">查看更多</el-button>
              </div>
            </template>
            <el-table :data="announcements" style="width: 100%" :show-header="false">
              <el-table-column prop="title" label="标题">
                <template #default="{ row }">
                  <div class="announcement-item">
                    <span class="title">{{ row.title }}</span>
                    <span class="date">{{ row.date }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column width="80">
                <template #default="{ row }">
                  <el-tag v-if="row.isNew" type="danger" size="small">新</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>近期招聘会</span>
                <el-button type="text" @click="viewJobInfo">查看更多</el-button>
              </div>
            </template>
            <div class="job-fair-list">
              <div v-for="(job, index) in jobFairs" :key="index" class="job-fair-item">
                <div class="job-fair-content">
                  <h4>{{ job.company }}</h4>
                  <p>{{ job.position }}</p>
                  <div class="job-meta">
                    <span><i class="el-icon-time"></i> {{ job.date }}</span>
                    <span><i class="el-icon-location"></i> {{ job.location }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 就业趋势图表 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>近三年就业趋势</span>
          </div>
        </template>
        <div class="chart-container">
          <div ref="trendChart" style="height: 300px;"></div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getEmploymentData, getAnnouncements, getJobFairs } from '@/api/home';

export default {
  name: 'HomeView',
  data() {
    return {
      activeMenu: 'home',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : { username: '未知用户', role: '' },
      employmentCount: 0,
      averageSalary: '0',
      employmentRate: 0,
      topIndustry: '--',
      topCompany: '--',
      jobFairCount: 0,
      announcements: [],
      jobFairs: [],
      trendChart: null
    };
  },
  async created() {
    await this.fetchData();
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeUnmount() {
    if (this.trendChart) {
      this.trendChart.dispose();
    }
  },
  methods: {
    async fetchData() {
      try {
        // 获取就业数据
        const employmentRes = await getEmploymentData();
        this.employmentCount = employmentRes.employmentCount;
        this.averageSalary = employmentRes.averageSalary + '元';
        this.employmentRate = employmentRes.employmentRate;
        this.topIndustry = employmentRes.topIndustry;
        this.topCompany = employmentRes.topCompany;
        this.jobFairCount = employmentRes.jobFairCount;

        // 获取公告数据
        const announcementRes = await getAnnouncements({ page: 1, size: 5 });
        this.announcements = announcementRes.list;

        // 获取招聘会数据
        const jobFairRes = await getJobFairs({ page: 1, size: 3 });
        this.jobFairs = jobFairRes.list;
      } catch (error) {
        console.error('获取数据失败:', error);
        this.$message.error('数据加载失败');
      }
    },
    initChart() {
      this.trendChart = echarts.init(this.$refs.trendChart);
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['就业人数', '就业率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['2022', '2023', '2024']
        },
        yAxis: [
          {
            type: 'value',
            name: '就业人数',
            position: 'left'
          },
          {
            type: 'value',
            name: '就业率(%)',
            min: 0,
            max: 100,
            position: 'right'
          }
        ],
        series: [
          {
            name: '就业人数',
            type: 'line',
            data: [1200, 1500, 1800],
            smooth: true,
            lineStyle: {
              width: 3,
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '就业率',
            type: 'line',
            yAxisIndex: 1,
            data: [85, 88, 92],
            smooth: true,
            lineStyle: {
              width: 3,
              color: '#67C23A'
            },
            itemStyle: {
              color: '#67C23A'
            }
          }
        ]
      };
      this.trendChart.setOption(option);
      window.addEventListener('resize', this.handleResize);
    },
    handleResize() {
      if (this.trendChart) {
        this.trendChart.resize();
      }
    },
    handleMenuSelect(index) {
      this.activeMenu = index;
      switch (index) {
        case 'job':
          this.viewJobInfo();
          break;
        case 'statistics':
          this.viewStatistics();
          break;
        case 'announcements':
          this.viewAnnouncements();
          break;
        case 'admin':
          this.backgroundManagement();
          break;
      }
    },
    goToPersonalCenter() {
      this.$router.push("/personal");
    },
    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      this.$message.success('已成功退出登录');
      this.$router.push("/login");
    },
    backgroundManagement() {
      this.$router.push("/admin");
    },
    viewStatistics() {
      this.$router.push("/statistics");
    },
    viewAnnouncements() {
      this.$router.push("/announcements");
    },
    viewJobInfo() {
      this.$router.push("/job");
    }
  }
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* 头部样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #687179;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.system-name {
  font-size: 18px;
  font-weight: bold;
  margin-right: 30px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  color: white;
  cursor: pointer;
}

.username {
  margin: 0 8px;
}

/* 主体内容样式 */
.main-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.welcome-content {
  padding: 30px;
  text-align: center;
}

.welcome-content h1 {
  font-size: 28px;
  margin-bottom: 15px;
  color: #303133;
}

.welcome-content p {
  font-size: 16px;
  color: #606266;
  margin-bottom: 20px;
}

/* 数据概览样式 */
.data-overview {
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.data-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s;
}

.data-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.data-item {
  padding: 20px;
  text-align: center;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 5px;
}

.data-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.data-trend {
  font-size: 12px;
  color: #909399;
}

.trend-up {
  color: #67C23A;
}

.trend-down {
  color: #F56C6C;
}

/* 信息卡片样式 */
.info-section {
  margin-bottom: 20px;
}

.info-card {
  border-radius: 8px;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 16px;
  font-weight: bold;
}

/* 公告列表样式 */
.announcement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
}

.announcement-item .title {
  font-size: 14px;
  color: #303133;
}

.announcement-item .date {
  font-size: 12px;
  color: #909399;
}

/* 招聘会列表样式 */
.job-fair-list {
  padding: 5px 0;
}

.job-fair-item {
  padding: 12px 0;
  border-bottom: 1px dashed #ebeef5;
}

.job-fair-item:last-child {
  border-bottom: none;
}

.job-fair-content h4 {
  font-size: 14px;
  margin-bottom: 5px;
  color: #409EFF;
}

.job-fair-content p {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.job-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

/* 图表卡片样式 */
.chart-card {
  border-radius: 8px;
  margin-bottom: 20px;
}

.chart-container {
  padding: 10px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header {
    padding: 0 10px;
  }
  
  .system-name {
    display: none;
  }
  
  .welcome-content h1 {
    font-size: 22px;
  }
  
  .data-value {
    font-size: 24px;
  }
}
</style>
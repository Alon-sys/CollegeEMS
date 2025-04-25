<template>
  <div>
    <!-- 头部 -->
    <el-header class="header">
      <div class="header-left">
        <img src="@/assets/img/logo.png" alt="logo" class="logo">
        <span @click="goHome()" class="system-title">高校就业管理系统</span>
        <el-menu mode="horizontal" background-color="#687179" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item v-if="user.role === 'ROLE_ADMIN'" @click="backgroundManagement()">后台管理</el-menu-item>
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
                <div @click="goToPersonalCenter()">
                  个人中心
                </div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout()">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-page-header class="page-goBack" @back="goBack" content="个人中心">
    </el-page-header>

    <!-- 主体 -->
    <!--用户信息卡片-->
    <div class="user-content">
      <!-- 用户信息卡片 -->
      <el-descriptions class="user-info" title="账户信息" :column="3" border>
        <template slot="extra">
          <el-button type="primary" size="small" @click="edit(item)">操作</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          {{ item.username }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            电话号码
          </template>
          {{ item.phoneNumber }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          {{ item.email }}
        </el-descriptions-item> <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-key"></i>
            权限
          </template>
          <el-tag size="small">{{ item.role }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-custom"></i>
            真实姓名
          </template>
          {{ item.realName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-lock"></i>
            密码
          </template>
          <p>******</p>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-time"></i>
            创建时间
          </template>
          {{ item.createdAt }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-edit"></i>
            更新时间
          </template>
          {{ item.updatedAt }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 学生信息卡片 -->

    <div class="card-content" v-if="user.role === 'ROLE_STUDENT'">
      <el-descriptions class="card-info" title="学生信息" :column="4" border direction="vertical">
        <template slot="extra">
          <el-button type="primary" size="small" @click="edit(item)">编辑</el-button>
        </template>
        <el-descriptions-item label="学号">{{ student.studentID }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ student.name }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ student.age }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ student.gender }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ student.college }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ student.major }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ student.classInfo }}</el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-tag v-if="student.isEmployed == 1" type="success">已就业</el-tag>
          <el-tag v-else type="info">未就业</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <!--公司信息卡片-->
    <div class="card-content" v-if="user.role === 'ROLE_COMPANY'">
      <el-descriptions class="card-info" title="企业信息" :column="4" border direction="vertical">
        <template slot="extra">
          <el-button type="primary" size="small" @click="edit(item)">编辑</el-button>
        </template>
        <el-descriptions-item label="公司logo" width="50px">
          <template>
            <el-image style="width: 70px; height: 70px; border-radius: 50%"
              :src="'http://localhost:8888/api/files/' + company.logo"
              :preview-src-list="['http://localhost:8888/api/files/' + company.logo]">
            </el-image>
          </template>
        </el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ company.name }}</el-descriptions-item>
        <el-descriptions-item label="公司官网">{{ company.website }}</el-descriptions-item>
        <el-descriptions-item label="所属行业">
          <el-tag size="small">{{ company.industry }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="公司规模">{{ company.size }}</el-descriptions-item>
        <el-descriptions-item label="公司描述">{{ company.description }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <!-- 编辑用户信息对话框 -->
    <el-dialog title="编辑账户信息" 
    :visible.sync="dialogFormVisible" 
    width="40%" center >
      <el-form  
      :model="form"
       label-position="top" 
      label-width="100px" 
      class="dialog-form">
        <el-form-item label="名称">
          <el-input v-model="form.username" placeholder="请输入名称" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phoneNumber" placeholder="请输入电话号码" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" placeholder="请输入邮箱" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" show-password v-model="form.password" placeholder="请输入密码"
            autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button @click="submit()" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
export default {
  name: "PersonalView",
  data() {
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      formLabelWidth: '120px',
      dialogFormVisible: false,
      params: {

      },
      total: 0,
      form: {},
      item: {},
      student: {},
      company: {},
    };
  },
  created() {
    this.findById();
  },
  methods: {
    findById() {
      request.get('/admin/' + this.user.id, this.form, { params: this.params }).then(res => {
        if (res.code === '200') {
          this.item = res.data;
          this.total = res.data.total;
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      });
      request.get('/student/' + this.user.id, this.form, { params: this.params }).then(res1 => {
        if (res1.code === '200') {
          this.student = res1.data;
        } else {
          this.$message({
            message: res1.msg,
            type: 'error'
          });
        }
      });
      request.get('/company/' + this.user.id, this.form, { params: this.params }).then(res2 => {
        if (res2.code === '200') {
          this.company = res2.data;
        } else {
          this.$message({
            message: res2.msg,
            type: 'error'
          });
        }
      });
    },
    edit(item) {
      this.form = item;
      this.dialogFormVisible = true;
    },
    goBack() {
      this.$router.go(-1);
    },
    backgroundManagement() {
      this.$router.push('/admin');
    },
    logout() {
      localStorage.removeItem('user');
      this.$router.push('/login');
    },
    goHome() {
      this.$router.push("/");
    },
    submit() {
      request.post('/admin', this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            message: ('操作成功'),
            type: 'success'
          });
          this.dialogFormVisible = false;
          this.findById();
        }
        else {
          this.$message({
            message: (res.msg),
            type: 'error'
          });
        }
      })
    }
  }
};
</script>

<style scoped>
.header {
  background-color: #687179;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  width: 50px;
  height: 50px;
  object-fit: contain;
}

.system-title {
  font-size: 20px;
  margin-left: 10px;
  color: aliceblue;
  
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

.user-content {
  margin-top: 20px;
  padding: 20px;
  height: auto;
}

.card-content {
  margin-top: 10px;
  margin-bottom: 40px;
  padding: 20px;
  height: 50%;
}

.user-info {
  margin-top: 20px;
  ;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 80%;
  margin: 0 auto;
}

.card-info {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 80%;
  margin: 0 auto;
  margin-top: 20px;

}

.dialog-footer {
  text-align: right;
  padding: 10px 20px;
}

.page-goBack {
  margin: 20px 20px;
  padding: 10px;

}

.dialog-form {
  padding: 10px 20px;
  margin: 0 auto;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 100%;
}
</style>
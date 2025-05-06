<template>
  <div>
    <!-- 头部导航栏 -->
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

    <!-- 页面标题和返回按钮 -->
    <el-page-header class="page-goBack" @back="goBack" content="个人中心"></el-page-header>

    <!-- 用户信息卡片 -->
    <div class="user-content">
      <el-descriptions class="user-info" title="账户信息" :column="3" border>
        <template slot="extra">
          <el-button type="primary" size="small" @click="editUserInfo">编辑</el-button>
          <el-button type="primary" size="small" @click="editPassword">修改密码</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-user"></i>用户名</template>
          {{ userInfo.username }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-mobile-phone"></i>电话号码</template>
          {{ userInfo.phoneNumber }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-message"></i>邮箱</template>
          {{ userInfo.email }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-key"></i>权限</template>
          <el-tag size="small">{{ userInfo.role }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-s-custom"></i>真实姓名</template>
          {{ userInfo.realName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-lock"></i>密码</template>
          <p>******</p>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-time"></i>创建时间</template>
          {{ userInfo.createdAt }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label"><i class="el-icon-edit"></i>更新时间</template>
          {{ userInfo.updatedAt }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 学生信息卡片 (仅对学生角色显示) -->
    <div class="card-content" v-if="user.role === 'ROLE_STUDENT' && studentInfo">
      <el-descriptions class="card-info" title="学生信息" :column="4" border direction="vertical">
        <template slot="extra">
          <el-button type="primary" size="small" @click="editStudentInfo">编辑</el-button>
        </template>
        <el-descriptions-item label="学号">{{ studentInfo.studentID }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ studentInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ studentInfo.age }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ studentInfo.gender }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ studentInfo.college }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ studentInfo.major }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ studentInfo.classInfo }}</el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-tag v-if="studentInfo.isEmployed == 1" type="success">已就业</el-tag>
          <el-tag v-else type="info">未就业</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 公司信息卡片 (仅对企业角色显示) -->
    <div class="card-content" v-if="user.role === 'ROLE_COMPANY' && companyInfo">
      <el-descriptions class="card-info" title="企业信息" :column="4" border direction="vertical">
        <template slot="extra">
          <el-button type="primary" size="small" @click="editCompanyInfo">编辑</el-button>
        </template>
        <el-descriptions-item label="公司logo" width="50px">
          <el-image style="width: 70px; height: 70px; border-radius: 50%"
            :src="companyInfo.logo ? 'http://localhost:8888/api/files/' + companyInfo.logo : ''"
            :preview-src-list="companyInfo.logo ? ['http://localhost:8888/api/files/' + companyInfo.logo] : []">
          </el-image>
        </el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ companyInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="公司官网">{{ companyInfo.website }}</el-descriptions-item>
        <el-descriptions-item label="所属行业"> {{ companyInfo.industry }}</el-descriptions-item>
        <el-descriptions-item label="公司规模">{{ companyInfo.size }}</el-descriptions-item>
        <el-descriptions-item label="公司描述">{{ companyInfo.description }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div>
      <!-- 编辑用户信息对话框 -->
      <el-dialog :title="editDialogTitle" :visible.sync="dialogFormVisible" width="40%" center>
        <!-- 管理员编辑表单 -->
        <template v-if="editType === 'user'">
          <el-form :model="editForm" :rules="userRules" ref="userForm" label-position="top" label-width="100px"
            class="dialog-form">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="editForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phoneNumber">
              <el-input v-model="editForm.phoneNumber" placeholder="请输入电话号码"></el-input>
            </el-form-item>
            <el-form-item label="Email" prop="email">
              <el-input v-model="editForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" show-password v-model="editForm.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="editForm.realName" placeholder="请输入真实姓名"></el-input>
            </el-form-item>
          </el-form>
        </template>

        <!-- 学生编辑表单 -->
        <template v-if="editType === 'student'">
          <el-form :model="editForm" :rules="studentRules" ref="studentForm" label-position="top" label-width="100px">
            <el-form-item label="学号" prop="studentID">
              <el-input v-model="editForm.studentID" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="学生姓名" prop="name">
              <el-input v-model="editForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model="editForm.age" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="editForm.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="学院" prop="college">
              <el-input v-model="editForm.college" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="专业" prop="major">
              <el-input v-model="editForm.major" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="班级" prop="classInfo">
              <el-input v-model="editForm.classInfo" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="就业情况" prop="isEmployed">
              <el-radio-group v-model="editForm.isEmployed">
                <el-radio :label="1">已就业</el-radio>
                <el-radio :label="0">未就业</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </template>

        <!-- 公司编辑表单 -->
        <template v-if="editType === 'company'">
          <el-form :model="editForm" :rules="companyRules" ref="companyForm" label-width="100px">
            <el-form-item label="公司logo" prop="logo">
              <el-upload action="http://localhost:8888/api/files/upload" :on-success="handleUploadSuccess"
                :before-upload="beforeUpload" :show-file-list="false">
                <el-image v-if="editForm.logo" :src="getImageUrl(editForm.logo)"
                  style="width: 100px; height: 100px; border-radius: 4px; margin-right: 10px;" fit="cover">
                </el-image>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <div slot="tip" class="el-upload__tip">建议上传1:1比例的图片，大小不超过2MB</div>
              </el-upload>
            </el-form-item>
            <el-form-item label="公司名称" prop="name">
              <el-input v-model="editForm.name" placeholder="请输入公司名称"></el-input>
            </el-form-item>
            <el-form-item label="公司官网" prop="website">
              <el-input v-model="editForm.website" placeholder="请输入公司官网地址"></el-input>
            </el-form-item>
            <el-form-item label="公司简介" prop="description">
              <el-input type="textarea" v-model="editForm.description" :rows="3" placeholder="请输入公司简介"></el-input>
            </el-form-item>
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="editForm.industry" placeholder="请输入所属行业"></el-input>
            </el-form-item>
            <el-form-item label="公司规模" prop="size">
              <el-input v-model="editForm.size" placeholder="请输入公司规模"></el-input>
            </el-form-item>
          </el-form>
        </template>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--修改密码-->
    <el-dialog title="修改密码" :visible.sync="passwordFormVisible" width="30%" center>
      <el-form :model="editForm" :rules="passwordRules" ref="passwordForm" label-width="100px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input type="password" v-model="editForm.oldPassword" placeholder="请输入旧密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="editForm.newPassword" placeholder="请输入新密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="editForm.confirmPassword" placeholder="请再次输入新密码" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="passwordFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitPasswordForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: "PersonalView",
  data() {
    // 表单验证规则
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入电话号码'));
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'));
      } else {
        callback();
      }
    };

    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入邮箱'));
      } else if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value)) {
        callback(new Error('请输入正确的邮箱格式'));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'));
      } else if (value.length < 6) {
        callback(new Error('密码长度不能少于6位'));
      } else {
        callback();
      }
    };

    const validateConfirmPassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.editForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      editType: '', // 添加这个属性
      userRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        phoneNumber: [{ validator: validatePhone, trigger: 'blur' }],
        email: [{ validator: validateEmail, trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
      },
      studentRules: {
        studentID: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
        major: [{ required: true, message: '请输入专业', trigger: 'blur' }]
      },
      companyRules: {
        name: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
        website: [{ required: true, message: '请输入公司官网', trigger: 'blur' }],
        industry: [{ required: true, message: '请输入所属行业', trigger: 'blur' }]
      },
      passwordRules: {
        oldPassword: [{ required: true, validator: validatePassword, trigger: 'blur' }],
        newPassword: [{ required: true, validator: validatePassword, trigger: 'blur' }],
        confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }]
      },
      // 用户信息
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      userInfo: {},         // 用户基本信息
      studentInfo: null,    // 学生信息(仅学生角色)
      companyInfo: null,    // 公司信息(仅企业角色)

      // 编辑相关数据
      dialogFormVisible: false,
      passwordFormVisible: false,
      editDialogTitle: '编辑账户信息',
      editForm: {},         // 编辑表单数据
    };

  },
  created() {
    this.fetchUserData();
  },
  methods: {
    /**
     * 获取用户数据
     */
    async fetchUserData() {
      try {
        // 获取用户基本信息
        const userRes = await request.get('/admin/' + this.user.id);
        if (userRes.code === '200') {
          this.userInfo = userRes.data;
        } else {
          this.$message.error(userRes.msg);
        }

        // 根据角色获取附加信息
        if (this.user.role === 'ROLE_STUDENT') {
          const studentRes = await request.get('/student/' + this.user.id);
          if (studentRes.code === '200') {
            this.studentInfo = studentRes.data;
          }
        } else if (this.user.role === 'ROLE_COMPANY') {
          const companyRes = await request.get('/company/' + this.user.id);
          if (companyRes.code === '200') {
            this.companyInfo = companyRes.data;
          }
        }
      } catch (error) {
        this.$message.error('数据加载失败');
        console.error('获取用户数据失败:', error);
      }
    },
    // 获取图片完整URL
    getImageUrl(logo) {
      return logo ? `http://localhost:8888/api/files/${logo}` : '';
    },

    // 文件上传前校验
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },

    // 文件上传成功处理
    handleUploadSuccess(res) {
      if (res.code === '200') {
        this.$set(this.editForm, 'logo', res.data);
        this.$message.success('上传成功');
      } else {
        this.$message.error(res.msg);
      }
    },
    /**
     * 编辑用户信息
     */
    editUserInfo() {
      this.editType = 'user';
      this.editDialogTitle = '编辑账户信息';
      this.editForm = { ...this.userInfo };
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.userForm && this.$refs.userForm.clearValidate();
      });
    },
    /**
     * 修改密码
     */
    editPassword() {
      this.editType = 'password';
      this.editDialogTitle = '修改密码';
      this.editForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      };
      this.passwordFormVisible = true;
      this.$nextTick(() => {
        this.$refs.userForm && this.$refs.userForm.clearValidate();
      });
    },

    /**
     * 编辑学生信息
     */
    editStudentInfo() {
      this.editType = 'student';
      this.editDialogTitle = '编辑学生信息';
      this.editForm = {
        ...this.studentInfo,
        userId: this.user.id // 确保关联用户ID
      };
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.studentForm && this.$refs.studentForm.clearValidate();
      });
    },

    /**
     * 编辑公司信息
     */
    editCompanyInfo() {
      this.editType = 'company';
      this.editDialogTitle = '编辑企业信息';
      this.editForm = {
        ...this.companyInfo,
        userId: this.user.id // 确保关联用户ID
      };
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.companyForm && this.$refs.companyForm.clearValidate();
      });
    },

    /**
     * 提交表单
     */
    submitForm() {
      let formName = this.editType + 'Form';
      this.$refs[formName].clearValidate(); // 清除之前的验证错误
      if (this.user.role === 'ROLE_ADMIN' || this.user.role === 'ROLE_COMPANY' || this.user.role === 'ROLE_STUDENT') {
        formName = 'userForm';
      } else if (this.user.role === 'ROLE_STUDENT') {
        formName = 'studentForm';
      } else if (this.user.role === 'ROLE_COMPANY') {
        formName = 'companyForm';
      }

      this.$refs[formName].validate(async (valid) => {
        if (!valid) {
          this.$message.error('请填写完整信息');
          return;
        }

        try {
          let apiUrl = '';
          let successMsg = '';
          let method = 'post'; // 默认用post

          // 根据编辑类型确定API地址和方法
          if (this.editType === 'user') {
            apiUrl = '/admin';
            successMsg = '用户信息更新成功';
          } else if (this.editType === 'student') {
            apiUrl = '/student';
            successMsg = '学生信息更新成功';
            // 确保isEmployed是数字类型
            this.editForm.isEmployed = Number(this.editForm.isEmployed);
          } else if (this.editType === 'company') {
            apiUrl = '/company';
            successMsg = '企业信息更新成功';
          }

          const res = await request.post(apiUrl, this.editForm);
          if (res.code === '200') {
            this.$message.success(successMsg);
            this.dialogFormVisible = false;
            await this.fetchUserData(); // 刷新数据
          } else {
            this.$message.error(res.msg);
          }
        } catch (error) {
          this.$message.error('提交失败');
          console.error('提交表单失败:', error);
        }
      });
    },
    async submitPasswordForm() {
      this.$refs.passwordForm.validate(async (valid) => {
        if (!valid) {
          this.$message.error('请填写完整信息');
          return;
        }

        try {
          const loading = this.$loading({
            lock: true,
            text: '正在修改密码...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          const res = await request.post('/admin/changePassword', {
            oldPassword: this.editForm.oldPassword,
            newPassword: this.editForm.newPassword,
            confirmPassword: this.editForm.confirmPassword
          });

          loading.close();

          if (res.code === '200') {
            this.$message.success({
              message: '密码修改成功，请重新登录',
              duration: 2000,
              onClose: () => {
                this.passwordFormVisible = false;
                this.logout();
              }
            });
          } else {
            this.$message.error(res.msg || '密码修改失败');
          }
        } catch (error) {
          this.$message.error('密码修改失败: ' + (error.response?.data?.msg || error.message));
          console.error('修改密码失败:', error);
        }
      });
    },

    // 导航相关方法
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
    goToPersonalCenter() {
      // 已经在个人中心，可以不做操作或刷新数据
      this.fetchUserData();
    }
  }
};
</script>

<style scoped>
/* 头部样式 */
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
  cursor: pointer;
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

/* 内容区域样式 */
.user-content,
.card-content {
  margin: 20px auto;
  padding: 20px;
  max-width: 1200px;
}

.user-info,
.card-info {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

/* 对话框样式 */
.dialog-footer {
  text-align: right;
  padding: 10px 20px;
}

.page-goBack {
  margin: 20px;
  padding: 10px;
}

.dialog-form {
  padding: 10px 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 100%;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
}

.avatar-uploader-icon:hover {
  border-color: #409EFF;
}
</style>
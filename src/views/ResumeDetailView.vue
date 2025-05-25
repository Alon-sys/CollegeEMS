<template>
  <div class="resume-detail-container">
    <el-skeleton :loading="loading" animated :count="4">
      <div class="resume-header">
        <el-page-header @back="goBack" content="简历详情"></el-page-header>
      </div>

      <div class="resume-content">
        <!-- 基本信息 -->
        <div class="resume-section">
          <div class="section-header">
            <h2>基本信息</h2>
            <div v-if="this.user.role !== 'ROLE_COMPANY'" class="action-buttons">
              <el-button type="primary" @click="editResume">编辑</el-button>
              <el-popconfirm title="确定删除这份简历吗？" @confirm="deleteResume">
                <el-button slot="reference" type="danger" style="margin-left: 20px;">删除</el-button>
              </el-popconfirm>
            </div>
          </div>
          <div class="basic-info">
            <div class="avatar-container">
              <el-image style="width: 120px; height: 120px; border-radius: 50%" :src="avatarUrl" fit="cover"
                :preview-src-list="[avatarUrl]">
                <div slot="error" class="image-slot">
                  <i class="el-icon-user"></i>
                </div>
              </el-image>
            </div>
            <div class="info-details">
              <div class="info-row">
                <span class="info-label">姓名：</span>
                <span class="info-value">{{ resume.name || '--' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">性别：</span>
                <span class="info-value">{{ resume.gender || '--' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">出生日期：</span>
                <span class="info-value">{{ formatDate(resume.birthday) }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">学校：</span>
                <span class="info-value">{{ resume.school || '--' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">学历：</span>
                <span class="info-value">{{ resume.education || '--' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">专业：</span>
                <span class="info-value">{{ resume.major || '--' }}</span>
              </div>
              <div class="info-row" v-if="resume.resumeUrl">
                <span class="info-label">简历附件：</span>
                <span class="info-value">
                  <el-button type="text" @click="handleDownload(resume.resumeUrl)" icon="el-icon-download">
                    下载
                  </el-button>
                </span>
              </div>
            </div>
          </div>
        </div>
        <!--简历描述-->
        <div class="resume-section"> 
          <div class="section-header">
            <h2>简历描述</h2>
          </div>
          <div class="section-content">
            <pre>{{ resume.content || '暂无信息' }}</pre>
          </div>
        </div>

        <!-- 专业技能 -->
        <div class="resume-section">
          <div class="section-header">
            <h2>专业技能</h2>
          </div>
          <div class="section-content">
            <pre>{{ resume.skills || '暂无信息' }}</pre>
          </div>
        </div>

        <!-- 工作经历 -->
        <div class="resume-section">
          <div class="section-header">
            <h2>工作经历</h2>
          </div>
          <div class="section-content">
            <pre>{{ resume.experience || '暂无信息' }}</pre>
          </div>
        </div>

        <!-- 项目经历 -->
        <div class="resume-section">
          <div class="section-header">
            <h2>项目经历</h2>
          </div>
          <div class="section-content">
            <pre>{{ resume.projects || '暂无信息' }}</pre>
          </div>
        </div> 
      </div>
    </el-skeleton>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑简历" :visible.sync="dialogFormVisible" width="50%" center>
      <el-form :model="form" :rules="rules" label-width="100px" ref="formRef">
        <el-form-item label="简历照片" prop="resumePhoto">
          <el-upload action="http://localhost:8888/api/files/upload" :show-file-list="false"
            :before-upload="beforeAvatarUpload" :on-success="res => handleUploadSuccess(res, 'resumePhoto')">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">建议上传1:1比例，大小不超过2MB的图片</div>
          </el-upload>
          <el-image v-if="form.resumePhoto" style="width: 100px; height: 100px; margin-top: 10px; border-radius: 4px"
            :src="'http://localhost:8888/api/files/' + form.resumePhoto" fit="cover">
          </el-image>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" maxlength="20" show-word-limit></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker v-model="form.birthday" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="请选择学历">
                <el-option label="高中" value="高中"></el-option>
                <el-option label="大专" value="大专"></el-option>
                <el-option label="本科" value="本科"></el-option>
                <el-option label="硕士" value="硕士"></el-option>
                <el-option label="博士" value="博士"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学校" prop="school">
              <el-input v-model="form.school" placeholder="请输入学校" maxlength="50" show-word-limit></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="form.major" placeholder="请输入专业" maxlength="30" show-word-limit></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="简历描述" prop="content">
          <el-input type="textarea" :rows="4" v-model="form.content" placeholder="请描述您的简历内容" maxlength="1000"
            show-word-limit>
          </el-input>
        </el-form-item>

        <el-form-item label="专业技能" prop="skills">
          <el-input type="textarea" :rows="3" v-model="form.skills" placeholder="请描述您的专业技能，如编程语言、工具等" maxlength="500"
            show-word-limit>
          </el-input>
        </el-form-item>

        <el-form-item label="工作经历" prop="experience">
          <el-input type="textarea" :rows="4" v-model="form.experience" placeholder="请按时间顺序描述您的工作经历" maxlength="1000"
            show-word-limit>
          </el-input>
        </el-form-item>

        <el-form-item label="项目经历" prop="projects">
          <el-input type="textarea" :rows="4" v-model="form.projects" placeholder="请描述您参与的项目及您的角色和贡献" maxlength="1000"
            show-word-limit>
          </el-input>
        </el-form-item>

        <el-form-item label="简历附件" prop="resumeUrl">
          <el-upload action="http://localhost:8888/api/files/upload"
            :on-success="res => handleUploadSuccess(res, 'resumeUrl')" :before-upload="beforeFileUpload" :limit="1"
            :auto-upload="true" accept=".pdf, .doc, .docx, .txt">
            <el-button type="primary">
              <el-icon>
                <upload />
              </el-icon>
              上传简历
            </el-button>
            <template #tip>
              <div class="el-upload__tip">支持格式：PDF、Word、TXT，大小不超过 10MB</div>
            </template>
          </el-upload>
          <div v-if="form.resumeUrl" class="file-preview">
            <span>{{ form.resumeUrl.split('/').pop() }}</span>
            <el-button type="text" @click="handleDownload(form.resumeUrl)" icon="el-icon-download">下载</el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';

export default {
  name: "ResumeDetail",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')) || {},
      loading: true,
      resume: {},
      dialogFormVisible: false,
      form: {},
      rules: {
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        birthday: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
        school: [{ required: true, message: '请输入学校', trigger: 'blur' }],
        education: [{ required: true, message: '请选择学历', trigger: 'change' }],
        resumePhoto: [{ required: true, message: '请上传证件照', trigger: 'change' }],
        skills: [{ max: 500, message: '长度不能超过500个字符', trigger: 'blur' }],
        experience: [{ max: 1000, message: '长度不能超过1000个字符', trigger: 'blur' }],
        projects: [{ max: 1000, message: '长度不能超过1000个字符', trigger: 'blur' }],
        content: [{ max: 500, message: '长度不能超过500个字符', trigger: 'blur' }]
      }
    };
  },
  computed: {
    avatarUrl() {
      return this.resume.resumePhoto
        ? `http://localhost:8888/api/files/${this.resume.resumePhoto}`
        : require('@/assets/img/default-avatar.png');
    }
  },
  created() {
    this.fetchResumeDetail();
  },
  methods: {
    // ==================== 数据获取 ====================
    async fetchResumeDetail() {
      try {
        this.loading = true;
        const id = this.$route.query.id;
        const resumeId = this.$route.query.resumeId;

        if (!id && !resumeId) {
          this.$message.error('无效的查询 ID');
          return this.$router.push('/resume');
        }

        let requestId;
        if (id) {
          requestId = id;
        } else {
          requestId = resumeId;
        }

        const res = await request.get(`/resume/${requestId}`);
        if (res.code === '200') {
          this.resume = res.data;
        } else if (res.code === '404') {
          this.$message.error('简历不存在');
          this.$router.push('/resume');
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      } catch (error) {
        console.error('获取简历详情失败:', error);
        this.$message.error('请求失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    // ==================== 页面操作 ====================
    goBack() {
      this.$router.go(-1);
    },

    editResume() {
      this.form = JSON.parse(JSON.stringify(this.resume));
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate();
      });
    },

    async deleteResume() {
      try {
        await request.delete(`/resume/${this.resume.id}`);
        this.$message.success('删除成功');
        this.$router.push('/resume');
      } catch (error) {
        this.$message.error(error.msg || '删除失败');
      }
    },

    // ==================== 表单操作 ====================
    submitForm() {
      this.$refs.formRef.validate(async valid => {
        if (!valid) return;

        try {
          const res = await request.post('/resume', this.form);
          if (res.code === '200') {
            this.$message.success('更新成功');
            this.dialogFormVisible = false;
            await this.fetchResumeDetail();
          } else {
            this.$message.error(res.msg);
          }
        } catch (error) {
          console.error('提交失败:', error);
          this.$message.error('提交失败，请稍后重试');
        }
      });
    },

    // ==================== 文件上传 ====================
    beforeAvatarUpload(file) {
      const isImage = ['image/jpeg', 'image/png', 'image/jpg'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('只能上传JPG/PNG格式图片!');
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过2MB!');
      }

      return isImage && isLt2M;
    },

    beforeFileUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$message.error('文件大小不能超过 10MB');
        return false;
      }

      const allowedTypes = ['pdf', 'doc', 'docx', 'txt'];
      const fileType = file.name.split('.').pop().toLowerCase();
      if (!allowedTypes.includes(fileType)) {
        this.$message.error('仅支持 PDF、Word、TXT 格式');
        return false;
      }

      return true;
    },

    handleUploadSuccess(res, field) {
      if (res.code === '200') {
        this.$set(this.form, field, res.data);
        this.$message.success('上传成功');
        this.$nextTick(() => {
          this.$refs.formRef.validateField(field);
        });
      } else {
        this.$message.error(res.msg || '上传失败');
      }
    },

    // ==================== 文件操作 ====================
    handleDownload(url) {
      const link = document.createElement('a');
      link.href = `http://localhost:8888/api/files/${url}`;
      link.download = url.split('/').pop();
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    // ==================== 工具方法 ====================
    formatDate(date) {
      return date ? dayjs(date).format('YYYY-MM-DD') : '--';
    }
  }
};
</script>

<style scoped>
.resume-detail-container {
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.resume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.resume-section {
  margin-bottom: 30px;
  margin-top: 20px;
  background: #fff;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;
}

.resume-section:hover {
  transform: translateY(-2px);
}

.section-header {
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 15px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-header h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.section-header h2::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 18px;
  background: #409EFF;
  margin-right: 12px;
  border-radius: 2px;
}

.basic-info {
  display: flex;
  align-items: flex-start;
  gap: 40px;
}

.avatar-container {
  flex-shrink: 0;
  border: 2px solid #f0f2f5;
  border-radius: 50%;
  padding: 4px;
}

.info-details {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px 30px;
}

.info-row {
  display: flex;
  min-height: 32px;
  align-items: center;
}

.info-label {
  font-weight: 500;
  color: #606266;
  min-width: 70px;
}

.info-value {
  color: #303133;
  word-break: break-word;
}

.section-content pre {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  font-family: inherit;
  margin: 0;
  font-size: 14px;
}

.file-preview {
  margin-top: 10px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.file-preview span {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 10px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  border-radius: 50%;
  color: #909399;
}

.el-image {
  transition: transform 0.3s;
}

.el-image:hover {
  transform: scale(1.05);
}

.el-form-item {
  margin-bottom: 22px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .resume-header {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }

  .basic-info {
    flex-direction: column;
    gap: 20px;
  }

  .info-details {
    grid-template-columns: 1fr;
  }

  .el-dialog {
    width: 90% !important;
  }
}

@media (max-width: 576px) {
  .resume-detail-container {
    padding: 15px;
  }

  .resume-section {
    padding: 15px;
  }

  .el-row {
    flex-direction: column;
    margin: 0 !important;
  }

  .el-col {
    width: 100%;
    padding: 0 !important;
  }
}
</style>
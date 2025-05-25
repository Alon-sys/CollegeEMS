<template>
  <div>
    <!-- 搜索区域 -->
    <div style="margin-bottom: 15px;margin-left: 15px;">
      <el-button type="primary" @click="add()">新增</el-button>
      <el-popconfirm title="确定删除这些简历吗？" @confirm="delBatch()">
        <el-button slot="reference" type="danger" style="margin-left: 5px">批量删除</el-button>
      </el-popconfirm>
    </div>

    <!-- 表格区域 -->
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px; 
      height:540px; 
      overflow-y: auto;" ref="table" @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column type="selection" width="55" align="center" :reserve-selection="true">
        </el-table-column>
        <el-table-column label="简历照片" width="100">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 50%"
              :src="'http://localhost:8888/api/files/' + scope.row.resumePhoto"
              :preview-src-list="['http://localhost:8888/api/files/' + scope.row.resumePhoto]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="birthday" label="出生日期">
          <template slot-scope="scope">
            {{ formatDate(scope.row.birthday) }}
          </template>
        </el-table-column>
        <el-table-column prop="school" label="学校"></el-table-column>
        <el-table-column prop="education" label="学历"></el-table-column>
        <el-table-column prop="major" label="专业" width="200"></el-table-column>
        <el-table-column prop="resumeUrl" label="简历附件" width="180">
          <template slot-scope="scope">
            <template v-if="scope.row.resumeUrl">
              <el-tooltip content="下载简历" placement="top">
                <el-button type="text" size="small" @click="handleDownload(scope.row.resumeUrl)"
                  icon="el-icon-download">
                  下载
                </el-button>
              </el-tooltip>
            </template>
            <span v-else>未上传</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="primary" @click="details(scope.row)">详情</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="deleteItem(scope.row.id)">
              <template slot="reference">
                <el-button type="danger" style="margin-left: 5px;">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="block">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="params.pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="params.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增弹窗 -->
    <el-dialog title="新增简历" :visible.sync="dialogFormVisible" width="50%" center>
      <el-form :model="form" label-width="100px" :rules="formRules" ref="formRef">
        <el-form-item label="简历照片" prop="resumePhoto">
          <el-upload class="avatar-uploader" action="http://localhost:8888/api/files/upload" :show-file-list="false"
            :on-success="res => handleUploadResponse(res, 'resumePhoto')" :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" maxlength="20" show-word-limit></el-input>
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
                style="width: 100%"></el-date-picker>
            </el-form-item>
            <el-form-item label="学校" prop="school">
              <el-input v-model="form.school" maxlength="50" show-word-limit></el-input>
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
        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" maxlength="30" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="简历附件" prop="resumeUrl">
          <el-upload action="http://localhost:8888/api/files/upload"
            :on-success="res => handleUploadResponse(res, 'resumeUrl')" :before-upload="beforeUpload" :limit="1"
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
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm()">提交</el-button>
          <el-button @click="dialogFormVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';

export default {
  name: "ResumeManagement",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')) || {},
      imageUrl: '',
      params: {
        name: '',
        education: '',
        pageNum: 1,
        pageSize: 5
      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      formRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        birthday: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
        school: [{ required: true, message: '请输入学校', trigger: 'blur' }],
        education: [{ required: true, message: '请选择学历', trigger: 'change' }],
        resumePhoto: [{ required: true, message: '请上传简历照片', trigger: 'change' }]
      },
      multipleSelection: [],
      isEditMode: false,
      submitting: false,

    }
  },
  created() {
    this.fetchResumeList();
  },
  methods: {
    // ========== 搜索相关方法 ==========
    async fetchResumeList() {
      this.loading = true;
      try {
        let res;
        if (this.user.role === 'ROLE_ADMIN') {
          res = await request.get('/resume/search', { params: this.params });
        } else if (this.user.role === 'ROLE_STUDENT') {
          res = await request.get('/resume/user/' + this.user.id, {
            params: this.params
          });
        }

        if (res.code === '200') {
          // 统一处理数据结构
          this.tableData = res.data.list || res.data || [];
          this.total = res.data.total || res.data.length || 0;

          // 调试输出
          console.log('接口返回数据:', res.data);
          console.log('表格数据:', this.tableData);
        }
      } catch (error) {
        console.error('获取数据失败:', error);
        this.$message.error('获取数据失败');
      } finally {
        this.loading = false;
      }
    },

    reset() {
      this.params = {
        name: '',
        education: '',
        pageNum: 1,
        pageSize: 5
      };
      this.fetchResumeList();
    },

    // ========== 分页相关方法 ==========
    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize;
      this.fetchResumeList();
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum;
      this.fetchResumeList();
    },

    // ========== CRUD操作方法 ==========
    add() {
      this.form = {};
      this.imageUrl = '';
      this.isEditMode = false;
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate();
        }
      });
    },

    async submitForm() {
      try {
        this.submitting = true;
        if (this.user.role === 'ROLE_STUDENT') {
          this.form.userId = this.user.id; // 设置用户ID
        }
        const apiUrl = this.isEditMode ? `/resume` : '/resume';
        const res = await request.post(apiUrl, this.form);

        if (res.code === '200') {
          this.$message.success(this.isEditMode ? '更新成功' : '新增成功');
          this.dialogFormVisible = false;
          this.fetchResumeList();
        } else {
          this.$message.error(res.msg || '操作失败');
        }
      } catch (error) {
        console.error('提交表单失败:', error);
        this.$message.error('操作失败，请稍后重试');
      } finally {
        this.submitting = false;
      }
    },
    deleteItem(id) {
      request.delete('/resume/' + id).then(res => {
        if (res.code == '200') {
          this.$message.success('删除成功');
          this.fetchResumeList();
        } else {
          this.$message.error(res.msg);
        }
      }).catch(error => {
        this.$message.error('删除失败');
        console.error(error);
      });
    },

    details(row) {
      this.$router.push({
        path: '/resumeDetail',
        query: { id: row.id }
      });
    },

    delBatch() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要删除的简历');
        return;
      }
      request.put("/resume/delBatch", this.multipleSelection).then(res => {
        if (res.code == '200') {
          this.$message.success('批量删除成功');
          this.fetchResumeList();
        } else {
          this.$message.error(res.msg);
        }
      }).catch(error => {
        this.$message.error('批量删除失败');
        console.error(error);
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    // ========== 上传相关方法 ==========
    beforeUpload(file) {
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

    beforeAvatarUpload(file) {
      const isImage = ['image/jpeg', 'image/png', 'image/jpg'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('上传头像图片只能是 JPG/PNG/JPEG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }

      if (isImage && isLt2M) {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.imageUrl = reader.result;
        };
      }

      return isImage && isLt2M;
    },

    handleUploadResponse(res, field) {
      if (res.code === '200') {
        this.form[field] = res.data;
        this.$message.success('上传成功');
        // 如果是照片上传，触发表单验证
        if (field === 'resumePhoto') {
          this.$refs.formRef.validateField('resumePhoto');
        }
        return true;
      }
      this.$message.error(res.msg || '上传失败');
      return false;
    },

    // ========== 文件预览/下载方法 ==========

    handleDownload(url) {
      const link = document.createElement('a');
      link.href = `http://localhost:8888/api/files/${url}`;
      link.download = url.split('/').pop();
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    // ========== 工具方法 ==========
    formatDate(date) {
      return date ? dayjs(date).format('YYYY-MM-DD') : '';
    },

    formatDateTime(datetime) {
      return datetime ? dayjs(datetime).format('YYYY-MM-DD HH:mm:ss') : '';
    },
    getRowKeys(row) {
      return row.id;
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.el-upload__tip {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}
</style>
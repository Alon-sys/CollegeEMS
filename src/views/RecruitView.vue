<template>
  <div>
    <!-- 搜索栏 -->
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.title" placeholder="职位标题" style="width: 200px; margin-right: 10px" />
      <el-input v-model="params.location" placeholder="职位地点" style="width: 200px; margin-right: 10px" />
      <el-input v-model="params.category" placeholder="职位类别" style="width: 200px; margin-right: 10px" />
      <el-select v-model="params.type" placeholder="职位类型" style="width: 200px; margin-right: 10px">
        <el-option label="全职" value="全职" />
        <el-option label="兼职" value="兼职" />
        <el-option label="实习" value="实习" />
      </el-select>
      <el-button type="warning" @click="findBySearch">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button v-if="user.role !== 'ROLE_STUDENT'" type="primary" @click="add">新增</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" v-loading="loading" style="margin: 15px 0">
      <el-table-column prop="title" label="职位标题" />
      <el-table-column prop="salary" label="薪资" width="120" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createdAt" label="发布时间" width="180" />
      <el-table-column prop="updatedAt" label="更新时间" width="180" />
      <el-table-column prop="location" label="地点" />
      <el-table-column prop="category" label="类别" />
      <el-table-column prop="type" label="类型" />
      <el-table-column label="操作" width="180">
        <template v-slot="{ row }">
          <el-button v-if="user.role !== 'ROLE_STUDENT'" style="margin-right: 5px;" type="primary"
            @click="edit(row)">编辑</el-button>
          <el-popconfirm v-if="user.role !== 'ROLE_STUDENT'" title="确认删除？" @confirm="deleteJob(row.id)">
            <el-button slot="reference" type="danger">删除</el-button>
          </el-popconfirm>
          <el-button style="width: 100px;" v-if="user.role === 'ROLE_STUDENT'" :type="isApplied(row.id) ? 'info' : 'success'"
            @click="applyJob(row.id)" :loading="applying" :disabled="isApplied(row.id)">
            {{ isApplied(row.id) ? '已投递' : '投递' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination :current-page="params.pageNum" :page-size="params.pageSize" :page-sizes="[5, 10, 15, 20]"
      :total="total" layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <!-- 表单弹窗 -->
    <el-dialog :title="form.id ? '编辑职位' : '新增职位'" :visible.sync="dialogFormVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="职位标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="薪资">
          <el-input v-model="form.salary" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="类别">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="全职" value="全职" />
            <el-option label="兼职" value="兼职" />
            <el-option label="实习" value="实习" />
          </el-select>
        </el-form-item>
        <el-form-item>

        </el-form-item>

      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'JobView',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')) || {},
      loading: false,
      params: {
        title: '',
        location: '',
        category: '',
        type: '',
        pageNum: 1,
        pageSize: 5
      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      isEditMode: false,
      submitting: false,
      applying: false,
      appliedJobIds: [], // 存储已投递职位ID

    };
  },
  created() {
    this.fetchJobList();
  },
  methods: {

    // 检查是否已投递
    isApplied(jobId) {
      return this.appliedJobIds.includes(jobId);
    },


    async fetchJobList() {
      this.loading = true;
      try {
        let res;
        if (this.user.role === 'ROLE_ADMIN' || this.user.role === 'ROLE_STUDENT') {
          // 学生和管理员使用统一的搜索接口
          res = await request.get('/jobs/search', { params: this.params });
        } else if (this.user.role === 'ROLE_COMPANY') {
          // 企业接口可能需要单独处理
          res = await request.get('/jobs/' + this.user.id, {
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
    add() {
      this.form = { type: '全职' };
      this.dialogFormVisible = true;
    },
    edit(row) {
      this.form = { ...row };
      this.dialogFormVisible = true;
    },
    async submit() {
      try {
        this.submitting = true;
        if (this.user.role === 'ROLE_COMPANY') {
          this.form.userId = this.user.id; // 设置用户ID
        }
        const apiUrl = this.isEditMode ? `/jobs` : '/jobs';
        const res = await request.post(apiUrl, this.form);

        if (res.code === '200') {
          this.$message.success(this.isEditMode ? '更新成功' : '新增成功');
          this.dialogFormVisible = false;
          this.fetchJobList();
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
    async deleteJob(id) {
      try {
        const res = await request.delete(`/jobs/${id}`);
        if (res.code === '200') {
          this.fetchJobList();
        }
      } catch (error) {
        this.$message.error('删除失败');
      }
    },
    findBySearch() {
      this.params.pageNum = 1;
      this.fetchJobList();
    },
    reset() {
      this.params = {
        title: '',
        location: '',
        category: '',
        type: '',
        pageNum: 1,
        pageSize: 5
      };
      this.fetchJobList();
    },
    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize;
      this.fetchJobList();
    },
    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum;
      this.fetchJobList();
    },
    async applyJob(jobId) {
      if (this.applying || this.isApplied(jobId)) return;
      this.applying = true;

      try {
        // 1. 检查用户是否登录
        if (!this.user?.id) {
          this.$message.warning('请先登录');
          return;
        }

        // 2. 检查用户角色
        if (this.user.role !== 'ROLE_STUDENT') {
          this.$message.warning('只有学生可以投递职位');
          return;
        }

        // 3. 获取用户简历
        let resumes = [];
        try {
          const res = await request.get(`/resume/user/${this.user.id}`);

          // 严格判断响应结构和数据
          if (res?.code !== '200') {
            throw new Error(res?.msg || '请求失败');
          }

          // 处理data为null或空数组的情况
          if (res.data === null || !Array.isArray(res.data)) {
            this.$message.warning('您尚未创建简历');
            this.$router.push('/resume');
            return;
          }

          resumes = res.data;
          if (resumes.length === 0) {
            this.$message.warning('请先完善您的简历');
            this.$router.push('/resume');
            return;
          }
        } catch (error) {
          console.error('获取简历失败:', error);
          this.$message.error(error.message || '获取简历信息失败');
          return;
        }

        // 4. 检查是否已投递过该职位
        try {
          const checkRes = await request.get('/applications/check', {
            params: {
              jobId: jobId,
              userId: this.user.id
            }
          });

          if (checkRes.data === true) {
            this.$message.warning('您已经投递过该职位');
            return;
          }
        } catch (error) {
          console.warn('检查投递状态失败:', error);
          // 如果检查接口出错，仍然允许继续投递
        }

        // 5. 处理简历选择
        let selectedResumeId;
        if (resumes.length > 1) {
          try {
            const { value } = await this.$prompt('请选择要使用的简历', '选择简历', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              inputType: 'select',
              inputOptions: resumes.reduce((options, resume) => {
                options[resume.id] = resume.title || `简历 ${resume.id}`;
                return options;
              }, {})
            });
            selectedResumeId = value;
          } catch {
            this.$message.info('已取消投递');
            return;
          }
        } else {
          selectedResumeId = resumes[0].id;
        }

        // 6. 提交投递
        const success = await this.submitApplication(jobId, selectedResumeId);
        if (success) {
          await this.fetchAppliedJobs(); // 确保状态同步
        }
      } catch (error) {
        console.error('投递流程异常:', error);
      } finally {
        this.applying = false;
      }
    },

    async submitApplication(jobId, resumeId) {
      try {
        const applicationData = {
          userId: this.user.id,
          jobId: jobId,
          resumeId: resumeId,
          status: '已投递'
        };

        const res = await request.post('/applications', applicationData);

        // 明确处理成功响应
        if (res.code === '200') {
          this.$message.success('投递成功');
          this.appliedJobIds.push(jobId); // 立即更新本地投递状态
          return true; // 返回成功状态
        } else {
          this.$message.error(res.msg || '投递失败');
          return false;
        }
      } catch (error) {
        // 精确处理特定错误状态码
        if (error.response?.status === 409) {
          this.$message.warning('您已经投递过该职位');
          await this.fetchAppliedJobs(); // 强制刷新投递状态
        } else {
          this.$message.error(error.response?.data?.message || '提交投递时出错');
        }
        return false;
      }
    },

  }

};
</script>
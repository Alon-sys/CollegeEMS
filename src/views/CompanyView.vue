<template>
  <div class="company-container">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-input v-model="searchParams.name" style="width: 200px; margin-right: 10px" placeholder="请输入公司名称" clearable
        @clear="handleSearchClear('name')"></el-input>
      <el-input v-model="searchParams.industry" style="width: 200px; margin-right: 10px" placeholder="请输入所属行业" clearable
        @clear="handleSearchClear('industry')"></el-input>
      <el-button type="warning" @click="handleSearch" :loading="loading">搜索</el-button>
      <el-button type="warning" @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-area">
      <el-table :data="tableData" v-loading="loading" style="width: 100%; margin: 15px 0px;" height="540px">
        <el-table-column label="公司logo" width="100" align="center">
          <template v-slot="{ row }">
            <el-image style="width: 70px; height: 70px; border-radius: 50%" :src="getImageUrl(row.logo)"
              :preview-src-list="[getImageUrl(row.logo)]" fit="cover">
              <template #error>
                <div class="image-error">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="公司名称" min-width="120"></el-table-column>
        <el-table-column prop="website" label="公司官网" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="公司简介" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="industry" label="所属行业" min-width="100"></el-table-column>
        <el-table-column prop="size" label="公司规模" min-width="100"></el-table-column>

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template v-slot="{ row }">
            <el-button type="primary" size="mini" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除该公司吗？" @confirm="handleDelete(row.id)" confirm-button-text="确定"
              cancel-button-text="取消">
              <template #reference>
                <el-button type="danger" size="mini" style="margin-left: 5px;">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-area">
      <el-pagination @size-change="handlePageSizeChange" @current-change="handlePageChange"
        :current-page="searchParams.pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="searchParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total" :background="true"></el-pagination>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%" center @closed="handleDialogClosed">
      <el-form :model="formData" :rules="formRules" ref="companyForm" label-width="100px">
        <el-form-item label="公司logo" prop="logo">
          <el-upload action="http://localhost:8888/api/files/upload" :on-success="handleUploadSuccess"
            :before-upload="beforeUpload" :show-file-list="false">
            <el-image v-if="formData.logo" :src="getImageUrl(formData.logo)"
              style="width: 100px; height: 100px; border-radius: 4px; margin-right: 10px;" fit="cover"></el-image>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">建议上传1:1比例的图片，大小不超过2MB</div>
          </el-upload>
        </el-form-item>

        <el-form-item label="公司名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入公司名称"></el-input>
        </el-form-item>

        <el-form-item label="公司官网" prop="website">
          <el-input v-model="formData.website" placeholder="请输入公司官网地址"></el-input>
        </el-form-item>

        <el-form-item label="公司简介" prop="description">
          <el-input type="textarea" v-model="formData.description" :rows="3" placeholder="请输入公司简介"></el-input>
        </el-form-item>

        <el-form-item label="所属行业" prop="industry">
          <el-input v-model="formData.industry" placeholder="请输入所属行业"></el-input>
        </el-form-item>

        <el-form-item label="公司规模" prop="size">
          <el-input v-model="formData.size" placeholder="请输入公司规模"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: "CompanyManagement",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      // 搜索参数
      searchParams: {
        name: '',
        industry: '',
        pageNum: 1,
        pageSize: 5
      },

      // 表格数据
      tableData: [],
      companyObject: [],
      // 分页相关
      total: 0,
      loading: false,

      // 对话框相关
      dialogVisible: false,
      dialogTitle: '新增公司',
      isEditMode: false,
      submitting: false,

      // 表单数据
      formData: {
        id: null,
        logo: '',
        name: '',
        userId: '',
        website: '',
        description: '',
        industry: '',
        size: ''
      },

      // 表单验证规则
      formRules: {
        name: [
          { required: true, message: '请输入公司名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在2到50个字符', trigger: 'blur' }
        ],
        logo: [
          { required: true, message: '请上传公司logo', trigger: 'change' }
        ],
        website: [
          { type: 'url', message: '请输入正确的网址', trigger: 'blur' }
        ],
        industry: [
          { required: true, message: '请输入所属行业', trigger: 'blur' }
        ]
      }
    };
  },

  created() {
    this.fetchCompanyList();
  },

  methods: {
    /**
     * 获取完整的图片URL
     * @param {String} path 图片路径
     * @returns {String} 完整URL
     */
    getImageUrl(path) {
      return path ? `http://localhost:8888/api/files/${path}` : '';
    },

    /**
     * 获取公司列表数据
     */
    async fetchCompanyList() {
      this.loading = true;
      try {
        let res;

        // 统一使用字符串比对（或使用常量，但需正确定义）
        if (this.user.role === 'ROLE_ADMIN') { 
          res = await request.get('/company/search', {
            params: this.searchParams
          });
        } else if (this.user.role === 'ROLE_COMPANY') { 
          res = await request.get('/company/'+this.user.id); 
        }

        if (res.code === '200') {
          // 统一使用相同比对方式
          if (this.user.role === 'ROLE_ADMIN') { 
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else if (this.user.role === 'ROLE_COMPANY') { 
            this.tableData = res.data ? [res.data] : []; 
            this.total = 1;
          }
        } else {
          this.$message.error(res.msg || '获取数据失败');
        }
      } catch (error) {
        console.error('获取公司列表失败:', error);
        this.$message.error('获取数据失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    /**
     * 处理搜索操作
     */
    handleSearch() {
      this.searchParams.pageNum = 1; // 搜索时重置到第一页
      this.fetchCompanyList();
    },

    /**
     * 处理重置操作
     */
    handleReset() {
      this.searchParams = {
        name: '',
        industry: '',
        pageNum: 1,
        pageSize: 5
      };
      this.fetchCompanyList();
    },

    /**
     * 清空单个搜索条件
     * @param {String} field 要清空的字段名
     */
    handleSearchClear(field) {
      this.searchParams[field] = '';
      this.handleSearch();
    },

    /**
     * 处理新增操作
     */
    handleAdd() {
      this.dialogTitle = '新增公司';
      this.isEditMode = false;
      this.formData = {
        id: null,
        logo: '',
        name: '',
        userId: '',
        website: '',
        description: '',
        industry: '',
        size: ''
      };
      this.dialogVisible = true;

      // 清除表单验证
      this.$nextTick(() => {
        if (this.$refs.companyForm) {
          this.$refs.companyForm.clearValidate();
        }
      });
    },

    /**
     * 处理编辑操作
     * @param {Object} row 当前行数据
     */
    handleEdit(row) {
      this.dialogTitle = '编辑公司信息';
      this.isEditMode = true;
      this.formData = { ...row };
      this.dialogVisible = true;
    },

    /**
     * 处理删除操作
     * @param {Number} id 公司ID
     */
    async handleDelete(id) {
      try {
        const res = await request.delete(`/company/${id}`);

        if (res.code === '200') {
          this.$message.success('删除成功');
          // 如果当前页只有一条数据且不是第一页，则返回上一页
          if (this.tableData.length === 1 && this.searchParams.pageNum > 1) {
            this.searchParams.pageNum -= 1;
          }
          this.fetchCompanyList();
        } else {
          this.$message.error(res.msg || '删除失败');
        }
      } catch (error) {
        console.error('删除公司失败:', error);
        this.$message.error('删除失败，请稍后重试');
      }
    },

    /**
     * 处理分页大小变化
     * @param {Number} size 每页条数
     */
    handlePageSizeChange(size) {
      this.searchParams.pageSize = size;
      this.fetchCompanyList();
    },

    /**
     * 处理页码变化
     * @param {Number} page 当前页码
     */
    handlePageChange(page) {
      this.searchParams.pageNum = page;
      this.fetchCompanyList();
    },

    /**
     * 处理对话框关闭
     */
    handleDialogClosed() {
      this.$refs.companyForm.resetFields();
    },

    /**
     * 处理文件上传前的校验
     * @param {File} file 上传的文件
     */
    beforeUpload(file) {
      const isImage = file.type.includes('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('只能上传图片文件');
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过2MB');
      }

      return isImage && isLt2M;
    },

    /**
     * 处理文件上传成功
     * @param {Object} response 上传响应
     */
    handleUploadSuccess(response) {
      if (response.code === '200') {
        this.formData.logo = response.data;
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.msg || '上传失败');
      }
    },

    /**
     * 提交表单数据
     */
    async handleSubmit() {
      try {
        // 表单验证
        const valid = await this.$refs.companyForm.validate();
        if (!valid) return;

        this.submitting = true;
        if (this.user.role === 'ROLE_COMPANY') {
          this.formData.userId = this.user.id; // 设置用户ID
        }
        const apiUrl = this.isEditMode ? `/company` : '/company';
        const res = await request.post(apiUrl, this.formData);

        if (res.code === '200') {
          this.$message.success(this.isEditMode ? '更新成功' : '新增成功');
          this.dialogVisible = false;
          this.fetchCompanyList();
        } else {
          this.$message.error(res.msg || '操作失败');
        }
      } catch (error) {
        console.error('提交表单失败:', error);
        this.$message.error('操作失败，请稍后重试');
      } finally {
        this.submitting = false;
      }
    }
  }
};
</script>

<style scoped>
.company-container {
  padding: 20px;
}

.search-area {
  margin-bottom: 15px;
}

.table-area {
  margin: 15px 0;
}

.pagination-area {
  text-align: center;
  margin-top: 20px;
}

.image-error {
  width: 70px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  color: #999;
  border-radius: 50%;
}

.el-upload__tip {
  margin-top: 5px;
  color: #999;
  font-size: 12px;
}
</style>
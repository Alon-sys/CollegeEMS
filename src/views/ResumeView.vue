<template>
  <div>
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.name" style="width: 200px; margin-right: 10px" placeholder="请输入姓名"></el-input>
      <el-input v-model="params.education" style="width: 200px; margin-right: 10px" placeholder="请输入学历"></el-input>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>

    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px" v-loading="loading">
        <!-- 基本信息列 -->
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column label="性别" width="80">
          <template slot-scope="scope">{{ scope.row.gender || '-' }}</template>
        </el-table-column>
        <el-table-column label="出生日期" width="120">
          <template slot-scope="scope">
            {{ scope.row.birthday ? formatDate(scope.row.birthday) : '-' }}
          </template>
        </el-table-column>

        <!-- 教育信息列 -->
        <el-table-column label="学历" width="100">
          <template slot-scope="scope">{{ scope.row.education || '-' }}</template>
        </el-table-column>
        <el-table-column prop="school" label="毕业院校" width="150"></el-table-column>
        <el-table-column prop="major" label="专业" width="150"></el-table-column>

        <!-- 技能和经验 -->
        <el-table-column label="技能" width="180">
          <template slot-scope="scope">
            <el-tag v-for="(skill, index) in scope.row.skills?.split(',')" :key="index" 
                    size="mini" style="margin: 2px">{{ skill }}</el-tag>
          </template>
        </el-table-column>

        <!-- 时间信息 -->
        <el-table-column label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.created_at) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="edit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="deleteItem(scope.row.id)">
              <el-button slot="reference" type="danger" size="mini" style="margin-left: 5px">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="params.pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="params.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog :title="form.id ? '编辑简历' : '新建简历'" :visible.sync="dialogFormVisible" width="600px">
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <!-- 基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                v-model="form.birthday"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="请选择">
                <el-option v-for="item in educationOptions" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 教育信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="毕业院校" prop="school">
              <el-input v-model="form.school"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="form.major"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 技能和经验 -->
        <el-form-item label="技能" prop="skills">
          <el-input 
            v-model="form.skills"
            placeholder="请输入技能，多个技能用逗号分隔"
            type="textarea"
            :rows="2"></el-input>
        </el-form-item>

        <el-form-item label="工作经历" prop="experience">
          <el-input
            v-model="form.experience"
            type="textarea"
            :rows="3"
            placeholder="请描述工作经历（时间+公司+职位+工作内容）"></el-input>
        </el-form-item>

        <el-form-item label="项目经验" prop="projects">
          <el-input
            v-model="form.projects"
            type="textarea"
            :rows="3"
            placeholder="请描述项目经验（项目名称+角色+技术栈+成果）"></el-input>
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
  name: "ResumeManagement",
  data() {
    return {
      params: {
        name: '',
        education: '',
        pageNum: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      loading: false,
      dialogFormVisible: false,
      form: {},
      educationOptions: ['大专', '本科', '硕士', '博士', '其他'],
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        education: [{ required: true, message: '请选择学历', trigger: 'change' }]
      }
    }
  },
  created() {
    this.findBySearch();
  },
  methods: {
    // 日期格式化方法
    formatDate(date) {
      return dayjs(date).format('YYYY-MM-DD');
    },
    formatDateTime(datetime) {
      return dayjs(datetime).format('YYYY-MM-DD HH:mm');
    },

    // 表单提交
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.submit();
        }
      });
    },

    // 数据操作方法 
    async findBySearch() {
      this.loading = true;
      try {
        const res = await request.get('/resume/search', { params: this.params });
        if (res.code === '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      } finally {
        this.loading = false;
      }
    },

    async submit() {
      const method = this.form.id ? 'put' : 'post';
      const res = await request[method]('/resume', this.form);
      
      if (res.code === '200') {
        this.$message.success('操作成功');
        this.dialogFormVisible = false;
        this.findBySearch();
      }
    }
  }
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
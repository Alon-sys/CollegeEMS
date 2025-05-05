<template>
  <div>
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.name" style="width: 200px; margin-right: 10px" placeholder="请输入学生姓名"></el-input>
      <el-input v-model="params.college" style="width: 200px; margin-right: 10px" placeholder="请输入学院"></el-input>
      <el-input v-model="params.major" style="width: 200px; margin-right: 10px" placeholder="请输入专业"></el-input>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px">
        <el-table-column prop="studentID" label="学号" width="180"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="college" label="学院" width="180"></el-table-column>
        <el-table-column prop="major" label="专业" width="180"></el-table-column>
        <el-table-column prop="classInfo" label="班级" width="180"></el-table-column>
        <el-table-column prop="isEmployed" label="就业情况">
          <template v-slot="{ row }">
            <el-tag v-if="row.isEmployed == 1" type="success">已就业</el-tag>
            <el-tag v-else type="info">未就业</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template v-slot="{ row }">
            <el-button style="margin-right: 5px;" type="primary" @click="edit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" @confirm="deletce(row.id)">
              <el-button slot="reference" type="danger">删除</el-button>
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

    <el-dialog title="请填学生信息" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form :model="form" label-position="top">
        <el-form-item label="学号" :label-width="formLabelWidth">
          <el-input v-model="form.studentID" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生姓名" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="年龄" :label-width="formLabelWidth">
          <el-input v-model="form.age" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <template>
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="学院" :label-width="formLabelWidth">
          <el-input v-model="form.college" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业" :label-width="formLabelWidth">
          <el-input v-model="form.major" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级" :label-width="formLabelWidth">
          <el-input v-model="form.classInfo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="就业情况">
          <el-radio-group v-model="form.isEmployed">
            <el-radio-button label="1">已就业</el-radio-button>
            <el-radio-button label="0">未就业</el-radio-button>
          </el-radio-group>
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
  name: "CompanyView",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      formLabelWidth: '100px',
      params: {
        name: '',
        college: '',
        major: '',
        pageNum: 1,
        pageSize: 5

      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      loading: false,
      submitting: false,
      isEditMode: false,

    }

  },

  //页面创建完成时执行
  created() {
    this.fetchStudentList();
  },
  //页面挂载完成时执行
  methods: {
    async fetchStudentList() {
      this.loading = true;
      try {
        let res;

        // 统一使用字符串比对（或使用常量，但需正确定义）
        if (this.user.role === 'ROLE_ADMIN') {
          res = await request.get('/student/search', {
            params: this.params
          });
        } else if (this.user.role === 'ROLE_STUDENT') {
          res = await request.get('/student/' + this.user.id);
        }

        if (res.code === '200') {
          // 统一使用相同比对方式
          if (this.user.role === 'ROLE_ADMIN') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else if (this.user.role === 'ROLE_STUDENT') {
            this.tableData = res.data?.id ? [res.data] : [];
            this.total = 1;
          }
        } else {
          this.$message.error(res.msg || '获取数据失败');
        }
      } catch (error) {
        console.error('获取学生列表失败:', error);
        this.$message.error('获取数据失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },


    //新增操作
    add() {
      this.form = {};
      this.isEditMode = false;
      this.dialogFormVisible = true;
    },
    //编辑操作
    edit(row) {
      this.form = { ...row };
      this.isEditMode = true; // 设置为编辑模式
      this.dialogFormVisible = true;
    },

    //重置操作
    reset() {
      this.params = {
        name: '',
        college: '',
        major: '',
        pageNum: '1',
        pageSize: '5',
      }
      this.fetchStudentList();
    },

    //分页操作
    // pageNum:当前页码，pageSize:每页显示条数
    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize;
      this.fetchStudentList();
    },
    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum;
      this.fetchStudentList();
    },
    //新增和编辑操作
    async submit() {
      try {

        this.submitting = true;
        if (this.user.role === 'ROLE_STUDENT') {
          this.form.userID = this.user.id; // 设置用户ID
        }
        const apiUrl = this.isEditMode ? `/student` : '/student';
        const res = await request.post(apiUrl, this.form);

        if (res.code === '200') {
          this.$message.success(this.isEditMode ? '更新成功' : '新增成功');
          this.dialogFormVisible = false;
          this.fetchStudentList();
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
    //删除操作
    deletce(id) {
      request.delete('/student/' + id).then(res => {
        if (res.code == '200') {
          this.$message({
            message: ('删除成功'),
            type: 'success'
          });
          this.fetchStudentList();
        }
        else {
          this.$message({
            message: (res.msg),
            type: 'error'
          });
        }
      })
    },
    successUpload(res) {
      this.form.logo = res.data;
    },
  }
}
</script>